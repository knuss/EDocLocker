package com.example.e_doclocker.ui;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.e_doclocker.AES;
import com.example.e_doclocker.Citizen;
import com.example.e_doclocker.DB;
import com.example.e_doclocker.DES;
import com.example.e_doclocker.Document;
import com.example.e_doclocker.R;
import com.example.e_doclocker.SMSManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

public class AddDocumentFragment extends Fragment {

    EditText txtNic,txtDocumentType,txtEncryptionKey;
    TextView txtCitizenName,txtCitizenEmail,txtCitizenMobile;
    Button btnSearch,btnChooseFile,btnSubmit;
    DB db;
    Citizen citizen;
    InputStream stream;
    static int SAVE_REQUEST_CODE=1;
    private String TAG=this.getTag();
    Uri file;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        View root = inflater.inflate(R.layout.fragment_add_document, container, false);
        db=new DB(getContext());
        citizen=null;
        stream=null;
        file=null;
        txtNic=(EditText)root.findViewById(R.id.txtNic);
        txtDocumentType=(EditText)root.findViewById(R.id.txtDocumentType);
        txtEncryptionKey=(EditText)root.findViewById(R.id.txtKey);
        txtCitizenName=(TextView) root.findViewById(R.id.txtCitizenName);
        txtCitizenEmail=(TextView) root.findViewById(R.id.txtCitizenEmail);
        txtCitizenMobile=(TextView) root.findViewById(R.id.txtCitizenMobile);
        btnSearch=(Button) root.findViewById(R.id.btnSearch);
        btnChooseFile=(Button) root.findViewById(R.id.btnChooseFile);
        btnSubmit=(Button) root.findViewById(R.id.btnSubmit);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nic=txtNic.getText().toString();
                if(TextUtils.isEmpty(nic)){
                    Toast.makeText(getContext(),"Enter the NIC of the citizen",Toast.LENGTH_LONG).show();
                }else{
                    citizen=db.getCitizens(nic);
                    if(citizen==null){
                        Toast.makeText(getContext(),"No record found for that NIC",Toast.LENGTH_LONG).show();
                    }else{
                        txtCitizenName.setText(citizen.getName());
                        txtCitizenEmail.setText(citizen.getEmail());
                        txtCitizenMobile.setText(citizen.getMpbile());
                    }
                }

            }
        });
        btnChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, SAVE_REQUEST_CODE);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String docType=txtDocumentType.getText().toString();
                String encKey=txtEncryptionKey.getText().toString();
                if(file==null){
                    Toast.makeText(getContext(),"You have not selected any file", Toast.LENGTH_LONG).show();
                }else if(TextUtils.isEmpty(docType)){
                    Toast.makeText(getContext(),"The document type is required", Toast.LENGTH_LONG).show();
                }else if(TextUtils.isEmpty(encKey)){
                    Toast.makeText(getContext(),"The secret key is required", Toast.LENGTH_LONG).show();
                }else if(citizen==null){
                    Toast.makeText(getContext(),"You have not selected any user", Toast.LENGTH_LONG).show();
                }else{
                    String filePath=file.toString();
                    String encryptedFilePath= AES.encrypt(filePath,encKey);
                    byte[] codedKey=null;
                    try {
                       codedKey=new DES().encrypt(encKey);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Document doc=new Document(docType,encryptedFilePath,codedKey,String.valueOf(citizen.getNic()));
                    db.addDocument(doc);
                    long current=System.currentTimeMillis();
                    db.addKey(docType,codedKey,Long.toString(current));
                    String message="A new document of type: "+docType+" has been uploaded\n The key used to encrypt is: "+encKey;
                    sendEmail(txtCitizenEmail.getText().toString(),message);

                    Toast.makeText(getContext(),"Document added successfully",Toast.LENGTH_LONG).show();
                }
            }
        });
        return root;
    }
    protected boolean sendEmail(String email,String message) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        String To[]={email};
        String CC[]={""};
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, To);
        emailIntent.putExtra(Intent.EXTRA_CC,CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "DOCUMENT UPLOAD SUCCESS");
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            return true;
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SAVE_REQUEST_CODE && resultCode == RESULT_OK && null != data) {
            file= data.getData();
            Uri fileuri = data.getData();
        }


    }

}