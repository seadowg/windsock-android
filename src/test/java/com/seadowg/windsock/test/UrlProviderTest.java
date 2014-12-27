package com.seadowg.windsock.test;

import com.seadowg.windsock.instance.UrlProvider;
import junit.framework.TestCase;
import org.junit.Test;

public class UrlProviderTest extends TestCase {

  @Test
  public void defaultsToConcourseCIInstance() {
    UrlProvider urlProvider = new UrlProvider();
    assertEquals("http://ci.concourse.ci", urlProvider.getUrl());
  }
}