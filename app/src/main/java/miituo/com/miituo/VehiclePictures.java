package miituo.com.miituo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.Manifest;

import miituo.com.miituo.api.ApiClient;
import miituo.com.miituo.data.IinfoClient;
import miituo.com.miituo.data.ImageProcessing;

public class VehiclePictures extends AppCompatActivity {

    public boolean flag1,flag2,flag3,flag4;

    private ImageView Img1,Img2,Img3,Img4,Img5;
    public static List<ImageProcessing> ImageList;
    private Button btn1,btn6 =null;
    private ApiClient api;

    public int PERMISO;
    public String NOMBRETEMP;

    final int FRONT_VEHICLE=1;
    final int SIDE_RIGHT_VEHICLE=2;
    final int REAR_VEHICLE=3;
    final int SIDE_LEFT_VEHICLE=4;
    final int ODOMETER=5;
    TextView leyenda;
    EditText odometeredit;
    //final String UrlApi="ImageProcessing/";
    final String UrlApi="ImageSendProcess/Array/";
    //final String UrlApi="http://10.69.237.103:1001/api/ImageProcessing/";
    final String UrlConfirmOdometer="ImageProcessing/ConfirmOdometer";
    //final String UrlConfirmOdometer="http://10.69.237.103:1001/api/ImageProcessing/ConfirmOdometer";
    Intent i;
    final static int constante=0;
    boolean IsTaken =false;
    Bitmap bmp;

    public File photoFile = null;
    public String mCurrentPhotoPath;
    private Typeface typeface;

    public String polizaFolio,tok;

    private static final int MY_CAMERA_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vehicle_pictures);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Fotografías");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);

        //get back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        polizaFolio = IinfoClient.getInfoClientObject().getPolicies().getNoPolicy();
        tok = IinfoClient.getInfoClientObject().getClient().getToken();
        flag1 = false;
        flag2 = false;
        flag3 = false;
        flag4 = false;

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        //Si o tengo todas la fotos...ejecuto view para capturar fitis que falta
        /*
        if(!IinfoClient.InfoClientObject.getPolicies().isHasVehiclePictures()) {
            setContentView(R.layout.activity_vehicle_pictures);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("Fotografías");
            toolbar.setTitleTextColor(Color.BLACK);
            setSupportActionBar(toolbar);

            btn1 = (Button)findViewById(R.id.btn1);

            //get label
            TextView tv = (TextView) findViewById(R.id.leyenda);
            //replace nombre con el nombre que es...
            tv.setText(tv.getText().toString().replace("{nombre}", IinfoClient.InfoClientObject.getClient().getName() + " " +
                    IinfoClient.InfoClientObject.getClient().getLastName() + " " +
                    IinfoClient.InfoClientObject.getClient().getMotherName()
            ));

            //inicializa imagens con clicklistener
            Init();
        }
        //Ya tengo todas las foos...excetp odometro
        else
        {
            //coloco view de caputarr odometro
            //setContentView(R.layout.vehicle_odometer);
            //Init5();

            //lanzamos intent con odometro view...
            Intent odo = new Intent(VehiclePictures.this,VehicleOdometer.class);
            startActivity(odo);
        }
        */
        //inicializa imagens con clicklistener
        TextView leyenda = (TextView)findViewById(R.id.leyenda);
        TextView res = (TextView)findViewById(R.id.textView36);

        Typeface typefacebold = Typeface.createFromAsset(getAssets(), "fonts/herne.ttf");

        typeface = Typeface.createFromAsset(getAssets(), "fonts/herne1.ttf");
        leyenda.setTypeface(typeface);
        res.setTypeface(typeface);
        Init();

        ImageList=new ArrayList<>();

        //show aler mensaje
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.mensaje_fotos);

        TextView textry = (TextView)dialog.findViewById(R.id.textView48);
        textry.setTypeface(typeface);
        textry = (TextView)dialog.findViewById(R.id.textView49);
        textry.setTypeface(typeface);
        textry = (TextView)dialog.findViewById(R.id.textView66);
        textry.setTypeface(typeface);
        textry = (TextView)dialog.findViewById(R.id.textView71);
        textry.setTypeface(typeface);
        textry = (TextView)dialog.findViewById(R.id.textView72);
        textry.setTypeface(typefacebold);
        textry = (TextView)dialog.findViewById(R.id.textView73);
        textry.setTypeface(typeface);
        textry = (TextView)dialog.findViewById(R.id.textView67);
        textry.setTypeface(typeface);
        textry = (TextView)dialog.findViewById(R.id.textView74);
        textry.setTypeface(typefacebold);
        textry = (TextView)dialog.findViewById(R.id.textView75);
        textry.setTypeface(typeface);
        textry = (TextView)dialog.findViewById(R.id.textView76);
        textry.setTypeface(typefacebold);
        textry = (TextView)dialog.findViewById(R.id.textView77);
        textry.setTypeface(typeface);
        textry = (TextView)dialog.findViewById(R.id.textView68);
        textry.setTypeface(typeface);

        TextView okbutton = (TextView)dialog.findViewById(R.id.textView70);
        okbutton.setTypeface(typeface);
        okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        api=new ApiClient(VehiclePictures.this);

        dialog.show();
    }

    public void tomarfoto(int p,String name){
        Intent takepic=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(i, FRONT_VEHICLE);
        if (takepic.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            try {
                photoFile = createImageFile(name,p);
            } catch (IOException ex) {
                // Error occurred while creating the File...
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(VehiclePictures.this, "miituo.com.miituo.provider", photoFile);
                //Uri photoURI = Uri.fromFile(photoFile);
                takepic.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takepic, p);
            }
        }
    }

    public void tomarfoto23(int p,String name){
        Intent takepic=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(i, FRONT_VEHICLE);
        if (takepic.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            try {
                photoFile = createImageFile(name,p);
            } catch (IOException ex) {
                // Error occurred while creating the File...
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                //Uri photoURI = FileProvider.getUriForFile(VehiclePictures.this, "miituo.com.miituo.provider", photoFile);
                Uri photoURI = Uri.fromFile(photoFile);
                takepic.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takepic, p);
            }
        }
    }

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
                        photoFile = createImageFile(NOMBRETEMP,PERMISO);
                    } catch (IOException ex) {
                        // Error occurred while creating the File...
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(VehiclePictures.this, "miituo.com.miituo.provider", photoFile);
                        //Uri photoURI = Uri.fromFile(photoFile);
                        takepic.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takepic, PERMISO);
                    }
                }
            } else {
                Toast.makeText(this, "No se pueden tomar fotos. Acceso denegado.", Toast.LENGTH_LONG).show();
            }
        }
    }

    void Init()
    {
        Img1=(ImageView)findViewById(R.id.Img1);
        Img2=(ImageView)findViewById(R.id.Img2);
        Img3=(ImageView)findViewById(R.id.Img3);
        Img4=(ImageView)findViewById(R.id.Img4);

        Img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT < 23) {

                    tomarfoto23(FRONT_VEHICLE,"frontal");
                }else{
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        NOMBRETEMP = "frontal";
                        PERMISO = FRONT_VEHICLE;
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                    }else{
                        tomarfoto(FRONT_VEHICLE,"frontal");
                    }
                }
            }
        });
        Img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT < 23) {

                    tomarfoto23(SIDE_RIGHT_VEHICLE,"derecho");

                }else {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        NOMBRETEMP = "derecho";
                        PERMISO = SIDE_RIGHT_VEHICLE;
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                    }else{
                        tomarfoto(SIDE_RIGHT_VEHICLE,"derecho");
                    }
                }
            }
        });
        Img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT < 23) {

                    tomarfoto23(REAR_VEHICLE,"back");

                }else {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        NOMBRETEMP = "back";
                        PERMISO = REAR_VEHICLE;
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                    }else{
                        tomarfoto(REAR_VEHICLE,"back");

                    }
                }
            }
        });
        Img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT < 23) {

                    tomarfoto23(SIDE_LEFT_VEHICLE,"izquierdo");
                }else {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        NOMBRETEMP = "izquierdo";
                        PERMISO = SIDE_LEFT_VEHICLE;
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                    }else{
                        tomarfoto(SIDE_LEFT_VEHICLE,"izquierdo");
                    }
                }
            }
        });
    }

    /*public void savefrontiamge(View v){
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
                //Uri photoURI = FileProvider.getUriForFile(VehiclePictures.this, "miituo.com.miituo", photoFile);
                Uri photoURI = Uri.fromFile(photoFile);
                takepic.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takepic, FRONT_VEHICLE);
            }
        }
    }*/

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
                Log.w("Alto camera",height+"");*/

                String filePath = mCurrentPhotoPath;
                //photoFile.getPath();

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 5;

                bmp = BitmapFactory.decodeFile(filePath,options);
                Bitmap resized;
                Bitmap resizedtoshow;

                resizedtoshow = Bitmap.createScaledBitmap(bmp, bmp.getWidth()/15, bmp.getHeight()/15, false);
                if(bmp.getHeight() > 3000){
                    resized = Bitmap.createScaledBitmap(bmp, bmp.getWidth()/4, bmp.getHeight()/4, false);
                }else{
                    resized = Bitmap.createScaledBitmap(bmp, bmp.getWidth()/2, bmp.getHeight()/2, false);
                }

                //OutputStream imagefile = new FileOutputStream(filePath);
                // Write 'bitmap' to file using JPEG and 80% quality hint for JPEG:
                //bmp.compress(Bitmap.CompressFormat.JPEG, 80, imagefile);

                if (requestCode == 1) {
                    flag1 = true;
                    //comprimer imagen antes de lanzarla al imageview
                    //bmp.compress(Bitmap.CompressFormat.JPEG,15);

                    //Img1.setImageBitmap(resizedtoshow);
                    Img1.setImageBitmap(bmp);
                }
                if (requestCode == 2) {
                    flag2 = true;
                    //Bundle ext=data.getExtras();
                    //bmp=(Bitmap)ext.get("data");
                    //Img2.setImageBitmap(resizedtoshow);
                    Img2.setImageBitmap(bmp);
                }
                if (requestCode == 3) {
                    flag3 = true;
                    //Bundle ext=data.getExtras();
                    //bmp=(Bitmap)ext.get("data");
                    //Img3.setImageBitmap(resizedtoshow);
                    Img3.setImageBitmap(bmp);
                }
                if (requestCode == 4) {
                    flag4 = true;
                    //Bundle ext=data.getExtras();
                    //bmp=(Bitmap)ext.get("data");
                    //Img4.setImageBitmap(resizedtoshow);
                    Img4.setImageBitmap(bmp);
                }
                if (requestCode == 5) {
                    //Bundle ext=data.getExtras();
                    //bmp=(Bitmap)ext.get("data");
                    //Img5.setImageBitmap(resizedtoshow);
                    Img5.setImageBitmap(bmp);
                }
                IsTaken = true;
                /*int existItem = getPositionItem(requestCode);
                if (existItem == -1) {
                    //si no exist...crea nuevo objectp
                    //bmp =
                    //String filePath = photoFile.getPath();
                    //Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                    ImageList.add(new ImageProcessing(resized, requestCode));
                } else {
                    //si existe...reemplza datos...
                    ImageList.get(existItem).setImage(resized);
                    ImageList.get(existItem).setImageType(requestCode);
                }*/
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private File createImageFile(String username, int tag) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PNG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+File.separator+username+"_"+polizaFolio+".png");
        //File image = FileProvider.getUriForFile(this,this.getApplicationContext().getPackageName() + ".provider",new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+File.separator+username+"_"+polizaFolio+".png")); //new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+File.separator+username+"_"+polizaFolio+".png");

        /*File image = File.createTempFile(
                imageFileName,  // prefix
                ".png",         // suffix
                storageDir      // directory
        );*/

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();

        SharedPreferences preferences = getSharedPreferences("miituo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("nombrefoto",mCurrentPhotoPath);
        editor.commit();
        return image;
    }

//******************************Thread to upload files***********************
    public void subirFotos(View v){
        //valida si ya subio todas las fotosv
        //if (ImageList.size() > 3 && !IinfoClient.InfoClientObject.getPolicies().isHasVehiclePictures()) {
        if (flag1 == true && flag2 == true && flag3 == true && flag4 == true && !IinfoClient.InfoClientObject.getPolicies().isHasVehiclePictures()) {
            AsyncTask<Void,Void,Void> sendVehiclePicture=new AsyncTask<Void, Void, Void>() {
                ProgressDialog progress=new ProgressDialog(VehiclePictures.this);
                String ErrorCode="";

                public void showAlerta(){
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(VehiclePictures.this);
                    builder.setTitle("Atención.");
                    builder.setMessage("Hubo un problema al subir las fotos. Intenta más tarde.");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i=new Intent(VehiclePictures.this,PrincipalActivity.class);
                            startActivity(i);
                        }
                    });

                    android.support.v7.app.AlertDialog alerta = builder.create();
                    alerta.show();
                }

                @Override
                protected void onCancelled() {
                    try {
                        //Toast msg = Toast.makeText(getApplicationContext(), "Ocurrio un Error:" + ErrorCode, Toast.LENGTH_LONG);
                        //msg.show();
                        progress.dismiss();
                        showAlerta();
                    }catch(Exception e){
                        showAlerta();
                    }
                }

                @Override
                protected void onPreExecute() {
                    try {
                        progress.setTitle("Subiendo Fotos de Vehículos");
                        progress.setMessage("Subiendo Fotos...");
                        //progress.setIcon(R.drawable.miituo);
                        progress.setCancelable(false);
                        progress.setIndeterminate(false);
                        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progress.setMax(4);
                        progress.setProgress(0);
                        progress.show();
                    }catch(Exception e){
                        showAlerta();
                    }
                }

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        //for(int i=0;i<ImageList.size();i++) {
                        for(int i=1;i<=4;i++) {
                            String username = "";
                            if(i==1)
                                username = "frontal";
                            if(i==2)
                                username = "derecho";
                            if(i==3)
                                username = "back";
                            if(i==4)
                                username = "izquierdo";

                            //File image = new File();

                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inSampleSize = 8;
                            Bitmap bit = BitmapFactory.decodeFile(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+File.separator+username+"_"+polizaFolio+".png",options);

                            api.UploadPhoto(i, bit, UrlApi,tok);
                            progress.incrementProgressBy(i);
                        }
                    }
                    catch (IOException ex)
                    {
                        ErrorCode=ex.getMessage();
                        this.cancel(true);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    try {
                        new AlertDialog.Builder(VehiclePictures.this)
                                .setTitle("Fotos de Vehículo")
                                .setMessage("Las fotos se han subido correctamente. !Gracias¡")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        progress.dismiss();
                                        IinfoClient.InfoClientObject.getPolicies().setHasVehiclePictures(true);

                                        ImageList = new ArrayList<ImageProcessing>();

                                        Intent odo = new Intent(VehiclePictures.this, VehicleOdometer.class);
                                        startActivity(odo);

                                        //setContentView(R.layout.vehicle_odometer);
                                        //Init5();
                                    }
                                })
                                .show();
                    }catch(Exception e){
                        showAlerta();
                    }
                }
            };

            sendVehiclePicture.execute();
        } else {
            //si no ha subido las fotos...err
            Toast msg = Toast.makeText(getApplicationContext(), "Falta tomar las fotos obligatorias", Toast.LENGTH_LONG);
            msg.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return true;
    }
}
