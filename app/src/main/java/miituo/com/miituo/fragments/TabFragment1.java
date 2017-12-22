package miituo.com.miituo.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import miituo.com.miituo.ExpandibleListAdpter;
import miituo.com.miituo.R;
import miituo.com.miituo.ReportOdometerActivity;
import miituo.com.miituo.data.IinfoClient;
import miituo.com.miituo.data.InfoSectionItem;

/**
 * Created by miituo on 04/05/17.
 */

public class TabFragment1 extends Fragment {

    ExpandibleListAdpter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<InfoSectionItem>> listDataChild;

    ImageView odometroboton;

    public Context context;

    public TextView carro,poliza,placas;
    public Typeface tipo;

    /*public TabFragment1(){}

    public TabFragment1(Context c, Typeface t){
        context = c;
        tipo = t;
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Log.w("Here","Fragment 1");

        View v = inflater.inflate(R.layout.tab_fragment_1,container,false);

        //get labels to show data
        carro = (TextView)v.findViewById(R.id.textView37);
        poliza = (TextView)v.findViewById(R.id.textViewpolizadetail);
        placas = (TextView)v.findViewById(R.id.textView39);

        ImageView fotocarro = (ImageView)v.findViewById(R.id.imageView5);
        File image = new File(PagerAdapter.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)+File.separator+"frontal_"+IinfoClient.InfoClientObject.getPolicies().getNoPolicy()+".png");
        if(image.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(image.getAbsolutePath());

            Bitmap resized = Bitmap.createScaledBitmap(myBitmap, myBitmap.getWidth()/4, myBitmap.getHeight()/4, false);
            fotocarro.setImageBitmap(resized);
        }

        carro.setTypeface(PagerAdapter.tipo);
        poliza.setTypeface(PagerAdapter.tipo);
        placas.setTypeface(PagerAdapter.tipo);

        expListView=(ExpandableListView)v.findViewById(R.id.explist);
        odometroboton = (ImageView)v.findViewById(R.id.imageViewOdometer);
        odometroboton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(PagerAdapter.context,ReportOdometerActivity.class);
                startActivity(i);
            }
        });

        GetData();
        listAdapter=new ExpandibleListAdpter(PagerAdapter.context, listDataChild,listDataHeader,tipo);
        expListView.setAdapter(listAdapter);
        //expListView.expandGroup(0);
        //expListView.expandGroup(1);
        //expListView.expandGroup(2);

        //VIEW OR HIDE BUTTON to register odometer
        /*int regOdometer = IinfoClient.InfoClientObject.getPolicies().getReportState();
        if(regOdometer == 13){
            odometroboton.setVisibility(View.VISIBLE);
        }
        TextView cad = (TextView)v.findViewById(R.id.textViewFrag1);
        cad.setText("Hello frag 1");*/

        return v;//inflater.inflate(R.layout.tab_fragment_1, container, false);
    }

    /*
       * Set data for expandableList...
       * */
    private void GetData()
    {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<InfoSectionItem>>();

        poliza.setText(IinfoClient.InfoClientObject.getPolicies().getNoPolicy());
        carro.setText(IinfoClient.InfoClientObject.getPolicies().getVehicle().getDescription().getDescription());
        placas.setText("");

        // Adding child data
        listDataHeader.add("Tu información");
        listDataHeader.add("Tu auto");
        listDataHeader.add("Tu odómetro");

        //Llenado de Información del Cliente
        List<InfoSectionItem> InfoPersonal=new ArrayList<>();
        //InfoPersonal.add(new InfoSectionItem("Nombre","Nombre"));
        InfoPersonal.add(new InfoSectionItem("Nombre", IinfoClient.InfoClientObject.getClient().getName()+" "+IinfoClient.InfoClientObject.getClient().getLastName()+" "+IinfoClient.InfoClientObject.getClient().getMotherName()));
        InfoPersonal.add(new InfoSectionItem("Celular", IinfoClient.InfoClientObject.getClient().getCelphone()));
        //InfoPersonal.add(new InfoSectionItem("Apellido Paterno",));
        //InfoPersonal.add(new InfoSectionItem("Apellido Materno",));

        //llenado de informaciín del Vehículo
        List<InfoSectionItem> Vehiculo=new ArrayList<>();
        Vehiculo.add(new InfoSectionItem("Placas",IinfoClient.InfoClientObject.getPolicies().getVehicle().getPlates()));
        Vehiculo.add(new InfoSectionItem("Modelo",String.valueOf(IinfoClient.InfoClientObject.getPolicies().getVehicle().getModel().getModel())));

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

        //odometro
        //List<InfoSectionItem> Odometro=new ArrayList<>();
        //Odometro.add(new InfoSectionItem("Ùltimo odómetro",IinfoClient.InfoClientObject.getPolicies().getLastOdometer()+""));
        //odometro
        List<InfoSectionItem> Odometro=new ArrayList<>();
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        Double dos = Double.parseDouble(IinfoClient.InfoClientObject.getPolicies().getLastOdometer()+"");
        String yourFormattedString = formatter.format(dos);

        Odometro.add(new InfoSectionItem("Ùltimo odómetro",yourFormattedString));

        //LLenado de información de cobertura
        //List<InfoSectionItem> Cobertura=new ArrayList<>();
        //Cobertura.add(new InfoSectionItem("Cobertura","CoberturaA"));
        //Cobertura.add(new InfoSectionItem("Cobertura",IinfoClient.InfoClientObject.getPolicies().getCoverage().getDescription()));

        listDataChild.put(listDataHeader.get(0), InfoPersonal); // Header, Child data
        listDataChild.put(listDataHeader.get(1), Vehiculo);
        listDataChild.put(listDataHeader.get(2), Odometro);
    }
}
