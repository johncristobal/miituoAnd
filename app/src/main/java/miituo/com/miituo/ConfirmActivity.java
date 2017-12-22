package miituo.com.miituo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import miituo.com.miituo.data.IinfoClient;

public class ConfirmActivity extends AppCompatActivity {

    private Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle("Odómetro");
        //toolbar.setTitleTextColor(Color.BLACK);
        //setSupportActionBar(toolbar);

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

        typeface = Typeface.createFromAsset(getAssets(), "fonts/herne1.ttf");
        TextView leyenda = (TextView)findViewById(R.id.textView8);
        TextView leyenda2 = (TextView)findViewById(R.id.textView58);
        TextView res = (TextView)findViewById(R.id.textView41);
        EditText edit = (EditText)findViewById(R.id.editTextConfirma);
        EditText editlast = (EditText)findViewById(R.id.editTextLast);
        edit.setTypeface(typeface);
        editlast.setTypeface(typeface);
        leyenda.setTypeface(typeface);
        leyenda2.setTypeface(typeface);
        res.setTypeface(typeface);

        int a = IinfoClient.InfoClientObject.getPolicies().getLastOdometer();

        if(a == 0){
            RelativeLayout ultimo = (RelativeLayout)findViewById(R.id.relativeLayout5);
            ultimo.setVisibility(View.GONE);

            //RelativeLayout ahora = (RelativeLayout)findViewById(R.id.relativeLayout4);
        }else{
            editlast.setText(a+"");
            editlast.setEnabled(false);
        }

        ImageButton back = (ImageButton)findViewById(R.id.imageView12);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ConfirmActivity.this,PrincipalActivity.class);
                startActivity(i);
            }
        });

        Typeface typefacebold = Typeface.createFromAsset(getAssets(), "fonts/herne.ttf");
        TextView titulo = (TextView)findViewById(R.id.textView27);
        titulo.setTypeface(typefacebold);
    }

    public void validar(View v){
        //get odometer value
        //validar no sea ""
        //lanzar nuevo actiivty con valor para confimar del otro lado...

        EditText odometro = (EditText)findViewById(R.id.editTextConfirma);
        String odo = odometro.getText().toString();

        if(odo.equals("")){
            Toast.makeText(this,"Es necesario capturar los kms. que marca el odómetro.",Toast.LENGTH_SHORT).show();
        }else{
            //launch activity and save odometro
            Intent i = new Intent(this,LastOdometerActivity.class);
            i.putExtra("valor",odo);
            startActivity(i);
        }
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
            //finish();
            Intent i = new Intent(ConfirmActivity.this, PrincipalActivity.class);
            startActivity(i);
        }
        return true;
    }

}
