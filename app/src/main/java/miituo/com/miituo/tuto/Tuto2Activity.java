package miituo.com.miituo.tuto;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import miituo.com.miituo.R;

/**
 * Created by john.cristobal on 26/05/17.
 */

public class Tuto2Activity extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Log.w("Here","Fragment 1");

        View view = inflater.inflate(R.layout.welcome_slide2,container,false);
        final VideoView mVideoView1 = (VideoView) view.findViewById(R.id.videoView3);
        String uri = "android.resource://" + TutorialActivity.MyAdapter.context.getPackageName() + "/" + R.raw.onb32;
        if (mVideoView1 != null) {
            //mVideoView1.setVideoURI(Uri.parse(uri));
            mVideoView1.setVideoPath(uri);
            mVideoView1.setZOrderOnTop(true);
            mVideoView1.requestFocus();
            //mVideoView1.start();
            mVideoView1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mVideoView1.start();
                }
            });

            final MediaController ctrl = new MediaController(getContext());
            ctrl.setVisibility(View.GONE);
            mVideoView1.setMediaController(ctrl);
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
        return view;
    }

}
