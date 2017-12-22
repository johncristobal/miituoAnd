package miituo.com.miituo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import miituo.com.miituo.data.IinfoClient;
import miituo.com.miituo.data.InfoSectionItem;
import miituo.com.miituo.fragments.PagerAdapter;

public class DetallesActivity extends AppCompatActivity {

    ExpandibleListAdpter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<InfoSectionItem>> listDataChild;

    ImageView odometroboton;
    private Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbartaby);
        toolbar.setTitle("General");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);

        //get back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("").setIcon(R.drawable.general));
        //tabLayout.addTab(tabLayout.newTab().setText("").setIcon(R.drawable.historial));
        tabLayout.addTab(tabLayout.newTab().setText("").setIcon(R.drawable.odo));
        //tabLayout.addTab(tabLayout.newTab().setText("").setIcon(R.drawable.pago));
        tabLayout.addTab(tabLayout.newTab().setText("").setIcon(R.drawable.sini));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        typeface = Typeface.createFromAsset(getAssets(), "fonts/herne1.ttf");
        Typeface typefacebold = Typeface.createFromAsset(getAssets(), "fonts/herne.ttf");

        tabLayout.setSelectedTabIndicatorColor(Color.rgb(34,201,252));
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(),this,typeface,typefacebold);

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                //change icon
                if(tab.getPosition()==0){
                    tab.setIcon(R.drawable.general);
                }else if(tab.getPosition()==1){
                    tab.setIcon(R.drawable.odoaz);
                }else if(tab.getPosition()==2){
                    tab.setIcon(R.drawable.sinia);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //change icon
                if(tab.getPosition()==0){
                    tab.setIcon(R.drawable.generalg);
                }else if(tab.getPosition()==1){
                    tab.setIcon(R.drawable.odo);
                }else if(tab.getPosition()==2){
                    tab.setIcon(R.drawable.sini);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        /*expListView=(ExpandableListView)findViewById(R.id.explist);
        odometroboton = (ImageView)findViewById(R.id.imageViewOdometer);
        odometroboton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(DetallesActivity.this,ReportOdometerActivity.class);
                startActivity(i);
            }
        });

        GetData();
        listAdapter=new ExpandibleListAdpter(this, listDataChild,listDataHeader);
        expListView.setAdapter(listAdapter);
        expListView.expandGroup(0);
        expListView.expandGroup(1);
        expListView.expandGroup(2);

        //VIEW OR HIDE BUTTON to register odometer
        int regOdometer = IinfoClient.InfoClientObject.getPolicies().getReportState();
        if(regOdometer == 13){
            odometroboton.setVisibility(View.VISIBLE);
        }*/
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
        }
        return true;
    }

    /*
    * Set data for expandableList...
    * */
    private void GetData()
    {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<InfoSectionItem>>();

        // Adding child data
        listDataHeader.add("Tu información");
        listDataHeader.add("Tu auto");
        listDataHeader.add("Tu odómetro");

        //Llenado de Información del Cliente
        List<InfoSectionItem> InfoPersonal=new ArrayList<>();
        InfoPersonal.add(new InfoSectionItem("Nombre","Nombre"));
        //InfoPersonal.add(new InfoSectionItem("Nombre",IinfoClient.InfoClientObject.getClient().getName()+" "+IinfoClient.InfoClientObject.getClient().getLastName()+" "+IinfoClient.InfoClientObject.getClient().getMotherName()));
        //InfoPersonal.add(new InfoSectionItem("Apellido Paterno",));
        //InfoPersonal.add(new InfoSectionItem("Apellido Materno",));

        //llenado de informaciín del Vehículo
        List<InfoSectionItem> Vehiculo=new ArrayList<>();
        Vehiculo.add(new InfoSectionItem("Color1","ColorA"));
        Vehiculo.add(new InfoSectionItem("Color2","ColorB"));
        Vehiculo.add(new InfoSectionItem("Color3","ColorC"));
        /*
        Vehiculo.add(new InfoSectionItem("Color",IinfoClient.InfoClientObject.getPolicies().getVehicle().getColor()));
        Vehiculo.add(new InfoSectionItem("Placas",IinfoClient.InfoClientObject.getPolicies().getVehicle().getPlates()));
        Vehiculo.add(new InfoSectionItem("Motor",IinfoClient.InfoClientObject.getPolicies().getVehicle().getNoMotor()));
        Vehiculo.add(new InfoSectionItem("Capacidad",String.valueOf(IinfoClient.InfoClientObject.getPolicies().getVehicle().getCapacity())));
        Vehiculo.add(new InfoSectionItem("Marca",IinfoClient.InfoClientObject.getPolicies().getVehicle().getBrand().getDescription()));
        Vehiculo.add(new InfoSectionItem("Modelo",String.valueOf(IinfoClient.InfoClientObject.getPolicies().getVehicle().getModel().getModel())));
        Vehiculo.add(new InfoSectionItem("Tipo",IinfoClient.InfoClientObject.getPolicies().getVehicle().getType().getDescription()));
        Vehiculo.add(new InfoSectionItem("Subtipo",IinfoClient.InfoClientObject.getPolicies().getVehicle().getSubtype().getDescription()));
        Vehiculo.add(new InfoSectionItem("Descripción",IinfoClient.InfoClientObject.getPolicies().getVehicle().getDescription().getDescription()));
        */

        //LLenado de información de cobertura
        List<InfoSectionItem> Cobertura=new ArrayList<>();
        Cobertura.add(new InfoSectionItem("Cobertura","CoberturaA"));
        //Cobertura.add(new InfoSectionItem("Cobertura",IinfoClient.InfoClientObject.getPolicies().getCoverage().getDescription()));

        listDataChild.put(listDataHeader.get(0), InfoPersonal); // Header, Child data
        listDataChild.put(listDataHeader.get(1), Vehiculo);
        listDataChild.put(listDataHeader.get(2), Cobertura);
    }

}
