package com.example.yelp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    String[] fruits = {"Apple", "Banana", "Cherry", "Date", "Grape", "Kiwi", "Mango", "Pear"};
    AutoCompleteTextView actv;
    EditText location_tv;
    Button submit;
    CheckBox auto_check;
    RequestQueue queue;
    MySingleton msl;
    String auto_complete_url = "https://songlin-lyu-571-hw8.wl.r.appspot.com/autocomplete";
    MainActivity ma = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        getWindow().setStatusBarColor(0xea3323);
        setTheme(R.style.Theme_Yelp);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);

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

        queue = Volley.newRequestQueue(this);
        msl = MySingleton.getInstance(this);

        auto_check = (CheckBox) findViewById(R.id.checkbox_auto_detect);
        auto_check.setOnClickListener(new MyAutoCheckListener());

        location_tv = (EditText) findViewById(R.id.location);

        Log.d("[DEBUG]","finish on create");
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
                                Log.d("[DEBUG]", auto_string_list[0]);
                                ArrayAdapter<String> auto_adapter = new ArrayAdapter<String>
                                        (ma, android.R.layout.select_dialog_item, auto_string_list);
                                actv.setAdapter(auto_adapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
            msl.addToRequestQueue(jsonArrayRequest);
        }
    }

    class MyAutoCheckListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Log.d("[DEBUG]", Boolean.toString(auto_check.isChecked()));
        }
    }

    class MySubmitListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Log.d("[DEBUG]", actv.getText().toString());
            String keyword_string = actv.getText().toString();
            String location_string = location_tv.getText().toString();
            if(keyword_string.matches("")){
                actv.setError("This field is required");
            } else{
                if(auto_check.isChecked()){
                    // auto check
                } else{
                    if(location_string.matches("")){
                        location_tv.setError("This field is required");
                    } else{
                        // get location
                    }
                }
            }
//            actv.setError("This field is required");
        }
    }
}