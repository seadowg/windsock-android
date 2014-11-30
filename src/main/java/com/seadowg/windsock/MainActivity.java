package com.seadowg.windsock;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.seadowg.windsock.jobs.Job;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    new FetchJobsTask(this).execute();
  }

  private class FetchJobsTask extends AsyncTask<Void, Void, List<Job>> {

    private final Activity activity;

    public FetchJobsTask(Activity activity) {
      this.activity = activity;
    }

    @Override
    protected List<Job> doInBackground(Void... params) {
      OkHttpClient httpClient = new OkHttpClient();
      Request request = new Request.Builder()
              .url("http://ci.concourse.ci/api/v1/jobs")
              .build();

      try {
        Response response = httpClient.newCall(request).execute();
        JSONArray jobsJson = new JSONArray(response.body().string());

        ArrayList<Job> jobs = new ArrayList<Job>();

        for (int i = 0; i < jobsJson.length(); i++) {
          JSONObject jobJson = jobsJson.getJSONObject(i);

          if (jobJson.get("finished_build") != JSONObject.NULL) {
            String name = jobJson.getString("name");
            String status = jobJson.getJSONObject("finished_build").getString("status");

            jobs.add(new Job(name, status));
          }
        }

        return jobs;
      } catch (IOException e) {
        throw new RuntimeException(e);
      } catch (JSONException e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    protected void onPostExecute(List<Job> jobs) {
      ListView jobsList = (ListView) activity.findViewById(R.id.jobs);
      jobsList.setAdapter(new JobsAdapter(jobs));
    }
  }

  private class JobsAdapter extends BaseAdapter {
    private final List<Job> jobs;

    public JobsAdapter(List<Job> jobs) {
      this.jobs = jobs;
    }

    @Override
    public int getCount() {
      return jobs.size();
    }

    @Override
    public Object getItem(int position) {
      return jobs.get(position);
    }

    @Override
    public long getItemId(int position) {
      return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      TextView view = (TextView) getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false);
      Job job = jobs.get(position);

      view.setText(job.getName());

      if (job.getStatus().equals("pending")) {
        view.setBackgroundColor(getResources().getColor(R.color.pending));
      } else if (job.getStatus().equals("started")) {
        view.setBackgroundColor(getResources().getColor(R.color.started));
      } else if (job.getStatus().equals("succeeded")) {
        view.setBackgroundColor(getResources().getColor(R.color.succeeded));
      } else if (job.getStatus().equals("failed")) {
        view.setBackgroundColor(getResources().getColor(R.color.failed));
      } else if (job.getStatus().equals("errored")) {
        view.setBackgroundColor(getResources().getColor(R.color.errored));
      } else if (job.getStatus().equals("aborted")) {
        view.setBackgroundColor(getResources().getColor(R.color.aborted));
      }

      return view;
    }
  }
}
