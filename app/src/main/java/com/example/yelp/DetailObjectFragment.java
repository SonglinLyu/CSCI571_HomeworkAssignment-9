package com.example.yelp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.util.Linkify;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.sql.Time;
import java.time.Clock;
import java.util.regex.Pattern;

public class DetailObjectFragment extends Fragment {

    JSONObject detail;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.detail_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        pref = getActivity().getSharedPreferences("Yelp", 0); // 0 - for private mode
        editor = pref.edit();

        Bundle args = getArguments();
        try {
            detail = new JSONObject(args.getString("detail_string"));
            ((TextView) view.findViewById(R.id.address_tv))
                    .setText(detail.getString("location"));
            ((TextView) view.findViewById(R.id.price_tv))
                    .setText(detail.getString("price"));
            ((TextView) view.findViewById(R.id.phone_tv))
                    .setText(detail.getString("phone"));

            if(detail.getString("is_open_now").matches("true")){
                ((TextView) view.findViewById(R.id.status_tv))
                        .setText("Open Now");
                ((TextView) view.findViewById(R.id.status_tv))
                        .setTextColor(Color.parseColor("#00FF00"));
            } else{
                ((TextView) view.findViewById(R.id.status_tv))
                        .setText("Closed");
                ((TextView) view.findViewById(R.id.status_tv))
                        .setTextColor(Color.parseColor("#FF0000"));
            }

            ((TextView) view.findViewById(R.id.category_tv))
                    .setText(detail.getString("title"));

            if(!detail.getString("url").matches("")){
                TextView link_tv = (TextView) view.findViewById(R.id.link_tv);
                link_tv.setText(R.string.business_link);
                link_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent open_url = new Intent(Intent.ACTION_VIEW, Uri.parse(detail.getString("url")));
                            startActivity(open_url);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            ImageView img1 = view.findViewById(R.id.img1);
            String img_url_1 = detail.getJSONArray("photos").getString(0);
            if(!img_url_1.matches("")){
                Picasso.get()
                        .load(img_url_1)
                        .into(img1);
            }

            ImageView img2 = view.findViewById(R.id.img2);
            String img_url_2 = detail.getJSONArray("photos").getString(1);
            if(!img_url_2.matches("")){
                Picasso.get()
                        .load(img_url_2)
                        .into(img2);
            }

            ImageView img3 = view.findViewById(R.id.img3);
            String img_url_3 = detail.getJSONArray("photos").getString(2);
            if(!img_url_3.matches("")){
                Picasso.get()
                        .load(img_url_3)
                        .into(img3);
            }

            Button reserve_bt = view.findViewById(R.id.reserve_bt);
            reserve_bt.setOnClickListener(new ReserveListener());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class ReserveListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Log.d("[DEBUG]", "reserve clicked");
            // custom dialog
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.reservation_popup);

            TextView name = (TextView) dialog.findViewById(R.id.name_tv);
            try {
                name.setText(detail.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            TextView email_et = (TextView) dialog.findViewById(R.id.email_et);

            TextView date_et = (TextView) dialog.findViewById(R.id.date_et);
            date_et.setInputType(InputType.TYPE_NULL);
            date_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if(date_et.hasFocus()){
                        final Calendar c = Calendar.getInstance();
                        int mYear = c.get(Calendar.YEAR);
                        int mMonth = c.get(Calendar.MONTH);
                        int mDay = c.get(Calendar.DAY_OF_MONTH);


                        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        date_et.setText(monthOfYear + "-" + dayOfMonth + "-" + year);
                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                        datePickerDialog.show();
                    }
                }
            });

            TextView start_time_et = (TextView) dialog.findViewById(R.id.start_time_et);
            start_time_et.setInputType(InputType.TYPE_NULL);
            start_time_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if(start_time_et.hasFocus()){
                        final Calendar c = Calendar.getInstance();
                        int hour = c.get(Calendar.HOUR_OF_DAY);
                        int minute = c.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                start_time_et.setText(Integer.toString(i) + ":" + Integer.toString(i1));
                            }
                        }, hour, minute, false);
                        timePickerDialog.show();
                    }
                }
            });


            TextView cancel_reserve_bt = (TextView)  dialog.findViewById(R.id.cancel_reserve_bt);
            cancel_reserve_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            TextView submit_reserve_bt = (TextView) dialog.findViewById(R.id.submit_reserve_bt);
            submit_reserve_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    if(isValid()){
                        try {
                            JSONObject reservation_list;
                            if(pref.getString("reservation_list",null) == null){
                                Log.d("[DEBUG] : revervation", "no reservation");
                                reservation_list = new JSONObject();
                            } else{
                                Log.d("[DEBUG] : revervation", pref.getString("reservation_list",null));
                                reservation_list = new JSONObject(pref.getString("reservation_list",null));
                            }
                            JSONObject reservation_info = new JSONObject();
                            reservation_info.put("name", detail.getString("name"));
                            reservation_info.put("date", date_et.getText().toString());
                            reservation_info.put("time", start_time_et.getText().toString());
                            reservation_info.put("email", email_et.getText().toString());

                            reservation_list.put(detail.getString("name"), reservation_info);
                            editor.putString("reservation_list", reservation_list.toString());
                            editor.commit();

                            showToats("Reservation Booked");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                public void showToats(String s){
                    CharSequence text = s;
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(getActivity(), text, duration);
                    toast.show();
                }

                public Boolean isValid(){
                    if(email_et.getText().toString().isEmpty()){
                        showToats("InValid Email Address.");
                        return false;
                    }
                    Pattern pattern = Patterns.EMAIL_ADDRESS;
                    if(!pattern.matcher(email_et.getText().toString()).matches()){
                        showToats("InValid Email Address.");
                        return false;
                    }
                    if(date_et.getText().toString().matches("")){
                        showToats("InValid Date.");
                        return false;
                    }
                    if(start_time_et.getText().toString().matches("")){
                        showToats("InValid Start Time.");
                        return false;
                    }
                    String start_time = start_time_et.getText().toString();
                    int start_hour = Integer.parseInt(start_time.split(":")[0]);
                    if(start_hour < 10 || start_hour >= 17){
                        showToats("Time should be between 10AM AND 5PM");
                        return false;
                    }
                    return true;
                }
            });

            dialog.show();
        }
    }
}