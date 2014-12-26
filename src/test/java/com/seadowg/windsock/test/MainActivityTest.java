package com.seadowg.windsock.test;

import android.app.Activity;
import android.widget.ListView;
import com.google.inject.AbstractModule;
import com.seadowg.windsock.MainActivity;
import com.seadowg.windsock.R;
import com.seadowg.windsock.instance.UrlProvider;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import roboguice.RoboGuice;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

    private UrlProvider urlProvider;
    private Activity activity;
    private MockWebServer server;

    @Before
    public void setup() throws Exception {
        urlProvider = new UrlProvider();
        RoboGuice.overrideApplicationInjector(Robolectric.application, new TestModule(urlProvider));
    }

    @After
    public void teardown() throws Exception {
        if (server != null) server.shutdown();
    }

    @Test
    public void fetchesJobsFromCorrectUrl() throws Exception {
        setupServer();
        startActivity();

        RecordedRequest recordedRequest = server.takeRequest();
        assertEquals("/api/v1/jobs", recordedRequest.getPath());
    }

    @Test
    public void showsJobs() throws Exception {
        setupServer();
        startActivity();

        shadowOf((ListView) activity.findViewById(R.id.jobs)).populateItems(); // Make sure list view has rendered
        assertEquals(true, innerTextOf(activity).contains("fly"));
        assertEquals(true, innerTextOf(activity).contains("atc"));
    }

    private String innerTextOf(Activity activity) {
        return shadowOf(activity.findViewById(R.id.layout)).innerText();
    }

    private void setupServer() throws IOException {
        server = new MockWebServer();

        String body = "[{ \"name\": \"fly\", \"finished_build\": { \"status\": \"succeeded\" }}, " +
            "{ \"name\": \"atc\", \"finished_build\": { \"status\": \"succeeded\" }}]";
        server.enqueue(new MockResponse().setBody(body));

        server.play();
    }

    private void startActivity() {
        urlProvider.setUrl(server.getUrl("").toString());
        activity = Robolectric.setupActivity(MainActivity.class);
    }

    public static class TestModule extends AbstractModule {
        private final UrlProvider urlProvider;

        public TestModule(UrlProvider urlProvider) {
            this.urlProvider = urlProvider;
        }

        @Override
        protected void configure() {
            bind(UrlProvider.class).toInstance(urlProvider);
        }
    }
}
