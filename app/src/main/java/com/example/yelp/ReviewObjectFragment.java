package com.example.yelp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReviewObjectFragment extends Fragment {
    public static final String ARG_OBJECT = "object";

    RecyclerView review_rv;
    JSONArray reviews;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.review_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        try {
            reviews = new JSONArray(args.getString("reviews_string"));
            review_rv = (RecyclerView) view.findViewById(R.id.review_rv);
            review_rv.setAdapter(new CustomAdapter(reviews));
            review_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
//            DividerItemDecoration black_divider = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
//            black_divider.setDrawable(getResources().getDrawable(R.drawable.divide_line));
//            review_rv.addItemDecoration(black_divider);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        review_rv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }
}