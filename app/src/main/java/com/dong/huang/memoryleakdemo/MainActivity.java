package com.dong.huang.memoryleakdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.squareup.leakcanary.LeakCanary;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LeakCanary.install(getApplication());
    }

    public void btnClick(View view){
        Intent intent = new Intent(this, HandlerActivity.class);
        startActivity(intent);
    }


}
