package miituo.com.miituo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.location.Criteria;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import miituo.com.miituo.api.ApiClient;
import miituo.com.miituo.data.IinfoClient;
import miituo.com.miituo.data.ImageProcessing;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.Manifest;

public class VehicleOdometer extends AppCompatActivity {

    public boolean flagodo = false;

    private ImageView Img1,Img2,Img3,Img4,Img5;
    private Button btn1,btn6 =null;
    final int FRONT_VEHICLE=1;
    final int SIDE_RIGHT_VEHICLE=2;
    final int REAR_VEHICLE=3;
    final int SIDE_LEFT_VEHICLE=4;
    final int ODOMETER=5;
    final String UrlConfirmOdometer="ImageProcessing/ConfirmOdometer";
    //final String UrlApi="ImageProcessing/";
    final String UrlApi="ImageSendProcess/Array";
    private ApiClient api;

    public List<ImageProcessing> ImageList;

    public File photoFile = null;
    public String mCurrentPhotoPath,tok;

    public EditText odometeredit,edit2;
    final static int constante=0;
    boolean IsTaken =false;
    Bitmap bmp;
    private Typeface typeface;

    LocationManager locationManager;

    LocationListener locationListener;

    Float speed;

    public String tipoodometro;
    SharedPreferences app_preferences;

    private static final int MY_CAMERA_REQUEST_CODE = 100;

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent takepic=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //startActivityForResult(i, FRONT_VEHICLE);
                if (takepic.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File...
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(VehicleOdometer.this, "miituo.com.miituo.provider", photoFile);
                        //Uri photoURI = Uri.fromFile(photoFile);
                        takepic.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takepic, ODOMETER);
                    }
                }
            } else {
                Toast.makeText(this, "No se pueden tomar fotos. Acceso denegado.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_odometer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle("Odómetro");
        //toolbar.setTitleTextColor(Color.BLACK);
        //setSupportActionBar(toolbar);

        //get back arrow
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        tok = IinfoClient.getInfoClientObject().getClient().getToken();

        typeface = Typeface.createFromAsset(getAssets(), "fonts/herne1.ttf");
        TextView leyenda = (TextView)findViewById(R.id.textView40);
        TextView res = (TextView)findViewById(R.id.textView38);
        leyenda.setTypeface(typeface);
        res.setTypeface(typeface);

        ImageList=new ArrayList<>();

        /*locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        try {
            speed = location.getSpeed();
            //Log.i("Location", speed+"");
        } catch (NullPointerException e) {
            speed = 0.0f;
            //Log.i("Location error", speed+"");
        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                speed = location.getSpeed();
                //Log.i("Location", speed+"");
                //Toast.makeText(VehicleOdometer.this,"Velocidad: "+speed,Toast.LENGTH_SHORT).show();
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
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // ask for permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                // we have permission!
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }*/
        ImageButton back = (ImageButton)findViewById(R.id.imageView12);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i=new Intent(VehicleOdometer.this,PrincipalActivity.class);
                //startActivity(i);
                finish();
            }
        });
        Typeface typefacebold = Typeface.createFromAsset(getAssets(), "fonts/herne.ttf");
        TextView titulo = (TextView)findViewById(R.id.textView27);
        titulo.setTypeface(typefacebold);

        Init5();
        api=new ApiClient(VehicleOdometer.this);
    }

    void Init5()
    {
        //edittext with odometer
        //odometeredit = (EditText)findViewById(R.id.editText);
        //edit2 = (EditText)findViewById(R.id.editText2);

        Img5=(ImageView)findViewById(R.id.img5);
        btn6=(Button)findViewById(R.id.btn6);
        /*btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });*/

        //lanzar picturoe para capturar foto
        Img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tomarFotografia();
            }
        });
    }

    public void tomarFotografia(){

        if (Build.VERSION.SDK_INT < 23) {
            Intent takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //startActivityForResult(i, FRONT_VEHICLE);
            if (takepic.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File...
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    //Uri photoURI = FileProvider.getUriForFile(VehicleOdometer.this, "miituo.com.miituo.provider", photoFile);
                    Uri photoURI = Uri.fromFile(photoFile);
                    takepic.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takepic, ODOMETER);
                }
            }
        }else{
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //PERMISO = FRONT_VEHICLE;
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
            }else{
                Intent takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //startActivityForResult(i, FRONT_VEHICLE);
                if (takepic.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File...
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(VehicleOdometer.this, "miituo.com.miituo.provider", photoFile);
                        //Uri photoURI = Uri.fromFile(photoFile);
                        takepic.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takepic, ODOMETER);
                    }
                }
            }
        }
    }

    public void subirFoto(View v){
        //final String odovalue = odometeredit.getText().toString();
        //if(!odovalue.equals(""))
        //{

        //alto....primero abro camara y luego el resto
        if (btn6.getText().toString().equals("Tomar fotografía")){
            tomarFotografia();
        }else {
            //si ya tengo tooooodas lass fotos....se suben...
            if (flagodo == true) {
            //if ((ImageList.size() > 4 && !IinfoClient.InfoClientObject.getPolicies().isHasVehiclePictures()) || (IinfoClient.InfoClientObject.getPolicies().isHasVehiclePictures() && ImageList.size() > 0)) {

                final AsyncTask<Void, Void, Void> sendOdometro = new AsyncTask<Void, Void, Void>() {
                    ProgressDialog progress = new ProgressDialog(VehicleOdometer.this);
                    String ErrorCode = "";

                    public void showAlerta(){
                        AlertDialog.Builder builder = new AlertDialog.Builder(VehicleOdometer.this);
                        builder.setTitle("Atención");
                        builder.setMessage("Hubo un problema. Intenta más tarde");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i=new Intent(VehicleOdometer.this,PrincipalActivity.class);
                                startActivity(i);
                            }
                        });

                        AlertDialog alerta = builder.create();
                        alerta.show();
                    }

                    @Override
                    protected void onPreExecute() {
                        try {
                            progress.setTitle("Registro de odómetro");
                            progress.setMessage("Subiendo imagen...");
                            //progress.setIcon(R.drawable.miituo);
                            progress.setCancelable(false);
                            progress.setIndeterminate(true);
                            progress.show();
                        }
                        catch(Exception e){

                            showAlerta();

                        }
                    }

                    @Override
                    protected Void doInBackground(Void... params) {
                        try {

                        /*if (!IinfoClient.InfoClientObject.getPolicies().isHasVehiclePictures() || ImageList.size() > 4)
                            api.UploadPhoto(ImageList.get(4).getImageType(), ImageList.get(4).getImage(), UrlApi);
                        else*/
                            //Subimos foto de odometro.....

                            SharedPreferences preferences = getSharedPreferences("miituo", Context.MODE_PRIVATE);
                            mCurrentPhotoPath = preferences.getString("nombrefoto", "null");

                            String filePath = mCurrentPhotoPath;

                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inSampleSize = 4;
                            bmp = BitmapFactory.decodeFile(filePath,options);

                            app_preferences= PreferenceManager.getDefaultSharedPreferences(VehicleOdometer.this);
                            tipoodometro = app_preferences.getString("odometro","null");

                            if(tipoodometro.equals("cancela"))
                            {
                                //api.UploadPhoto(6, ImageList.get(0).getImage(), UrlApi);
                                api.UploadPhoto(6, bmp, UrlApi,tok);
                                IinfoClient.InfoClientObject.getPolicies().setRegOdometer(Integer.parseInt("1000"));
                            }else{
                                //api.UploadPhoto(ImageList.get(0).getImageType(), ImageList.get(0).getImage(), UrlApi);
                                api.UploadPhoto(5, bmp, UrlApi,tok);
                                IinfoClient.InfoClientObject.getPolicies().setRegOdometer(Integer.parseInt("1000"));
                            }


                            //After upload the photo, we have to conform odometer with ConformOdometer api
                                /*Boolean response;
                                if (!IinfoClient.InfoClientObject.getPolicies().isHasVehiclePictures() || ImageList.size() > 4)
                                    response = api.ConfirmOdometer(ImageList.get(4).getImageType(), UrlConfirmOdometer);
                                else
                                    response = api.ConfirmOdometer(ImageList.get(0).getImageType(), UrlConfirmOdometer);
                                if (!response) {
                                    Toast msg = Toast.makeText(getApplicationContext(), "Error inesperado, no se pudo procesar la imagen", Toast.LENGTH_LONG);
                                    msg.show();
                                } else {
                                    Intent i = new Intent(VehicleOdometer.this, SyncActivity.class);
                                    startActivity(i);
                                }*/
                        } catch (IOException ex) {
                            ErrorCode = ex.getMessage();
                            this.cancel(true);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {

                        try {
                            progress.dismiss();
                            Intent i = new Intent(VehicleOdometer.this, ConfirmActivity.class);
                            startActivity(i);
                        }
                        catch (Exception e){
                            showAlerta();
                        }
                    }

                    @Override
                    protected void onCancelled() {
                        try {
                            if (ErrorCode.equals("1000")) {
                                Intent i = new Intent(VehicleOdometer.this, ConfirmActivity.class);
                                startActivity(i);

                                progress.dismiss();

                            } else {
                                Toast msg = Toast.makeText(getApplicationContext(), "Ocurrio un Error:" + ErrorCode, Toast.LENGTH_LONG);
                                msg.show();
                                progress.dismiss();
                            }
                        }catch (Exception e){
                            showAlerta();
                        }
                    }

                };
                sendOdometro.execute();
            } else {
                Toast msg = Toast.makeText(getApplicationContext(), "Tome la foto del odómetro para finalizar", Toast.LENGTH_LONG);
                msg.show();
            }
        }
    }

    //Al tomar la foto...la procesamos en el activty for result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK)
        {
            try {
                SharedPreferences preferences = getSharedPreferences("miituo", Context.MODE_PRIVATE);
                mCurrentPhotoPath = preferences.getString("nombrefoto", "null");

                /*Camera camera = Camera.open(0);
                android.hardware.Camera.Parameters parameters = camera.getParameters();
                android.hardware.Camera.Size size = parameters.getPictureSize();

                int height = size.height;
                int width = size.width;

                Log.w("Ancho camera",width+"");
                Log.w("Alto camera",height+"");
                camera.release();*/

                String filePath = mCurrentPhotoPath;
                //photoFile.getPath();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 5;

                bmp = BitmapFactory.decodeFile(filePath,options);

                Bitmap resized;
                Bitmap resizedtoshow;

                resizedtoshow = Bitmap.createScaledBitmap(bmp, bmp.getWidth()/8, bmp.getHeight()/8, false);
                if(bmp.getHeight() > 3000){

                    resized = Bitmap.createScaledBitmap(bmp, bmp.getWidth()/4, bmp.getHeight()/4, false);
                }else{

                    resized = Bitmap.createScaledBitmap(bmp, bmp.getWidth()/2, bmp.getHeight()/2, false);
                }

                //OutputStream imagefile = new FileOutputStream(filePath);
                // Write 'bitmap' to file using JPEG and 80% quality hint for JPEG:
                //bmp.compress(Bitmap.CompressFormat.JPEG, 80, imagefile);
                //Bundle ext = data.getExtras();
                //bmp = (Bitmap) ext.get("data");
                /*if (requestCode == 1)
                    Img1.setImageBitmap(resized);
                if (requestCode == 2)
                    Img2.setImageBitmap(resized);
                if (requestCode == 3)
                    Img3.setImageBitmap(resized);
                if (requestCode == 4)
                    Img4.setImageBitmap(resized);*/
                if (requestCode == 5) {
                    flagodo = true;
                    Img5.setImageBitmap(bmp);
                }
                IsTaken = true;
                /*int existItem = getPositionItem(requestCode);
                if (existItem == -1)
                    //si no exist...crea nuevo objectp
                    ImageList.add(new ImageProcessing(resized, requestCode));
                else {
                    //si existe...reemplza datos...
                    ImageList.get(existItem).setImage(resized);
                    ImageList.get(existItem).setImageType(requestCode);
                }*/

                btn6.setText("Continuar");
            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }

    int getPositionItem(int Type)
    {
        int result= -1;
        for(int i=0; i<ImageList.size();i++)
        {
            if(ImageList.get(i).getImageType() == Type)
                return i;
        }

        return result;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PNG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".png",         // suffix
                storageDir      // directory
        );

        //File image = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+File.separator+"odometro_"+polizaFolio+".png");

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();

        SharedPreferences preferences = getSharedPreferences("miituo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("nombrefoto",mCurrentPhotoPath);
        editor.commit();
        return image;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.manu_principal,menu);
        //invalidateOptionsMenu();

        //Aqui necesito el estatus para validarlo y saber si muestro o no el menu...
        //when push notification...open activity to take odometer

        //get regOdometer and validate...
        //validate regOdometer to set visible the menu...
        //String res = "0";
        //if(res.equals("1")){

        /*else if (regOdometer == 13){

        }*/

        //MenuItem item = menu.findItem(R.id.rpt_odometer);
        //item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        /*switch (item.getItemId())
        {
            case R.id.rpt_odometer:
                Intent i=new Intent(this,ReportOdometerActivity.class);
                startActivity(i);
                break;
        }*/

        if (item.getItemId() == android.R.id.home) {
            //finish(); // close this activity and return to preview activity (if there is any)
            //Intent i=new Intent(this,PrincipalActivity.class);
            //startActivity(i);
        }
        return true;
    }
}
