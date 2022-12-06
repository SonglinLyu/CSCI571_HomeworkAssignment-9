package com.example.yelp;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailObjectFragment extends Fragment {

    JSONObject detail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.detail_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}