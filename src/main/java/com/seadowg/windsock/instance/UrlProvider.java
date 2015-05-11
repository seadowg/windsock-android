package com.seadowg.windsock.instance;

import com.google.inject.Singleton;

@Singleton
public class UrlProvider {
  private String url;

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUrl() {
    if (url != null) {
      return url;
    } else {
      return "https://ci.concourse.ci";
    }
  }
}
