package miituo.com.miituo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import miituo.com.miituo.api.ApiClient;
import miituo.com.miituo.data.IinfoClient;
import miituo.com.miituo.data.ImageProcessing;

public class ReportOdometerActivity extends AppCompatActivity {

    public EditText odometro,edit2;
    String respuesta;

    public File photoFile = null;
    public String mCurrentPhotoPath;

    Bitmap bmp;

    ImageView rptView;
    Button btn_sendRpt;
    Toast msg;
    List<ImageProcessing> ImgList;
    //final String UrlApi="ImageProcessing/";
    final String UrlApi="ImageSendProcess/Array/";
    final String ApiSendReport="ReportOdometer/";
    public ApiClient api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Use the same layout to take iocture odometer
        setContentView(R.layout.activity_vehicle_odometer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Odómetro");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);

        //odometro = (EditText)findViewById(R.id.editTextodo);
        //edit2 = (EditText)findViewById(R.id.editText2odo);

        /*odometro.addTextChangedListener(new TextWatcher() {
            boolean isEdiging;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(isEdiging) return;
                isEdiging = true;

                if(s.toString().equals("")){
                    isEdiging = false;
                    return;
                }

                try {
                    String str = s.toString().replaceAll("[^\\d]", "");
                    double s1 = Double.parseDouble(str);

                    NumberFormat nf2 = NumberFormat.getInstance(Locale.ENGLISH);
                    ((DecimalFormat) nf2).applyPattern("$ ###,###.###");
                    s.replace(0, s.length(), nf2.format(s1));

                    isEdiging = false;
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });*/
        api=new ApiClient(ReportOdometerActivity.this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ImgList=new ArrayList<>();
        rptView=(ImageView)findViewById(R.id.rpt_odometerImageView);
        btn_sendRpt=(Button)findViewById(R.id.btn_rptodometer);
        rptView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //startActivityForResult(i,5);

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
                        startActivityForResult(takepic, 5);
                    }
                }

            }
        });
        btn_sendRpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ImgList.size()>0)
                {
                    AsyncTask<Void,Void,Void> sendOdometerPicture=new AsyncTask<Void, Void, Void>() {
                        ProgressDialog progress=new ProgressDialog(ReportOdometerActivity.this);
                        String ErrorCode="";
                        @Override
                        protected void onPreExecute() {
                            progress.setTitle("Procesando Imagen");
                            progress.setMessage("Se esta procesando la imagen para obtener odómetro");
                            progress.setIcon(R.drawable.miituo);
                            progress.setIndeterminate(true);
                            progress.setCancelable(false);
                            progress.show();
                        }

                        @Override
                        protected Void doInBackground(Void... params) {
                            try
                            {
                                //subimos la imagens y ahora tmb subimos el odometro para confirmar y luego abrimos el alert
                                api.UploadPhoto(ImgList.get(0).getImageType(), ImgList.get(0).getImage(), UrlApi,"");
                            }
                            catch (IOException ex)
                            {
                                ex.printStackTrace();
                                ErrorCode=ex.getMessage();
                                this.cancel(true);
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            progress.dismiss();
                            //Intent i = new Intent(report_odometer.this, report_confirm.class);
                            //startActivity(i);
                            //validate odometer
                            if(!odometro.getText().equals("")) {

                                final String cadenita = odometro.getText().toString();
                                //Creamos alert para validar odometro antes de enviarlo...
                                AlertDialog.Builder builder;
                                AlertDialog alerta;
                                builder = new AlertDialog.Builder(ReportOdometerActivity.this);
                                builder.setTitle("Confirmar odómetro");
                                builder.setIcon(R.drawable.miituo);
                                builder.setMessage("¿Desea confirmar el odómetro: "+cadenita+"?");
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        sendOdometer();
                                    }
                                });

                                alerta = builder.create();
                                alerta.show();
                            }
                            else
                            {
                                msg=Toast.makeText(getApplicationContext(),"El odometro debe tener un valor",Toast.LENGTH_LONG);
                                msg.show();
                            }
                        }

                        @Override
                        protected void onCancelled() {
                            progress.dismiss();
                            if(ErrorCode.equals("1000"))
                            {
                                IinfoClient.getInfoClientObject().getPolicies().setLastOdometer(-1);
                                //Intent i=new Intent(report_odometer.this,report_confirm.class);
                                //startActivity(i);

                                if(!odometro.getText().equals("")) {

                                    final String cadenita = odometro.getText().toString();
                                    //Creamos alert para validar odometro antes de enviarlo...
                                    AlertDialog.Builder builder;
                                    AlertDialog alerta;
                                    builder = new AlertDialog.Builder(ReportOdometerActivity.this);
                                    builder.setTitle("Confirmar odómetro");
                                    builder.setIcon(R.drawable.miituo);
                                    builder.setMessage("¿Desea confirmar el odómetro: "+cadenita+"?");
                                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                    builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            sendOdometer();
                                        }
                                    });

                                    alerta = builder.create();
                                    alerta.show();
                                }
                                else
                                {
                                    msg=Toast.makeText(getApplicationContext(),"El odómetro debe tener un valor",Toast.LENGTH_LONG);
                                    msg.show();
                                }
                            }
                            else {
                                msg = Toast.makeText(getApplicationContext(), ErrorCode, Toast.LENGTH_LONG);
                                msg.show();
                            }
                            super.onCancelled();
                        }

                    };
                    sendOdometerPicture.execute();
                }
                else
                {
                    msg=Toast.makeText(getApplicationContext(),"Captura la imagen del odómetro para continuar",Toast.LENGTH_LONG);
                    msg.show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
        {
            SharedPreferences preferences = getSharedPreferences("miituo", Context.MODE_PRIVATE);
            mCurrentPhotoPath = preferences.getString("nombrefoto", "null");

            String filePath = mCurrentPhotoPath;//photoFile.getPath();
            bmp = BitmapFactory.decodeFile(filePath);

            Bitmap resized = Bitmap.createScaledBitmap(bmp, bmp.getWidth()/4, bmp.getHeight()/4, false);

            int existItem=getPositionItem(requestCode);
            if(existItem==-1)
                ImgList.add(new ImageProcessing(resized,requestCode));
            else
            {
                ImgList.get(existItem).setImage(resized);
                ImgList.get(existItem).setImageType(requestCode);
            }
            rptView.setImageBitmap(resized);
        }
    }

    int getPositionItem(int Type)
    {
        int result= -1;
        for(int i=0; i<ImgList.size();i++)
        {
            if(ImgList.get(i).getImageType() == Type)
                return i;
        }

        return result;
    }

    //after we validate odomter...lets send to WS
    public void sendOdometer(){
        try {
            NumberFormat format = NumberFormat.getInstance(Locale.US);
            Number number = format.parse(odometro.getText().toString());
            //int d = number.intValue();//.doubleValue();
            int d = Integer.parseInt(odometro.getText().toString());//.doubleValue();
            //Log.w("fon", d + "");

            IinfoClient.getInfoClientObject().getPolicies().setRegOdometer(Integer.parseInt(d+""));
            AsyncTask<Void, Void, Void> sendReporter = new AsyncTask<Void, Void, Void>() {
                ProgressDialog progress = new ProgressDialog(ReportOdometerActivity.this);
                String ErrorCode = "";

                @Override
                protected void onCancelled() {
                    progress.dismiss();
                    msg = Toast.makeText(getApplicationContext(), ErrorCode, Toast.LENGTH_LONG);
                    msg.show();

                    super.onCancelled();
                }

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        respuesta = api.ConfirmReport(ApiSendReport,"");

                    } catch (Exception ex) {
                        ErrorCode = ex.getMessage();
                        this.cancel(true);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                            /*new AlertDialog.Builder(report_confirm.this)
                                    .setTitle("Odometro Reportado")
                                    .setMessage("El odometro fue reportado con exito")
                                    .setCancelable(false)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            progress.dismiss();
                                            Intent i = new Intent(report_confirm.this,MainActivity.class);
                                            startActivity(i);
                                        }
                                    })
                                    .show();*/

                    //resuesta is the json from ws....
                    //we have to decode and show values => => => DYNAMIC...HOW???
                    try {
                        //String json = "{\"Id\":33,\"Name\":\"Calculo de Mensualidad General\",\"DesClave\":\"Mensualidad_General\",\"FormulaStr\":\"Fee+TarifaNeta-promocion\",\"Amount\":268008.04,\"Parameters\":[{\"Name\":\"Fee\",\"Amount\":80.0},{\"Name\":\"promocion\",\"Amount\":0.0}],\"FormulaChilds\":[{\"Id\":34,\"Name\":\"Tarifa Neta\",\"DesClave\":\"TarifaNeta\",\"FormulaStr\":\"tarifa*KmCobrado\",\"Amount\":267928.04,\"Parameters\":[{\"Name\":\"tarifa\",\"Amount\":0.62}],\"FormulaChilds\":[{\"Id\":35,\"Name\":\"Kilometro Cobrado\",\"DesClave\":\"KmCobrado\",\"FormulaStr\":\"kilometroAct-kilometroReg\",\"Amount\":432142.0,\"Parameters\":[{\"Name\":\"kilometroAct\",\"Amount\":555698.0},{\"Name\":\"kilometroReg\",\"Amount\":123556.0}],\"FormulaChilds\":null}]}]}";

                        JSONObject object = new JSONObject(respuesta);
                        String dato = object.getString("Amount");

                        JSONArray arrayfee = new JSONArray(object.getString("Parameters"));
                        JSONObject feeobject = arrayfee.getJSONObject(0);
                        JSONObject promoobject = arrayfee.getJSONObject(1);
                        String fee =feeobject.getString("Amount");
                        String promo = promoobject.getString("Amount");

                        JSONArray arraychild = new JSONArray(object.getString("FormulaChilds"));
                        JSONObject child1object = arraychild.getJSONObject(0);
                        String cobroportarifa = child1object.getString("Amount");

                        JSONArray arrayparametrostarifa = new JSONArray(child1object.getString("Parameters"));
                        String feetarifa = arrayparametrostarifa.getJSONObject(0).getString("Amount");

                        JSONArray arraychild2 = new JSONArray(child1object.getString("FormulaChilds"));
                        JSONObject child1object2 = arraychild2.getJSONObject(0);
                        String diferencia = child1object2.getString("Amount");

                        JSONArray arraykms = new JSONArray(child1object2.getString("Parameters"));
                        String actual = arraykms.getJSONObject(0).getString("Amount");
                        String anterior = arraykms.getJSONObject(1).getString("Amount");

                        //Log.w("Ok","si");

                        //Get all elements from dialog to set values
                        final Dialog dialog = new Dialog(ReportOdometerActivity.this);
                        dialog.setContentView(R.layout.custom_alert);
                        dialog.setTitle("Resumen mensual");

                        Button cancel = (Button)dialog.findViewById(R.id.button3);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                progress.dismiss();
                            }
                        });

                        Button aceptar = (Button)dialog.findViewById(R.id.button2);
                        aceptar.setOnClickListener(new View.OnClickListener() {
                            //call the other method to confirm odometer...

                            @Override
                            public void onClick(View view) {
                                //before launch alert, we have to send the confirmReport
                                try {
                                    String res = api.ConfirmReportLast("ReportOdometer/Confirmreport","");
                                    if (res.equals("false") || res.equals("true")) {

                                        new AlertDialog.Builder(ReportOdometerActivity.this)
                                                .setTitle("Odómetro Reportado")
                                                .setIcon(R.drawable.miituo)
                                                .setMessage("El odómetro fue reportado con exito")
                                                .setCancelable(false)
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        progress.dismiss();
                                                        Intent i = new Intent(ReportOdometerActivity.this, PrincipalActivity.class);
                                                        startActivity(i);
                                                    }
                                                })
                                                .show();
                                    }else{
                                        new AlertDialog.Builder(ReportOdometerActivity.this)
                                                .setTitle("Odómetro no reportado")
                                                .setMessage("Problema al reportar odómetro. Intente más tarde.")
                                                .setIcon(R.drawable.miituo)
                                                .setCancelable(false)
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        progress.dismiss();
                                                        Intent i = new Intent(ReportOdometerActivity.this, PrincipalActivity.class);
                                                        startActivity(i);
                                                    }
                                                })
                                                .show();
                                    }
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });

                        //GET textviews to show info

                        TextView hoy = (TextView)dialog.findViewById(R.id.textView11);
                        hoy.setText(actual);

                        TextView antes = (TextView)dialog.findViewById(R.id.textView14);
                        antes.setText(anterior);

                        TextView difer = (TextView)dialog.findViewById(R.id.textView17);
                        difer.setText(diferencia);

                        //String str = feetarifa.toString().replaceAll("[^\\d]", "");
                        double s1 = Double.parseDouble(feetarifa);
                        DecimalFormat formatter = new DecimalFormat("$ #,###.00");
                        TextView tarifa = (TextView)dialog.findViewById(R.id.textView21);
                        tarifa.setText(formatter.format(s1));

                        //str = promo.toString().replaceAll("[^\\d]", "");
                        s1 = Double.parseDouble(promo);
                        TextView prom = (TextView)dialog.findViewById(R.id.textView31);
                        prom.setText(formatter.format(s1));

                        //str = fee.toString().replaceAll("[^\\d]", "");
                        s1 = Double.parseDouble(fee);
                        TextView pormes = (TextView)dialog.findViewById(R.id.textView19);
                        pormes.setText(formatter.format(s1));

                        //str = cobroportarifa.toString().replaceAll("[^\\d]", "");
                        s1 = Double.parseDouble(cobroportarifa);
                        TextView tarifafinal = (TextView)dialog.findViewById(R.id.textView24);
                        tarifafinal.setText(formatter.format(s1));

                        //str = dato.toString().replaceAll("[^\\d]", "");
                        s1 = Double.parseDouble(dato);
                        //TextView ending = (TextView)dialog.findViewById(R.id.textView27);
                        //ending.setText(formatter.format(s1));

                        dialog.show();
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                }

                @Override
                protected void onPreExecute() {
                    progress.setTitle("Reportando");
                    progress.setMessage("Enviando Reporte...");
                    progress.setIcon(R.drawable.miituo);
                    progress.setIndeterminate(true);
                    progress.setCancelable(false);
                    progress.show();
                }
            };
            sendReporter.execute();
        }
        catch(Exception e){}
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

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();

        SharedPreferences preferences = getSharedPreferences("miituo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("nombrefoto",mCurrentPhotoPath);
        editor.commit();
        return image;
    }
}


