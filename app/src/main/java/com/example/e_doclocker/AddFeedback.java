package com.example.e_doclocker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddFeedback extends AppCompatActivity {

    EditText txtFeedback;
    Button btnSend;
    DB db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feedback);
        txtFeedback=(EditText)findViewById(R.id.txtFeedback);
        btnSend=(Button)findViewById(R.id.btnSend);
        db=new DB(getApplicationContext());
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedBack=txtFeedback.getText().toString();
                if(TextUtils.isEmpty(feedBack)){
                    Toast.makeText(getApplicationContext(),"Enter your feedback and press send",Toast.LENGTH_LONG).show();
                }else{
                    db.addFeedback(new Feedback(UserHome.current.getNic(),feedBack));
                    Toast.makeText(getApplicationContext(),"Feedback sent successfully",Toast.LENGTH_LONG).show();
                    txtFeedback.setText("");
                }
            }
        });
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
            case R.id.add_feedback:
                // User chose the "profile" item, show the app settings UI...
                startActivity(new Intent(getApplicationContext(),AddFeedback.class));
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
