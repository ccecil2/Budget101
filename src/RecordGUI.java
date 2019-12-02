package com.budget101;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.budget101.Data.Category;
import com.budget101.Data.Record;
import com.budget101.Database.DatabaseAccess;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RecordGUI extends MainActivity
{
    private DatabaseAccess access;

    private Category[] categories;
    private Category currentCat;

    private Spinner spin;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_gui);

        this.spin = findViewById(R.id.spinner);

        this.access = super.getDatabase();
        this.addListeners();
        populateCategories();
    }


    /**
     * Add listeners to components.
     */
    private void addListeners()
    {
        this.spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                int slot = parent.getSelectedItemPosition();

                if(slot >= 0)
                    currentCat = categories[slot];
                else
                    currentCat = null;
            }

            public void onNothingSelected(AdapterView<?> parent)
            {
                currentCat = null;
            }
        });
    }



    /**
     * Populate the spinner with categories.
     */
    private void populateCategories()
    {
        this.categories = this.access.getAllCategories(); // Get categories
        ArrayList<String> list = new ArrayList<String>(10);

        for(int i = 0; i < this.categories.length; i++)
        {
            list.add(this.categories[i].getName()); // Add names to list
        }

        ArrayAdapter<String> ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        this.spin.setAdapter(ad);
        this.currentCat = this.categories[0]; // Set to initial value in spinner
    }


    /**
     * Verifies monetary format.
     * @param str String to check
     * @return True if of correct format, else, false
     */
    public boolean regex(String str)
    {
        String valid = "([1-9]\\d{0,2})|(([1-9]\\d*)?\\d)(\\.\\d\\d)?$"; // Valid regex
        return str.matches(valid);
    }


    /**
     * Verifies date format.
     * @param str String to check
     * @return True if of correct format, else, false
     */
    public boolean regexDate(String str)
    {
        String valid = "^(1[0-2]|0[1-9])/(3[01]|[12][0-9]|0[1-9])/[0-9]{4}$";
        return str.matches(valid);
    }


    /**
     * Verifies input in components.
     * @param limit Text for amount
     * @param d Text for date
     * @return True if inputs pass requirements
     */
    public boolean checkInputs(String limit, String d)
    {
        // Check date format
        if(d.length() != 10)
        {
            Toast t = Toast.makeText(this, "Date must be MM/DD/YYYY", Toast.LENGTH_LONG);
            t.show();
            return false;
        }


        // Check date format
        if(!this.regexDate(d))
        {
            Toast t = Toast.makeText(this, "Invalid input for date", Toast.LENGTH_LONG);
            t.show();
            return false;
        }


        // Check limit format
        if(!this.regex(limit))
        {
            Toast t = Toast.makeText(this, "Invalid input for amount", Toast.LENGTH_LONG);
            t.show();
            return false;
        }


        return true; // Verified inputs
    }


    /**
     * Formats given string in format MM/DD/YYYY into
     * the corresponding Date object.
     * @param d String of date
     * @return Date object matching input
     */
    private Date formatDate(String d)
    {
        int month = Integer.parseInt(d.substring(0, 2)) - 1;
        int day = Integer.parseInt(d.substring(3, 5));
        int year = Integer.parseInt(d.substring(6));
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, 0, 0, 0);
        c.set(Calendar.MILLISECOND, 0);

        return c.getTime();
    }


    /**
     * Save component values to database.
     * @param view
     */
    public void saveRecord(View view)
    {
        String comment = ((TextView)findViewById(R.id.note_txt)).getText().toString();
        String dateTxt = ((TextView)findViewById(R.id.date_txt)).getText().toString();
        String amountTxt = ((TextView)findViewById(R.id.amount_txt)).getText().toString();

        if(this.checkInputs(amountTxt, dateTxt))
        {
            double amount = Double.parseDouble(amountTxt);
            Date date = this.formatDate(dateTxt);

            Record rec = this.access.newRecord(amount, comment, date, this.currentCat);


            if (rec != null)
            {
                Toast t = Toast.makeText(this, "Record saved", Toast.LENGTH_SHORT);
                t.show();
            }
            else // Something failed
            {
                Toast t = Toast.makeText(this, "Record not saved", Toast.LENGTH_SHORT);
                t.show();
            }

            this.finish(); // Close activity
        }
    }
}
