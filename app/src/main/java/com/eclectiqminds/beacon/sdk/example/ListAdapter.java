package com.eclectiqminds.beacon.sdk.example;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eclectiqminds.beacon.sdk.remote.beacon.Beacon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zsolt Szabo Negyedi on 1/6/2016.
 */
public class ListAdapter extends BaseAdapter {

    private List<Beacon> beacons = null;
    private Context context;

    public ListAdapter(Context context) {
        this.context = context;
        beacons = new ArrayList<>();
    }

    public void add(Beacon beacon) {
        beacons.add(beacon);
        notifyDataSetChanged();
    }

    public void reset(){
        beacons.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return beacons.size();
    }

    @Override
    public Object getItem(int position) {
        return beacons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        View vi = view;
        ViewHolderItem holder;
        if (vi == null) {
            //The view is not a recycled one: we have to inflate
            vi = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
            holder = new ViewHolderItem();
            holder.uuid = (TextView) vi.findViewById(R.id.first_line);
            holder.major = (TextView) vi.findViewById(R.id.second_line);
            holder.minor = (TextView) vi.findViewById(R.id.third_line);
            vi.setTag(holder);
        } else {
            holder = (ViewHolderItem) vi.getTag();
        }
        Beacon beacon = beacons.get(position);
        holder.uuid.setText(String.format("UUID: %s", beacon.getUuid()));
        holder.major.setText(String.format("Major: %s", beacon.getMajor()));
        holder.minor.setText(String.format("Minor :%s", beacon.getMinor()));

        return vi;
    }

    static class ViewHolderItem {
        TextView uuid;
        TextView major;
        TextView minor;
    }
}
