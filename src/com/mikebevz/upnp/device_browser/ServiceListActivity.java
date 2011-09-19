/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mikebevz.upnp.device_browser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.mikebevz.upnp.ControlUIFactory;
import com.mikebevz.upnp.R;
import com.mikebevz.upnp.UpnpBrowserApp;
import com.mikebevz.upnp.tasks.GetDeviceServicesTask;
import com.mikebevz.upnp.tasks.OnDeviceServiceList;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.Service;
import org.cybergarage.upnp.ServiceList;

/**
 *
 * @author mikebevz
 */
public class ServiceListActivity extends Activity implements OnDeviceServiceList, OnItemClickListener {
    
    private ServiceList sList;
    private ServiceListAdapter adapter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.list_view);  
        
        Bundle bundle = getIntent().getExtras();
        int position = bundle.getInt("device");
        Device device = (Device) ((UpnpBrowserApp)getApplication()).getDeviceList().get(position);
        this.setTitle("Services at " + device.getFriendlyName());
        
        GetDeviceServicesTask getServiceTask = new GetDeviceServicesTask();
        getServiceTask.setOnDeviceServiceListHandler(this);
        getServiceTask.execute(device);
        
        
        ListView listView = (ListView)findViewById(R.id.list_view);
        adapter = new ServiceListAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        
    }
    
    public void OnDeviceServiceListSuccess(ServiceList sList) {
        this.sList = sList;
        Log.d("ServiceList", String.valueOf(sList.size()));
        ((UpnpBrowserApp)getApplication()).setServiceList(sList);
        adapter.setServices(sList);
        adapter.notifyDataSetChanged();
    }

    public void OnDeviceServiceListProgressUpdate(Integer value) {
        Log.d("Progress", String.valueOf(value));
        setProgress(value);
    }

    public void onItemClick(AdapterView<?> av, View view, int position, long id) {
        
        //Service service = (Service) adapter.getItem(position);
        
        
        Intent intent = new Intent(this, ActionListActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
        
    }
}