package com.aiqiyi.ediswit.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.aiqiyi.ediswit.R;
import com.aiqiyi.ediswit.SkinManager;

/**
 * Created by tyr on 2017/6/16.
 */
public class SkinActivity extends AppCompatActivity {

    SkinManager skinManager;
    private RadioGroup radioGroup;
    private ImageButton skin_back;
    private Toolbar skin_top;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.skinlayout);
        skin_top = (Toolbar)findViewById(R.id.skin_topbar);
        skinManager = new SkinManager(this);
        radioGroup = (RadioGroup)findViewById(R.id.radiogroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.back1:
                        skinManager.setSkinType(0);
                        break;
                    case R.id.back2:
                        skinManager.setSkinType(1);
                        break;
                }
                Intent intent = new Intent("skin");
                sendBroadcast(intent);
                skinManager.changeSkin(skinManager.getSkinType());
                skin_top.setBackgroundResource(skinManager.getCurrentSkinRes());
            }
        });


        skin_back = (ImageButton)findViewById(R.id.skin_top_img_btn);
        skin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = "";
                Intent data = new Intent();
                data.putExtra("id", id);
                data.putExtra("title", "");
                setResult(0, data);
                finish();
            }
        });

        skinManager.initSkins();
        skin_top.setBackgroundResource(skinManager.getCurrentSkinRes());
    }

}
