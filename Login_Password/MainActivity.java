package com.budget101;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.budget101.Data.User;
import com.budget101.Database.DatabaseAccess;

import static android.hardware.camera2.params.RggbChannelVector.RED;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Variable names of widgets
    EditText name , password;
    Button submit, register;
    private DatabaseAccess access;
    private User new_user, u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        access = new DatabaseAccess(this);

        // Linking buttons
        name = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        submit =  (Button) findViewById(R.id.submit);
        register = (Button) findViewById(R.id.register);
        new_user = new User(null, null, null);
        u = new User(null,null,null);

        submit.setOnClickListener(this);
        register.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                String username = name.getText().toString();
                String pass = password.getText().toString();
                new_user.setPassword(pass);
                u = this.access.newUser(username, new_user.getPassword(), null);

                if (u == null) {
                    Toast.makeText(this, "Redirecting...", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, Login.class));
                }
                else {
                    Toast.makeText(this,"Wrong name or password...",Toast.LENGTH_SHORT).show();
                    name.setBackgroundColor(Color.CYAN);
                    password.setBackgroundColor(Color.CYAN);
                }
                break;

            case R.id.register:
                startActivity(new Intent(this, Register.class));
                break;
        }
    }

    public DatabaseAccess getDatabase () {
        return this.access;
    }
}


