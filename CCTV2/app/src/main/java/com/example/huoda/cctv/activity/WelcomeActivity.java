package com.example.huoda.cctv.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.huoda.cctv.R;


public class WelcomeActivity extends Activity {
    private static final int THREE_SECOND = 3000;
    private static final int ONE_SECOND = 1000;
    private static final int GO_MAIN = 100;
    private static final int GO_GUIDE = 101;

    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_MAIN:
                    goMain();
                    break;
                case GO_GUIDE:
                    goGuide();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        init();//调用
    }

    private void init() {
        SharedPreferences sf = getSharedPreferences("data", MODE_PRIVATE);//判断是否是第一次进入
        boolean isFirstIn = sf.getBoolean("isFirstIn", true);
        SharedPreferences.Editor editor = sf.edit();
        if (isFirstIn) {     //若为true，则是第一次进入
            editor.putBoolean("isFirstIn", false);
            mhandler.sendEmptyMessageDelayed(GO_GUIDE, THREE_SECOND);//将欢迎页停留3秒，并且将message设置为跳转到
            //   引导页SplashActivity，跳转在goGuide中实现
        } else {
            mhandler.sendEmptyMessageDelayed(GO_MAIN, ONE_SECOND);//将欢迎页停留1秒，并且将message设置文跳转到                                                                   MainActivity，跳转功能在goMain中实现
        }
        editor.commit();
    }

    private void goMain() {
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void goGuide() {
        Intent intent = new Intent(WelcomeActivity.this, GuideActivity.class);
        startActivity(intent);
        finish();
    }
}
