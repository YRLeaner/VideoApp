package com.aiqiyi.ediswit.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.aiqiyi.ediswit.R;
import com.aiqiyi.ediswit.SkinManager;


public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private Button button;


    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private CheckBox checkBox4;
    private CheckBox checkBox5;
    private CheckBox checkBox6;

    private SkinManager skinManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("submit", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (sharedPreferences.getInt("submit",0)==0){
            editor.putInt("submit",1);
            editor.commit();
        }else {
            Intent intent = new Intent(MainActivity.this, PlayActivity.class);
            startActivity(intent);
            finish();
        }

        sharedPreferences = getSharedPreferences("like", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        initCheckBox();
      

        button = (Button)findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                startActivity(intent);
                finish();
            }
        });

        skinManager = new SkinManager(this);
        skinManager.initSkins();

    }

    private void initCheckBox() {
        checkBox1 = (CheckBox)findViewById(R.id.checkbox1);
        checkBox2 = (CheckBox)findViewById(R.id.checkbox2);
        checkBox3 = (CheckBox)findViewById(R.id.checkbox3);
        checkBox4 = (CheckBox)findViewById(R.id.checkbox4);
        checkBox5 = (CheckBox)findViewById(R.id.checkbox5);
        checkBox6 = (CheckBox)findViewById(R.id.checkbox6);

        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putInt("TED", 1);
                } else {
                    editor.putInt("TED", 0);
                }
                editor.commit();
            }
        });
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    editor.putInt("Activity",1);
                }else {
                    editor.putInt("Activity",0);
                }
                editor.commit();
            }
        });
        checkBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    editor.putInt("Food",1);
                }else {
                    editor.putInt("Food",0);
                }
                editor.commit();
            }
        });
        checkBox4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putInt("MV", 1);
                } else {
                    editor.putInt("MV", 0);
                }
                editor.commit();
            }
        });
        checkBox5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    editor.putInt("Gui",1);
                }else {
                    editor.putInt("Gui",0);
                }
                editor.commit();
            }
        });
        checkBox6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putInt("Tiny", 1);
                } else {
                    editor.putInt("Tiny", 0);
                }
                editor.commit();
            }
        });
    }



}
