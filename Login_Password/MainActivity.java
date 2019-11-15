package com.example.appdevteam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Variable names of widgets
    EditText username , password;
    Button submit, register;
    UserLocalStore uls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Linking buttons
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        submit =  (Button) findViewById(R.id.submit);
        register = (Button) findViewById(R.id.register);

        submit.setOnClickListener(this);
        register.setOnClickListener(this);

        uls = new UserLocalStore(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (authentic() == true) {
            displayUserDetails();
        }

    }

    private boolean authentic() {
        return uls.getUserLoggedIn();
    }

    private void displayUserDetails() {
        User user = uls.getLoggedInUser();

        username.setText(user.username);
        password.setText(user.password);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                User user = new User(null,null);
                uls.storeData(user);
                uls.setUserLoggedIn(true);

                startActivity(new Intent(this, Money.class));
                break;

            case R.id.register:
                startActivity(new Intent(this, Register.class));
                break;
        }
    }

}
