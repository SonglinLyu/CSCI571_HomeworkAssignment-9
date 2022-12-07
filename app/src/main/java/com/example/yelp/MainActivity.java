package com.example.yelp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    String[] fruits = {"Apple", "Banana", "Cherry", "Date", "Grape", "Kiwi", "Mango", "Pear"};
    String[] category_list = {"all", "arts", "health", "hotelstravel", "food", "professional"};
    AutoCompleteTextView actv;
    EditText location_tv;
    EditText keyword_et;
    EditText distance_et;
    Spinner category_sp;
    Button submit;
    Button clear;
    CheckBox auto_check;
    TextView noresult_tv;
    TableLayout result_tl;
    RequestQueue queue;
    MySingleton msl;
    String auto_complete_url = "https://songlin-lyu-571-hw8.wl.r.appspot.com/autocomplete";
    String business_search_url = "https://songlin-lyu-571-hw8.wl.r.appspot.com/business_search";
    String geocode_url = "https://songlin-lyu-571-hw8.wl.r.appspot.com/geocode";
    String businesses_url = "https://songlin-lyu-571-hw8.wl.r.appspot.com/businesses";
    String reviews_url = "https://songlin-lyu-571-hw8.wl.r.appspot.com/reviews";
    String ip_info_url = "https://ipinfo.io/?token=08e55711db41ec";
    MainActivity ma = this;
    int black = Color.parseColor("#FF000000");
    int gray = Color.parseColor("#F2F2F2");

    public static int dip2pix(Context context, float dpValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        getWindow().setStatusBarColor(0xea3323);
        setTheme(R.style.Theme_Yelp);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        keyword_et = (EditText) findViewById(R.id.keyword);
        distance_et = (EditText) findViewById(R.id.distance);
        category_sp = (Spinner) findViewById(R.id.category_spinner);
        result_tl = (TableLayout) findViewById(R.id.result_table);
        noresult_tv = (TextView) findViewById(R.id.noresult_tv);



        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ImageButton reservation_bt = (ImageButton) findViewById(R.id.reservation_button);
        reservation_bt.setOnClickListener(new MyReservationListener());


        Spinner spinner = (Spinner) findViewById(R.id.category_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //Creating the instance of ArrayAdapter containing list of fruit names
        ArrayAdapter<String> auto_adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, fruits);
        //Getting the instance of AutoCompleteTextView
        actv = (AutoCompleteTextView) findViewById(R.id.keyword);
        actv.setThreshold(1);//will start working from first character
        actv.setAdapter(auto_adapter);//setting the adapter data into the AutoCompleteTextView
        actv.addTextChangedListener(new MyKeywordListener());

        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new MySubmitListener());

        clear = (Button) findViewById(R.id.clear);
        clear.setOnClickListener(new MyClearListener());

        queue = Volley.newRequestQueue(this);
        msl = MySingleton.getInstance(this);

        auto_check = (CheckBox) findViewById(R.id.checkbox_auto_detect);
        auto_check.setOnClickListener(new MyAutoCheckListener());

        location_tv = (EditText) findViewById(R.id.location);

        Log.d("[DEBUG]","finish on create");
    }

    class MyReservationListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Intent intent=new Intent(ma, ReservationActivity.class);
            startActivity(intent);
        }
    }

    class MyKeywordListener implements TextWatcher{
        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            doSomething();
            if(s.toString() == ""){
                return;
            }
            Log.d("[DEBUG]",s.toString());
            String url = auto_complete_url + "?text=" + s.toString();
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                JSONArray auto_list = response.getJSONArray(0);
                                int len = auto_list.length();
                                Log.d("[DEBUG]", Integer.toString(len));
                                String[] auto_string_list = new String[len];
                                for(int i=0; i<len; i++) {
                                    auto_string_list[i]=auto_list.getJSONObject(i).get("title").toString();
                                }
//                                Log.d("[DEBUG]", auto_string_list[0]);
                                ArrayAdapter<String> auto_adapter = new ArrayAdapter<String>
                                        (ma, android.R.layout.select_dialog_item, auto_string_list);
                                actv.setAdapter(auto_adapter);
                            } catch (JSONException e) {
//                                e.printStackTrace();
                                Log.d("[DEBUG]", e.toString());
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("[DEBUG]", error.toString());
                        }
                    });
            msl.addToRequestQueue(jsonArrayRequest);
        }
    }

    class MyAutoCheckListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            EditText location = (EditText) findViewById(R.id.location);
            if(auto_check.isChecked()){
                location.setVisibility(View.INVISIBLE);
            } else{
                location.setVisibility(View.VISIBLE);
            }
        }
    }

    class MySubmitListener implements View.OnClickListener{

        private void clearTable(){
            noresult_tv.setVisibility(View.GONE);
            result_tl.removeAllViews();
        }

        private void addTableRow(JSONObject business) throws JSONException {
            int index = result_tl.getChildCount() + 1;
            String image_url = business.get("image_url").toString();
            String name = business.get("name").toString();
            String rating = business.get("rating").toString();
            String distance = business.get("distance").toString();
            String id = business.get("id").toString();

            TableRow tr = new TableRow(getApplicationContext());
            tr.setBackgroundColor(black);
            tr.setPadding(0,0,0,dip2pix(ma, 3));

            LinearLayout ll = new LinearLayout(getApplicationContext());
            TableRow.LayoutParams ll_param = new TableRow.LayoutParams(0,0);
            ll_param.weight = 1;
            ll_param.width = TableRow.LayoutParams.MATCH_PARENT;
            ll_param.height = dip2pix(ma, 80);

            Log.d("[DEBUG]: height", Integer.toString(ll.getHeight()));

            TextView index_tv = new TextView(getApplicationContext());
            index_tv.setText(Integer.toString(index));
            index_tv.setBackgroundColor(gray);
            index_tv.setGravity(Gravity.CENTER);
            ll.addView(index_tv);
            LinearLayout.LayoutParams index_tv_layout = new LinearLayout.LayoutParams(0,0);
            index_tv_layout.width = 0;
            index_tv_layout.height = LinearLayout.LayoutParams.MATCH_PARENT;
            index_tv_layout.weight = (float) 1.7;
            index_tv.setLayoutParams(index_tv_layout);

            ImageView imgv = new ImageView(getApplicationContext());
            if(!image_url.matches("")){
                Picasso.get()
                        .load(image_url)
                        .into(imgv);
            }
            imgv.setBackgroundColor(gray);
            imgv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            int pd = dip2pix(ma,10);
            imgv.setPadding(pd,pd,pd,pd);
            ll.addView(imgv);
            LinearLayout.LayoutParams imgv_layout = new LinearLayout.LayoutParams(0,0);
            imgv_layout.width = 0;
            imgv_layout.height = LinearLayout.LayoutParams.MATCH_PARENT;
            imgv_layout.weight = (float) 1.5;
            imgv.setLayoutParams(imgv_layout);

            TextView name_tv = new TextView(getApplicationContext());
            name_tv.setMaxLines(3);
            name_tv.setText(name);
            name_tv.setBackgroundColor(gray);
            name_tv.setGravity(Gravity.CENTER);
            ll.addView(name_tv);
            LinearLayout.LayoutParams name_tv_layout = new LinearLayout.LayoutParams(0,0);
            name_tv_layout.width = 0;
            name_tv_layout.height = LinearLayout.LayoutParams.MATCH_PARENT;
            name_tv_layout.weight = (float) 1.5;
            name_tv.setLayoutParams(name_tv_layout);

            TextView rating_tv = new TextView(getApplicationContext());
            rating_tv.setText(rating);
            rating_tv.setBackgroundColor(gray);
            rating_tv.setGravity(Gravity.CENTER);
            ll.addView(rating_tv);
            LinearLayout.LayoutParams rating_tv_layout = new LinearLayout.LayoutParams(0,0);
            rating_tv_layout.width = 0;
            rating_tv_layout.height = LinearLayout.LayoutParams.MATCH_PARENT;
            rating_tv_layout.weight = (float) 1.5;
            rating_tv.setLayoutParams(rating_tv_layout);

            TextView distance_tv = new TextView(getApplicationContext());
            distance_tv.setText(distance);
            distance_tv.setBackgroundColor(gray);
            distance_tv.setGravity(Gravity.CENTER);
            ll.addView(distance_tv);
            LinearLayout.LayoutParams distance_tv_layout = new LinearLayout.LayoutParams(0,0);
            distance_tv_layout.width = 0;
            distance_tv_layout.height = LinearLayout.LayoutParams.MATCH_PARENT;
            distance_tv_layout.weight = (float) 1.7;
            distance_tv.setLayoutParams(distance_tv_layout);


            tr.addView(ll);
            ll.setLayoutParams(ll_param);

            tr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("[DEBUG]: id", id);
                    String url = businesses_url + "?id=" + id;
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    String detail_string = response.toString();
                                    String temp_url = reviews_url + "?id=" + id;
                                    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                                            (Request.Method.GET, temp_url, null, new Response.Listener<JSONArray>() {
                                                @Override
                                                public void onResponse(JSONArray response) {
                                                    Intent intent=new Intent(ma, DetailActivity.class);
                                                    intent.putExtra("detail", detail_string);
                                                    intent.putExtra("reviews", response.toString());
                                                    startActivity(intent);
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Log.d("[DEBUG]", error.toString());
                                                }
                                            });
                                    msl.addToRequestQueue(jsonArrayRequest);
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("[DEBUG]", error.toString());
                                }
                            });
                    msl.addToRequestQueue(jsonObjectRequest);
                }
            });

            result_tl.addView(tr);
//            tr.setLayoutParams(tr_param);
        }

        private String generateParam(String latitude, String longitude){
            String term = keyword_et.getText().toString();
            String radius = "10";
            if(! distance_et.getText().toString().matches("")){
                radius = distance_et.getText().toString();
            }
            String categories = category_list[(int)category_sp.getSelectedItemId()];
            return "?term=" + term + "&categories=" + categories + "&radius=" + radius + "&latitude=" + latitude + "&longitude=" + longitude;
        }

        private void searchByLoc(String loc){
            String address = "";
            String[] splitted_address = loc.split(" ");
            address += splitted_address[0];
            for(int i=1; i<splitted_address.length; i++){
                address += "+" + splitted_address[i];
            }

            String geo_url = geocode_url + "?address=" + address;
            clearTable();

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                    (Request.Method.GET, geo_url, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                String url = business_search_url + generateParam(response.getString(0), response.getString(1));
                                clearTable();
                                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                                        (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                                            @Override
                                            public void onResponse(JSONArray response) {
                                                try {
                                                    int len = response.length();
                                                    for(int i=0; i<len; i++){
                                                        addTableRow(response.getJSONObject(i));
                                                    }
                                                    Log.d("[DEBUG]", response.toString());
                                                } catch (JSONException e) {
//                                e.printStackTrace();
                                                    Log.d("[DEBUG]", e.toString());
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Log.d("[DEBUG]", error.toString());
                                                noresult_tv.setVisibility(View.VISIBLE);
                                            }
                                        });
                                msl.addToRequestQueue(jsonArrayRequest);
                            } catch (JSONException e){
                                Log.d("[DEBUG]", e.toString());
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            noresult_tv.setVisibility(View.VISIBLE);
                        }
                    });
            msl.addToRequestQueue(jsonArrayRequest);
        }

        private void searchByIP(){
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, ip_info_url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String loc = response.get("loc").toString();
                                String[] locs = loc.split(",");
//                                Log.d("[DEBUG]", locs[1]);
//                                Log.d("[DEBUG]", categories);
                                String url = business_search_url + generateParam(locs[0], locs[1]);
                                clearTable();
                                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                                        (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                                            @Override
                                            public void onResponse(JSONArray response) {
                                                try {
                                                    int len = response.length();
                                                    for(int i=0; i<len; i++){
                                                        addTableRow(response.getJSONObject(i));
                                                    }
                                                    Log.d("[DEBUG]", response.toString());
                                                } catch (JSONException e) {
//                                e.printStackTrace();
                                                    Log.d("[DEBUG]", e.toString());
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                noresult_tv.setVisibility(View.VISIBLE);
                                                Log.d("[DEBUG]", error.toString());
                                            }
                                        });
                                msl.addToRequestQueue(jsonArrayRequest);
                            } catch (JSONException e){
                                Log.d("[DEBUG]", e.toString());
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            noresult_tv.setVisibility(View.VISIBLE);
                        }
                    });
            msl.addToRequestQueue(jsonObjectRequest);
        }

        @Override
        public void onClick(View view) {
            Log.d("[DEBUG]", actv.getText().toString());
            String keyword_string = actv.getText().toString();
            String distance_string = distance_et.getText().toString();
            String location_string = location_tv.getText().toString();
            if(keyword_string.matches("")){
                actv.setError("This field is required");
            } else{
                if(distance_string.matches("")){
                    distance_et.setError("This field is required");
                } else{
                    if(auto_check.isChecked()){
                        // auto check
                        searchByIP();
                    } else{
                        if(location_string.matches("")){
                            location_tv.setError("This field is required");
                        } else{
                            // get location
                            searchByLoc(location_tv.getText().toString());
                        }
                    }
                }
            }
//            actv.setError("This field is required");
        }
    }

    class MyClearListener implements View.OnClickListener{

        private void clearTable(){
            noresult_tv.setVisibility(View.GONE);
            result_tl.removeAllViews();
        }

        @Override
        public void onClick(View view) {
            actv.setText("");
            actv.setError(null);
            distance_et.setText("");
            distance_et.setError(null);
            Spinner spinner = (Spinner) findViewById(R.id.category_spinner);
            spinner.setSelection(0);
            location_tv.setText("");
            location_tv.setError(null);
            location_tv.setVisibility(View.VISIBLE);
            auto_check.setChecked(false);
            clearTable();
        }
    }
}