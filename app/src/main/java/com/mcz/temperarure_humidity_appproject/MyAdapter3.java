package com.mcz.temperarure_humidity_appproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MyAdapter3 extends ArrayAdapter<MySettingdata> {
    private int resourceId;
    public MyAdapter3(@NonNull Context context, int resource, @NonNull List<MySettingdata> objects) {
        super(context, resource, objects);
        resourceId=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final MySettingdata mySettingdata=getItem(position);
        View view=LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        ImageView imageView=(ImageView)view.findViewById(R.id.whatimage);
        TextView textView=(TextView)view.findViewById(R.id.whatname);
        imageView.setImageResource(mySettingdata.getImageId());
        textView.setText(mySettingdata.getName());

        return  view;
    }
}
