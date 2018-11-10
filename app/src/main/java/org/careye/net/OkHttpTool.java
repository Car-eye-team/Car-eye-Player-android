package org.careye.net;

import android.util.Log;

import org.careye.Callback.NetCallback;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * OKHTTP util
 */
public class OkHttpTool {

    private final static String LOG_TAG = "OkHttpTool";

    public final static int CONNECT_TIMEOUT = 60;
    public final static int READ_TIMEOUT = 100;
    public final static int WRITE_TIMEOUT = 60;
    public static final OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)//设置写的超时时间
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
            .build();

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static int post(String url, String json, NetCallback callback) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            if (callback != null) {
                String str = "";
                if (response.body() != null) {
                    str = response.body().string();
                }
                callback.onResult(response.code(), str);
            }
            return response.code();
        } else {
            Log.e(LOG_TAG, "response : " + response);
            throw new IOException("Unexpected code " + response);
        }
    }

    public static void get(String url, final OnResponseListener listener, String token, final int sequence) {

        Request.Builder builder = new Request.Builder();
        builder.addHeader("authorization", "Token " + token);
//        builder.addHeader("authorization", "Token 4e50b364566fab30d8ee08804e5d30f1c4ac94ed");
        builder.addHeader("Content-Type", "application/json");
        Request request = builder.get().url(url).build();
        //3将Request封装成call
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //失败调用
                Log.e(LOG_TAG, "onFailure: " );
                if (listener != null) {
                    listener.onError(sequence);
                }
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                //成功调用
                Log.e(LOG_TAG, "onResponse: " );
                String string = response.body().string();
                if (listener != null) {
                    listener.onSuccess(string, sequence);
                }
            }
        });
    }

    public interface OnResponseListener {

        void onError(int sequence);
        void onSuccess(String result, int sequence);
    }
}
