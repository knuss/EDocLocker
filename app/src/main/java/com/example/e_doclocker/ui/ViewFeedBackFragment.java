package com.example.e_doclocker.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.e_doclocker.DB;
import com.example.e_doclocker.Feedback;
import com.example.e_doclocker.R;
import com.example.e_doclocker.UserHome;

import java.util.ArrayList;

public class ViewFeedBackFragment extends Fragment {

    DB db;
    ListView listView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        View root = inflater.inflate(R.layout.fragment_view_feedback, container, false);
        db=new DB(getContext());
        listView=(ListView)root.findViewById(R.id.listView);
        ArrayList<Feedback> feedbacks=db.getFeedback();
        ArrayAdapter<Feedback> arrayAdapter = new ArrayAdapter<Feedback>(getContext(), android.R.layout.simple_list_item_1,feedbacks);
        listView.setAdapter(arrayAdapter);
        return root;
    }
}