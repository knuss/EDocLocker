package com.example.e_doclocker;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

//custom ArrayAdapter
public class DispAdapter extends ArrayAdapter<Citizen> {

    private Context context;
    private List<Citizen> records;

    //constructor, call on creation
    public DispAdapter(Context context, int resource, ArrayList<Citizen> objects) {
        super(context, resource, objects);

        this.context = context;
        this.records = objects;
    }

    //called when rendering the list
    public View getView(int position, View convertView, ViewGroup parent) {

        //get the citizen we are displaying
        Citizen record = records.get(position);

        //get the inflater and inflate the XML layout for each item
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item, null);

        TextView nic = (TextView) view.findViewById(R.id.txtNIC);
        TextView name = (TextView) view.findViewById(R.id.txtName);
        ImageView image = (ImageView) view.findViewById(R.id.imageView);
        nic.setText(String.valueOf(record.getNic()));
        name.setText(record.getName().toLowerCase());
        if(record.getImage()!=null) {
            image.setImageBitmap(record.getImage());
        }

        return view;
    }

}
