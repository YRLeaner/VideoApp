package com.aiqiyi.ediswit.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.aiqiyi.ediswit.MyApplication;
import com.aiqiyi.ediswit.R;
import com.aiqiyi.ediswit.SkinManager;
import com.aiqiyi.ediswit.TestRecyclerViewAdapter2;
import com.aiqiyi.ediswit.entity.Movie;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by tyr on 2017/5/28.
 */
public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SearchView sb;
    private String seach_txt;
    private List<Movie> searches;
    private ImageButton search_back;
    private TestRecyclerViewAdapter2 testRecyclerViewAdapter2;
    private SkinManager skinManager;
    private Toolbar search_top;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_main);
        sb = (SearchView)findViewById(R.id.search);
        recyclerView = (RecyclerView)findViewById(R.id.search_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        sb.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                volley_Get(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        search_back = (ImageButton)findViewById(R.id.search_top_img_btn);
        search_back.setOnClickListener(new View.OnClickListener() {
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

        search_top = (Toolbar)findViewById(R.id.search_topbar);
        skinManager = new SkinManager(this);
        skinManager.initSkins();
        search_top.setBackgroundResource(skinManager.getCurrentSkinRes());

    }






    private void volley_Get(String sb){
        String url = " http://iface.qiyi.com/openapi/batch/search?app_k=f0f6c3ee5709615310c0f053dc9c65f2&app_v=8.4&app_t=0&platform_id=10&dev_os=6.0&dev_ua=MI%205&dev_hw=%257B%252%202cpu%2522%253A0%252C%2522gpu%2522%253A%2522%2522%252C%2522mem%2522%253%20A%252250.4MB%2522%257D&net_sts=1&scrn_sts=1&scrn_res=1334*750&scrn_dpi=153600&qyid=87390BD2-DACE-497B-9CD4-2FD14354B2A4&secure_v=1&secure_p=GPhone&core=1&req_sn=1493946331320&req_times=1&from=mobile_list&key="+sb+"&version=7.5";
        JsonObjectRequest request= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                FastJson(jsonObject.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(SearchActivity.this,volleyError.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag("abc2Get");
        MyApplication.getHttpQueues().add(request);

    }

    private void FastJson(String json){
        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(json);
        JSONArray result = jsonObject.getJSONArray("data");
        searches = JSON.parseArray(result.toString(),Movie.class);
        Log.d("TXT", searches.size() + "" + "2");
        testRecyclerViewAdapter2 = new TestRecyclerViewAdapter2(searches, this);
        testRecyclerViewAdapter2.setmOnItemClickListener(new TestRecyclerViewAdapter2.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String id = searches.get(position).getId();
                Intent data = new Intent();
                data.putExtra("id", id);
                data.putExtra("title", searches.get(position).getTitle());
                Log.d("TXT", id);
                setResult(0, data);
                finish();
            }
        });
        recyclerView.setAdapter(testRecyclerViewAdapter2);
    }





    @Override
    public void onBackPressed() {
        String id = "";
        Intent data = new Intent();
        data.putExtra("id",id);
        data.putExtra("title","");
        setResult(0, data);
        super.onBackPressed();
    }


}
