package org.careye.presenter;

import android.util.Log;

import com.google.gson.Gson;

import org.careye.Callback.ILoginCallback;
import org.careye.activity.LoginActivity;
import org.careye.model.LoginModel;
import org.careye.util.Encript;
import org.careye.util.MD5Util;
import org.careye.view.ILoginView;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginPresenter {

    private final String TAG = "LoginPresenter";

    private ILoginView mView;

    public LoginPresenter(ILoginView view) {
        this.mView = view;
    }

    public void login(String username, String password){

        try {
            String tradeno = "" + System.currentTimeMillis();
//            String sign = Encript.md5(username + tradeno);
            //8121BA45FC413AFC48A36B4D9942668E
            String sign = MD5Util.MD5Encode(username+ password + tradeno, "UTF-8");

            JSONObject json = new JSONObject();
            json.put("username", username);
            json.put("password", password);
            json.put("tradeno", tradeno);
            json.put("sign", sign);

            LoginModel.login(json.toString(), new ILoginCallback() {
                @Override
                public void onResult(final int resultCode, final String resultInfo) {

                    if (mView == null){
                        Log.d(TAG, "login : onResult : mView is NULL" );
                        return;
                    }

                    //请求结果
                    if (resultCode == 200) {
                        try {
                            JSONObject json = new JSONObject(resultInfo);
                            int rc = json.getInt("errCode");
                            String rd = json.getString("resultMsg");
                            mView.onNetResult(rc, rd);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            mView.onNetResult(resultCode, resultInfo);
                        }
                    } else {
                        mView.onNetResult(resultCode, resultInfo);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            //正常处理异常
        }
    }
}
