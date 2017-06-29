package com.aiqiyi.ediswit.activity;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiqiyi.ediswit.LLVideoView;
import com.aiqiyi.ediswit.LikeTypeActivity;
import com.aiqiyi.ediswit.MyApplication;
import com.aiqiyi.ediswit.R;
import com.aiqiyi.ediswit.SkinManager;
import com.aiqiyi.ediswit.db.LikeDatabaseHelper;
import com.aiqiyi.ediswit.entity.Movie;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class PlayActivity extends AppCompatActivity {

    private Button search_btn;
    private Button setting_btn;
    private Button like_btn;
    private Button mylike_btn;
    private Button change_btn;
    private CheckBox love_check;
    private LLVideoView mLLVideoView;
    private Toolbar mToolbar;
    private LinearLayout llPanel;
    private TextView tv;

    private LikeDatabaseHelper dbHelper;
    int TED,Activity,Food,MV,Gui,Tiny;
    int[] like = new int[6];
    String[] string = {"TED","运动","美食","MV","鬼畜","微电影"};
    private String sb="";
    private List<Movie> movies;
    private int num = 0;

    SharedPreferences sharedPreferences;
    private long exitTime = 0;
    private static int currentPos = 0;

    private SkinManager skinManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_main);
        tv = (TextView)findViewById(R.id.play_tv);
        sharedPreferences = getSharedPreferences("like",MODE_PRIVATE);

        initView();
        initButton();
        initDate();
        skinManager = new SkinManager(this);
        skinManager.initSkins();
        mToolbar.setBackgroundResource(skinManager.getCurrentSkinRes());

        movies = new ArrayList<>();
        for (int i=0;i<6;i++){
            if(like[i]==1){
                sb+=(string[i]);
            }
        }
        Toast.makeText(this,sb,Toast.LENGTH_SHORT).show();
       synchronized (movies){
           volley_Get();
       }
        dbHelper = new LikeDatabaseHelper(this,"LikeStore.db",null,1);
        //Toast.makeText(this,TED+" "+Activity+" "+Food+" "+MV+" "+Gui+" "+Tiny,Toast.LENGTH_SHORT).show();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.topbar);
        llPanel = (LinearLayout) findViewById(R.id.panel);
        mLLVideoView = (LLVideoView) findViewById(R.id.video);
        mLLVideoView.setOnFullScreenListener(new LLVideoView.OnFullScreenListener() {
            @Override
            public void onFullScreen(int currentPosition, String id) {
                Log.d("TXT", "listener");
                PlayActivity.currentPos = currentPosition;
                int i = getResources().getConfiguration().orientation;
                if (i == Configuration.ORIENTATION_PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    mToolbar.setVisibility(View.GONE);
                    llPanel.setVisibility(View.GONE);
                    tv.setVisibility(View.GONE);

                } else if (i == Configuration.ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    mToolbar.setVisibility(View.VISIBLE);
                    llPanel.setVisibility(View.VISIBLE);
                    tv.setVisibility(View.VISIBLE);
                }
            }
        });
        regist();

    }

    private BroadcastReceiver mBroadCastRec = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("skin")){
                Log.d("TXT","receive");
                skinManager.changeSkin(skinManager.getSkinType());
                mToolbar.setBackgroundResource(skinManager.getCurrentSkinRes());
            }
        }
    };

    public void regist(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("skin");
        registerReceiver(mBroadCastRec,intentFilter);
    }




    private void initDate() {
        like[0] = sharedPreferences.getInt("TED", 0);
        like[1] = sharedPreferences.getInt("Activity", 0);
        like[2] = sharedPreferences.getInt("Food",0);
        like[3] = sharedPreferences.getInt("MV",0);
        like[4] = sharedPreferences.getInt("Gui",0);
        like[5] = sharedPreferences.getInt("Tiny",0);

    }

    private void initButton() {
        search_btn = (Button)this.findViewById(R.id.play_search);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayActivity.this,SearchActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        setting_btn = (Button)this.findViewById(R.id.play_setting);
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayActivity.this,SettingActivity.class);
                startActivity(intent);
            }
        });
        like_btn = (Button)findViewById(R.id.play_liketype);
        like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayActivity.this,LikeTypeActivity.class);
                startActivityForResult(intent,1);
            }
        });

        mylike_btn = (Button)findViewById(R.id.play_mylike);
        mylike_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayActivity.this,MyLikeActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        change_btn = (Button)findViewById(R.id.play_change);
        change_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TXT", "changerececive");
                if (movies != null && movies.size() != 0) {
                    if (num == 9) num = 0;
                    else num++;
                    Log.d("TXT", movies.get(num).getId() + "" + "main");
                    mLLVideoView.release();
                    mLLVideoView.setPlayData(movies.get(num).getId());
                    tv.setText(movies.get(num).getTitle());
                    if (query(num)) {
                        love_check.setChecked(true);
                    } else {
                        love_check.setChecked(false);
                    }
                }
            }
        });

        love_check = (CheckBox) findViewById(R.id.play_love);
        love_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (movies != null && movies.size() != 0) {
                    Log.d("TXT", "loverececive");
                    if (isChecked && love_check.isPressed()) {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put("id", movies.get(num).getId());
                        values.put("title", movies.get(num).getTitle());
                        values.put("short_title", movies.get(num).getShort_title());
                        values.put("img", movies.get(num).getImg());
                        db.insert("like", null, values);
                        Log.d("TXT", "insert succeed");
                    } else if (!isChecked && love_check.isPressed()) {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.delete("like", "id=?", new String[]{movies.get(num).getId()});
                        Log.d("TXT", "delete succeed");
                    }
                }
            }
        });



    }

    private void volley_Get(){
        String url = " http://iface.qiyi.com/openapi/batch/search?app_k=f0f6c3ee5709615310c0f053dc9c65f2&app_v=8.4&app_t=0&platform_id=10&dev_os=6.0&dev_ua=MI%205&dev_hw=%257B%252%202cpu%2522%253A0%252C%2522gpu%2522%253A%2522%2522%252C%2522mem%2522%253%20A%252250.4MB%2522%257D&net_sts=1&scrn_sts=1&scrn_res=1334*750&scrn_dpi=153600&qyid=87390BD2-DACE-497B-9CD4-2FD14354B2A4&secure_v=1&secure_p=GPhone&core=1&req_sn=1493946331320&req_times=1&from=mobile_list&key="+sb+"&version=7.5";
        JsonObjectRequest request= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                FastJson(jsonObject.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(PlayActivity.this,volleyError.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag("abcGet");
        MyApplication.getHttpQueues().add(request);

    }

    private void FastJson(String json){
        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(json);
        JSONArray result = jsonObject.getJSONArray("data");

        if (result==null){

        }else {
            movies = JSON.parseArray(result.toString(),Movie.class);
        }

        Log.d("TXT", movies.size() + "" + "2");
       if (movies==null||movies.size()==0){
       }else {
           mLLVideoView.setPlayData(movies.get(0).getId());
           mLLVideoView.start();
           tv.setText(movies.get(0).getTitle());
            if (query(0)){
                love_check.setChecked(true);
            }else {
                love_check.setChecked(false);
            }
       }
    }

    public boolean query(int num){
        SQLiteDatabase db1 = dbHelper.getWritableDatabase();
        String sql = "select*from like where id="+movies.get(num).getId();
        Cursor cursor = db1.rawQuery(sql,null);
        while (cursor.moveToNext()){
            String id = cursor.getString(cursor.getColumnIndex("id"));
            if (id.equals(movies.get(num).getId())){
                Log.d("TXT","query succeed");
                return true;
            }else {
                Log.d("TXT","query faile");
                return false;
            }
        }
        return false;
    }

    public boolean querybyId(String id){
        SQLiteDatabase db1 = dbHelper.getWritableDatabase();
        String sql = "select*from like where id="+id;
        Cursor cursor = db1.rawQuery(sql,null);
        while (cursor.moveToNext()){
            String sid = cursor.getString(cursor.getColumnIndex("id"));
            if (sid.equals(id)){
                Log.d("TXT","query succeed2");
                return true;
            }else {
                Log.d("TXT","query faile2");
                return false;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (resultCode==0){
          String id = data.getStringExtra("id");
          String title = data.getStringExtra("title");
          Log.d("TXT",id+"rececive");
          if (id!=null&&!id.equals("")){
              Log.d("TXT", id + "mainid");
              mLLVideoView.release();
              mLLVideoView.setPlayData(id);
              mLLVideoView.start();
              tv.setText(title);
              if (querybyId(id)) {
                  love_check.setChecked(true);
              }
          }
      }else if(resultCode==1){
          movies.clear();
          sb="";
          initDate();
          for (int i=0;i<6;i++){
              if(like[i]==1){
                  sb+=(string[i]);
              }
          }
          Toast.makeText(this,sb,Toast.LENGTH_SHORT).show();
          volley_Get();
      }
        super.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            int i = getResources().getConfiguration().orientation;
            if (i == Configuration.ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                mToolbar.setVisibility(View.VISIBLE);
                llPanel.setVisibility(View.VISIBLE);
                tv.setVisibility(View.VISIBLE);
            }else {
                exit();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    //横竖屏操作--ll
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mLLVideoView.setFullScreen();
        } else {
            mLLVideoView.setNormalScreen();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int i = getResources().getConfiguration().orientation;
        if (i == Configuration.ORIENTATION_PORTRAIT) {
            mToolbar.setVisibility(View.VISIBLE);
            llPanel.setVisibility(View.VISIBLE);
            tv.setVisibility(View.VISIBLE);
        } else if (i == Configuration.ORIENTATION_LANDSCAPE) {
            mToolbar.setVisibility(View.GONE);
            llPanel.setVisibility(View.GONE);
            tv.setVisibility(View.GONE);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mLLVideoView.seekTo(PlayActivity.currentPos);
            }
        }, 2000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (null != mLLVideoView) {
            mLLVideoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLLVideoView.destroy();
        unregisterReceiver(mBroadCastRec);
    }

}
