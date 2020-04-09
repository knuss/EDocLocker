package com.example.e_doclocker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.e_doclocker.ui.ManageCitizenFragment;


public class EditCitizen extends Fragment {
    //static final int RESULT_OK=1;
    private Button  btnRegister;
    private EditText txtNic, txtName, txtEmail, txtMobile, txtDob, txtCity;
    DB db;
    Citizen current;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        View root = inflater.inflate(R.layout.fragment_edit_citizen, container, false);
        db = new DB(getContext());
        current=AdminHome.selected;
        txtNic = (EditText) root.findViewById(R.id.txtNic);
        txtName = (EditText) root.findViewById(R.id.txtName);
        txtEmail = (EditText) root.findViewById(R.id.txtEmail);
        txtMobile = (EditText) root.findViewById(R.id.txtPhone);
        txtDob = (EditText) root.findViewById(R.id.txtDob);
        txtCity = (EditText) root.findViewById(R.id.txtCity);
        prepopulateFields();
        Toast.makeText(getContext(),current.getName(),Toast.LENGTH_LONG).show();
        btnRegister = (Button) root.findViewById(R.id.btnRegister);
        txtNic.setEnabled(false);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nic = txtNic.getText().toString();
                String name = txtName.getText().toString();
                String email = txtEmail.getText().toString();
                String mobile = txtMobile.getText().toString();
                String dob = txtDob.getText().toString();
                String city = txtCity.getText().toString();
                if (TextUtils.isEmpty(nic) || TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(mobile) || TextUtils.isEmpty(dob) || TextUtils.isEmpty(city) ) {
                    Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_LONG).show();
                }  else {
                    Citizen citizen = new Citizen(nic, name, email, mobile, dob, city);
                    db.updateCitizen(citizen);
                    Toast.makeText(getContext(), "Citizen updated successfully", Toast.LENGTH_LONG).show();
                    ManageCitizenFragment nextFrag= new ManageCitizenFragment();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.nav_host_fragment, nextFrag, "OPeningEditFragment")
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
        return root;
    }
    private void prepopulateFields(){
        txtNic.setText(String.valueOf(current.getNic()));
        txtName.setText(current.getName());
        txtEmail.setText(current.getEmail());
        txtMobile.setText(current.getMpbile());
        txtDob.setText(current.getDob());
        txtCity.setText(current.getCity());
    }
}
