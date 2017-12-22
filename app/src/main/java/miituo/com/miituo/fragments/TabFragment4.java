package miituo.com.miituo.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import miituo.com.miituo.R;

/**
 * Created by john.cristobal on 04/05/17.
 */

public class TabFragment4 extends Fragment {

    Context context;
    Typeface tipo;

    /*public TabFragment4(Context c,Typeface t){
        context = c;
        tipo = t;
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_fragment_4, container, false);

        Button call = (Button)v.findViewById(R.id.button6);
        TextView res = (TextView)v.findViewById(R.id.textView56);
        TextView fon = (TextView)v.findViewById(R.id.textView);

        res.setTypeface(PagerAdapter.tipo);
        fon.setTypeface(PagerAdapter.tipo);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //Intent intent = new Intent(Intent.ACTION_CALL);
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "11025280", null));
                    //intent.setData(Uri.parse("11025280"));
                    getActivity().startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        return v;
    }

}
