package com.example.huoda.left_ship;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {

    private long exitTime;
    private EditText et_name;
    private EditText et_pwd;
    private ImageView ivLogo;
    private Button bt_login;

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();

        initAnims();
    }*/
   private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 注意：此处将setContentView()方法注释掉
       // setContentView(R.layout.activity_start);
        setContentView(R.layout.activity_login);
        initViews();
        initAnims();
        et_name.setVisibility(View.INVISIBLE);
        et_pwd.setVisibility(View.INVISIBLE);
        bt_login.setVisibility(View.INVISIBLE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               // initAnims();
                et_name.setVisibility(View.VISIBLE);
                et_pwd.setVisibility(View.VISIBLE);
                bt_login.setVisibility(View.VISIBLE);
            }
        }, 2601);
    }


    private void initViews() {
        et_name = (EditText) findViewById(R.id.et_name);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        ivLogo = (ImageView) findViewById(R.id.iv_logo);
        bt_login= (Button)findViewById(R.id.bt_login);
    }

    private void initAnims() {
        //初始化底部注册、登录的按钮动画
        //以控件自身所在的位置为原点，从下方距离原点200像素的位置移动到原点
        ObjectAnimator tranLogin = ObjectAnimator.ofFloat(et_name, "translationY", 400, 0);
        ObjectAnimator tranRegister = ObjectAnimator.ofFloat(et_pwd, "translationY", 400, 0);
        ObjectAnimator tranbt = ObjectAnimator.ofFloat(bt_login, "translationY", 400, 0);
        //将注册、登录的控件alpha属性从0变到1
        ObjectAnimator alphaLogin = ObjectAnimator.ofFloat(et_name, "alpha", 0, 1);
        ObjectAnimator alphaRegister = ObjectAnimator.ofFloat(et_pwd, "alpha", 0, 1);
        ObjectAnimator alphabt = ObjectAnimator.ofFloat(bt_login, "alpha", 0, 1);

        final AnimatorSet bottomAnim = new AnimatorSet();
        bottomAnim.setDuration(2100);
        //同时执行控件平移和alpha渐变动画
        bottomAnim.play(tranLogin).with(tranRegister).with(tranbt).with(alphaLogin).with(alphaRegister).with(alphabt);

        //获取屏幕高度
        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        int screenHeight = metrics.heightPixels;

        //通过测量，获取ivLogo的高度
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        ivLogo.measure(w, h);
        int logoHeight = ivLogo.getMeasuredHeight();

        //初始化ivLogo的移动和缩放动画
        float transY = (screenHeight - logoHeight) * 0.28f;
        //ivLogo向上移动 transY 的距离
        ObjectAnimator tranLogo = ObjectAnimator.ofFloat(ivLogo, "translationY", 0, -transY);
        //ivLogo在X轴和Y轴上都缩放0.75倍
        ObjectAnimator scaleXLogo = ObjectAnimator.ofFloat(ivLogo, "scaleX", 1f, 0.75f);
        ObjectAnimator scaleYLogo = ObjectAnimator.ofFloat(ivLogo, "scaleY", 1f, 0.75f);

        AnimatorSet logoAnim = new AnimatorSet();
        logoAnim.setDuration(2000);
        logoAnim.play(tranLogo).with(scaleXLogo).with(scaleYLogo);
        logoAnim.start();
        logoAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //待ivLogo的动画结束后,开始播放底部注册、登录按钮的动画
                bottomAnim.start();
            }
        });
    }
/*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(LoginActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                LoginActivity.this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }*/

    public void Tomain(View view){
        initViews();
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        String user1 =  et_name.getText().toString().trim();
        MainActivity.users=user1;
        String pwd1 =  et_pwd.getText().toString().trim();
        if (user1.equals(pwd1)) {
           // startActivity(new Intent(LoginActivity.this, MainActivity.class));
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
        }

    }

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }*/
}
