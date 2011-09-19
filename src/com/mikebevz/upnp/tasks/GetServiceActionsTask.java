/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mikebevz.upnp.tasks;

import android.os.AsyncTask;
import org.cybergarage.upnp.ActionList;
import org.cybergarage.upnp.Service;

/**
 *
 * @author mikebevz
 */
public class GetServiceActionsTask  extends AsyncTask<Service, Integer, ActionList> {
    
    OnServiceActionsList delegate;
    
    
    @Override
    protected ActionList doInBackground(Service... services) {
        
        return services[0].getActionList();
    }
    
    @Override
    protected void onPostExecute(ActionList result) {
        this.delegate.OnServiceActionsListSuccess(result);
    }

    public void setOnServiceActionListHandler(OnServiceActionsList delegate) {
        this.delegate = delegate;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        this.delegate.OnServiceActionsListProgressUpdate(values[0]);
    }
}