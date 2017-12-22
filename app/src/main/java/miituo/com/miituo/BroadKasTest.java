package miituo.com.miituo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by john.cristobal on 23/06/17.
 */

public class BroadKasTest extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //ComponentName comp = new ComponentName(context.getPackageName(), GcmIntentService.class.getName());
        //startWakefulService(context, (intent.setComponent(comp)));
        //setResultCode(Activity.RESULT_OK);


        context.startService(new Intent(context,GcmIntentService.class));
    }
}