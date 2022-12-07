package com.example.yelp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class MapObjectFragment extends Fragment {
    public static final String ARG_OBJECT = "object";

    JSONObject detail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        Bundle args = getArguments();
//        ((TextView) view.findViewById(android.R.id.text1))
//                .setText("map");
        Bundle args = getArguments();
        try {
            detail = new JSONObject(args.getString("detail_string"));
            double lat = Double.parseDouble(detail.getJSONObject("coordinates").getString("latitude"));
            double lng = Double.parseDouble(detail.getJSONObject("coordinates").getString("longitude"));

            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(@NonNull GoogleMap googleMap) {
                    LatLng loc = new LatLng(lat, lng);
                    googleMap.addMarker(new MarkerOptions()
                            .position(loc)
                            .title("Marker"));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 12f));
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}