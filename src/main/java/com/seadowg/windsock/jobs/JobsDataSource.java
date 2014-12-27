package com.seadowg.windsock.jobs;

import android.os.AsyncTask;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

@Singleton
public class JobsDataSource {

  private final Bus bus;
  private final String url;
  private JobsList jobs;

  @Inject
  public JobsDataSource(String url) {
    this.url = url;

    this.bus = new Bus();
    this.jobs = new JobsList();

    bus.register(this);
  }

  @Produce
  public JobsList getJobs() {
    return jobs;
  }

  public void register(Object object) {
    bus.register(object);
  }

  public void update() {
    new FetchJobsTask(url).execute();
  }

  private void setJobs(JobsList jobs) {
    this.jobs = jobs;
    bus.post(jobs);
  }

  private class FetchJobsTask extends AsyncTask<Void, Void, JobsList> {
    private final String url;

    public FetchJobsTask(String hostUrl) {
      this.url = hostUrl;
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
      setJobs(jobs);
    }
  }
}
