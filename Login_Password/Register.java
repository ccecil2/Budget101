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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            super.onCreate(savedInstanceState);
        }
        setContentView(R.layout.activity_register);

        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        submit =  (Button) findViewById(R.id.submit);
        this.access = this.getDatabase();

        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:

                String username = et_username.getText().toString();
                String password = et_password.getText().toString();
                User new_user;

                try {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        new_user = access.newUser(username, User.hash(password), null);
                    }
                }
                catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }

    public DatabaseAccess getDatabase () {
        return this.access;
    }
}
