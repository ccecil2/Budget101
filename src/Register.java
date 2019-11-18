package com.budget101;

import androidx.annotation.RequiresApi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.budget101.Data.User;
import com.budget101.Database.DatabaseAccess;

public class Register extends MainActivity implements View.OnClickListener {

    EditText et_username, et_password;
    Button submit;
    private DatabaseAccess access;
    private User new_user, u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.access = super.getDatabase();
        setContentView(R.layout.activity_register);

        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        submit = (Button) findViewById(R.id.submit);
        new_user = new User(null, null, null);
        u = new User(null,null,null);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();
                new_user.setPassword(password);
                u = this.access.newUser(username, new_user.getPassword(), null);

                if (u == null) {
                    Toast.makeText(this, username + " exists", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, username + " created", Toast.LENGTH_SHORT).show();
                    this.finish();
                }
                break;
        }

    }
}

