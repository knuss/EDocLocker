package com.example.e_doclocker.ui;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.e_doclocker.AdminHome;
import com.example.e_doclocker.Citizen;
import com.example.e_doclocker.DB;
import com.example.e_doclocker.DispAdapter;
import com.example.e_doclocker.EditCitizen;
import com.example.e_doclocker.R;

import java.util.ArrayList;

public class ManageCitizenFragment extends Fragment {
    ListView listView;
    DB db;
    ArrayList<Citizen> data;
    ArrayAdapter<Citizen> arrayAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        View root = inflater.inflate(R.layout.fragment_manage_citizen, container, false);
        listView=(ListView)root.findViewById(R.id.listView);
        db=new DB(getContext());
        data=db.getCitizens();
        arrayAdapter=new DispAdapter(getContext(),0,data);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AdminHome.selected=data.get(position);
                EditCitizen nextFrag= new EditCitizen();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, nextFrag, "Opening EditFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
        return root;
    }
}