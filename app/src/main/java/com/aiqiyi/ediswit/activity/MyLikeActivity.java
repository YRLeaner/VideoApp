package com.aiqiyi.ediswit.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.aiqiyi.ediswit.R;
import com.aiqiyi.ediswit.SkinManager;
import com.aiqiyi.ediswit.TestRecyclerViewAdapter2;
import com.aiqiyi.ediswit.db.LikeDatabaseHelper;
import com.aiqiyi.ediswit.entity.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tyr on 2017/5/31.
 */
public class MyLikeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TestRecyclerViewAdapter2 testRecyclerViewAdapter2;
    List<Movie> items;
    private ImageButton my_back;
    private SkinManager skinManager;

    private Toolbar my_topbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mylike_main);
        recyclerView = (RecyclerView)findViewById(R.id.mylike_recycler);
        items = new ArrayList<>();
        items = queryLike();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        testRecyclerViewAdapter2 = new TestRecyclerViewAdapter2(items,this);
        testRecyclerViewAdapter2.setmOnItemClickListener(new TestRecyclerViewAdapter2.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String id = items.get(position).getId();
                Intent data = new Intent();
                data.putExtra("id", id);
                data.putExtra("title", items.get(position).getTitle());
                Log.d("TXT", id);
                setResult(0, data);
                finish();
            }
        });
        recyclerView.setAdapter(testRecyclerViewAdapter2);

        my_back = (ImageButton)findViewById(R.id.my_top_img_btn);
        my_back.setOnClickListener(new View.OnClickListener() {
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

        my_topbar = (Toolbar)findViewById(R.id.my_topbar);
        skinManager = new SkinManager(this);
        skinManager.initSkins();
        my_topbar.setBackgroundResource(skinManager.getCurrentSkinRes());

    }

    public List<Movie> queryLike(){
        List<Movie> list = new ArrayList<>();
        LikeDatabaseHelper dbHelper = new LikeDatabaseHelper(this,"LikeStore.db",null,1);
        SQLiteDatabase db1 = dbHelper.getWritableDatabase();
        String sql = "select*from like ";
        Cursor cursor = db1.rawQuery(sql,null);
        while (cursor.moveToNext()){
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String short_title= cursor.getString(cursor.getColumnIndex("short_title"));
            String title= cursor.getString(cursor.getColumnIndex("title"));
            String img =  cursor.getString(cursor.getColumnIndex("img"));

            Movie m = new Movie();
            m.setId(id);
            m.setShort_title(short_title);
            m.setTitle(title);
            m.setImg(img);
            list.add(m);
        }
        return list;

    }



    @Override
    public void onBackPressed() {
        String id = "";
        Intent data = new Intent();
        data.putExtra("id",id);
        data.putExtra("title","");
        setResult(0,data);
        super.onBackPressed();
    }
}
