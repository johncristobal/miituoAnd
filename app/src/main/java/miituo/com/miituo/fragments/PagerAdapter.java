package miituo.com.miituo.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by john.cristobal on 04/05/17.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    public static Context context;
    public static Typeface tipo;
    public static Typeface tipobold;

    public PagerAdapter(FragmentManager fm, int NumOfTabs,Context c,Typeface t,Typeface tb) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        context = c;
        tipo = t;
        tipobold = tb;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TabFragment1 tab1 = new TabFragment1();
                //TabFragment1 tab1 = new TabFragment1(context,tipo);
                return tab1;
            case 1:
                TabFragment2 tab2 = new TabFragment2();
                //TabFragment2 tab2 = new TabFragment2(tipo);
                return tab2;
            /*case 2:
                TabFragment3 tab3 = new TabFragment3();
                return tab3;*/
            case 2:
                TabFragment4 tab4 = new TabFragment4();
                //TabFragment4 tab4 = new TabFragment4(context,tipo);
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
