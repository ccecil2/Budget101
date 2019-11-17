package com.budget101;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.budget101.Data.Record;
import com.budget101.Database.DatabaseAccess;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class LogsGUI extends MainActivity
{
    private DatabaseAccess access;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs_gui);
        this.access = this.getDatabase();

        fillSpinners();
    }


    /**
     * Search database by month/year and fill table.
     * @param view
     */
    public void searchLogs(View view)
    {
        int month = ((Spinner)findViewById(R.id.spinMonth)).getSelectedItemPosition(); // Get month

        Spinner spin = findViewById(R.id.spinYear);
        String y = (String)spin.getSelectedItem(); // Get selected item
        int year = Integer.valueOf(y); // Convert to int

        // Get records
        Record[] record = this.access.getRecordByDate(month, year);
        this.fillTable(record);
    }


    /**
     * Fill the table with record information.
     * @param rec Array of records to display
     */
    private void fillTable(Record[] rec)
    {
        TableLayout layout = (TableLayout)findViewById(R.id.tablelayout);
        layout.removeAllViews(); // Reset table

        // Add column header
        TableRow header = new TableRow(this);
        TableRow.LayoutParams p = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        header.setLayoutParams(p);

        TextView h1 = new TextView(this);
        h1.setText("Date");
        h1.setPadding(15, 15, 15 ,15);
        this.textViewParams(h1);
        header.addView(h1, 0);

        TextView h2 = new TextView(this);
        h2.setText("Amount");
        h2.setPadding(15, 15, 15 ,15);
        this.textViewParams(h2);
        header.addView(h2, 1);

        TextView h3 = new TextView(this);
        h3.setText("Category");
        this.textViewParams(h3);
        h3.setPadding(15, 15, 15 ,15);
        header.addView(h3, 2);

        layout.addView(header);


        // Fill table layout
        for(int i = 0; i < rec.length; i++)
        {
            TableRow row = new TableRow(this);
            TableRow.LayoutParams param = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(param);

            if((i % 2) == 0)
                row.setBackgroundColor(Color.LTGRAY);

            // Add date
            TextView t1 = new TextView(this);
            t1.setText(rec[i].getDate().toString());
            this.textViewParams(t1);
            row.addView(t1, 0);

            // Add amount
            TextView t2 = new TextView(this);
            t2.setText(String.valueOf(rec[i].getAmount()));
            this.textViewParams(t2);
            t2.setGravity(Gravity.RIGHT);
            row.addView(t2, 1);

            // Add category
            TextView t3 = new TextView(this);
            t3.setText(rec[i].getCategory().getName());
            this.textViewParams(t3);
            row.addView(t3, 2);

            layout.addView(row);
        }
    }


    /**
     * Updates a TextView object for table format.
     * @param v TextView to update
     */
    private void textViewParams(TextView v)
    {
        v.setTextSize(17);
        v.setTextColor(Color.BLACK);
        v.setGravity(Gravity.CENTER_HORIZONTAL);
    }


    /**
     * Initialize the spinners with string values.
     */
    private void fillSpinners()
    {
        ArrayList<String> monthList = new ArrayList<String>(12);
        ArrayList<String> yearList = new ArrayList<String>(4);

        // Create strings
        monthList.add("JAN");
        monthList.add("FEB");
        monthList.add("MAR");
        monthList.add("APR");
        monthList.add("MAY");
        monthList.add("JUN");
        monthList.add("JUL");
        monthList.add("AUG");
        monthList.add("SEP");
        monthList.add("OCT");
        monthList.add("NOV");
        monthList.add("DEC");

        yearList.add("2019");
        yearList.add("2020");
        yearList.add("2021");

        // Create adapters
        ArrayAdapter<String> monAd = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, monthList);
        ArrayAdapter<String> yearAd = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearList);

        // Set adapters
        Spinner spinMonth = ((Spinner) findViewById(R.id.spinMonth));
        Spinner spinYear = ((Spinner) findViewById(R.id.spinYear));
        spinMonth.setAdapter(monAd);
        spinYear.setAdapter(yearAd);
    }
}
