package com.seadowg.windsock.test.support;

import com.squareup.okhttp.mockwebserver.MockResponse;

public class ApiResponses {
  public static MockResponse jobsResponse = new MockResponse().setBody("[" +
      "{ \"name\": \"fly\", \"finished_build\": { \"status\": \"succeeded\" }}" +
      ", { \"name\": \"atc\", \"finished_build\": { \"status\": \"succeeded\" }}" +
      "]");

  public static MockResponse differentJobsResponse = new MockResponse().setBody("[" +
      "{ \"name\": \"different_fly\", \"finished_build\": { \"status\": \"succeeded\" }}" +
      ", { \"name\": \"different_atc\", \"finished_build\": { \"status\": \"succeeded\" }}" +
      "]");
}
