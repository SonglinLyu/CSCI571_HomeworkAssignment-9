package com.example.yelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReservationActivity extends AppCompatActivity {

    RecyclerView reservation_rv;
    RecyclerViewAdapter mAdapter;
    ArrayList<String> stringArrayList = new ArrayList<>();
    ConstraintLayout constraintLayout;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Yelp);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        pref = this.getSharedPreferences("Yelp", 0); // 0 - for private mode
        editor = pref.edit();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.reservation_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        reservation_rv = findViewById(R.id.reservation_rv);
//        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        constraintLayout = findViewById(R.id.constrain_layout);

        try {
            populateRecyclerView();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        enableSwipeToDeleteAndUndo();
    }

    private void populateRecyclerView() throws JSONException {
//        stringArrayList.add("1");
//        stringArrayList.add("2");
//        stringArrayList.add("3");
//        stringArrayList.add("4");
//        stringArrayList.add("5");
//        stringArrayList.add("6");
//        stringArrayList.add("7");
//        stringArrayList.add("8");
//        stringArrayList.add("9");
//        stringArrayList.add("10");

        JSONObject reservation_list;
        if(pref.getString("reservation_list",null) == null){
            Log.d("[DEBUG] : revervation", "no reservation");
            reservation_list = new JSONObject();
        } else{
            Log.d("[DEBUG] : revervation", pref.getString("reservation_list",null));
            reservation_list = new JSONObject(pref.getString("reservation_list",null));
        }

        if(reservation_list.length() == 0){
            TextView nobook_tv = (TextView) findViewById(R.id.nobook_tv);
            nobook_tv.setVisibility(View.VISIBLE);
        }

        mAdapter = new RecyclerViewAdapter(reservation_list);
        reservation_rv.setAdapter(mAdapter);

    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
//                final String item = mAdapter.getData().get(position);

                try {
                    JSONObject reservation_list;
                    reservation_list = new JSONObject(pref.getString("reservation_list",null));
                    String key = reservation_list.names().getString(position);
                    reservation_list.remove(key);
                    editor.putString("reservation_list", reservation_list.toString());
                    editor.commit();

                    mAdapter.removeItem(position);

                    Snackbar snackbar = Snackbar
                            .make(constraintLayout, "Removing Existing Reservation", Snackbar.LENGTH_LONG);

                    snackbar.show();

                    if(reservation_list.length() == 0){
                        TextView nobook_tv = (TextView) findViewById(R.id.nobook_tv);
                        nobook_tv.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(reservation_rv);
    }

}