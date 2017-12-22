package miituo.com.miituo;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import miituo.com.miituo.api.ApiClient;
import miituo.com.miituo.data.DBaseMethods;
import miituo.com.miituo.data.IinfoClient;
import miituo.com.miituo.data.modelBase;

import static miituo.com.miituo.VehiclePictures.ImageList;

public class LastOdometerActivity extends AppCompatActivity {

    public String odometroAnterior;

    public String tipoodometro,tok;
    SharedPreferences app_preferences;

    final String UrlAjuste="Ticket";
    final String UrlAjusteCasi="UpStateCasification";
    final String UrlAjustelast="Policy/UpdatePolicyStatusReport/";

    final String UrlConfirmOdometer="ImageProcessing/ConfirmOdometer";

    private ApiClient api;

    public Boolean response;
    public String respuesta,res;
    final String ApiSendReport="ReportOdometer/";

    public Typeface typeface;
    public Typeface typefacebold;

    public String datoamount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_odometer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle("Confirmar odómetro");
        //toolbar.setTitleTextColor(Color.BLACK);
        //setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        typeface = Typeface.createFromAsset(getAssets(), "fonts/herne1.ttf");
        typefacebold = Typeface.createFromAsset(getAssets(), "fonts/herne.ttf");

        TextView leyenda = (TextView)findViewById(R.id.textView8);
        TextView res = (TextView)findViewById(R.id.textView41);
        EditText edit = (EditText)findViewById(R.id.editTextConfirmaOdo);
        edit.setTypeface(typeface);
        leyenda.setTypeface(typeface);
        res.setTypeface(typeface);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        odometroAnterior = getIntent().getStringExtra("valor");
        tok = IinfoClient.getInfoClientObject().getClient().getToken();

        app_preferences= PreferenceManager.getDefaultSharedPreferences(this);
        tipoodometro = app_preferences.getString("odometro","null");

        ImageButton back = (ImageButton)findViewById(R.id.imageView12);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Typeface typefacebold = Typeface.createFromAsset(getAssets(), "fonts/herne.ttf");
        TextView titulo = (TextView)findViewById(R.id.textView27);
        titulo.setTypeface(typefacebold);

        api=new ApiClient(LastOdometerActivity.this);
    }

    public void validar(View v)
    {
        //get odometro
        EditText odo = (EditText)findViewById(R.id.editTextConfirmaOdo);
        String odometro = odo.getText().toString();

        //odometroAnterior = "1111111";
        //tipoodometro = "mensual";
        //validar que no sea ""
        //validar que coincida con el anterior
        //lanzar alertdialog custom para verificar odometro una vez mas...
        if(odometro.equals("")){
            Toast.makeText(this,"Es necesario capturar los kms. que marca el odómetro.",Toast.LENGTH_SHORT).show();
        } else if(!odometro.equals(odometroAnterior)){
            Toast.makeText(this,"Los odómetros no coinciden, por favor verifícalos.",Toast.LENGTH_SHORT).show();
        }else{
            //udpate value into listclient
            IinfoClient.InfoClientObject.getPolicies().setRegOdometer(Integer.parseInt(odometro));

            //all it's ok...launch alertdialog custom_validation_odometer
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_validation_odometer);
            //dialog.setTitle("Tu odómetro");

            DecimalFormat formatter = new DecimalFormat("#,###,###");
            Double dos = Double.parseDouble(odometro);
            String yourFormattedString = formatter.format(dos);
            TextView odom = (TextView)dialog.findViewById(R.id.textView44);
            odom.setTypeface(typeface);
            odom.setText(yourFormattedString);

            TextView title = (TextView)dialog.findViewById(R.id.textView42);
            TextView title1 = (TextView)dialog.findViewById(R.id.textView43);
            TextView title2 = (TextView)dialog.findViewById(R.id.textView45);
            TextView title3 = (TextView)dialog.findViewById(R.id.textView46);

            title.setTypeface(typeface);
            title1.setTypeface(typeface);
            title2.setTypeface(typeface);
            title3.setTypeface(typeface);

            //buttons actiosn
            Button cancel = (Button)dialog.findViewById(R.id.buttonCancel);
            Button aceptar = (Button)dialog.findViewById(R.id.buttonOk);

            aceptar.setTypeface(typeface);
            cancel.setTypeface(typeface);

            //canecler logic
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //launch vehicle odometet again...
                    Intent i = new Intent(LastOdometerActivity.this, ConfirmActivity.class);
                    //dialog.dismiss();
                    startActivity(i);
                }
            });

            //aceptar logic
            aceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Aqui validamos si es first o meontlhy
                    if(tipoodometro.equals("first")){
                        reporteFirst();
                        //dialog.dismiss();
                    }else if(tipoodometro.equals("mensual")){
                        reporteMensual();
                        //dialog.dismiss();
                    }else if(tipoodometro.equals("cancela")){
                        ajusteMensual();    //mismo flujo para cancelacion
                        //dialog.dismiss();
                    }else if(tipoodometro.equals("ajuste")){
                        ajusteMensual();
                        //dialog.dismiss();
                    }else{
                        //dialog.dismiss();
                    }
                }
            });
            dialog.show();
        }
    }

    //reporte firt time*****************************************************************************
    public void ajusteMensual(){
        //thread to call api and save furst odometer
        AsyncTask<Void, Void, Void> sendOdometro = new AsyncTask<Void, Void, Void>() {
            ProgressDialog progress = new ProgressDialog(LastOdometerActivity.this);
            String ErrorCode = "";

            @Override
            protected void onPreExecute() {
                progress.setTitle("Registrando odómetro");
                progress.setMessage("Procesando información.");
                //progress.setIcon(R.drawable.miituo);
                progress.setCancelable(false);
                progress.setIndeterminate(true);
                progress.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    //response = api.ConfirmOdometer(5, UrlConfirmOdometer);
                    response = api.AjusteOdometerCasification(5, UrlAjusteCasi,LastOdometerActivity.this,tok);
                    response = api.AjusteOdometerTicket(5, UrlAjuste,LastOdometerActivity.this,tok);
                    response = api.AjusteOdometerLast(5, UrlAjustelast,IinfoClient.getInfoClientObject().getPolicies().getId(),tok);

                } catch (IOException ex) {
                    ErrorCode = ex.getMessage();
                    this.cancel(true);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                if (!response) {
                    //Toast msg = Toast.makeText(getApplicationContext(), "Error inesperado, no se pudo procesar la imagen", Toast.LENGTH_LONG);
                    //msg.show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(LastOdometerActivity.this);
                    builder.setTitle("Error");
                    builder.setMessage("Error al subir información. Intente más tarde");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(LastOdometerActivity.this, SyncActivity.class);
                            startActivity(i);
                        }
                    });
                    AlertDialog alerta = builder.create();
                    alerta.show();
                } else {
                    new android.app.AlertDialog.Builder(LastOdometerActivity.this)
                            .setTitle("Gracias.")
                            .setMessage("Tu información ha sido actualizada.")
                            //.setIcon(R.drawable.miituo)
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //progresslast.dismiss();
                                    //finalizamos....
                                    //IinfoClient.getInfoClientObject().getPolicies().setReportState(13);

                                    //updateReportState();
                                    IinfoClient.getInfoClientObject().getPolicies().setLastOdometer(IinfoClient.getInfoClientObject().getPolicies().getRegOdometer());
                                    //IinfoClient.getInfoClientObject().getPolicies().setHasVehiclePictures(true);
                                    //IinfoClient.getInfoClientObject().getPolicies().setHasOdometerPicture(true);
                                    IinfoClient.getInfoClientObject().getPolicies().setReportState(12);

                                    String rs = UpdateDataBase(modelBase.FeedEntryPoliza.TABLE_NAME,IinfoClient.getInfoClientObject().getPolicies().getNoPolicy());

                                    Intent i = new Intent(LastOdometerActivity.this, PrincipalActivity.class);
                                    i.putExtra("actualizar","1");
                                    startActivity(i);
                                }
                            })
                            .show();
                }
                //progress.dismiss();
            }

            @Override
            protected void onCancelled() {
                if (ErrorCode.equals("1000")) {
                    progress.dismiss();
                    Toast msg = Toast.makeText(getApplicationContext(), ErrorCode, Toast.LENGTH_LONG);
                    msg.show();

                    super.onCancelled();

                } else {
                    progress.dismiss();

                    Toast msg = Toast.makeText(getApplicationContext(), "Ocurrio un Error:" + ErrorCode, Toast.LENGTH_LONG);
                    msg.show();
                }
            }

            public String UpdateDataBase(String...strings){

                String val = strings[0];
                //Log.w("Here",val);

                switch (val){

                    case modelBase.FeedEntryPoliza.TABLE_NAME:
                        // Gets the data repository in write mode
                        SQLiteDatabase db = DBaseMethods.base.getWritableDatabase();

                        // Create a new map of values, where column names are the keys
                        ContentValues values = new ContentValues();
                        //values.put(modelBase.FeedEntryArticle.COLUMN_ID, strings[6]);
                        values.put(modelBase.FeedEntryPoliza.COLUMN_NOPOLICY, strings[1]);
                        //values.put(modelBase.FeedEntryPoliza.COLUMN_ODOMETERPIE, true);
                        //values.put(modelBase.FeedEntryPoliza.COLUMN_VEHICLEPIE, true);
                        values.put(modelBase.FeedEntryPoliza.COLUMN_LASTODOMETER,IinfoClient.getInfoClientObject().getPolicies().getRegOdometer()+"");
                        values.put(modelBase.FeedEntryPoliza.COLUMN_REPORT_STATE, 12);

                        // Insert the new row, returning the primary key value of the new row
                        //Just change name table and the values....
                        long newRowId = db.update(val, values,modelBase.FeedEntryPoliza.COLUMN_NOPOLICY+"='"+strings[1]+"'",null);
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        return "" + newRowId;

                    default:
                        break;
                }
                return "";
            }
        };
        sendOdometro.execute();
    }

//reporte firt time*****************************************************************************
    public void reporteFirst(){
        //thread to call api and save furst odometer
        AsyncTask<Void, Void, Void> sendOdometro = new AsyncTask<Void, Void, Void>() {
            ProgressDialog progress = new ProgressDialog(LastOdometerActivity.this);
            String ErrorCode = "";

            @Override
            protected void onPreExecute() {
                progress.setTitle("Registrando odómetro");
                progress.setMessage("Procesando información.");
                //progress.setIcon(R.drawable.miituo);
                progress.setCancelable(false);
                progress.setIndeterminate(true);
                progress.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    response = api.ConfirmOdometer(5, UrlConfirmOdometer,tok);

                } catch (IOException ex) {
                    ErrorCode = ex.getMessage();
                    this.cancel(true);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                if (!response) {
                    //Toast msg = Toast.makeText(getApplicationContext(), "Error inesperado, no se pudo procesar la imagen", Toast.LENGTH_LONG);
                    //msg.show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(LastOdometerActivity.this);
                    builder.setTitle("Error");
                    builder.setMessage("Error al subir información. Intente más tarde");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(LastOdometerActivity.this, PrincipalActivity.class);
                            i.putExtra("actualizar","1");
                            startActivity(i);
                        }
                    });
                    AlertDialog alerta = builder.create();
                    alerta.show();
                } else {
                    new android.app.AlertDialog.Builder(LastOdometerActivity.this)
                            .setTitle("Gracias.")
                            .setMessage("Tu información ha sido actualizada.")
                            //.setIcon(R.drawable.miituo)
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //progresslast.dismiss();
                                    //finalizamos....
                                    //IinfoClient.getInfoClientObject().getPolicies().setReportState(13);

                                    //updateReportState();
                                    IinfoClient.getInfoClientObject().getPolicies().setLastOdometer(IinfoClient.getInfoClientObject().getPolicies().getRegOdometer());
                                    IinfoClient.getInfoClientObject().getPolicies().setHasVehiclePictures(true);
                                    IinfoClient.getInfoClientObject().getPolicies().setHasOdometerPicture(true);
                                    IinfoClient.getInfoClientObject().getPolicies().setReportState(12);

                                    String rs = UpdateDataBase(modelBase.FeedEntryPoliza.TABLE_NAME,IinfoClient.getInfoClientObject().getPolicies().getNoPolicy());

                                    Intent i = new Intent(LastOdometerActivity.this, PrincipalActivity.class);
                                    i.putExtra("actualizar","1");
                                    startActivity(i);
                                }
                            })
                            .show();
                }
                //progress.dismiss();
            }

            @Override
            protected void onCancelled() {
                if (ErrorCode.equals("1000")) {
                    progress.dismiss();
                    Toast msg = Toast.makeText(getApplicationContext(), ErrorCode, Toast.LENGTH_LONG);
                    msg.show();

                    super.onCancelled();

                } else {
                    progress.dismiss();

                    Toast msg = Toast.makeText(getApplicationContext(), "Ocurrio un Error:" + ErrorCode, Toast.LENGTH_LONG);
                    msg.show();
                }
            }

            public String UpdateDataBase(String...strings){

                String val = strings[0];
                //Log.w("Here",val);

                switch (val){

                    case modelBase.FeedEntryPoliza.TABLE_NAME:
                        // Gets the data repository in write mode
                        SQLiteDatabase db = DBaseMethods.base.getWritableDatabase();

                        // Create a new map of values, where column names are the keys
                        ContentValues values = new ContentValues();
                        //values.put(modelBase.FeedEntryArticle.COLUMN_ID, strings[6]);
                        values.put(modelBase.FeedEntryPoliza.COLUMN_NOPOLICY, strings[1]);
                        values.put(modelBase.FeedEntryPoliza.COLUMN_ODOMETERPIE, true);
                        values.put(modelBase.FeedEntryPoliza.COLUMN_VEHICLEPIE, true);
                        values.put(modelBase.FeedEntryPoliza.COLUMN_LASTODOMETER,IinfoClient.getInfoClientObject().getPolicies().getRegOdometer()+"");
                        values.put(modelBase.FeedEntryPoliza.COLUMN_REPORT_STATE, 12);

                        // Insert the new row, returning the primary key value of the new row
                        //Just change name table and the values....
                        long newRowId = db.update(val, values,modelBase.FeedEntryPoliza.COLUMN_NOPOLICY+"='"+strings[1]+"'",null);
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        return "" + newRowId;

                    default:
                        break;
                }
                return "";
            }
        };
        sendOdometro.execute();
    }

//reporte mensaul****************************************************************************
    public void reporteMensual(){

        try {
            /*NumberFormat format = NumberFormat.getInstance(Locale.US);
            Number number = format.parse(odometro.getText().toString());
            //int d = number.intValue();//.doubleValue();
            int d = Integer.parseInt(odometro.getText().toString());//.doubleValue();
            Log.w("fon", d + "");*/

            IinfoClient.getInfoClientObject().getPolicies().setRegOdometer(Integer.parseInt(odometroAnterior));

            AsyncTask<Void, Void, Void> sendReporter = new AsyncTask<Void, Void, Void>() {
                ProgressDialog progress = new ProgressDialog(LastOdometerActivity.this);
                String ErrorCode = "";

                @Override
                protected void onPreExecute() {
                    progress.setTitle("Odómetro");
                    progress.setMessage("Enviando información...");
                    progress.setIndeterminate(true);
                    progress.setCancelable(false);
                    //progress.setIcon(R.drawable.miituo);
                    progress.show();
                }

                @Override
                protected void onCancelled() {
                    progress.dismiss();
                    Toast msg = Toast.makeText(getApplicationContext(), ErrorCode, Toast.LENGTH_LONG);
                    msg.show();

                    super.onCancelled();
                }

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        respuesta = api.ConfirmReport(ApiSendReport,tok);

                    } catch (Exception ex) {
                        ErrorCode = ex.getMessage();
                        this.cancel(true);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid)
                {
                    //resuesta is the json from ws....
                    //we have to decode and show values => => => DYNAMIC...HOW???
                    try {
                        //String json = "{\"Id\":33,\"Name\":\"Calculo de Mensualidad General\",\"DesClave\":\"Mensualidad_General\",\"FormulaStr\":\"Fee+TarifaNeta-promocion\",\"Amount\":268008.04,\"Parameters\":[{\"Name\":\"Fee\",\"Amount\":80.0},{\"Name\":\"promocion\",\"Amount\":0.0}],\"FormulaChilds\":[{\"Id\":34,\"Name\":\"Tarifa Neta\",\"DesClave\":\"TarifaNeta\",\"FormulaStr\":\"tarifa*KmCobrado\",\"Amount\":267928.04,\"Parameters\":[{\"Name\":\"tarifa\",\"Amount\":0.62}],\"FormulaChilds\":[{\"Id\":35,\"Name\":\"Kilometro Cobrado\",\"DesClave\":\"KmCobrado\",\"FormulaStr\":\"kilometroAct-kilometroReg\",\"Amount\":432142.0,\"Parameters\":[{\"Name\":\"kilometroAct\",\"Amount\":555698.0},{\"Name\":\"kilometroReg\",\"Amount\":123556.0}],\"FormulaChilds\":null}]}]}";
                        JSONObject object = new JSONObject(respuesta);
                        String dato = object.getString("Amount");
                        datoamount = dato;

                        JSONArray arrayfee = new JSONArray(object.getString("Parameters"));

                        String fee = "";
                        String promo = "";
                        String derecho = "";
                        String feepormktarifa = "";
                        String cobroportarifa = "";

                        String diferencia = "";
                        String actual = "";
                        String anterior = "";

                        String ivafee = "";
                        String ivatarifa = "";
                        String ivaderecho = "";

                        if (arrayfee.length() == 10)
                        {
                            JSONObject feeobject = arrayfee.getJSONObject(0);
                            JSONObject promoobject = arrayfee.getJSONObject(1);
                            JSONObject feeporkmobject = arrayfee.getJSONObject(2);
                            JSONObject kmactualbject = arrayfee.getJSONObject(3);
                            JSONObject lastkmbject = arrayfee.getJSONObject(4);
                            JSONObject diferenciaobject = arrayfee.getJSONObject(5);

                            JSONObject tarafanetaciaobject = arrayfee.getJSONObject(7);
                            JSONObject ivafeeobject = arrayfee.getJSONObject(8);
                            JSONObject ivatarifaobject = arrayfee.getJSONObject(9);
                            //JSONObject ivaderechoobject = arrayfee.getJSONObject(10);

                            fee =feeobject.getString("Amount");
                            promo = promoobject.getString("Amount");
                            feepormktarifa = feeporkmobject.getString("Amount");

                            diferencia = diferenciaobject.getString("Amount");
                            actual = kmactualbject.getString("Amount");
                            anterior = lastkmbject.getString("Amount");

                            cobroportarifa = tarafanetaciaobject.getString("Amount");

                            ivafee = ivafeeobject.getString("Amount");
                            ivatarifa = ivatarifaobject.getString("Amount");
                            //ivaderecho = ivaderechoobject.getString("Amount");
                        }
                        else if(arrayfee.length() == 12) {
                            JSONObject feeobject = arrayfee.getJSONObject(0);
                            JSONObject derechoobject = arrayfee.getJSONObject(1);
                            JSONObject promoobject = arrayfee.getJSONObject(2);
                            JSONObject feeporkmobject = arrayfee.getJSONObject(3);

                            JSONObject kmactualbject = arrayfee.getJSONObject(4);
                            JSONObject lastkmbject = arrayfee.getJSONObject(5);
                            JSONObject diferenciaobject = arrayfee.getJSONObject(6);

                            JSONObject tarafanetaciaobject = arrayfee.getJSONObject(8);

                            JSONObject ivafeeobject = arrayfee.getJSONObject(9);
                            JSONObject ivatarifaobject = arrayfee.getJSONObject(10);
                            JSONObject ivaderechoobject = arrayfee.getJSONObject(11);

                            fee = feeobject.getString("Amount");
                            derecho = derechoobject.getString("Amount");
                            promo = promoobject.getString("Amount");
                            feepormktarifa = feeporkmobject.getString("Amount");
                            diferencia = diferenciaobject.getString("Amount");
                            actual = kmactualbject.getString("Amount");
                            anterior = lastkmbject.getString("Amount");

                            cobroportarifa = tarafanetaciaobject.getString("Amount");

                            ivafee = ivafeeobject.getString("Amount");
                            ivatarifa = ivatarifaobject.getString("Amount");
                            ivaderecho = ivaderechoobject.getString("Amount");
                        }

                        /*JSONArray arraychild = new JSONArray(object.getString("FormulaChilds"));
                        JSONObject child1object = arraychild.getJSONObject(0);
                        String cobroportarifa = child1object.getString("Amount");

                        JSONArray arrayparametrostarifa = new JSONArray(child1object.getString("Parameters"));
                        String feetarifa = arrayparametrostarifa.getJSONObject(0).getString("Amount");

                        JSONArray arraychild2 = new JSONArray(child1object.getString("FormulaChilds"));
                        JSONObject child1object2 = arraychild2.getJSONObject(0);
                        String diferencia = child1object2.getString("Amount");

                        JSONArray arraykms = new JSONArray(child1object2.getString("Parameters"));
                        String actual = arraykms.getJSONObject(0).getString("Amount");
                        String anterior = arraykms.getJSONObject(1).getString("Amount");*/

                        //Log.w("Ok","si");

                        //Get all elements from dialog to set values
                        final Dialog dialog = new Dialog(LastOdometerActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.custom_alert);
                        //dialog.setTitle("Resumen mensual");

                        progress.dismiss();

                        TextView t47 = (TextView)dialog.findViewById(R.id.textView47);
                        t47.setTypeface(typeface);
                        t47 = (TextView)dialog.findViewById(R.id.textView55);
                        t47.setTypeface(typeface);
                        t47 = (TextView)dialog.findViewById(R.id.textView9);
                        t47.setTypeface(typeface);
                        t47 = (TextView)dialog.findViewById(R.id.textView12);
                        t47.setTypeface(typeface);
                        t47 = (TextView)dialog.findViewById(R.id.textView15);
                        t47.setTypeface(typeface);
                        t47 = (TextView)dialog.findViewById(R.id.textView20);
                        t47.setTypeface(typeface);
                        t47 = (TextView)dialog.findViewById(R.id.textView22);
                        t47.setTypeface(typeface);
                        t47 = (TextView)dialog.findViewById(R.id.textView18);
                        t47.setTypeface(typeface);
                        t47 = (TextView)dialog.findViewById(R.id.textView30);
                        t47.setTypeface(typeface);
                        t47 = (TextView)dialog.findViewById(R.id.textView40);
                        t47.setTypeface(typeface);
                        t47 = (TextView)dialog.findViewById(R.id.textView26);
                        t47.setTypeface(typeface);
                        t47 = (TextView)dialog.findViewById(R.id.textoiva);
                        t47.setTypeface(typeface);

                        Button cancel = (Button)dialog.findViewById(R.id.button3);
                        cancel.setTypeface(typeface);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //dialog.dismiss();
                                //launch vehicle odometet again...
                                Intent i = new Intent(LastOdometerActivity.this, ConfirmActivity.class);
                                startActivity(i);
                            }
                        });

                        Button aceptar = (Button)dialog.findViewById(R.id.button2);
                        aceptar.setTypeface(typeface);
                        aceptar.setOnClickListener(new View.OnClickListener() {
                            //call the other method to confirm odometer...

                            @Override
                            public void onClick(View view) {
                                saygoodbye();
                            }
                        });

                        DecimalFormat formatter = new DecimalFormat("$ #,###.00");
                        //GET textviews to show info
                        TextView primashow = (TextView)dialog.findViewById(R.id.textView40);
                        TextView prima = (TextView)dialog.findViewById(R.id.textView41);
                        primashow.setTypeface(typeface);
                        prima.setTypeface(typefacebold);

                        DecimalFormat formatterTwo = new DecimalFormat("#,###,###");

                        //odometros-----------------------------------------------------------------
                        TextView hoy = (TextView)dialog.findViewById(R.id.textView11);
                        hoy.setTypeface(typeface);
                        Double dos = Double.parseDouble(actual);
                        String yourFormattedString = formatterTwo.format(dos);
                        hoy.setText(yourFormattedString);

                        TextView antes = (TextView)dialog.findViewById(R.id.textView14);
                        antes.setTypeface(typeface);
                        Double antesdos = Double.parseDouble(anterior);
                        String yourFormattedStringTwo = formatterTwo.format(antesdos);
                        antes.setText(yourFormattedStringTwo);

                        TextView difer = (TextView)dialog.findViewById(R.id.textView17);
                        difer.setTypeface(typeface);
                        Double antestres = Double.parseDouble(diferencia);
                        String formatedThree = formatterTwo.format(antestres);
                        difer.setText(formatedThree);

                        //Tarifas-------------------------------------------------------------------
                        double s1 = Double.parseDouble(feepormktarifa);
                        TextView tarifa = (TextView)dialog.findViewById(R.id.textView21);
                        tarifa.setTypeface(typeface);
                        DecimalFormat formattertarifa = new DecimalFormat("$ 0.0000");
                        tarifa.setText(formattertarifa.format(s1));

                        TextView labelderecho = (TextView)dialog.findViewById(R.id.textoderecho);
                        TextView labelderechostart = (TextView)dialog.findViewById(R.id.textView65);

                        if(derecho.equals("")){
                            primashow.setVisibility(View.INVISIBLE);
                            prima.setVisibility(View.INVISIBLE);
                            labelderecho.setVisibility(View.INVISIBLE);
                            labelderechostart.setVisibility(View.INVISIBLE);
                            LinearLayout derecholay = (LinearLayout)dialog.findViewById(R.id.derecholayout);
                            derecholay.setVisibility(View.GONE);
                        }else{
                            labelderecho.setTypeface(typeface);
                            s1 = Double.parseDouble(derecho);
                            Double s4 = Double.parseDouble(ivaderecho);
                            primashow.setVisibility(View.VISIBLE);
                            prima.setVisibility(View.VISIBLE);
                            prima.setText(formatter.format(s1+s4));
                        }

                        TextView promoshow = (TextView)dialog.findViewById(R.id.textView30);
                        TextView prom = (TextView)dialog.findViewById(R.id.textView31);
                        promoshow.setTypeface(typeface);
                        prom.setTypeface(typefacebold);

                        if(promo.equals("0")){
                            promoshow.setVisibility(View.INVISIBLE);
                            prom.setVisibility(View.INVISIBLE);
                            LinearLayout promolay = (LinearLayout)dialog.findViewById(R.id.promolayout);
                            promolay.setVisibility(View.GONE);
                        }else{
                            s1 = Double.parseDouble(promo);
                            prom.setText(formatter.format(s1));
                            promoshow.setVisibility(View.VISIBLE);
                            prom.setVisibility(View.VISIBLE);
                        }

                        //str = fee.toString().replaceAll("[^\\d]", "");
                        s1 = Double.parseDouble(fee);
                        Double s2 = Double.parseDouble(ivafee);
                        TextView basepormes = (TextView)dialog.findViewById(R.id.textView19);
                        basepormes.setTypeface(typefacebold);
                        //basepormes.setText(formatter.format(Math.round(s1+s2)));
                        basepormes.setText(formatter.format((s1+s2)));

                        if(fee.equals("0")){
                            basepormes.setVisibility(View.GONE);
                            TextView t18 = (TextView)dialog.findViewById(R.id.textView18);
                            t18.setVisibility(View.GONE);
                        }

                        //str = cobroportarifa.toString().replaceAll("[^\\d]", "");
                        s1 = Double.parseDouble(cobroportarifa);
                        Double s3 = Double.parseDouble(ivatarifa);
                        TextView tarifafinal = (TextView)dialog.findViewById(R.id.textView24);
                        tarifafinal.setTypeface(typefacebold);
                        tarifafinal.setText(formatter.format(s1+s3));

                        //str = dato.toString().replaceAll("[^\\d]", "");
                        s1 = Double.parseDouble(dato);
                        //TextView ending = (TextView)dialog.findViewById(R.id.textView27);
                        TextView ending2 = (TextView)dialog.findViewById(R.id.textView50);
                        //ending.setTypeface(typeface);
                        ending2.setTypeface(typefacebold);
                        //ending.setText(formatter.format(s1));
                        ending2.setText(formatter.format(s1));

                        dialog.show();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            };
            sendReporter.execute();
        }
        catch(Exception e){}
    }

    public void saygoodbye(){
        AsyncTask<Void, Void, Void> sendthelast = new AsyncTask<Void, Void, Void>() {
            ProgressDialog progress = new ProgressDialog(LastOdometerActivity.this);
            String ErrorCode = "";

            @Override
            protected void onPreExecute() {
                progress.setTitle("Reportando");
                progress.setMessage("Subiendo información...");
                progress.setIndeterminate(true);
                progress.setCancelable(false);
                //progress.setIcon(R.drawable.miituo);
                progress.show();
            }

            @Override
            protected void onCancelled() {
                progress.dismiss();
                Toast msg = Toast.makeText(getApplicationContext(), ErrorCode, Toast.LENGTH_LONG);
                msg.show();

                super.onCancelled();
            }

            @Override
            protected Void doInBackground(Void... params) {
                //before launch alert, we have to send the confirmReport
                try {
                    res = api.ConfirmReportLast("ReportOdometer/Confirmreport",tok);

                }catch(Exception e){
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                progress.dismiss();

                String title = "Tu pago se ha realizado";
                String mensajee = "Gracias por ser parte del consumo equitativo.";
                if(datoamount.equals("0.0")){
                    title = "Gracias";
                    mensajee = "Recibimos tu reporte mensual de odómetro.";
                }else if (IinfoClient.getInfoClientObject().getPolicies().getPaymentType().equals("AMEX")){
                    title = "Tu pago esta en proceso.";
                    mensajee = "En cuanto quede listo te lo haremos saber.";
                }

                if (res.equals("false") || res.equals("true"))
                {
                    new android.app.AlertDialog.Builder(LastOdometerActivity.this)
                            .setTitle(title)
                            .setMessage(mensajee)
                            //.setIcon(R.drawable.miituo)
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //progresslast.dismiss();
                                    //finalizamos....
                                    //IinfoClient.getInfoClientObject().getPolicies().setReportState(13);

                                    //updateReportState();
                                    IinfoClient.getInfoClientObject().getPolicies().setReportState(12);
                                    IinfoClient.getInfoClientObject().getPolicies().setLastOdometer(IinfoClient.getInfoClientObject().getPolicies().getRegOdometer());

                                    String rs = UpdateDataBase(modelBase.FeedEntryPoliza.TABLE_NAME,IinfoClient.getInfoClientObject().getPolicies().getNoPolicy());

                                    Intent i = new Intent(LastOdometerActivity.this, PrincipalActivity.class);
                                    i.putExtra("actualizar","1");
                                    startActivity(i);
                                }
                            })
                            .show();
                } else {
                    if (res.contains("Por el momento tu pago")) {
                        String mensaje;
                        try {
                            JSONObject mes = new JSONObject(res);
                            mensaje = mes.getString("Message");
                            new android.app.AlertDialog.Builder(LastOdometerActivity.this)
                                    .setTitle("Odómetro")
                                    .setMessage(mensaje)
                                    //.setIcon(R.drawable.miituo)
                                    .setCancelable(false)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //progresslast.dismiss();
                                            Intent i = new Intent(LastOdometerActivity.this, PrincipalActivity.class);
                                            i.putExtra("actualizar", "1");
                                            startActivity(i);
                                        }
                                    })
                                    .show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        new android.app.AlertDialog.Builder(LastOdometerActivity.this)
                                .setTitle("Odómetro no reportado")
                                .setMessage("Problema al reportar odómetro, intente más tarde.")
                                //.setIcon(R.drawable.miituo)
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //progresslast.dismiss();
                                        Intent i = new Intent(LastOdometerActivity.this, PrincipalActivity.class);
                                        i.putExtra("actualizar", "1");
                                        startActivity(i);
                                    }
                                })
                                .show();
                    }
                }
            }

            public String UpdateDataBase(String...strings){
                String val = strings[0];
                //Log.w("Here",val);

                switch (val){

                    case modelBase.FeedEntryPoliza.TABLE_NAME:
                        // Gets the data repository in write mode
                        SQLiteDatabase db = DBaseMethods.base.getWritableDatabase();

                        // Create a new map of values, where column names are the keys
                        ContentValues values = new ContentValues();
                        //values.put(modelBase.FeedEntryArticle.COLUMN_ID, strings[6]);
                        values.put(modelBase.FeedEntryPoliza.COLUMN_NOPOLICY, strings[1]);
                        //values.put(modelBase.FeedEntryPoliza.COLUMN_ODOMETERPIE, true);
                        //values.put(modelBase.FeedEntryPoliza.COLUMN_VEHICLEPIE, true);
                        values.put(modelBase.FeedEntryPoliza.COLUMN_LASTODOMETER,IinfoClient.getInfoClientObject().getPolicies().getRegOdometer()+"");
                        values.put(modelBase.FeedEntryPoliza.COLUMN_REPORT_STATE, 12);

                        // Insert the new row, returning the primary key value of the new row
                        //Just change name table and the values....
                        long newRowId = db.update(val, values,modelBase.FeedEntryPoliza.COLUMN_NOPOLICY+"='"+strings[1]+"'",null);
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        return "" + newRowId;

                    default:
                        break;
                }
                return "";
            }
        };
        sendthelast.execute();
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
            finish(); // close this activity and return to preview activity (if there is any)
            //Intent i = new Intent(LastOdometerActivity.this, PrincipalActivity.class);
            //startActivity(i);
        }
        return true;
    }
}
