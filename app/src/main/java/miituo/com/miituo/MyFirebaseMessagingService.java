package miituo.com.miituo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by miituo on 15/02/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public String donde;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        //Log.e("FIREBASE", remoteMessage.getNotification().getBody());

        /*for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            //Log.d("Datos", "key, " + key + " value " + value);
        }*/

        donde = "reporte_mensual";

        /*String click_action = remoteMessage.getNotification().getClickAction();
        if(click_action.equals("") || click_action == null){
            Log.w("log","No hya clic action");
        }else{
            Log.w("Log",click_action);
        }*/

        /*AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("titulo");
        builder.setMessage(donde+ " "+click_action);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alerta = builder.create();
        alerta.show();*/

        sendNotification(remoteMessage.getNotification().getBody());
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    private void sendNotification(String _msg)
    {
        Intent intent;

        if(donde.equals("reporte_mensual")){
            intent = new Intent(this,MainActivity.class);
        }else{
            intent = new Intent(this,MainActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pending=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        //Uri sound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pushfin);
        NotificationCompat.Builder noti=new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("miituo")
                .setContentText(_msg)
                .setAutoCancel(true)
                .setContentIntent(pending)
                .setSound(sound);
        NotificationManager nmg=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        nmg.notify(0,noti.build());
    }
}
