package com.aiqiyi.ediswit;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.SharedPreferences;

import java.security.PublicKey;

/**
 * Created by tyr on 2017/6/16.
 */
public class SkinManager {

    public final static String SKIN_PREF = "skinSetting";
    public SharedPreferences skinSettingPreference;

    private int[] skinResources = { R.drawable.totalback,R.drawable.totalback2
    };

    private Activity mActivity;

    public SkinManager(Activity mActivity) {
        this.mActivity = mActivity;
        skinSettingPreference = mActivity.getSharedPreferences(SKIN_PREF,3);
    }

    public int getSkinType(){
        String key = "skin_type";
        return skinSettingPreference.getInt(key,0);
    }

    public void setSkinType(int j){
        SharedPreferences.Editor editor = skinSettingPreference.edit();
        String key = "skin_type";
        editor.putInt(key,j);
        editor.commit();
    }

    public int getCurrentSkinRes(){
        int skinLen = skinResources.length;
        int getSkinLen = getSkinType();
        if (getSkinLen>=skinLen){
            getSkinLen = 0;
        }
        return skinResources[getSkinLen];
    }

    public void toggleSkins(){
        int skinType = getSkinType();
        if (skinType==skinResources.length-1){
            skinType = 0;
        }else {
            skinType++;
        }
        setSkinType(skinType);
        mActivity.getWindow().setBackgroundDrawable(null);
        try {
            mActivity.getWindow().setBackgroundDrawableResource(getCurrentSkinRes());
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    public void initSkins(){
        mActivity.getWindow().setBackgroundDrawableResource(getCurrentSkinRes());
    }
    public void changeSkin(int id){
        setSkinType(id);
        try {
            mActivity.getWindow().setBackgroundDrawableResource(getCurrentSkinRes());
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

}
