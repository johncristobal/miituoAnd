package miituo.com.miituo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import miituo.com.miituo.api.ApiClient;

import static miituo.com.miituo.MainActivity.result;
import static miituo.com.miituo.MainActivity.token;

public class TokenSmsActivity extends AppCompatActivity {

    EditText token;
    SharedPreferences app_preferences;
    private Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_sms);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        typeface = Typeface.createFromAsset(getAssets(), "fonts/herne1.ttf");
        token = (EditText)findViewById(R.id.editTextToken);

        TextView t1 = (TextView)findViewById(R.id.textView28);
        TextView t2 = (TextView)findViewById(R.id.textView29);
        TextView t3 = (TextView)findViewById(R.id.textView32);
        TextView t4 = (TextView)findViewById(R.id.textView33);
        TextView t5 = (TextView)findViewById(R.id.textView34);
        TextView t6 = (TextView)findViewById(R.id.textView59);

        t1.setTypeface(typeface);
        t2.setTypeface(typeface);
        t3.setTypeface(typeface);
        t4.setTypeface(typeface);
        t5.setTypeface(typeface);
        t6.setTypeface(typeface);

        Button b3 = (Button)findViewById(R.id.button);
        b3.setTypeface(typeface);

        token.setTypeface(typeface);
        app_preferences= PreferenceManager.getDefaultSharedPreferences(this);
    }

//*************************** Clic on enviar tokne*************************************************
    public void loadInfo(View v){

        String tok = token.getText().toString();

        if(tok.equals("")){
            Toast.makeText(this,"Favor de colocar token de seguridad",Toast.LENGTH_SHORT).show();
        }else{
            //validamos si es el mismo que el mensaje...

            //It's the same token, launch principal
            app_preferences.edit().putString("sesion","1").apply();

            Intent ii = new Intent(TokenSmsActivity.this,PrincipalActivity.class);
            startActivity(ii);
        }
    }

    public void openView(View v){
        try {
            Intent i = new Intent(this, CotizaActivity.class);
            startActivity(i);
        }
        catch(Exception e){
            Toast.makeText(this,"No se puede abrir la página.",Toast.LENGTH_SHORT).show();
        }
    }

    public void sendTokenA(View v){

        String telefon = app_preferences.getString("Celphone","null");

        threadtosync hilos = new threadtosync();
        hilos.SyncToken.execute(telefon, result.get(0).getPolicies().getId() + "", "Apptoken");

    }

    private class threadtosync {
        //************************** Asytask para obtener recuperat otken ws **********************************************
        AsyncTask<String, Void, Void> SyncToken = new AsyncTask<String, Void, Void>() {
            ProgressDialog progress = new ProgressDialog(TokenSmsActivity.this);
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
                        Intent i = new Intent(TokenSmsActivity.this, SyncActivity.class);
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
                progress.setTitle("Token");
                progress.setMessage("Recuperando...");
                //progress.setIcon(R.drawable.miituo);
                progress.setIndeterminate(true);
                progress.setCancelable(false);
                progress.show();
            }

            //va por listas de clientes
            //result es un List<InfoClient>
            @Override
            protected Void doInBackground(String... params) {
                ApiClient client = new ApiClient(TokenSmsActivity.this);
                try {
                    //recupera datos
                    //params[0] es el telefono
                    //result.clear();
                    //result = client.getInfoClient("InfoClientMobil/Celphone/" + params[0]);
                    String tokenreceived = client.getToken("TemporalToken/GetTemporalTokenPhone/" + params[0] + "/" + params[1] + "/" + params[2]);
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
                progress.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(TokenSmsActivity.this);
                builder.setTitle("");
                builder.setMessage("Se ha enviado un nuevo token");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog aler = builder.create();
                aler.show();
            }
        };
    }
}


