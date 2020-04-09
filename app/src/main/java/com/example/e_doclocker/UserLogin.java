package com.example.e_doclocker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserLogin extends AppCompatActivity {

    private EditText txtUsername,txtPassword;
    private Button btnLogin;
    private DB db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        txtUsername=(EditText)findViewById(R.id.txtUsername);
        txtPassword=(EditText)findViewById(R.id.txtPassword);
        btnLogin=(Button) findViewById(R.id.btnLogin);
        db=new DB(getApplicationContext());
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=txtUsername.getText().toString();
                String password=txtPassword.getText().toString();
                if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(),"All fields are required",Toast.LENGTH_LONG).show();
                }else{
                    if(db.verifyUserLogin(username,password)!=null){
                        //login successful
                        startActivity(new Intent(getApplicationContext(),UserHome.class));
                        UserHome.current=db.verifyUserLogin(username,password);
                    }else{
                        Toast.makeText(getApplicationContext(),"Invalid NIC or password",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
