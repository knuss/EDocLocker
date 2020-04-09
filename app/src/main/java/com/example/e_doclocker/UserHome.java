package com.example.e_doclocker;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class UserHome extends AppCompatActivity {

    public static Citizen current=null;
    private ImageView imageView;
    private TextView txtUserInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageView=(ImageView)findViewById(R.id.imageView);
        txtUserInfo=(TextView)findViewById(R.id.txtUserInfo);
        if(current!=null){
            imageView.setImageBitmap(current.getImage());
            txtUserInfo.setText(current.toString());
        }

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
