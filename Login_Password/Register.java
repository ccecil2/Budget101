package com.example.budget101;

import androidx.annotation.RequiresApi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.budget101.Data.User;
import com.example.budget101.Database.DatabaseAccess;

import java.security.NoSuchAlgorithmException;

public class Register extends MainActivity implements View.OnClickListener {

    EditText et_username, et_password;
    Button submit;
    private DatabaseAccess access;
    private User new_user;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
       
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_register);

        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        submit =  (Button) findViewById(R.id.submit);
        this.access = this.getDatabase();
        new_user = new User(null,null,null);
        
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:

                String username = et_username.getText().toString();
                String password = et_password.getText().toString();
              
                try {                  
                        new_user = access.newUser(username, User.hash(password), null);
                    }
                }
                catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
        
                this.finish();
                break;
        }
    }

    public DatabaseAccess getDatabase () {
        return this.access;
    }
}
