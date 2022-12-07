package com.example.yelp;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private JSONArray localReviewSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View this_view;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            this_view = view;
//            textView = (TextView) view.findViewById(R.id.textView);
        }

        public void setName(String name){
            ((TextView) this_view.findViewById(R.id.name_tv)).setText(name);
        }

        public void setRating(String rating){
            ((TextView) this_view.findViewById(R.id.rating_tv)).setText("Rating:" + rating + "/5");
        }

        public void setText(String text){
            ((TextView) this_view.findViewById(R.id.text_tv)).setText(text);
        }

        public void setTime(String time){
            ((TextView) this_view.findViewById(R.id.time_tv)).setText(time);
        }
    }


    public CustomAdapter(JSONArray reviewSet) {
        localReviewSet = reviewSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.review_row, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
//        viewHolder.getTextView().setText(localDataSet[position]);
        try {
            viewHolder.setName(localReviewSet.getJSONObject(position).getString("name"));
            viewHolder.setRating(localReviewSet.getJSONObject(position).getString("rating"));
            viewHolder.setText(localReviewSet.getJSONObject(position).getString("text"));
            viewHolder.setTime(localReviewSet.getJSONObject(position).getString("time"));

            if(position == 2){
                LinearLayout review_ll = (LinearLayout) viewHolder.this_view.findViewById(R.id.review_ll);
                review_ll.setPadding(0,0,0,0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return 3;
    }
}
