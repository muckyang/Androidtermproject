package com.example.yn782.androidtermproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by yn782 on 2016-11-30.
 */

public class SplashActivity  extends AppCompatActivity {

    final String TAG = "AnimationTest";
    FrameLayout mFrame;
    ImageView mRocket;
    ImageView mFirework;
    ImageView mBack;
    int mScreenHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        mFrame = (FrameLayout)findViewById(R.id.splash);
        mBack = (ImageView) findViewById(R.id.back);
        mFirework = (ImageView) findViewById(R.id.fire);
        mRocket = (ImageView) findViewById(R.id.rocket);
    }

    @Override
    protected void onResume() {
        super.onResume();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        mScreenHeight = displaymetrics.heightPixels;

        startCountDownFrameAnimation();

        startFireTweenAnimation();

        /**
         * 아래 4가지 startRocket 애니메이션 중에 하나를 선택하여 테스트해 보세요.
         */
        startRocketTweenAnimation();
//      startRocketObjectPropertyAnimation();
//      startRocketPropertyAnimationByXML();
//      startRocketValuePropertyAnimation();

    }

    private void startCountDownFrameAnimation() {
        mBack.setBackgroundResource(R.drawable.back);
        // AnimationDrawable countdownAnim = (AnimationDrawable) mBack.getBackground();
        // countdownAnim.start();

    }

    private void startFireTweenAnimation() {
        Animation fire_anim = AnimationUtils.loadAnimation(this, R.anim.text);
        mFirework.startAnimation(fire_anim);
        fire_anim.setAnimationListener(animationListener);
    }

    private void startRocketTweenAnimation() {
        Animation rocket_anim = AnimationUtils.loadAnimation(this, R.anim.book);
        mRocket.startAnimation(rocket_anim);
    }

    Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            Log.i(TAG, "onAnimationStart");
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            Log.i(TAG, "onAnimationEnd");
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            Log.i(TAG, "onAnimationRepeat");
        }
    };

}
