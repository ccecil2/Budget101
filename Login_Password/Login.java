package com.example.budget101;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.budget101.Data.Category;
import com.example.budget101.Data.Enum.Money;
import com.example.budget101.Data.Enum.Status;
import com.example.budget101.Data.Record;
import com.example.budget101.Data.Split;
import com.example.budget101.Data.User;
import com.example.budget101.Database.DatabaseAccess;

import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;

public class Login extends MainActivity {

    private DatabaseAccess access;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.access = this.getDatabase();

        // Make a user
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                User u = access.newUser("Alex", User.hash("password123"), null);
            }
        }
        catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
/*
        // Make a few categories
        Category c1 = access.newCategory("Food", true, 160.0, Money.EXPENSE);
        Category c2 = access.newCategory("Gas", true, 60.0, Money.EXPENSE);
        Category c3 = access.newCategory("Rent", false, 645.0, Money.EXPENSE);
        Category c4 = access.newCategory("Work", false, 1500.0, Money.INCOME);

        //Make a few records
        access.newRecord(9.65, "Panda Express", new Date(), c1);
        access.newRecord(6.52, "McDonalds", new Date(), c1);
        access.newRecord(19.88, "", new Date(), c2);
        access.newRecord(645.0, "November Rent", new Date(), c3);
        access.newRecord(1500.0, "", new Date(), c4);


        // Pull all records
        Record[] r = access.getRecordByDate(10, 2019); // Nov/2019
        Category[] c = access.getAllCategories();
        Split[] s = access.getPendingSplits();


        // Make Splits
        Split s1 = access.newSplit("Gary", r[0]);
        Split s2 = access.newSplit("Ed", r[1]);
        Split s3 = access.newSplit("Work", r[3]);*/
    }

    public DatabaseAccess getDatabase () {
        return this.access;
    }

    public void openCategory (View view) {
        Intent intent = new Intent(this, CategoryGUI.class);
        startActivity(intent);
    }

    public void openLogs(View v) {
        Intent intent = new Intent(this, LogsGUI.class);
        startActivity(intent);
    }
}
