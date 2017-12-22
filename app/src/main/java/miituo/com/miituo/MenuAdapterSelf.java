package miituo.com.miituo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

/**
 * Created by john.cristobal on 30/06/17.
 */

public class MenuAdapterSelf extends BaseAdapter {

    private Context mContext;
    public Typeface tipo;
    public String[] values = new String[] { "Tus pólizas",
            "Acerca de",
            "Cotiza"
    };


    public MenuAdapterSelf(Context c, Typeface t){
        mContext = c;
        tipo = t;
    }

    @Override
    public int getCount() {
        return values.length;
    }

    @Override
    public Object getItem(int position) {
        return values[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v=View.inflate(mContext,R.layout.menuitem,null);
        TextView opcion=(TextView)v.findViewById(R.id.textView63);
        ImageView imagen = (ImageView)v.findViewById(R.id.imageView11);

        opcion.setTypeface(tipo);
        //CardView card = (CardView)v.findViewById(R.id.view2);
        //ImageView circulo = (ImageView)v.findViewById(R.id.imageView4);
        //set values

        opcion.setText(values[position]);

        if(position == 0){
            imagen.setImageResource(R.drawable.ico_poliza);
        }else if(position == 1){
            imagen.setImageResource(R.drawable.ico_ayuda);
        }else if(position == 2){
            imagen.setImageResource(R.drawable.icon_cotiza);
        }



        return v;

    }
}
