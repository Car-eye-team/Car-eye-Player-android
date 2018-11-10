package org.careye.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Space;
import android.widget.Toast;

import com.careye.rtmp.careyeplayer.R;

import org.careye.presenter.LoginPresenter;
import org.careye.util.SPUtil;
import org.careye.view.ILoginView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ILoginView {

    private EditText mEtLoginName;
    private EditText mEtLoginPassword;
    private Button mBtnOk;

    private LoginPresenter mLoginPresenter;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");

        initView();
        initListener();

        mLoginPresenter = new LoginPresenter(this);
    }

    private void initListener() {

        mBtnOk.setOnClickListener(this);
    }

    private void initView() {

        mEtLoginName = findViewById(R.id.et_login_name);
        mEtLoginPassword = findViewById(R.id.et_login_password);
        mBtnOk = findViewById(R.id.btn_ok);

        mEtLoginName.setText("admin");
        mEtLoginPassword.setText("123456");

//        username = SPUtil.getString(this, "username");
//        password = SPUtil.getString(this, "password");


    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.btn_ok) {
            username = mEtLoginName.getText().toString().trim();
            password = mEtLoginPassword.getText().toString().trim();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            mLoginPresenter.login(username, password);
        }
    }

    @Override
    public void onNetResult(final int resultCode, final String resultDate) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (resultCode == 0) {
                    Toast.makeText(LoginActivity.this, resultCode + " : " + resultDate, Toast.LENGTH_SHORT).show();

                    SPUtil.putString(LoginActivity.this, SPUtil.SP_USERNAME, username);
                    SPUtil.putString(LoginActivity.this, SPUtil.SP_PASSWORD, password);

                    Intent intent = new Intent(LoginActivity.this, DemoActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, resultCode + " : " + resultDate, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
