package com.seadowg.windsock;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.inject.Inject;
import com.seadowg.windsock.instance.UrlProvider;
import com.seadowg.windsock.jobs.Job;
import com.seadowg.windsock.jobs.JobsDataSource;
import com.seadowg.windsock.jobs.JobsList;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.otto.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import roboguice.activity.RoboActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends RoboActivity {

  @Inject
  UrlProvider urlProvider;

  @Inject
  EventBus bus;

  @Inject
  JobsDataSource jobs;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    final JobsAdapter adapter = new JobsAdapter(bus);

    new FetchJobsTask(urlProvider.getUrl(), new FetchJobsTask.Callback() {
      @Override
      public void call(JobsList jobs) {
        MainActivity.this.jobs.setJobs(jobs);
      }
    }).execute();

    ListView jobsList = (ListView) findViewById(R.id.jobs);
    jobsList.setEmptyView(findViewById(R.id.no_jobs));
    jobsList.setAdapter(adapter);
  }

  private static class FetchJobsTask extends AsyncTask<Void, Void, JobsList> {
    private final String url;
    private final Callback callback;

    public FetchJobsTask(String hostUrl, Callback callback) {
      this.url = hostUrl;
      this.callback = callback;
    }

    @Override
    protected JobsList doInBackground(Void... params) {
      OkHttpClient httpClient = new OkHttpClient();
      Request request = new Request.Builder()
              .url(url + "/api/v1/jobs")
              .build();

      try {
        Response response = httpClient.newCall(request).execute();
        JSONArray jobsJson = new JSONArray(response.body().string());

        JobsList jobs = new JobsList();

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
    protected void onPostExecute(JobsList jobs) {
      callback.call(jobs);
    }

    public interface Callback {
      public void call(JobsList jobs);
    }
  }

  private class JobsAdapter extends BaseAdapter {
    private List<Job> jobs;

    public JobsAdapter(EventBus bus) {
      bus.register(this);

      this.jobs = new ArrayList<Job>();
    }

    @Subscribe
    public void setJobs(JobsList jobs) {
      this.jobs = jobs;
      notifyDataSetChanged();
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
      TextView view = (TextView) (convertView != null ? convertView : inflateItem(parent));
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

    private View inflateItem(ViewGroup parent) {
      return getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false);
    }
  }
}
