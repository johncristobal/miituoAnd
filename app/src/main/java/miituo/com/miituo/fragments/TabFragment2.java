package miituo.com.miituo.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import miituo.com.miituo.R;
import miituo.com.miituo.data.IinfoClient;

/**
 * Created by john.cristobal on 04/05/17.
 */

public class TabFragment2 extends Fragment {

    Typeface tipo;

    /*public TabFragment2(Typeface t){
        tipo = t;
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_fragment_2, container, false);

        TextView tex = (TextView)v.findViewById(R.id.textView);
        tex.setTypeface(PagerAdapter.tipo);
        tex = (TextView)v.findViewById(R.id.textView78);
        tex.setTypeface(PagerAdapter.tipo);

        TextView fecha = (TextView)v.findViewById(R.id.texdate);
        TextView fechaabajo = (TextView)v.findViewById(R.id.textView80);
        fecha.setTypeface(PagerAdapter.tipobold);
        fechaabajo.setTypeface(PagerAdapter.tipobold);

        Calendar c = Calendar.getInstance();

        //fecha limite
        Date fechacadena = IinfoClient.InfoClientObject.getPolicies().getLimitReportDate();
        c.setTime(fechacadena);
        c.add(Calendar.DATE, -1);
        fechacadena = c.getTime();

        SimpleDateFormat month_date = new SimpleDateFormat("MMM");
        String month_name = month_date.format(c.getTime());
        String real = getName(month_name);
        SimpleDateFormat year_data = new SimpleDateFormat("yyyy");
        String year = year_data.format(c.getTime());

        //fecha menos 5 dias
        Date fechalimite = IinfoClient.InfoClientObject.getPolicies().getLimitReportDate();
        c.setTime(fechalimite);
        SimpleDateFormat month_date_before = new SimpleDateFormat("MMM");
        c.add(Calendar.DATE, -4);
        fechalimite = c.getTime();
        String month_name_before = month_date_before.format(c.getTime());
        String real_before = getName(month_name_before);
        SimpleDateFormat year_data_bef = new SimpleDateFormat("yyyy");
        String year_bef = year_data_bef.format(c.getTime());

        fecha.setText("del "+new SimpleDateFormat("dd").format(fechalimite)+" de "+real);
        fechaabajo.setText("al "+new SimpleDateFormat("dd").format(fechacadena)+" de "+real_before);

        return v;
    }

    public String getName(String nombre){

        if(nombre.equals("ene") || nombre.equals("Jan") || nombre.equals("jan") || nombre.equals("Ene")){
            return "enero";
        }else if(nombre.equals("feb") || nombre.equals("Feb")){
            return "febrero";
        }else if (nombre.equals("mar") || nombre.equals("Mar")){
            return "marzo";
        }else if (nombre.equals("abr") || nombre.equals("apr") || nombre.equals("Apr")){
            return "abril";
        }else if (nombre.equals("may") || nombre.equals("May")){
            return "mayo";
        }else if (nombre.equals("jun") || nombre.equals("Jun")){
            return "junio";
        }else if (nombre.equals("jul") || nombre.equals("Jul")){
            return "julio";
        }else if (nombre.equals("ago") || nombre.equals("aug") || nombre.equals("Aug") || nombre.equals("Ago")){
            return "agosto";
        }else if (nombre.equals("sep") || nombre.equals("Sep")){
            return "septiembre";
        }else if (nombre.equals("oct") || nombre.equals("Oct")){
            return "octubre";
        }else if (nombre.equals("nov") || nombre.equals("Nov")){
            return "noviembre";
        }else if (nombre.equals("dic") || nombre.equals("dec") || nombre.equals("Dic") || nombre.equals("Dec")){
            return "diciembre";
        }else{
            return nombre;
        }
    }
}
