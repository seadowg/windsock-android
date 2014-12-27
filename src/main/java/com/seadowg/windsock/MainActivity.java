package com.seadowg.windsock;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.*;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.inject.Inject;
import com.seadowg.windsock.instance.UrlProvider;
import com.seadowg.windsock.jobs.Job;
import com.seadowg.windsock.jobs.JobsDataSource;
import com.seadowg.windsock.jobs.JobsList;
import com.squareup.otto.Subscribe;
import roboguice.activity.RoboActivity;
import java.util.List;

public class MainActivity extends RoboActivity {

  @Inject
  UrlProvider urlProvider;

  JobsDataSource jobs;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    jobs = new JobsDataSource(urlProvider.getUrl());
    jobs.update();

    ListView jobsList = (ListView) findViewById(R.id.jobs);
    jobsList.setEmptyView(findViewById(R.id.no_jobs));

    final JobsAdapter adapter = new JobsAdapter(jobs);
    jobsList.setAdapter(adapter);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main_activity_actions, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.refresh:
        jobs.update();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private static class JobsAdapter extends BaseAdapter {
    private List<Job> jobs;

    public JobsAdapter(JobsDataSource jobsDataSource) {
      jobsDataSource.register(this);
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

      Resources resources = parent.getContext().getResources();

      if (job.getStatus().equals("pending")) {
        view.setBackgroundColor(resources.getColor(R.color.pending));
      } else if (job.getStatus().equals("started")) {
        view.setBackgroundColor(resources.getColor(R.color.started));
      } else if (job.getStatus().equals("succeeded")) {
        view.setBackgroundColor(resources.getColor(R.color.succeeded));
      } else if (job.getStatus().equals("failed")) {
        view.setBackgroundColor(resources.getColor(R.color.failed));
      } else if (job.getStatus().equals("errored")) {
        view.setBackgroundColor(resources.getColor(R.color.errored));
      } else if (job.getStatus().equals("aborted")) {
        view.setBackgroundColor(resources.getColor(R.color.aborted));
      }

      return view;
    }

    private View inflateItem(ViewGroup parent) {
      LayoutInflater inflater = LayoutInflater.from(parent.getContext());
      return inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
    }
  }
}
