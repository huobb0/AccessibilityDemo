package mx.xx.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import static android.content.ContentValues.TAG;
import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityNodeInfo;

public class MyAccessibilityService extends AccessibilityService {

    private String getEventText(AccessibilityEvent event) {
        StringBuilder sb = new StringBuilder();
        for (CharSequence s : event.getText()) {
            sb.append(s);
        }
        return sb.toString();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TAG,"onAccessibiltyEvent reached");
        Log.v(TAG, String.format(
                "onAccessibilityEvent: type = [ %s ], class = [ %s ], package = [ %s ], time = [ %s ], text = [ %s ]",
                event.getEventType(), event.getClassName(), event.getPackageName(),
                event.getEventTime(),event.getClassName().toString()));

        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) {
            AccessibilityNodeInfo mNodeInfo = event.getSource();
            try{
                Log.d(TAG,mNodeInfo.toString());
            } catch (Exception e){
                Log.d(TAG,Log.getStackTraceString(e));
            }
        }

        if (event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
                StringBuilder message = new StringBuilder();
                if (!event.getText().isEmpty()) {
                    for (CharSequence subText : event.getText()) {
                        message.append(subText);
                    }
                    if (message.toString().contains("Message from")) {
                        Log.d(TAG, message.toString());
                    }
                }
        }
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG,"onInterrupt reached");
    }


}
