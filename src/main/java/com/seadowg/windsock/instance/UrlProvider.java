package com.seadowg.windsock.instance;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class UrlProvider {
  private String url;

  @Inject
  public UrlProvider() {

  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }
}
