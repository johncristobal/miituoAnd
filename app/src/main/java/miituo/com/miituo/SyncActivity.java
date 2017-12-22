package miituo.com.miituo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import miituo.com.miituo.api.ApiClient;
import miituo.com.miituo.data.ClientMovil;
import miituo.com.miituo.data.Coverage;
import miituo.com.miituo.data.DBaseMethods;
import miituo.com.miituo.data.IinfoClient;
import miituo.com.miituo.data.InfoClient;
import miituo.com.miituo.data.InsuranceCarrier;
import miituo.com.miituo.data.InsurancePolicyDetail;
import miituo.com.miituo.data.InsurancePolicyState;
import miituo.com.miituo.data.VehicleBrand;
import miituo.com.miituo.data.VehicleDescription;
import miituo.com.miituo.data.VehicleModel;
import miituo.com.miituo.data.VehicleMovil;
import miituo.com.miituo.data.VehicleSubtype;
import miituo.com.miituo.data.VehicleType;
import miituo.com.miituo.data.modelBase;

import static miituo.com.miituo.MainActivity.result;
import static miituo.com.miituo.MainActivity.token;

public class SyncActivity extends AppCompatActivity {
    //Array Adapter that will hold our ArrayList and display the items on the ListView
    private ListView vList;
    private VehicleModelAdapter vadapter;
    SharedPreferences app_preferences;
    TextView txtCelphone;
    EditText telefono;

    //public static String token;

    public threadtosync hilos;

    public Typeface typeface;

    public ClientMovil cli;
    public String tokencliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_sync);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //TextView hola = (TextView)findViewById(R.id.textView3);
        //Typeface font = Typeface.createFromAsset(getAssets(),"fonts/herne.ttf");//.createFromAsset(getAssets(), "herne.ttf");
        //hola.setTypeface(font);

        //font
        /*RelativeLayout relative = (RelativeLayout)findViewById(R.id.layoutSyncPrincipal);
        for( int i = 0; i < relative.getChildCount(); i++ ) {
            if (relative.getChildAt(i) instanceof TextView) {
                TextView txt = (TextView) relative.getChildAt(i);
                //Typeface font = Typeface.createFromFile("android.resource://" + getPackageName() + "/" +R.raw.herne);//.createFromAsset(getAssets(), "herne.ttf");
                //Typeface font = Typeface.defaultFromStyle(R.raw.herne);//"android.resource://" + getPackageName() + "/" +R.raw.herne);//.createFromAsset(getAssets(), "herne.ttf");
                Typeface font = Typeface.createFromAsset(getAssets(), "herne.ttf");
                txt.setTypeface(font);
            }
        }*/

        typeface = Typeface.createFromAsset(getAssets(), "fonts/herne1.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "fonts/herne.ttf");

        TextView hola = (TextView)findViewById(R.id.textView3);
        TextView hola1 = (TextView)findViewById(R.id.textView2);
        TextView hola2 = (TextView)findViewById(R.id.textView7);
        TextView hola3 = (TextView)findViewById(R.id.textView5);
        TextView hola4 = (TextView)findViewById(R.id.textView10);
        TextView hola5 = (TextView)findViewById(R.id.textView13);
        TextView hola6 = (TextView)findViewById(R.id.textView16);

        hola.setTypeface(typeface);
        hola1.setTypeface(typeface);
        hola2.setTypeface(typeface2);
        hola3.setTypeface(typeface);
        hola4.setTypeface(typeface);
        hola5.setTypeface(typeface);
        hola6.setTypeface(typeface);

        hilos = new threadtosync();

        app_preferences= PreferenceManager.getDefaultSharedPreferences(this);
        telefono = (EditText)findViewById(R.id.cephonetext);

        //Hide also this code togglebutton --1
        /*final ToggleButton toggle = (ToggleButton)findViewById(R.id.toggleButton);
        toggle.setTextOff("dev");
        toggle.setTextOn("qas");

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    toggle.setTextOn("qas");
                    app_preferences.edit().putString("api","http://miituodev.sytes.net:1003/api/").apply();
                }else{
                    toggle.setTextOff("dev");
                    app_preferences.edit().putString("api","http://miituodev.sytes.net:1001/api/").apply();
                }
            }
        });*/

        try
        {
            /*StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String mPhoneNumber = tMgr.getLine1Number();*/
            String mPhoneNumber = "";
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            AlertDialog alert = builder.create();
            if (!app_preferences.contains("Celphone")) {
                if (mPhoneNumber.equals("")) {
                    //alert.setTitle("Aviso");
                    //alert.setMessage("No se pudo obtener el número teléfonico de la SIM CARD, por lo que tendrás que ingresarlo de manera manual");
                    //alert.show();
                }else {
                    telefono.setText("");
                }
            }
            else
            {
                if(!app_preferences.getString("Celphone", "Default").equals("")) {
                    TextView txtCelphone = (TextView) findViewById(R.id.cephonetext);
                    txtCelphone.setText(app_preferences.getString("Celphone", "Default"));
                    GetSync(null);
                }
            }

            lanzaralertapush();

        }catch(Exception e){
            e.printStackTrace();
        }

        Button b4 = (Button)findViewById(R.id.button4);
        b4.setTypeface(typeface);

        //every three digits, must set a coma in the edit...
        telefono = (EditText) findViewById(R.id.cephonetext);
        telefono.setTypeface(typeface);
        telefono.addTextChangedListener(new TextWatcher() {
            boolean isEdiging;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    private void lanzaralertapush() {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Atención usuario");
        builder.setMessage("Le recordamos que es importante activar las notificaciones para saber cuando reportar.");
        builder.setPositiveButton("Si, dejar notificaciones activas", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                app_preferences.edit().putString("pushalert","si").apply();
            }
        });

        builder.setNegativeButton("No, desactivar notificaciones", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                app_preferences.edit().putString("pushalert","no").apply();

            }
        });


        String alerta = app_preferences.getString("pushalert", "null");
        if (alerta.equals("null")) {
            android.support.v7.app.AlertDialog alertashow = builder.create();
            alertashow.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_listapolizas,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId())
        {
            /*
            * Autor: moon
            * Ojo, aqui no lo borres aun, podria dar en cancelar
            * cuando tngas el nuev numeero, entonces si borras y actualizas
            */
            case R.id.cambia_numero:
                SharedPreferences.Editor editor = app_preferences.edit();
                editor.putString("Celphone", "");
                editor.commit();
                setContentView(R.layout.activity_sync);

                break;
        }
        return true;
    }

    /*
    * Autor: moon
    * Thread to get data (json) from WS
    * Show listview with data
    * */

    public void openView(View v){
        try {
            Intent i = new Intent(this, CotizaActivity.class);
            startActivity(i);
        }
        catch(Exception e){
            Toast.makeText(this,"No se puede abrir la página.",Toast.LENGTH_SHORT).show();
        }
    }

//-------------Llamada del boton to call    ---------------------------------
    public void GetSync(View view)
    {
        final TextView celphone = (TextView) findViewById(R.id.cephonetext);
        //valida si hay numero de telefono
        /*try {
            NumberFormat format = NumberFormat.getInstance(Locale.US);
            Number number = format.parse(celphone.getText().toString());
            double d = number.doubleValue();
            Log.w("fon", d+"");
        }catch(Exception e){}
        */

        if(!celphone.getText().toString().equals(""))
        {
            //valida si esta conectado a la red
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);//getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                //recupero telefono
                txtCelphone = (TextView) findViewById(R.id.cephonetext);

                //Aqui manda a llamar a asynktask .. envia token de firebase para guardar device unico
                cli = new ClientMovil(0, null, null, null);
                cli.setCelphone(txtCelphone.getText().toString());
                String token = FirebaseInstanceId.getInstance().getToken();
                //String token = "tokenprueba";
                cli.setToken(token);

                //now send first infolientmobile
                hilos = new threadtosync();
                hilos.Sync.execute(txtCelphone.getText().toString());
                //hilos.sendToken.execute(cli);
            }
            else {
                //else de sin conexion...
                Toast.makeText(getApplicationContext(),"Sin conexión a Internet. Intente más tarde.",Toast.LENGTH_LONG).show();
                try {
                    //result se llena directamente de la base...& launch principal
                    //get data from DataBase
                    DBaseMethods.ThreadDBRead leerdata = new DBaseMethods.ThreadDBRead();
                    DBaseMethods.ThreadDBRead leerdatapoli = new DBaseMethods.ThreadDBRead();
                    DBaseMethods.ThreadDBRead leerdatacarro = new DBaseMethods.ThreadDBRead();
                    ArrayList<Object> datos = leerdata.execute(modelBase.FeedEntryUsuario.TABLE_NAME).get();
                    ArrayList<Object> polizas = leerdatapoli.execute(modelBase.FeedEntryPoliza.TABLE_NAME).get();
                    ArrayList<Object> carritos = leerdatacarro.execute(modelBase.FeedEntryVehiculo.TABLE_NAME).get();

                    List<InfoClient> InfoList = new ArrayList<>();//List<InfoClient>();
                    if(datos.size() != 0) {
                        for (int k = 0; k < datos.size(); k++) {
                            ClientMovil auto1 = (ClientMovil) datos.get(k);

                            VehicleMovil autito = (VehicleMovil) carritos.get(k);//new VehicleMovil(0, "color", "placas", "motor", 5, new VehicleBrand(0, "descr"), new VehicleModel(0, 10), new VehicleSubtype(0, "subtype"), new VehicleType(0, "type"), new VehicleDescription(0, "vehicledesc"));
                            InsurancePolicyDetail poliza1 = (InsurancePolicyDetail) polizas.get(k);//new InsurancePolicyDetail(false, false, 1, "numpoliza", new Date(), new Date(), 0.0f, 100, new Date(), new InsuranceCarrier(0, "carrier"), new InsurancePolicyState(0, "description"), new Coverage("coverage", 0), autito, 12);

                            poliza1.setVehicle(autito);

                            InfoClient ciente1 = new InfoClient(poliza1, auto1);
                            InfoList.add(ciente1);
                        }
                        result = InfoList;

                        //readDataBase();
                        Intent ii = new Intent(SyncActivity.this, PrincipalActivity.class);
                        startActivity(ii);
                    }else{

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        else
        {
            //introducir numero celular
            Toast.makeText(getApplicationContext(),"Introduce número de teléfono",Toast.LENGTH_LONG).show();
        }
    }

    //***********************************
    private class threadtosync {
//************************** Asytask para web service **********************************************
        AsyncTask<ClientMovil, Void, Void> sendToken = new AsyncTask<ClientMovil, Void, Void>() {
            String ErrorCode = "";

            @Override
            protected void onCancelled() {
                super.onCancelled();
                Toast msg = Toast.makeText(getApplicationContext(), "Error de conexión. Intenta más tarde.", Toast.LENGTH_LONG);
                msg.show();
            }

            @Override
            protected Void doInBackground(ClientMovil... params) {
                ApiClient client = new ApiClient(SyncActivity.this);
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
                //Creamos alert para validar odometro antes de enviarlo...
                //Ejecuto hilo para validar telefono
                String sesion = app_preferences.getString("sesion","null");
                //String tuto = sharedPreferences.getString("tutohecho","null");

                if(sesion.equals("1")) {
                    //Intent ii = new Intent(SyncActivity.this,PrincipalActivity.class);
                    Intent ii = new Intent(SyncActivity.this, PrincipalActivity.class);
                    startActivity(ii);
                }else {
                    //UN NUEVI HILo para recuepera token de seguridad
                    hilos = new threadtosync();
                    hilos.SyncToken.execute(txtCelphone.getText().toString(), result.get(0).getPolicies().getId() + "", "Apptoken");
                }
                //hilos.Sync.execute(txtCelphone.getText().toString());
            }
        };

//************************** Asytask para obtener datos ws **********************************************
        AsyncTask<String, Void, Void> Sync = new AsyncTask<String, Void, Void>() {
            ProgressDialog progress = new ProgressDialog(SyncActivity.this);
            String ErrorCode = "", Celphone = "";

            //Si cnacelo...inicio nuevo layout para reiniciar y borro de preferences
            @Override
            protected void onCancelled() {
                progress.dismiss();
                if (ErrorCode.contains("null")){
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(SyncActivity.this);
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
                ApiClient client = new ApiClient(SyncActivity.this);
                try {
                    //recupera datos
                    //params[0] es el telefono
                    result = client.getInfoClient("InfoClientMobil/Celphone/" + params[0],SyncActivity.this);
                    //result = null;
                    if(result == null){
                        progress.dismiss();
                        ErrorCode = "Estamos haciendo algunas actualizaciones, si sigues teniendo problemas para ingresar, contáctate con nosotros.";
                        this.cancel(true);
                    } else {
                        //guardar en Celphone el telefono que viene en params[0]
                        //get name
                        String na = result.get(0).getClient().getName();
                        tokencliente = result.get(0).getClient().getToken();
                        app_preferences.edit().putString("nombre", na).apply();

                        //recover iamges----------------------------------------------------------------
                        for (int i = 0; i < result.size(); i++) {
                            String filePathString = getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "frontal_" + result.get(i).getPolicies().getNoPolicy() + ".png";
                            File f = new File(filePathString);

                            //Si no hay imagen...intentamos recuperarla...
                            if (!f.exists() && result.get(i).getPolicies().isHasVehiclePictures()) {
                                try {
                                    String id = result.get(i).getPolicies().getId() + "";

                                    Bitmap m = client.DownloadPhoto("ImageSendProcess/GetFrontImageCarApp/" + id, SyncActivity.this,tokencliente);
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
                        //savedata into local DB
                        //use DAO's defined in data folders
                        //modelBase...
                        //we have result already...then get info and save into DB
                        //fisrt erase db
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
                    }

                    //Log.e("Aqui", "Actualizando datos de nuevo");
                    Celphone = params[0];
                } catch (Exception ex) {
                    if(ErrorCode.equals("")) {
                        ErrorCode = ex.getMessage();
                    }
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
            ProgressDialog progress = new ProgressDialog(SyncActivity.this);
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
                        Intent i = new Intent(SyncActivity.this, SyncActivity.class);
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
                //progress.setIcon(R.drawable.miituo);
                progress.setIndeterminate(true);
                progress.setCancelable(false);
                progress.show();
            }

            //va por listas de clientes
            //result es un List<InfoClient>
            @Override
            protected Void doInBackground(String... params) {
                ApiClient client = new ApiClient(SyncActivity.this);
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
                Intent ii = new Intent(SyncActivity.this, TokenSmsActivity.class);
                startActivity(ii);

                progress.dismiss();
            }
        };
    }
}
