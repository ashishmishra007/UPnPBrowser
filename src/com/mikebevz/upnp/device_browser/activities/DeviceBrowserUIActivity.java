/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mikebevz.upnp.device_browser.activities;

import android.view.View;
import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.mikebevz.upnp.ControlUIFactory;
import com.mikebevz.upnp.R;
import com.mikebevz.upnp.R.layout;
import com.mikebevz.upnp.UpnpBrowserApp;
import com.mikebevz.upnp.device_browser.DeviceDetailsAdapter;
import org.cybergarage.upnp.Device;

/**
 *
 * @author mikebevz
 */
public class DeviceBrowserUIActivity extends Activity implements OnClickListener {

    private DeviceDetailsAdapter adapter;
    private Device device;
    private LinearLayout layout;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.device_details);
        
        Button servicesBtn = (Button) findViewById(R.id.services_btn);
        servicesBtn.setOnClickListener(this);

        
        
        if (((UpnpBrowserApp)getApplication()).getContentBrowser() == true) {
            
            /**
             * <Button 
    android:layout_width="fill_parent"
    android:layout_height="wrap_content"
    android:text="Browse Content"
    android:id="@+id/content_btn"
    android:padding="10sp"
/>

             */
            
            Button contentBtn = new Button(this);
            //Button contentBtn = (Button) findViewById(R.id.content_btn);
            contentBtn.setOnClickListener(this);
            
            layout = (LinearLayout)findViewById(R.id.main_layout);
            layout.addView(contentBtn, 1);
        }
        
        
        
        
        Bundle bundle = getIntent().getExtras();
        int position = bundle.getInt("device");
        
        device = ((UpnpBrowserApp)getApplication()).getDeviceList().getDevice(position);
        
        adapter = new DeviceDetailsAdapter(this, position);
        
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);

    }

    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.content_btn:
                ControlUIFactory factory = new ControlUIFactory();
                intent = factory.getIntent(this, device.getDeviceType());
                break;
            case R.id.services_btn:
                intent = new Intent(this, ServiceListActivity.class);
                break;
            default:
                intent = new Intent(this, ServiceListActivity.class);

        }

        intent.putExtra("device", getIntent().getExtras().getInt("device"));
        startActivity(intent);

    }
}
