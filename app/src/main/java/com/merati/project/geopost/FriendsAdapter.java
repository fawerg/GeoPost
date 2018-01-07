package com.merati.project.geopost;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.google.android.gms.maps.model.LatLng;
import java.util.List;

/**
 * Created by fawerg on 1/3/18.
 */

public class FriendsAdapter extends ArrayAdapter<Friend> {

    public FriendsAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }
    public FriendsAdapter(Context context, int resource, List<Friend> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.friend_element, null);
        }
        Friend p = getItem(position);
        if (p != null) {
            TextView tt1 = v.findViewById(R.id.name);
            TextView tt2 = v.findViewById(R.id.status);
            TextView tt3 = v.findViewById(R.id.distance);
            tt1.setText("Username: "+p.getName());
            tt2.setText("Status: "+p.getLast_status());

            tt3.setText("Distance: "+p.getDistance()+" Km");
        }
        return v;
    }
}
