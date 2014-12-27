package com.seadowg.windsock;

import android.app.Application;
import com.google.inject.AbstractModule;
import com.seadowg.windsock.instance.UrlDataSource;
import org.robolectric.Robolectric;
import roboguice.RoboGuice;

public class TestWindsockApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    RoboGuice.overrideApplicationInjector(Robolectric.application, new TestModule());
  }

  public static class TestModule extends AbstractModule {

    @Override
    protected void configure() {
      bind(UrlDataSource.class).toInstance(new UrlDataSource());
    }
  }
}
