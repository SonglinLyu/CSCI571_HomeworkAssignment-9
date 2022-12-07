package com.example.yelp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {
    String name = "";
    MySingleton msl;
    String businesses_url = "https://songlin-lyu-571-hw8.wl.r.appspot.com/businesses";
    String id = "";
    JSONObject detail;
    JSONArray reviews;
    DemoCollectionAdapter demoCollectionAdapter;
    ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Yelp);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        // tool bar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageView twitter_bt = (ImageView) findViewById(R.id.twitter_button);
        twitter_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent open_url = null;
                    open_url = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/intent/tweet?text=" + detail.getString("url")));
                    startActivity(open_url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        ImageView facebook_bt = (ImageView) findViewById(R.id.facebook_button);
        facebook_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent open_url = null;
                    open_url = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/sharer/sharer.php?u=" + detail.getString("url")));
                    startActivity(open_url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Intent myIntent = getIntent();
        try {
            detail = new JSONObject(myIntent.getStringExtra("detail"));
            reviews = new JSONArray(myIntent.getStringExtra("reviews"));
            myToolbar.setTitle(detail.get("name").toString());
        } catch (JSONException e) {
            Log.d("[DEBUG]", e.toString());
        }

        try {
            demoCollectionAdapter = new DemoCollectionAdapter(this, myIntent.getStringExtra("detail"), myIntent.getStringExtra("reviews"));
            viewPager = findViewById(R.id.pager);
            viewPager.setAdapter(demoCollectionAdapter);

            TabLayout tabLayout = findViewById(R.id.tab_layout);
            new TabLayoutMediator(tabLayout, viewPager,
                    (tab, position) -> {
                        if (position == 0){
                            tab.setText("Business Details");
                        } else if(position == 1){
                            tab.setText("Map Location");
                        } else if(position == 2){
                            tab.setText("Reviews");
                        }
                    }
            ).attach();
        } catch (JSONException e) {
            Log.d("[DEBUG]", e.toString());
        }

    }
}