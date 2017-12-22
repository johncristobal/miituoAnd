package miituo.com.miituo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

public class CotizaActivity extends AppCompatActivity {

    public WebView vista;
    SharedPreferences app_preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cotiza);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Cotiza");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        vista = (WebView)findViewById(R.id.vistaweb);

        vista.loadUrl("https://miituo.com");
        app_preferences= PreferenceManager.getDefaultSharedPreferences(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == android.R.id.home) {
            //finish(); // close this activity and return to preview activity (if there is any)

            if(app_preferences.getString("sesion","null").equals("1")){
                Intent i = new Intent(CotizaActivity.this, PrincipalActivity.class);
                startActivity(i);
            }else{
                finish();
            }
        }
        return true;
    }
}
