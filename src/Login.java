package com.budget101;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.budget101.Data.User;
import com.budget101.Database.DatabaseAccess;

import java.security.NoSuchAlgorithmException;

public class Login extends AppCompatActivity implements View.OnClickListener
{
    private EditText name , password;
    private Button submit, register;
    private DatabaseAccess access;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Linking buttons
        name = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        submit =  (Button) findViewById(R.id.submit);
        register = (Button) findViewById(R.id.register);

        submit.setOnClickListener(this);
        register.setOnClickListener(this);

        this.access = new DatabaseAccess(this); // Open database
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.submit:
                String username = name.getText().toString(); // Username input
                String pass = "";

                try { pass = User.hash(password.getText().toString()); } // Hash password input
                catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
                User user = this.access.getUser(username); // Query database for user

                // Check for valid user object
                if(user != null)
                {
                    if(pass.equals(user.getPassword()))
                    {
                        //Toast.makeText(getApplicationContext(), "Redirecting...", Toast.LENGTH_SHORT).show();
                        this.access.close(); // Close database
                        startActivity(new Intent(this, MainActivity.class));
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Wrong name or password...", Toast.LENGTH_SHORT).show();
                        name.setBackgroundColor(Color.RED);
                        password.setBackgroundColor(Color.RED);
                    }
                }
                else
                {
                    Toast.makeText(this, username + " not found", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.register:
                startActivity(new Intent(this, Register.class));
                break;
        }
    }


    /**
     * Returns database connection object.
     * @return DatabaseAccess object
     */
    public DatabaseAccess getDatabase()
    {
        return this.access;
    }
}
