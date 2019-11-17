package com.example.budget101;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.budget101.Data.User;
import com.example.budget101.Database.DatabaseAccess;

import static android.hardware.camera2.params.RggbChannelVector.RED;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Variable names of widgets
    EditText name , password;
    Button submit, register;
    private DatabaseAccess access;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Linking buttons
        name = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        submit =  (Button) findViewById(R.id.submit);
        register = (Button) findViewById(R.id.register);

        submit.setOnClickListener(this);
        register.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                User user = new User(null,null, null);
                // find user
                if (name.getText().toString().equals(/*access.getUser(user.getName())*/"ed") &&
                        password.getText().toString().equals(/*access.getUser(user.getPassword())*/"password1234")) {
                    Toast.makeText(getApplicationContext(), "Redirecting...", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, Login.class));
                }
                else {
                    Toast.makeText(getApplicationContext(),"Wrong name or password...",Toast.LENGTH_SHORT).show();
                    name.setBackgroundColor(Color.RED);
                    password.setBackgroundColor(Color.RED);
                }

                break;

            case R.id.register:
                startActivity(new Intent(this, Register.class));
                break;
        }
    }

}
