package com.seadowg.windsock.test;

import android.app.Activity;
import android.view.MenuItem;
import android.widget.ListView;
import com.google.inject.Inject;
import com.seadowg.windsock.MainActivity;
import com.seadowg.windsock.R;
import com.seadowg.windsock.instance.UrlProvider;
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

import static com.seadowg.windsock.test.support.ApiResponses.*;
import static com.seadowg.windsock.test.support.Helpers.innerTextOf;
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
        server = new MockWebServer();
        server.enqueue(jobsResponse);
        server.play();

        startActivity(server);

        RecordedRequest recordedRequest = server.takeRequest();
        assertEquals("/api/v1/pipelines/main/jobs", recordedRequest.getPath());
    }

    @Test
    public void showsJobs() throws Exception {
        server = new MockWebServer();
        server.enqueue(jobsResponse);
        server.play();

        startActivity(server);

        shadowOf((ListView) activity.findViewById(R.id.jobs)).populateItems(); // Make sure list view has rendered
        assertEquals(true, innerTextOf(activity).contains("fly"));
        assertEquals(true, innerTextOf(activity).contains("atc"));
    }

    @Test
    public void clickingOnRefreshButton_refreshesListOfJobs() throws Exception {
        server = new MockWebServer();
        server.enqueue(jobsResponse);
        server.enqueue(differentJobsResponse);
        server.play();

        startActivity(server);

        MenuItem item = new TestMenuItem(R.id.refresh);
        activity.onOptionsItemSelected(item);

        shadowOf((ListView) activity.findViewById(R.id.jobs)).populateItems(); // Make sure list view has rendered
        assertEquals(true, innerTextOf(activity).contains("different_fly"));
        assertEquals(true, innerTextOf(activity).contains("different_atc"));
    }

    private void startActivity(MockWebServer server) {
        urlProvider.setUrl(this.server.getUrl("").toString());
        activity = Robolectric.setupActivity(MainActivity.class);
    }
}