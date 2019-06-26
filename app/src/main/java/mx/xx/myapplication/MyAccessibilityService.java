package mx.xx.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityNodeInfo;

public class MyAccessibilityService extends AccessibilityService {
    final static String TAG = "AccTag";

    private String getEventText(AccessibilityEvent event) {
        StringBuilder sb = new StringBuilder();
        for (CharSequence s : event.getText()) {
            sb.append(s);
        }
        return sb.toString();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        /*Log.d(TAG,"onAccessibilityEvent reached");
        Log.v(TAG, String.format(
                "****************************** onAccessibilityEvent: type = [ %s ], class = [ %s ], package = [ %s ], time = [ %s ], text = [ %s ]",
                event.getEventType(), event.getClassName(), event.getPackageName(),
                event.getEventTime(),event.toString()));*/

        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) {
            AccessibilityNodeInfo mNodeInfo = event.getSource();
            try{
                /*Log.d(TAG,mNodeInfo.toString());*/
                Log.d(TAG,"ENTERED TEXT:" + mNodeInfo.getText().toString());
            } catch (Exception e){
                Log.d(TAG,Log.getStackTraceString(e));
            }
        }

        if (event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED){
                Log.d(TAG,"Message received - " + event.getText());
        }
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG,"onInterrupt reached");
    }


}
