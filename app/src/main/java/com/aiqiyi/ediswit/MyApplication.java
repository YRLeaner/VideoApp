package com.aiqiyi.ediswit;

import android.app.Application;

import com.aiqiyi.ediswit.db.LikeDatabaseHelper;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.qiyi.video.playcore.QiyiVideoView;

/**
 * Created by tyr on 2017/6/6.
 */
public class MyApplication extends Application {

    public static RequestQueue requestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        QiyiVideoView.init(this);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

    }

    public static RequestQueue getHttpQueues(){
        return requestQueue;
    }
}
