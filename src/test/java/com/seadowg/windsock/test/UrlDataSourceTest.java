package com.seadowg.windsock.test;

import com.seadowg.windsock.instance.UrlDataSource;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UrlDataSourceTest {

  @Test
  public void defaultsToConcourseCIInstance() {
    UrlDataSource urlDataSource = new UrlDataSource();
    assertEquals("http://ci.concourse.ci", urlDataSource.getUrl());
  }
}