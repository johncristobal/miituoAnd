package miituo.com.miituo;

import android.app.IntentService;
//import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by john.cristobal on 23/06/17.
 */

public class GcmIntentService extends Service {


    //private Firebase f = new Firebase("https://somedemo.firebaseio-demo.com/");
    DatabaseReference f = FirebaseDatabase.getInstance().getReference();
    private ValueEventListener handler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        handler = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot arg0) {
                postNotif(arg0.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        f.addValueEventListener(handler);
    }

    private void postNotif(String notifString) {
        /*NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int icon = R.drawable.miituo;

        RemoteMessage.Notification notification = new RemoteMessage.Notification(icon, "Firebase" + Math.random(), System.currentTimeMillis());
        //Notification notification = new Notification(icon, "Firebase" + Math.random(), System.currentTimeMillis());
//		notification.flags |= Notification.FLAG_AUTO_CANCEL;

        Context context = getApplicationContext();
        CharSequence contentTitle = "Background" + Math.random();
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, contentTitle, notifString, contentIntent);
        mNotificationManager.notify(1, notification);*/

        Intent intent;

        intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pending=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri sound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder noti=new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("miituo")
                .setContentText(notifString)
                .setAutoCancel(true)
                .setContentIntent(pending)
                .setSound(sound);
        NotificationManager nmg=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        nmg.notify(0,noti.build());

    }

}
