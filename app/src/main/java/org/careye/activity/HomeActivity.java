/*
 * Car eye 车辆管理平台: www.car-eye.cn
 * Car eye 开源网址: https://github.com/Car-eye-team
 * Copyright 2018
 */
package org.careye.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.careye.rtmp.careyeplayer.R;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "HomeActivity";

    private Button mBtnDemo;
    private Button mBtnMore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
        initListener();

    }

    private void initListener() {
        mBtnDemo.setOnClickListener(this);
        mBtnMore.setOnClickListener(this);
    }

    private void initView() {
        mBtnDemo = findViewById(R.id.btn_demo);
        mBtnMore = findViewById(R.id.btn_more);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        Intent intent = null;
        if (id == R.id.btn_demo) {
            intent = new Intent(this, DemoActivity.class);
        } else if (id == R.id.btn_more) {
            intent = new Intent(this, MoreLiveActivity.class);
        }
        startActivity(intent);
    }
}

