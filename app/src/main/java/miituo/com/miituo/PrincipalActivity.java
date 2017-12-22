package miituo.com.miituo;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutionException;

import miituo.com.miituo.api.ApiClient;
import miituo.com.miituo.data.ClientMovil;
import miituo.com.miituo.data.DBaseMethods;
import miituo.com.miituo.data.IinfoClient;
import miituo.com.miituo.data.InfoClient;
import miituo.com.miituo.data.InsurancePolicyDetail;
import miituo.com.miituo.data.VehicleMovil;
import miituo.com.miituo.data.modelBase;

import static miituo.com.miituo.MainActivity.result;

import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareMediaContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;

public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView vList;
    private VehicleModelAdapter vadapter;
    private MenuAdapterSelf menuadapter;
    SharedPreferences app_preferences;

    public float velocidad;

    public TextView nombre;

    DrawerLayout drawer;
    private Typeface typeface;

    private SwipeRefreshLayout swipeContainer;

    public threadtosync hilos;

    LocationManager locationManager;

    LocationListener locationListener;

    public ShareButton shareButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Tus pólizas");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);

        app_preferences= PreferenceManager.getDefaultSharedPreferences(this);

        typeface = Typeface.createFromAsset(getAssets(), "fonts/herne1.ttf");

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent inte = new Intent(PrincipalActivity.this,SyncActivity.class);
                startActivity(inte);
            }
        });*/

        configurarVelocidad();

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                //fetchTimelineAsync(0);
                hilos = new threadtosync();
                hilos.Sync.execute(app_preferences.getString("Celphone","0"));
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        Typeface typefacebold = Typeface.createFromAsset(getAssets(), "fonts/herne.ttf");

        nombre = (TextView)findViewById(R.id.textViewNombreleo);
        nombre.setText(app_preferences.getString("nombre","null"));
        nombre.setTypeface(typefacebold);
        nombre = (TextView)findViewById(R.id.textViewNombre);
        nombre.setText(app_preferences.getString("nombre","null"));
        nombre.setTypeface(typefacebold);

        shareButton = (ShareButton)findViewById(R.id.share_btn);

        TextView hola = (TextView)findViewById(R.id.textView35);
        hola.setTypeface(typefacebold);
        TextView resumen = (TextView)findViewById(R.id.textView25);
        resumen.setTypeface(typeface);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        ListView lista = (ListView)findViewById(R.id.lst_menu_items);
        lista.setFastScrollEnabled(true);
        lista.setScrollingCacheEnabled(false);
        menuadapter = new MenuAdapterSelf(getApplicationContext(), typeface);
        lista.setAdapter(menuadapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0){
                    Intent i = new Intent(PrincipalActivity.this,PrincipalActivity.class);
                    startActivity(i);
                }else if(position == 1){
                    Intent i = new Intent(PrincipalActivity.this,AcercaActivity.class);
                    startActivity(i);
                }else if(position == 2){
                    Intent i = new Intent(PrincipalActivity.this,CotizaActivity.class);
                    startActivity(i);
                }
            }
        });

        TextView cerrar = (TextView)findViewById(R.id.textView62);
        cerrar.setTypeface(typeface);

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PrincipalActivity.this);
                builder.setTitle("Atención");
                builder.setMessage("¿Deseas cerrar sesión?");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = app_preferences.edit();
                        editor.putString("Celphone", "");
                        editor.putString("sesion", "");
                        editor.commit();

                        Intent intent = new Intent(PrincipalActivity.this,SyncActivity.class);
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alerta = builder.create();
                alerta.show();
            }
        });

        if(result!=null) {
            vList = (ListView) findViewById(R.id.listviewinfoclient);
            vadapter = new VehicleModelAdapter(getApplicationContext(), result,typeface);
            vList.setAdapter(vadapter);

            vList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(velocidad >= 5){
                        AlertDialog.Builder builder = new AlertDialog.Builder(PrincipalActivity.this);
                        builder.setTitle("¡Vas manejando!");
                        builder.setMessage("Reporta más tarde…");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(PrincipalActivity.this, SyncActivity.class);
                                startActivity(i);
                            }
                        });
                        AlertDialog alerta = builder.create();
                        alerta.show();
                    } else {
                        //Share image androd to facebook sdk
                        /*Bitmap image = BitmapFactory.decodeResource(PrincipalActivity.this.getResources(),R.drawable.miituo);
                        SharePhoto photo = new SharePhoto.Builder()
                                .setBitmap(image)
                                .build();
                        SharePhotoContent content = new SharePhotoContent.Builder()
                                .addPhoto(photo)
                                .build();

                        ShareLinkContent contentlink = new ShareLinkContent.Builder()
                                .setContentUrl(Uri.parse("https://miituo.com"))
                                .build();

                        ShareContent shareContent = new ShareMediaContent.Builder()
                                .addMedium(photo)
                                .build();

                        shareButton.setShareContent(shareContent);*/

                        //share content android native tool
                        /*Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.putExtra(Intent.EXTRA_TEXT,"Gracias por ser parte del consumo equitativo.");
                        String _ImageFile = "android.resource://" + getResources().getResourceName(R.drawable.atlas).replace(":", "/");
                        Uri imageUri = Uri.parse(_ImageFile);
                        share.setType("image/jpeg");
                        share.putExtra(Intent.EXTRA_STREAM,imageUri);
                        startActivity(Intent.createChooser(share,"Share via"));*/

                        /*PackageManager pm = getPackageManager();
                        List<ResolveInfo> activityList = pm.queryIntentActivities(share, 0);
                        for (final ResolveInfo app : activityList) {
                            if ((app.activityInfo.name).contains("facebook")) {
                                final ActivityInfo activity = app.activityInfo;
                                final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                                share.addCategory(Intent.CATEGORY_LAUNCHER);
                                //share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                                share.setComponent(name);
                                startActivity(Intent.createChooser(share,"Share via"));
                                break;
                            }
                        }*/

                        //startActivity(Intent.createChooser(share,"Share via"));

                        //Clic into row to launch activity
                        InfoClient item = (InfoClient) view.getTag();
                        //static class -- set client in this part
                        IinfoClient.setInfoClientObject(item);
                        IinfoClient.getInfoClientObject().getClient().setCelphone(app_preferences.getString("Celphone", "0"));

                        //firebase to get tokne....temp for now
                        String token = IinfoClient.getInfoClientObject().getClient().getToken();

                        //set token...
                        //IinfoClient.getInfoClientObject().getClient().setToken(token);
                        Intent i;
                        if (!item.getPolicies().isHasVehiclePictures() && !item.getPolicies().isHasOdometerPicture()) {
                            i = new Intent(PrincipalActivity.this, VehiclePictures.class);
                            app_preferences.edit().putString("odometro", "first").commit();
                            startActivity(i);
                        }
                        else if (item.getPolicies().isHasVehiclePictures() && !item.getPolicies().isHasOdometerPicture()) {
                            i = new Intent(PrincipalActivity.this, VehicleOdometer.class);
                            app_preferences.edit().putString("odometro", "first").commit();
                            startActivity(i);
                        } else if (item.getPolicies().getReportState() == 13) {
                            i = new Intent(PrincipalActivity.this, VehicleOdometer.class);
                            app_preferences.edit().putString("odometro", "mensual").commit();
                            startActivity(i);
                        } else if (item.getPolicies().getReportState() == 14) {
                            i = new Intent(PrincipalActivity.this, VehicleOdometer.class);
                            app_preferences.edit().putString("odometro", "cancela").commit();
                            startActivity(i);
                        } else if (item.getPolicies().getReportState() == 15) {
                            i = new Intent(PrincipalActivity.this, VehicleOdometer.class);
                            app_preferences.edit().putString("odometro", "ajuste").commit();
                            startActivity(i);
                        } else {
                            i = new Intent(PrincipalActivity.this, DetallesActivity.class);
                            startActivity(i);
                        }

                        //threadtogetfoto hilosfoto = new threadtogetfoto();
                        //hilosfoto.Sync.execute("58");
                    }
                }
            });
        }

        String act = getIntent().getStringExtra("actualizar");
        if(act != null) {
            if (act.equals("1")) {
                hilos = new threadtosync();
                hilos.Sync.execute(app_preferences.getString("Celphone", "0"));
            }
        }
    }

//--------------------------------------- Velocidad ------------------------------------------------
    private void configurarVelocidad() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //Log.i("Location", location.toString());
                //Toast.makeText(PrincipalActivity.this,"speed: "+location.getSpeed(),Toast.LENGTH_SHORT).show();
                velocidad = location.getSpeed();
            }
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }
            @Override
            public void onProviderEnabled(String s) {
            }
            @Override
            public void onProviderDisabled(String s) {
            }
        };

        // If device is running SDK < 23
        if (Build.VERSION.SDK_INT < 23) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        } else {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // ask for permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {
                // we have permission!
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
                //finish();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.ayuda) {
            // Handle the camera action
            Intent i = new Intent(PrincipalActivity.this,AcercaActivity.class);
            startActivity(i);
        }

        else if (id == R.id.cerrar) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Atención");
            builder.setMessage("¿Deseas cerrar sesión?");
            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences.Editor editor = app_preferences.edit();
                    editor.putString("Celphone", "");
                    editor.putString("sesion", "");
                    editor.apply();

                    Intent intent = new Intent(PrincipalActivity.this,SyncActivity.class);
                    startActivity(intent);
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            AlertDialog alerta = builder.create();
            alerta.show();

        } else if (id == R.id.polizas) {

        }/* else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//***********************************
    private class threadtosync {
//************************** Asytask para web service **********************************************
//************************** Asytask para obtener datos ws **********************************************
        AsyncTask<String, Void, Void> Sync = new AsyncTask<String, Void, Void>() {
            ProgressDialog progress = new ProgressDialog(PrincipalActivity.this);
            String ErrorCode = "", Celphone = "";

            //Si cnacelo...inicio nuevo layout para reiniciar y borro de preferences
            @Override
            protected void onCancelled() {
                progress.dismiss();
                Toast message = Toast.makeText(getApplicationContext(), "Error al actualizar. Intenté más tarde.", Toast.LENGTH_LONG);
                message.show();

                swipeContainer.setRefreshing(false);

                /*setContentView(R.layout.error_sync);
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
                //progress.show();
            }

            //va por listas de clientes
            //result es un List<InfoClient>
            @Override
            protected Void doInBackground(String... params) {
                ApiClient client = new ApiClient(PrincipalActivity.this);
                try {
                    //recupera datos
                    //params[0] es el telefono
                    //result.clear();

                    result = client.getInfoClient("InfoClientMobil/Celphone/" + params[0],PrincipalActivity.this);
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
                                        polizatemp.getPaymentType() + "");

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

                //progress.dismiss();

                String sesion = app_preferences.getString("sesion","null");
                //String tuto = sharedPreferences.getString("tutohecho","null");

                //RELOAD ADAPATER
                vList = (ListView)findViewById(R.id.listviewinfoclient);
                vadapter = new VehicleModelAdapter(getApplicationContext(), result,typeface);
                vList.setAdapter(vadapter);

                vList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        if(velocidad >= 5){
                            AlertDialog.Builder builder = new AlertDialog.Builder(PrincipalActivity.this);
                            builder.setTitle("¡Vas manejando!");
                            builder.setMessage("Reporta más tarde…");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(PrincipalActivity.this, SyncActivity.class);
                                    startActivity(i);
                                }
                            });
                            AlertDialog alerta = builder.create();
                            alerta.show();

                        }else {
                            InfoClient item = (InfoClient) view.getTag();
                            //static class -- set client in this part
                            IinfoClient.setInfoClientObject(item);
                            IinfoClient.getInfoClientObject().getClient().setCelphone(app_preferences.getString("Celphone", "0"));

                            //firebase to get tokne....temp for now
                            //String token = "tokenprueba";
                            //IinfoClient.getInfoClientObject().getClient().setToken(token);

                            Intent i;
                            if (!item.getPolicies().isHasVehiclePictures() && !item.getPolicies().isHasOdometerPicture()) {
                                i = new Intent(PrincipalActivity.this, VehiclePictures.class);
                                app_preferences.edit().putString("odometro", "first").commit();
                                startActivity(i);
                            }
                            else if (item.getPolicies().isHasVehiclePictures() && !item.getPolicies().isHasOdometerPicture()) {
                                i = new Intent(PrincipalActivity.this, VehicleOdometer.class);
                                app_preferences.edit().putString("odometro", "first").commit();
                                startActivity(i);
                            } else if (item.getPolicies().getReportState() == 13) {
                                i = new Intent(PrincipalActivity.this, VehicleOdometer.class);
                                app_preferences.edit().putString("odometro", "mensual").commit();
                                startActivity(i);
                            } else if (item.getPolicies().getReportState() == 14) {
                                i = new Intent(PrincipalActivity.this, VehicleOdometer.class);
                                app_preferences.edit().putString("odometro", "cancela").commit();
                                startActivity(i);
                            } else if (item.getPolicies().getReportState() == 15) {
                                i = new Intent(PrincipalActivity.this, VehicleOdometer.class);
                                app_preferences.edit().putString("odometro", "ajuste").commit();
                                startActivity(i);
                            } else {
                                i = new Intent(PrincipalActivity.this, DetallesActivity.class);
                                startActivity(i);
                            }
                        }
                    }
                });

                swipeContainer.setRefreshing(false);
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
                        valuesvei.put(modelBase.FeedEntryVehiculo.COLUMN_SUBTYPE, strings[8]);

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
    }

}


