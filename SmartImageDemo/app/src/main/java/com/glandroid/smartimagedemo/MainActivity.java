package com.glandroid.smartimagedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.glandroid.smartimagedemo.loopj.android.image.SmartImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SmartImageView siv = (SmartImageView) findViewById(R.id.siv);
        siv.setImageUrl("http://192.168.1.130:8080/WebServer/img/a.jpg", R.drawable.ic_launcher);
    }
}
