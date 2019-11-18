package com.budget101;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.budget101.Data.Category;
import com.budget101.Data.Enum.Money;
import com.budget101.Data.Enum.Status;
import com.budget101.Data.Record;
import com.budget101.Data.Split;
import com.budget101.Data.User;
import com.budget101.Database.DatabaseAccess;

import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private EditText name , password;
    private Button submit, register;
    private DatabaseAccess access;

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

        submit.setOnClickListener(this);
        register.setOnClickListener(this);

        // Make a user
        /*try { User u = access.newUser("Alex", User.hash("password123"), null); }
        catch (NoSuchAlgorithmException e) { e.printStackTrace(); }*/

        // Make a few categories
        /*Category c1 = access.newCategory("Food", true, 160.0, Money.EXPENSE);
        Category c2 = access.newCategory("Gas", true, 60.0, Money.EXPENSE);
        Category c3 = access.newCategory("Rent", false, 645.0, Money.EXPENSE);
        Category c4 = access.newCategory("Work", false, 1500.0, Money.INCOME);

        //Make a few records
        access.newRecord(9.65, "Panda Express", new Date(), c1);
        access.newRecord(6.52, "McDonalds", new Date(), c1);
        access.newRecord(19.88, "", new Date(), c2);
        access.newRecord(645.0, "November Rent", new Date(), c3);
        access.newRecord(1500.0, "", new Date(), c4);*/


        // Pull all records
        //Record[] r = access.getRecordByDate(10, 2019); // Nov/2019
        //Category[] c = access.getAllCategories();
        //Split[] s = access.getPendingSplits();


        // Make Splits
        /*Split s1 = access.newSplit("Gary", r[0]);
        Split s2 = access.newSplit("Ed", r[1]);
        Split s3 = access.newSplit("Work", r[3]);*/
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
                        Toast.makeText(getApplicationContext(), "Redirecting...", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(this, Login.class));
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

                // find user
                /*if (name.getText().toString().equals(/*access.getUser(user.getName())"ed") &&
                        password.getText().toString().equals(/*access.getUser(user.getPassword())"password1234")) {
                    Toast.makeText(getApplicationContext(), "Redirecting...", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, Login.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong name or password...", Toast.LENGTH_SHORT).show();
                    name.setBackgroundColor(Color.RED);
                    password.setBackgroundColor(Color.RED);
                }

                break;*/

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

    public void openCategory(View view)
    {
        Intent intent = new Intent(this, CategoryGUI.class);
        startActivity(intent);
    }

    public void openLogs(View view)
    {
        Intent intent = new Intent(this, LogsGUI.class);
        startActivity(intent);
    }
}
