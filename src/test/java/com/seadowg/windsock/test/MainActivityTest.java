package com.seadowg.windsock.test;

import android.app.Activity;
import android.view.MenuItem;
import android.widget.ListView;
import com.google.inject.Inject;
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
import org.robolectric.annotation.Config;
import org.robolectric.tester.android.view.TestMenuItem;
import roboguice.RoboGuice;
import roboguice.inject.RoboInjector;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.robolectric.Robolectric.shadowOf;

@Config(manifest = "./src/main/AndroidManifest.xml", emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

    @Inject
    private UrlProvider urlProvider;

    private Activity activity;
    private MockWebServer server;

    @Before
    public void setup() throws Exception {
        RoboInjector injector = RoboGuice.getInjector(Robolectric.application);
        injector.injectMembers(this);
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

    @Test
    public void clickingOnRefreshButton_refreshesListOfJobs() throws Exception {
        setupServer();
        startActivity();

        MenuItem item = new TestMenuItem(R.id.refresh);
        activity.onOptionsItemSelected(item);

        shadowOf((ListView) activity.findViewById(R.id.jobs)).populateItems(); // Make sure list view has rendered
        assertEquals(true, innerTextOf(activity).contains("different_fly"));
        assertEquals(true, innerTextOf(activity).contains("different_atc"));
    }

    private String innerTextOf(Activity activity) {
        return shadowOf(activity.findViewById(R.id.layout)).innerText();
    }

    private void setupServer() throws IOException {
        server = new MockWebServer();

        String body = "[{ \"name\": \"fly\", \"finished_build\": { \"status\": \"succeeded\" }}, " +
            "{ \"name\": \"atc\", \"finished_build\": { \"status\": \"succeeded\" }}]";
        server.enqueue(new MockResponse().setBody(body));

        body = "[{ \"name\": \"different_fly\", \"finished_build\": { \"status\": \"succeeded\" }}, " +
            "{ \"name\": \"different_atc\", \"finished_build\": { \"status\": \"succeeded\" }}]";
        server.enqueue(new MockResponse().setBody(body));

        server.play();
    }

    private void startActivity() {
        urlProvider.setUrl(server.getUrl("").toString());
        activity = Robolectric.setupActivity(MainActivity.class);
    }
}