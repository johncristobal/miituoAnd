package miituo.com.miituo.tuto;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import miituo.com.miituo.R;
import miituo.com.miituo.SyncActivity;

public class TutorialActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private MyAdapter myAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext;
    private PrefManager prefManager;

    //public video1 v1;
    //public video2 v2;

    public VideoView mVideoView1,mVideoView2,mVideoView3;

    static final int ITEMS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        prefManager = new PrefManager(this);
        /*if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }*/

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_view_pager_demo);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);

        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.welcome_slide1,
                R.layout.welcome_slide2,
                R.layout.welcome_slide3};
        //R.layout.welcome_slide4};

        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        myAdapter = new MyAdapter(getSupportFragmentManager(),this);
        //viewPager.setAdapter(myAdapter);
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        //startActivity(new Intent(TutorialActivity.this, MainActivity.class));
        startActivity(new Intent(TutorialActivity.this, SyncActivity.class));
        overridePendingTransition(R.anim.slide_in_up,R.anim.slide_out_up);
        //finish();
    }

    //viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText(getString(R.string.start));
                btnNext.setVisibility(View.VISIBLE);
                btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText(getString(R.string.next));
                btnNext.setVisibility(View.GONE);
                btnSkip.setVisibility(View.GONE);
            }

            if(position == 0){
                mVideoView1.start();
            }else if (position == 1){
                mVideoView2.start();
            }else if (position == 2){
                mVideoView3.start();
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

            /*Log.e("Cambio: ",arg0+"");
            if(arg0 == 0){
                v1.mPreview1.setBackgroundColor(android.R.color.transparent);
                v1.play0();
            }else if(arg0 == 1){
                v2.mPreview1.setBackgroundColor(android.R.color.transparent);
                v2.play1();
            }*/
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            /*if(arg0 == 0){
                //v1.mPreview1.setBackgroundColor(android.R.color.transparent);
                v1.play0();
            }else if(arg0 == 1){
                //v2.mPreview1.setBackgroundColor(android.R.color.transparent);
                v2.play1();
            }*/

        }
    };

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }


    /*
    Nuevo page adapter
     */
    public static class MyAdapter extends FragmentStatePagerAdapter {

        public static Context context;
        public static Typeface tipo;

        public MyAdapter(FragmentManager fragmentManager,Context c) {
            super(fragmentManager);
            context = c;
        }

        @Override
        public int getCount() {
            return ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show image
                    //return ImageFragment.init(position);
                    Tuto1Activity tab1 = new Tuto1Activity();
                    //TabFragment1 tab1 = new TabFragment1(context,tipo);
                    return tab1;
                case 1: // Fragment # 1 - This will show image
                    Tuto2Activity tab2 = new Tuto2Activity();
                    //TabFragment1 tab1 = new TabFragment1(context,tipo);
                    return tab2;
                case 2: // Fragment # 1 - This will show image
                    Tuto3Activity tab3 = new Tuto3Activity();
                    //TabFragment1 tab1 = new TabFragment1(context,tipo);
                    return tab3;
                default:// Fragment # 2-9 - Will show list
                    return ImageFragment.init(position);
            }
        }
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter //implements SurfaceHolder.Callback
    {
        private LayoutInflater layoutInflater;

        //MediaPlayer mp1,mp2,mp3;
        //SurfaceView mPreview1,mPreview2,mPreview3;
        //SurfaceHolder holder;

        String resource;

        String flag;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);

            try {
                if (position == 0) {
                    //SurfaceView surfaceViewFrame = (SurfaceView) view.findViewById(R.id.surfaceView);
                    //MediaPlayer player = new MediaPlayer();

                /*GifImageView gifImageView = (GifImageView)view.findViewById(R.id.GifImageView);
                gifImageView.setGifImageResource(R.drawable.splash1);*/

                /*flag = "0";

                v1 = new video1();

                //getWindow().setFormat(PixelFormat.UNKNOWN);
                v1.mPreview1 = (SurfaceView)view.findViewById(R.id.surfaceView1);
                v1.holder = v1.mPreview1.getHolder();
                v1.holder.setFixedSize(800, 480);
                v1.holder.addCallback(v1);*/
                    //holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                    //v1.mp1 = new MediaPlayer();

                    //VideoView mVideoView = (VideoView)view.findViewById(R.id.videoView2);
                    mVideoView1 = (VideoView) view.findViewById(R.id.videoView2);
                    String uri = "android.resource://" + getPackageName() + "/" + R.raw.miituoonb12a;
                    if (mVideoView1 != null) {
                        //mVideoView1.setVideoURI(Uri.parse(uri));
                        mVideoView1.setVideoPath(uri);
                        mVideoView1.setZOrderOnTop(true);
                        mVideoView1.requestFocus();
                        mVideoView1.start();

                        //ViewGroup.LayoutParams params=mVideoView.getLayoutParams();
                        //params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
                        //params.addRule(RelativeLayout.CENTER_IN_PARENT, 0);
                        //mVideoView.setLayoutParams(new FrameLayout.LayoutParams(550,200));
                        //DisplayMetrics metrics = new DisplayMetrics();
                        //getWindowManager().getDefaultDisplay().getMetrics(metrics);
                        //mVideoView.setLayoutParams(new Toolbar.LayoutParams(metrics.widthPixels, metrics.heightPixels));

                        //mVideoView.start();
                    } else { //toast or print "mVideoView is null"
                    }
                } else if (position == 1) {

                /*flag = "1";

                v2 = new video2();

                //getWindow().setFormat(PixelFormat.UNKNOWN);
                v2.mPreview1 = (SurfaceView)view.findViewById(R.id.surfaceView2);
                v2.holder = v2.mPreview1.getHolder();
                v2.holder.setFixedSize(800, 480);
                v2.holder.addCallback(v2);*/

                /*flag = "1";
                getWindow().setFormat(PixelFormat.UNKNOWN);
                mPreview2 = (SurfaceView)view.findViewById(R.id.surfaceView2);
                holder = mPreview2.getHolder();
                holder.setFixedSize(800, 480);
                holder.addCallback(new video1());
                holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                mp2 = new MediaPlayer();*/

                /**/

                /*getWindow().setFormat(PixelFormat.UNKNOWN);
                mPreview = (SurfaceView)view.findViewById(R.id.surfaceView);
                holder = mPreview.getHolder();
                holder.setFixedSize(800, 480);
                holder.addCallback(this);
                holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                mp = new MediaPlayer();*/

                    //VideoView mVideoView = (VideoView)view.findViewById(R.id.videoView3);
                    mVideoView2 = (VideoView) view.findViewById(R.id.videoView3);
                    String uri = "android.resource://" + getPackageName() + "/" + R.raw.miituoonb2b;
                    if (mVideoView2 != null) {
                        mVideoView2.setVideoPath(uri);
                        //mVideoView2.setVideoURI(Uri.parse(uri));
                        mVideoView2.setZOrderOnTop(true);
                        mVideoView2.requestFocus();
                        //mVideoView2.start();
                    } else { //toast or print "mVideoView is null"
                    }
                } else if (position == 2) {

                    //flag = "2";
                /*getWindow().setFormat(PixelFormat.UNKNOWN);
                mPreview3 = (SurfaceView)view.findViewById(R.id.surfaceView3);
                holder = mPreview3.getHolder();
                holder.setFixedSize(800, 480);
                holder.addCallback(this);
                holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                mp3 = new MediaPlayer();*/

                /*getWindow().setFormat(PixelFormat.UNKNOWN);
                mPreview = (SurfaceView)view.findViewById(R.id.surfaceView);
                holder = mPreview.getHolder();
                holder.setFixedSize(800, 480);
                holder.addCallback(this);
                holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                mp = new MediaPlayer();*/

                    //VideoView mVideoView = (VideoView)view.findViewById(R.id.videoView4);
                    mVideoView3 = (VideoView) view.findViewById(R.id.videoView4);
                    String uri = "android.resource://" + getPackageName() + "/" + R.raw.onb32;
                    if (mVideoView3 != null) {
                        //mVideoView3.setVideoURI(Uri.parse(uri));
                        mVideoView3.setVideoPath(uri);
                        mVideoView3.setZOrderOnTop(true);
                        mVideoView3.requestFocus();
                        //mVideoView.start();
                    } else { //toast or print "mVideoView is null"
                    }
                }

                container.addView(view);
            }catch(Exception e){
                e.printStackTrace();
            }

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }


        //@Override
        public void surfaceCreated(SurfaceHolder holder) {

            /*if(flag.equals("0")){
                mp1.setDisplay(holder);
                resource = "android.resource://" + getPackageName() + "/" + R.raw.miituoonb12;
                //play0();
            }else if(flag.equals("1")){
                mp2.setDisplay(holder);
                resource = "android.resource://" + getPackageName() + "/" + R.raw.miituoonb2;
                play1();
            }else if(flag.equals("2")){
                mp3.setDisplay(holder);
                resource = "android.resource://" + getPackageName() + "/" + R.raw.onb3;
                play2();
            }*/
        }

        //@Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        //@Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }

        /*void play1(){
            try {
                //String filepath = "android.resource://" + getPackageName() + "/" + R.raw.miituoonb12;
                //mp.setDataSource(filepath);
                mp2.setDataSource(TutorialActivity.this, Uri.parse(resource));
                mp2.prepare();

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mp2.start();
        }
        void play2(){
            try {
                //String filepath = "android.resource://" + getPackageName() + "/" + R.raw.miituoonb12;
                //mp.setDataSource(filepath);
                mp3.setDataSource(TutorialActivity.this, Uri.parse(resource));
                mp3.prepare();

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mp3.start();
        }*/
    }

    public class video1 implements SurfaceHolder.Callback{

        MediaPlayer mp1,mp2,mp3;
        SurfaceView mPreview1,mPreview2,mPreview3;
        SurfaceHolder holder;

        String resource;

        public video1(){
            mp1 = new MediaPlayer();
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mp1.setDisplay(holder);
            resource = "android.resource://" + getPackageName() + "/" + R.raw.miituoonb12;
            //play0();
            try {
                //String filepath = "android.resource://" + getPackageName() + "/" + R.raw.miituoonb12;
                //mp.setDataSource(filepath);
                mp1.setDataSource(TutorialActivity.this, Uri.parse(resource));
                mp1.prepare();

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
        }

        void play0(){
            mp1.start();
        }
    }

    public class video2 implements SurfaceHolder.Callback{

        MediaPlayer mp1,mp2,mp3;
        SurfaceView mPreview1,mPreview2,mPreview3;
        SurfaceHolder holder;

        String resource;

        public video2(){
            mp2 = new MediaPlayer();
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mp2.setDisplay(holder);
            resource = "android.resource://" + getPackageName() + "/" + R.raw.miituoonb2;
            //play1();
            try {
                //String filepath = "android.resource://" + getPackageName() + "/" + R.raw.miituoonb12;
                //mp.setDataSource(filepath);
                mp2.setDataSource(TutorialActivity.this, Uri.parse(resource));
                mp2.prepare();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
        }

        void play1(){
            mp2.start();
        }
    }
}