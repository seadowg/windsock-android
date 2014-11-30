package com.seadowg.windsock.test;

import android.app.Activity;
import android.widget.ListView;
import com.seadowg.windsock.MainActivity;
import com.seadowg.windsock.R;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Robolectric.shadowOf;

@Config(manifest = "./src/main/AndroidManifest.xml", emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

    @Test
    public void loadsConcourseJobs() throws Exception {
        Activity activity = Robolectric.setupActivity(MainActivity.class);
        shadowOf((ListView) activity.findViewById(R.id.jobs)).populateItems(); // Make sure list view has rendered

        assertEquals(true, innerTextOf(activity).contains("fly"));
        assertEquals(true, innerTextOf(activity).contains("atc"));
    }

    public String innerTextOf(Activity activity) {
        return shadowOf(activity.findViewById(R.id.layout)).innerText();
    }
}
