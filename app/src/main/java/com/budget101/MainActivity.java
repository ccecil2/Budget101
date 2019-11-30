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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class MainActivity extends AppCompatActivity
{
    private DatabaseAccess access;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        access = new DatabaseAccess(this);


        Category[] c = access.getAllCategories();

        // Make a few categories
        Category c1 = access.newCategory("Food", true, 160.0, Money.EXPENSE);
        Category c2 = access.newCategory("Gas", true, 60.0, Money.EXPENSE);
        Category c3 = access.newCategory("Rent", false, 645.0, Money.EXPENSE);
        Category c4 = access.newCategory("Work", false, 1500.0, Money.INCOME);
        Category c5 = access.newCategory("Other", false, 120, Money.EXPENSE);
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

    public void openReport(View view)
    {
        Intent intent = new Intent(this, ReportGUI.class);
        startActivity(intent);
    }


    public void openIncomeGUI(View view)
    {
        Intent intent = new Intent(this, IncomeGUI.class);
        startActivity(intent);

    }

    public void openExpenseGUI(View view)
    {
        Intent intent = new Intent(this, ExpenseGUI.class);
        startActivity(intent);

    }

}