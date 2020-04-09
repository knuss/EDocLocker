package com.example.e_doclocker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.security.AccessController.getContext;

public class ViewDocuments extends AppCompatActivity {

    private ListView listView;
    DB db;
    private String m_Text = "";
    Document selected=null;
    Uri file;
    String phoneNo,sms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_documents);
        listView=(ListView)findViewById(R.id.listView);
        db=new DB(getApplicationContext());
        final ArrayList<Document> documents=db.getDocuments(String.valueOf(UserHome.current.getNic()));
        ArrayAdapter<Document> arrayAdapter = new ArrayAdapter<Document>(this, android.R.layout.simple_list_item_1,documents );
        listView.setAdapter(arrayAdapter);
        file=null;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected = documents.get(position);
                final Key enKey = db.getKey(selected.getDocType());
                String generated = genKey(6);
                Log.d("KULUKULU", String.valueOf(getTimeDiff(Long.parseLong(enKey.ts))));
                if (getTimeDiff(Long.parseLong(enKey.ts)) > 300) {
                    byte[] newKey = new byte[0];
                    try {
                        newKey = new DES().encrypt(generated);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String ts = Long.toString(System.currentTimeMillis());
                    db.addKey(selected.getDocType(), newKey, ts);
                    phoneNo = UserHome.current.getMpbile();
                    sms = "Your new decryption key is " + generated + " and is valid for 5 minutes from now";
                    sendSMSMessage();
                    Toast.makeText(getApplicationContext(), "A new key has been sent to your registered mobile number", Toast.LENGTH_LONG).show();
                } else {


                    //start decryption
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ViewDocuments.this);
                    builder.setTitle("Enter Secret Key");

                    // Set up the input
                    final EditText input = new EditText(ViewDocuments.this);
                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    builder.setView(input);

                    // Set up the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            int diff = getTimeDiff(Long.parseLong(enKey.ts));
                            Log.d("KULUKULU","Current Diff"+Long.toString(diff));
                            m_Text = input.getText().toString();
                            if (diff < 300) {
                                try {
                                    String key = new DES().decrypt(selected.getKey());
                                    String unlockingKey = new DES().decrypt(enKey.getKey());
                                    if (unlockingKey.equals(m_Text)) {
                                        //key matches
                                        Log.d("THIYUMI", Arrays.toString(selected.getFile().getBytes()));

                                        String filePath = AES.decrypt(selected.getFile(), key);
                                        file = Uri.parse(filePath);
                                        Toast.makeText(getApplicationContext(), "Valid Key...Decrypting file", Toast.LENGTH_LONG).show();
                                        if (file != null) {
                                            String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(file.toString()));
                                            try {
                                                Toast.makeText(getApplicationContext(), file.toString(), Toast.LENGTH_LONG).show();

                                                Intent i;
                                                i = new Intent(Intent.ACTION_VIEW);
                                                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                                i.setDataAndType(file, mimeType);
                                                startActivity(i);

                                            } catch (ActivityNotFoundException e) {
                                                Toast.makeText(getApplicationContext(), "No Application Available to view this file type", Toast.LENGTH_LONG).show();
                                            }
                                        }

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Invalid Key", Toast.LENGTH_LONG).show();
                                        Log.d("KULUKULU",key+"="+m_Text+" rejected");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "The current key is not valid anymore", Toast.LENGTH_LONG).show();
                                dialog.cancel();
                            }
                        }

                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();


                }
            }
        });

    }
    private int getTimeDiff(long past){
        long now=System.currentTimeMillis();
        long diff=now-past;
        int secs=(int) (diff/1000);
        return secs;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_home, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_quit:
                // User chose the "logout" item, show the app settings UI...
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                return true;
            case R.id.action_documents:
                // User chose the "documents" item, show the app settings UI...
                startActivity(new Intent(getApplicationContext(),ViewDocuments.class));
                return true;
            case R.id.action_profile:
                // User chose the "profile" item, show the app settings UI...
                startActivity(new Intent(getApplicationContext(),UserHome.class));
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    private String genKey(int n)
    {

        // chose a Character random from this String
        String AlphaNumericString ="0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
    protected void sendSMSMessage() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS}, 1);
            }
        }else {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, sms, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, sms, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }
}
