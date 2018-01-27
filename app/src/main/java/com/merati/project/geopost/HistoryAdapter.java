package com.merati.project.geopost;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by fawerg on 1/26/18.
 */

public class HistoryAdapter extends ArrayAdapter<HistElement> {
    public HistoryAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }
    public HistoryAdapter(Context context, int resource, List<HistElement> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.adapter_element, null);
        }
        HistElement p = getItem(position);
        if (p != null) {
            TextView tt1 = v.findViewById(R.id.status);
            TextView tt2 = v.findViewById(R.id.tmst);

            tt1.setText(p.getStatus());
            tt2.setText(p.getTmst());


        }
        return v;
    }
}
