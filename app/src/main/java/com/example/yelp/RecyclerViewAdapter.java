package com.example.yelp;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private JSONObject reservation_list;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView index;
        private TextView name;
        private TextView date;
        private TextView time;
        private TextView email;
        RelativeLayout relativeLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            index = itemView.findViewById(R.id.index_tv);
            name = itemView.findViewById(R.id.name_tv);
            date = itemView.findViewById(R.id.date_tv);
            time = itemView.findViewById(R.id.time_tv);
            email = itemView.findViewById(R.id.email_tv);
        }
    }

    public RecyclerViewAdapter(JSONObject data) {
        this.reservation_list = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            String key = reservation_list.names().getString(position);
            JSONObject reservation_info = reservation_list.getJSONObject(key);
            holder.index.setText(Integer.toString(position + 1));
            holder.name.setText(reservation_info.getString("name"));
            holder.date.setText(reservation_info.getString("date"));
            holder.time.setText(reservation_info.getString("time"));
            holder.email.setText(reservation_info.getString("email"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return reservation_list.length();
    }


    public void removeItem(int position) throws JSONException {
        String key = reservation_list.names().getString(position);
        reservation_list.remove(key);
//        data.remove(position);
        notifyItemRemoved(position);
    }
}
