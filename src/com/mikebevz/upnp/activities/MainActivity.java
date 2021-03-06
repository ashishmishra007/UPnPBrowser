package com.mikebevz.upnp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.bugsense.trace.BugSenseHandler;
import com.google.ads.AdView;
import com.mikebevz.upnp.ActivityState;
import com.mikebevz.upnp.DeviceListAdapter;
import com.mikebevz.upnp.R;
import com.mikebevz.upnp.UpnpBrowserApp;
import com.mikebevz.upnp.activities.states.*;
import com.mikebevz.upnp.device_browser.activities.DeviceBrowserUIActivity;
import org.cybergarage.upnp.Device;

/**
 * @author mikebevz
 */
public class MainActivity extends Activity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

private ActivityState state;
public DeviceListAdapter devListAdapter = null;
//Boolean controlPointStatus = false; // false = stopped, true = running
public ListView devicesList;
private AdView av;

// WiFi is disabled
public final ActivityState STATE_WIFI_DISABLED = new StateWifiDisabled(this);
// WiFi is not connected to any network
public final ActivityState STATE_WIFI_NOTCONNECTED = new StateWifiNotConnected(this);
// Control point stopped scanning
private final ActivityState STATE_CONTROL_POINT_STOPPED = new StateControlPointStopped(this);
// Control point started scanning
private final ActivityState STATE_CONTROL_POINT_STARTED = new StateControlPointStarted(this);
// Application wide error happened
public final StateError STATE_ERROR = new StateError(this);

@Override
public void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);

  getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

  UpnpBrowserApp app = ((UpnpBrowserApp) getApplication());

  if (app.isDebug()) {
    // Bugsense library for handling crashes
    BugSenseHandler.setup(this, "a8c5f7db");
  }


  /*
  if (app.isAdEnabled()) {
      av = (AdView)findViewById(R.id.ad_view);
      AdRequest request = new AdRequest();
      request.addTestDevice("B3EEABB8EE11C2BE770B684D95219ECB");
      av.loadAd(request);
  }
  */

  setState(STATE_CONTROL_POINT_STARTED);
}

@Override
protected void onPause() {
  super.onPause();
  setState(STATE_CONTROL_POINT_STOPPED);
}

@Override
protected void onRestart() {
  super.onRestart();
  Log.d("ControlPoint", "Restart Activity");
  setState(STATE_CONTROL_POINT_STARTED);

}

@Override
protected void onResume() {
  super.onResume();
  Log.d("ControlPoint", "Resume Activity");
  setState(STATE_CONTROL_POINT_STARTED);
}

@Override
protected void onDestroy() {
  Log.d("ControlPoint", "Destroying Activity");
  setState(STATE_CONTROL_POINT_STOPPED);
  super.onDestroy();
}

public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
  Device dev = (Device) devListAdapter.getItem(position);

  Intent intent = new Intent(this, DeviceBrowserUIActivity.class);
  intent.putExtra("device", position);
  startActivity(intent);

}

public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
  Device dev = (Device) devListAdapter.getItem(position);
  Log.d("Device", "Show Device Details " + dev.getFriendlyName());
}

public void onNothingSelected(AdapterView<?> arg0) {
}

@Override
public void onCreateContextMenu(ContextMenu menu, View v,
                                ContextMenuInfo menuInfo) {
  super.onCreateContextMenu(menu, v, menuInfo);
  MenuInflater inflater = getMenuInflater();
  inflater.inflate(R.menu.main_context_menu, menu);

}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
  MenuInflater inflater = getMenuInflater();
  inflater.inflate(R.menu.main_context_menu, menu);
  return true;
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
  switch (item.getItemId()) {
    case R.id.refresh_network:

      setState(STATE_CONTROL_POINT_STOPPED);
      setState(STATE_CONTROL_POINT_STARTED);
      Toast.makeText(this, "Restarting scanning", Toast.LENGTH_LONG).show();
      return true;

    case R.id.help:
      Intent intent = new Intent(this, HelpActivity.class);
      startActivity(intent);
      return true;


    default:
      return super.onOptionsItemSelected(item);
  }

}

public ActivityState getState() {
  return state;
}

public void setState(ActivityState state) {
  Log.d("State: ", state.getClass().getName());
  if (this.state != state) {
    Log.d("Setting State: ", state.getClass().getName());
    this.state = state;
    this.state.setSettings();
  }
}

public void onWifiNotConnected() {
  setState(STATE_WIFI_NOTCONNECTED);
}

void onWifiDisabled() {
  setState(STATE_WIFI_DISABLED);
}
}