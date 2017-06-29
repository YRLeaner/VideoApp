package com.aiqiyi.ediswit.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.aiqiyi.ediswit.R;
import com.aiqiyi.ediswit.SkinManager;

/**
 * Created by tyr on 2017/5/29.
 */
public class SettingActivity extends AppCompatActivity {
    private ImageButton set_back;
    private Button change_back_btn;
    private SkinManager skinManager;
    private Toolbar set_top;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_main);
        set_back = (ImageButton)findViewById(R.id.set_top_img_btn);
        set_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        change_back_btn = (Button)findViewById(R.id.change_background);
        change_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,SkinActivity.class);
                startActivity(intent);
            }
        });

        set_top =(Toolbar)findViewById(R.id.set_topbar);
        skinManager = new SkinManager(this);
        skinManager.initSkins();
        set_top.setBackgroundResource(skinManager.getCurrentSkinRes());
        regist();
    }


    private BroadcastReceiver mBroadCastRec = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("skin")){
                Log.d("TXT","receive");
                skinManager.changeSkin(skinManager.getSkinType());
                set_top.setBackgroundResource(skinManager.getCurrentSkinRes());
            }
        }
    };

    public void regist(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("skin");
        registerReceiver(mBroadCastRec, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadCastRec);
    }
}
