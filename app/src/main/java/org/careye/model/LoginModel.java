package org.careye.model;

import org.careye.Callback.ILoginCallback;
import org.careye.Callback.NetCallback;
import org.careye.net.CarUrl;
import org.careye.net.OkHttpTool;

import java.io.IOException;

public class LoginModel {

    public static void login(final String param, final ILoginCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpTool.post(CarUrl.URL_BASE + CarUrl.URL_LOGIN, param, new NetCallback() {
                        @Override
                        public void onResult(int result, String resultDate) {
                            if (callback != null) {
                                callback.onResult(result, resultDate);
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
