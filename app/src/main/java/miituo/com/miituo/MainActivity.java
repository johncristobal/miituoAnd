package miituo.com.miituo;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import miituo.com.miituo.api.ApiClient;
import miituo.com.miituo.data.ClientMovil;
import miituo.com.miituo.data.DBaseMethods;
import miituo.com.miituo.data.InfoClient;
import miituo.com.miituo.data.InsurancePolicyDetail;
import miituo.com.miituo.data.VehicleMovil;
import miituo.com.miituo.data.modelBase;
import miituo.com.miituo.tuto.TutorialActivity;

public class MainActivity extends AppCompatActivity {

    private static final long SPLASH_SCREEN_DELAY=4000;

    VideoView mVideoView;
    SharedPreferences app_preferences;
    public modelBase base;

    public String telefono;
    public threadtosync hilos;
    public static String token;

    public static List<InfoClient> result;

    public static String idTicket;
    public String tokencliente;
    public ClientMovil cli;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        base = new modelBase(getApplicationContext(),Integer.parseInt(getResources().getString(R.string.dbversion)));
        DBaseMethods.base = base;

        mVideoView = (VideoView)findViewById(R.id.videoView);
        app_preferences= PreferenceManager.getDefaultSharedPreferences(this);

        //hilos = new threadtosync();

        //get data from notification and launch view....
        //if we have to report/cancel/ajust we have to set sharedpreferences with an specfigi value
        //        tipoodometro = app_preferences.getString("odometro","null");

        /*Intent i = getIntent();
        String llave = i.getStringExtra("click");
        if(llave != null)
        {

        }*/

        String uri = "android.resource://" + getPackageName() + "/" + R.raw.comp31;
        if (mVideoView != null)
        {
            mVideoView.setVideoURI(Uri.parse(uri));
            mVideoView.setZOrderOnTop(true);
            mVideoView.requestFocus();
            mVideoView.start();
        }
        else
        {
            //toast or print "mVideoView is null"
        }

        TimerTask task=new TimerTask() {
            @Override
            public void run() {
                String sesion = app_preferences.getString("sesion","null");

                //SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sharedName), Context.MODE_PRIVATE);
                if(sesion.equals("1")){
                    Intent i = new Intent(getApplicationContext(), SyncActivity.class);
                    startActivity(i);
                }else{
                    Intent i = new Intent(getApplicationContext(), TutorialActivity.class);
                    startActivity(i);
                }
            }
        };
        Timer timer= new Timer();
        timer.schedule(task,SPLASH_SCREEN_DELAY);

        startService(new Intent(this,GcmIntentService.class));

        //String tuto = sharedPreferences.getString("tutohecho","null");
        //telefono = app_preferences.getString("Celphone", "Default");
        //GetSync(null);
    }

    /*@Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Intent restartService = new Intent(getApplicationContext(), MyFirebaseMessagingService.class);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(),1,restartService,PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME,5000,pendingIntent);
    }*/

    //***********************************
    private class threadtosync {
        //************************** Asytask para web service **********************************************
        AsyncTask<ClientMovil, Void, Void> sendToken = new AsyncTask<ClientMovil, Void, Void>() {
            String ErrorCode = "";

            @Override
            protected void onCancelled() {
                super.onCancelled();
                Toast msg = Toast.makeText(getApplicationContext(), ErrorCode, Toast.LENGTH_LONG);
                msg.show();
            }

            //First methos to update tokne mobile
            @Override
            protected Void doInBackground(ClientMovil... params) {
                ApiClient client = new ApiClient(MainActivity.this);
                try {
                    client.updateToken("ClientUser", params[0],tokencliente);
                } catch (Exception ex) {
                    ErrorCode = ex.getMessage();
                    this.cancel(true);
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //Ejecuto hilo para validar telefono
                String sesion = app_preferences.getString("sesion","null");
                if(sesion.equals("1")) {
                    //Intent ii = new Intent(SyncActivity.this,PrincipalActivity.class);
                    Intent ii = new Intent(MainActivity.this, PrincipalActivity.class);
                    startActivity(ii);
                }else {
                    //UN NUEVI HILo para recuepera token
                    hilos.SyncToken.execute(telefono, result.get(0).getPolicies().getId() + "", "Apptoken");
                }
            }
        };

//************************** Asytask para obtener datos ws **********************************************
        AsyncTask<String, Void, Void> Sync = new AsyncTask<String, Void, Void>() {
            ProgressDialog progress = new ProgressDialog(MainActivity.this);
            String ErrorCode = "", Celphone = "";

            //Si cnacelo...inicio nuevo layout para reiniciar y borro de preferences
            @Override
            protected void onCancelled() {
                progress.dismiss();
                if (ErrorCode.contains("null")){
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Atención");
                    builder.setMessage("No tienes pólizas registradas. Vuelve a cotizar.");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor = app_preferences.edit();
                            editor.putString("Celphone", "");
                            editor.putString("sesion", "");
                            editor.commit();

                            //Intent intent = new Intent(SyncActivity.this,MainActivity.class);
                            //startActivity(intent);
                        }
                    });

                    android.support.v7.app.AlertDialog alerta = builder.create();
                    alerta.show();
                }else {
                    Toast message = Toast.makeText(getApplicationContext(), ErrorCode, Toast.LENGTH_LONG);
                    message.show();
                }                /*setContentView(R.layout.error_sync);
                Button reintent = (Button) findViewById(R.id.reintentar);
                reintent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(SyncActivity.this, SyncActivity.class);
                        startActivity(i);
                    }
                });
                SharedPreferences.Editor editor = app_preferences.edit();
                editor.putString("Celphone", "");
                editor.commit();*/
                super.onCancelled();
            }

            //Coloca datos de progress antes de arrancar
            //This gonna help for me
            @Override
            protected void onPreExecute() {
                progress.setTitle("Información");
                progress.setMessage("Actualizando pólizas...");
                //progress.setIcon(R.drawable.miituo);
                progress.setIndeterminate(true);
                progress.setCancelable(false);
                progress.show();
            }

            //va por listas de clientes
            //result es un List<InfoClient>
            @Override
            protected Void doInBackground(String... params) {
                ApiClient client = new ApiClient(MainActivity.this);
                try {
                    //recupera datos
                    //params[0] es el telefono
                    result = client.getInfoClient("InfoClientMobil/Celphone/" + params[0],MainActivity.this);
                    if(result == null){
                        progress.dismiss();
                        ErrorCode = "Estamos haciendo algunas actualizaciones, si sigues teniendo problemas para ingresar, contáctate con nosotros.";
                        this.cancel(true);
                    }else {
                        //guardar en Celphone el telefono que viene en params[0]
                        //get name
                        String na = result.get(0).getClient().getName();
                        app_preferences.edit().putString("nombre", na).apply();

                        //savedata into local DB
                        //use DAO's defined in data folders
                        //modelBase...
                        //we have result already...then get info and save into DB
                        //fisrt erase db
                        //recover iamges----------------------------------------------------------------
                        for (int i = 0; i < result.size(); i++) {
                            String filePathString = getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "frontal_" + result.get(i).getPolicies().getNoPolicy() + ".png";
                            File f = new File(filePathString);

                            //Si no hay imagen...intentamos recuperarla...
                            if (!f.exists() && result.get(i).getPolicies().isHasVehiclePictures()) {
                                try {
                                    String id = result.get(i).getPolicies().getId() + "";
                                    tokencliente = result.get(i).getClient().getToken();

                                    Bitmap m = client.DownloadPhoto("ImageSendProcess/GetFrontImageCarApp/" + id, MainActivity.this,tokencliente);
                                    if (m != null) {
                                        //before load...save it in order to not load again...
                                        FileOutputStream out = null;
                                        try {
                                            out = new FileOutputStream(filePathString);
                                            m.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                                            // PNG is a lossless format, the compression factor (100) is ignored
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        } finally {
                                            try {
                                                if (out != null) {
                                                    out.close();
                                                }
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        borrarDB();

                        for (int i = 0; i < result.size(); i++) {
                            InsurancePolicyDetail polizatemp = result.get(i).getPolicies();
                            ClientMovil clienttemp = result.get(i).getClient();
                            //String inse = inserta.execute(modelBase.FeedEntryUsuario.TABLE_NAME,clienttemp.getName(),clienttemp.getMotherName(),clienttemp.getLastName(),clienttemp.getCelphone(),clienttemp.getToken()).get();
                            String inse = saveData(modelBase.FeedEntryUsuario.TABLE_NAME, clienttemp.getName(), clienttemp.getMotherName(), clienttemp.getLastName(), clienttemp.getCelphone(), clienttemp.getToken());

                            if (!inse.equals("")) {

                                String date = new SimpleDateFormat("dd-MM-yyyy").format(polizatemp.getLimitReportDate());
                                //String insepoli = insertapoli.execute(
                                String insepoli = saveData(
                                        modelBase.FeedEntryPoliza.TABLE_NAME,
                                        clienttemp.getId() + "",
                                        polizatemp.getInsuranceCarrier().getName(),
                                        polizatemp.getLastOdometer() + "",
                                        polizatemp.getNoPolicy(),
                                        polizatemp.isHasOdometerPicture() + "",
                                        polizatemp.isHasVehiclePictures() + "",
                                        polizatemp.getReportState() + "",
                                        polizatemp.getRate() + "",
                                        date + "",
                                        polizatemp.getPaymentType());

                                if (!insepoli.equals("")) {

                                    VehicleMovil carritotemp = polizatemp.getVehicle();
                                    //String insecarrito = insertacar.execute(
                                    String insecarrito = saveData(
                                            modelBase.FeedEntryVehiculo.TABLE_NAME,
                                            polizatemp.getNoPolicy(),
                                            carritotemp.getBrand().getDescription(),
                                            carritotemp.getCapacity() + "",
                                            carritotemp.getColor(),
                                            carritotemp.getDescription().getDescription(),
                                            carritotemp.getModel().getModel() + "",
                                            carritotemp.getPlates(),
                                            carritotemp.getType().getDescription(),
                                            carritotemp.getSubtype().getDescription()
                                    );

                                    if (!insecarrito.equals("")) {
                                        //Log.w("Ok","Datos guardados correctamente");
                                    }
                                }
                            }
                        }

                        //Log.e("Aqui", "Actualizando datos de nuevo");
                        Celphone = params[0];
                    }
                } catch (IOException ex) {
                    ErrorCode = ex.getMessage();
                    this.cancel(true);
                }
                return null;
            }

            //Guardo y muestro infoClientList layout
            //recupero polizas
            @Override
            protected void onPostExecute(Void aVoid) {
                //guardo telefono en preferences
                SharedPreferences.Editor editor = app_preferences.edit();
                editor.putString("Celphone", Celphone);
                editor.commit();
                //inicio infoclientelist layout
                //setContentView(R.layout.infoclientlist);

                //Intent ii = new Intent(SyncActivity.this,PrincipalActivity.class);
                //Intent ii = new Intent(SyncActivity.this,TokenSmsActivity.class);
                //startActivity(ii);

                progress.dismiss();
                hilos = new threadtosync();
                hilos.sendToken.execute(cli);

                //String tuto = sharedPreferences.getString("tutohecho","null");
            }

            public String saveData(String... strings){
                //Aqui va el nombre de la tala
                String val = strings[0];
                //Log.w("Here",val);

                switch (val){
                    case modelBase.FeedEntryUsuario.TABLE_NAME:
                        // Gets the data repository in write mode
                        SQLiteDatabase db2 = DBaseMethods.base.getWritableDatabase();

                        // Create a new map of values, where column names are the keys
                        ContentValues values2 = new ContentValues();
                        //values.put(modelBase.FeedEntryArticle.COLUMN_ID, strings[6]);
                        values2.put(modelBase.FeedEntryUsuario.COLUMN_NAME, strings[1]);
                        values2.put(modelBase.FeedEntryUsuario.COLUMN_MOTHERNAME, strings[2]);
                        //values2.put(modelBase.FeedEntryUsuario.COLUMN_UBI, strings[3]);
                        values2.put(modelBase.FeedEntryUsuario.COLUMN_LASTNAME, strings[3]);
                        values2.put(modelBase.FeedEntryUsuario.COLUMN_CELPHONE, strings[4]);
                        values2.put(modelBase.FeedEntryUsuario.COLUMN_TOKEN, strings[5]);

                        // Insert the new row, returning the primary key value of the new row
                        //Just change name table and the values....
                        long newRowId2 = db2.insert(val, null, values2);
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        return "" + newRowId2;

                    case modelBase.FeedEntryPoliza.TABLE_NAME:
                        // Gets the data repository in write mode
                        SQLiteDatabase db = DBaseMethods.base.getWritableDatabase();

                        // Create a new map of values, where column names are the keys
                        ContentValues values = new ContentValues();
                        //values.put(modelBase.FeedEntryArticle.COLUMN_ID, strings[6]);
                        values.put(modelBase.FeedEntryPoliza.COLUMN_IDUSER, strings[1]);
                        values.put(modelBase.FeedEntryPoliza.COLUMN_INSURANCE, strings[2]);
                        values.put(modelBase.FeedEntryPoliza.COLUMN_LASTODOMETER, strings[3]);
                        values.put(modelBase.FeedEntryPoliza.COLUMN_NOPOLICY, strings[4]);
                        values.put(modelBase.FeedEntryPoliza.COLUMN_ODOMETERPIE, strings[5]);
                        values.put(modelBase.FeedEntryPoliza.COLUMN_VEHICLEPIE, strings[6]);
                        values.put(modelBase.FeedEntryPoliza.COLUMN_REPORT_STATE, strings[7]);
                        values.put(modelBase.FeedEntryPoliza.COLUMN_RATE, strings[8]);
                        values.put(modelBase.FeedEntryPoliza.COLUMN_LIMIT_DATE, strings[9]);
                        values.put(modelBase.FeedEntryPoliza.COLUMN_PAYMENT, strings[10]);

                        // Insert the new row, returning the primary key value of the new row
                        //Just change name table and the values....
                        long newRowId = db.insert(val, null, values);
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        return "" + newRowId;

                    case modelBase.FeedEntryVehiculo.TABLE_NAME:
                        // Gets the data repository in write mode
                        SQLiteDatabase dbvei = DBaseMethods.base.getWritableDatabase();

                        // Create a new map of values, where column names are the keys
                        ContentValues valuesvei = new ContentValues();
                        //values.put(modelBase.FeedEntryArticle.COLUMN_ID, strings[6]);
                        valuesvei.put(modelBase.FeedEntryVehiculo.COLUMN_POLIZA, strings[1]);
                        valuesvei.put(modelBase.FeedEntryVehiculo.COLUMN_BRAND, strings[2]);
                        valuesvei.put(modelBase.FeedEntryVehiculo.COLUMN_CAPACITY, strings[3]);
                        valuesvei.put(modelBase.FeedEntryVehiculo.COLUMN_COLOR, strings[4]);
                        valuesvei.put(modelBase.FeedEntryVehiculo.COLUMN_DESCRIPTION, strings[5]);
                        valuesvei.put(modelBase.FeedEntryVehiculo.COLUMN_MODEL, strings[6]);
                        valuesvei.put(modelBase.FeedEntryVehiculo.COLUMN_PLATES, strings[7]);
                        valuesvei.put(modelBase.FeedEntryVehiculo.COLUMN_TYPE, strings[8]);
                        valuesvei.put(modelBase.FeedEntryVehiculo.COLUMN_SUBTYPE, strings[9]);

                        // Insert the new row, returning the primary key value of the new row
                        //Just change name table and the values....
                        long newRowvei = dbvei.insert(val, null, valuesvei);
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        return "" + newRowvei;

                    default:
                        break;
                }

                return "";
            }

            public void borrarDB(){

                SQLiteDatabase db = DBaseMethods.base.getWritableDatabase();

                db.execSQL("DELETE FROM " + modelBase.FeedEntryUsuario.TABLE_NAME);
                db.execSQL("DELETE FROM " + modelBase.FeedEntryPoliza.TABLE_NAME);
                db.execSQL("DELETE FROM " + modelBase.FeedEntryVehiculo.TABLE_NAME);
                db.close();
            }
        };

        //************************** Asytask para obtener recuperat otken ws **********************************************
        AsyncTask<String, Void, Void> SyncToken = new AsyncTask<String, Void, Void>() {
            ProgressDialog progress = new ProgressDialog(MainActivity.this);
            String ErrorCode = "", Celphone = "";

            //Si cnacelo...inicio nuevo layout para reiniciar y borro de preferences
            @Override
            protected void onCancelled() {
                progress.dismiss();
                Toast message = Toast.makeText(getApplicationContext(), ErrorCode, Toast.LENGTH_LONG);
                message.show();
                setContentView(R.layout.error_sync);
                Button reintent = (Button) findViewById(R.id.reintentar);
                reintent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MainActivity.this, SyncActivity.class);
                        startActivity(i);
                    }
                });
                SharedPreferences.Editor editor = app_preferences.edit();
                editor.putString("Celphone", "");
                editor.commit();
                super.onCancelled();
            }

            //Coloca datos de progress antes de arrancar
            //This gonna help for me
            @Override
            protected void onPreExecute() {
                progress.setTitle("Información");
                progress.setMessage("Actualizando...");
                progress.setIcon(R.drawable.miituo);
                progress.setIndeterminate(true);
                progress.setCancelable(false);
                progress.show();
            }

            //va por listas de clientes
            //result es un List<InfoClient>
            @Override
            protected Void doInBackground(String... params) {
                ApiClient client = new ApiClient(MainActivity.this);
                try {
                    //recupera datos
                    //params[0] es el telefono
                    //result.clear();
                    //result = client.getInfoClient("InfoClientMobil/Celphone/" + params[0]);
                    token = client.getToken("TemporalToken/GetTemporalTokenPhone/" + params[0] + "/" + params[1]+ "/"+ params[2]);
                    //guardar en Celphone el telefono que viene en params[0]

                    //Log.e("Aqui", "Actualizando datos de nuevo");
                    Celphone = params[0];
                } catch (Exception ex) {
                    ErrorCode = ex.getMessage();
                    this.cancel(true);
                }
                return null;
            }

            //Guardo y muestro infoClientList layout
            //recupero polizas
            @Override
            protected void onPostExecute(Void aVoid) {
                //guardo telefono en preferences
                SharedPreferences.Editor editor = app_preferences.edit();
                editor.putString("Celphone", Celphone);
                editor.commit();
                //inicio infoclientelist layout
                //setContentView(R.layout.infoclientlist);

                //Intent ii = new Intent(SyncActivity.this,PrincipalActivity.class);
                Intent ii = new Intent(MainActivity.this, TokenSmsActivity.class);
                startActivity(ii);

                progress.dismiss();
            }
        };
    }
}
