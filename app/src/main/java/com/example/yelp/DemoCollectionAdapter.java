package com.example.yelp;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DemoCollectionAdapter extends FragmentStateAdapter {

    String detail_string;
    String reviews_string;
    JSONObject detail;
    JSONArray reviews;

    public DemoCollectionAdapter(DetailActivity fragment, String d, String r) throws JSONException {
        super(fragment);
        detail_string = d;
        detail =  new JSONObject(d);
        reviews_string = r;
        reviews = new JSONArray(r);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return a NEW fragment instance in createFragment(int)
//        Fragment fragment = new DemoObjectFragment();
//        Bundle args = new Bundle();
//        // Our object is just an integer :-P
//        args.putInt(DemoObjectFragment.ARG_OBJECT, position + 1);
//        fragment.setArguments(args);
        Fragment fragment = new Fragment();
        if(position == 0){
            fragment = new DetailObjectFragment();
            Bundle args = new Bundle();
            args.putString("detail_string", detail_string);
            fragment.setArguments(args);
        } else if(position == 1){
            fragment = new MapObjectFragment();
            Bundle args = new Bundle();
            args.putString("detail_string", detail_string);
            fragment.setArguments(args);
        } else if(position == 2){
            fragment = new ReviewObjectFragment();
            Bundle args = new Bundle();
            args.putString("reviews_string", reviews_string);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}