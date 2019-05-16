package com.example.moviesrappi;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.moviesrappi.httpconects.DownloadJSON;
import com.example.moviesrappi.cache.SingletonCache;

public class SplashActivity extends AppCompatActivity {
    private LottieAnimationView lav;
    TextView titleProgress;
    View contentProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SingletonCache.initInstance();
        setContentView(R.layout.activity_splash);
        lav = (LottieAnimationView)findViewById(R.id.lav);
        titleProgress = (TextView) findViewById(R.id.titleProgress);
        contentProgress = (View)findViewById(R.id.contentProgress);
        titleProgress.setText("");
        contentProgress.setVisibility(View.GONE);
        startLAV();
        try{
            new DownloadJSON(this,true, contentProgress).ejecutar();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void startLAV(){
        ValueAnimator ani = ValueAnimator.ofFloat(0f, 1f).setDuration(2000);
        ani.setRepeatCount(ValueAnimator.INFINITE);
        ani.setRepeatMode(ValueAnimator.RESTART);
        ani.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lav.setProgress((Float)animation.getAnimatedValue());
            }
        });
        if (lav.getProgress() == 0f){
            ani.start();
        }else{
            lav.setProgress(0f);
        }
    }
}
