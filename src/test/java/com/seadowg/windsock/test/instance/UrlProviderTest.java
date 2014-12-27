package com.seadowg.windsock.test.instance;

import com.seadowg.windsock.instance.UrlProvider;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UrlProviderTest {

  @Test
  public void defaultsToConcourseCIInstance() {
    UrlProvider urlProvider = new UrlProvider();
    assertEquals("http://ci.concourse.ci", urlProvider.getUrl());
  }
}