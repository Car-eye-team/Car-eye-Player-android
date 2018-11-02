/*
 * Car eye 车辆管理平台: www.car-eye.cn
 * Car eye 开源网址: https://github.com/Car-eye-team
 * Copyright 2018
 */
package org.careye.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;

import com.careye.rtmp.careyeplayer.R;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.careye.fragment.LiveFragment;
import org.careye.fragment.LocalFragment;
import org.careye.fragment.YunFragment;

public class DemoActivity extends AppCompatActivity implements OnTabSelectListener, LiveFragment.OnFragmentInteractionListener {

    private final String TAG = "DemoActivity";

    private FrameLayout mFlView;
    private BottomBar mBomMainBar;

    private Fragment mCurrentFragment;
    private LiveFragment mLiveFragment;
    private LocalFragment mLocalFragment;
    private YunFragment mYunFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        mLiveFragment = LiveFragment.newInstance();
        mLocalFragment = LocalFragment.newInstance();
        mYunFragment = YunFragment.newInstance();
        initView();
        initListener();

    }

    private void initListener() {
        mBomMainBar.setOnTabSelectListener(this);
    }

    private void initView() {
        mBomMainBar = findViewById(R.id.bom_main_bar);
        mBomMainBar.selectTabAtPosition(0);
    }

    private void switchFragment(Fragment targetFragment) {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_view, targetFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onTabSelected(int tabId) {
        if (tabId == R.id.tab_live) {
            switchFragment(mLiveFragment);
        }
//        else if (tabId == R.id.tab_local){
//            switchFragment(mLocalFragment);
//        } else if (tabId == R.id.tab_yun) {
//            switchFragment(mYunFragment);
//        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d(TAG, "onFragmentInteraction");
    }
}

