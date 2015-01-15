package com.seadowg.windsock.test.support;

import android.app.Activity;
import com.seadowg.windsock.R;

import static org.robolectric.Robolectric.shadowOf;

public class Helpers {
  public static String innerTextOf(Activity activity) {
    return shadowOf(activity.findViewById(R.id.layout)).innerText();
  }
}
