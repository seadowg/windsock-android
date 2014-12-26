package com.seadowg.windsock.jobs;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.seadowg.windsock.EventBus;
import com.squareup.otto.Produce;

@Singleton
public class JobsDataSource {

  private final EventBus bus;
  private JobsList jobs;

  @Inject
  public JobsDataSource(EventBus bus) {
    this.bus = bus;
    this.jobs = new JobsList();

    bus.register(this);
  }

  @Produce
  public JobsList getJobs() {
    return jobs;
  }

  public void setJobs(JobsList jobs) {
    this.jobs = jobs;
    bus.post(jobs);
  }
}
