package com.example.e_doclocker.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.e_doclocker.Citizen;
import com.example.e_doclocker.DB;
import com.example.e_doclocker.R;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Random;

import static android.app.Activity.RESULT_OK;

public class AddCitizenFragment extends Fragment {
    //static final int RESULT_OK=1;
    private ImageView imageView;
    private Button btnUploadImage,btnRegister;
    public Bitmap bitmap=null;
    private EditText txtNic, txtName,txtEmail,txtMobile,txtDob,txtCity,txtPassword;
    DB db;
    private static int RESULT_LOAD_IMAGE = 1;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        View root = inflater.inflate(R.layout.fragment_add_citizen, container, false);
        db=new DB(getContext());
        imageView=(ImageView)root.findViewById(R.id.imageView);
        txtNic=(EditText)root.findViewById(R.id.txtNic);
        txtName=(EditText)root.findViewById(R.id.txtName);
        txtEmail=(EditText)root.findViewById(R.id.txtEmail);
        txtMobile=(EditText)root.findViewById(R.id.txtPhone);
        txtDob=(EditText)root.findViewById(R.id.txtDob);
        txtCity=(EditText)root.findViewById(R.id.txtCity);
        txtPassword=(EditText)root.findViewById(R.id.txtPassword);

        btnUploadImage=(Button)root.findViewById(R.id.btnUploadImage);
        btnRegister=(Button)root.findViewById(R.id.btnRegister);
        //txtNic.setText(getNIc(12));
        //txtNic.setEnabled(false);
        final String[] options={"Select Image from Gallery","Take picture using Camera"};
        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which==0){
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto , 1);
                        }else{
                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePicture, 0);
                        }
                    }
                });
                builder.show();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nic=txtNic.getText().toString();
                String name=txtName.getText().toString();
                String email=txtEmail.getText().toString();
                String mobile=txtMobile.getText().toString();
                String dob=txtDob.getText().toString();
                String city=txtCity.getText().toString();
                String password=txtPassword.getText().toString();
                if(TextUtils.isEmpty(nic) || TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(mobile) || TextUtils.isEmpty(dob) || TextUtils.isEmpty(city) || TextUtils.isEmpty(password)){
                    Toast.makeText(getContext(),"All fields are required",Toast.LENGTH_LONG).show();
                }
                else{
                    Citizen citizen =new Citizen(nic,name,email,mobile,dob,city,password,bitmap);
                    db.addCitizen(citizen);
                    resetForm();
                    String message="Your account has been successfully created.\n Your login details are as follows.\nNIC:"+nic+"\nPassword:"+password;
                    if(sendEmail(email,message)) {
                        Toast.makeText(getContext(), "Citizen added successfully", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getContext(), "Citizen added successfully but no email was sent. Check your mail client", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        return root;
    }
    private void resetForm(){
        txtNic.setText("");
        txtName.setText("");
        txtEmail.setText("");
        txtMobile.setText("");
        txtDob.setText("");
        txtCity.setText("");
        txtPassword.setText("");
        imageView.setImageBitmap(null);
    }
    protected boolean sendEmail(String email,String message) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        String To[]={email};
        String CC[]={""};
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, To);
        emailIntent.putExtra(Intent.EXTRA_CC,CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "REGISTRATION SUCCESS");
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            return true;
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    static String getNIc(int n)
    {
        // length is bounded by 256 Character
        byte[] array = new byte[256];
        new Random().nextBytes(array);
        String randomString
                = new String(array, Charset.forName("UTF-8"));
        // Create a StringBuffer to store the result
        StringBuffer r = new StringBuffer();
        // remove all spacial char
        String  AlphaNumericString
                = randomString
                .replaceAll("[^A-Za-z0-9]", "");
        // Append first 20 alphanumeric characters
        // from the generated random String into the result
        for (int k = 0; k < AlphaNumericString.length(); k++) {
            if (Character.isLetter(AlphaNumericString.charAt(k))
                    && (n > 0)
                    || Character.isDigit(AlphaNumericString.charAt(k))
                    && (n > 0)) {

                r.append(AlphaNumericString.charAt(k));
                n--;
            }
        }
        // return the resultant string
        return r.toString();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 0:
                if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                    Uri selectedImageURI = data.getData();
                    bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImageURI);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    imageView.setImageBitmap(bitmap);

                }

                break;
            case 1:
                if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                    Uri selectedImageURI = data.getData();
                    bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImageURI);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    imageView.setImageBitmap(bitmap);

                }
                break;
        }
    }
}