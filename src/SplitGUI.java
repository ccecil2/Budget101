package com.budget101;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.budget101.Data.Category;
import com.budget101.Data.Enum.Status;
import com.budget101.Data.Record;
import com.budget101.Data.Split;
import com.budget101.Database.DatabaseAccess;

import java.util.ArrayList;
import java.util.Date;

public class SplitGUI extends MainActivity
{
    private DatabaseAccess access;
    private View.OnClickListener listen;
    private Split currentSplit;
    private TableLayout layout;
    private Status status;
    private Category[] categories;
    private Category currentCat;

    private Spinner spin;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split_gui);

        this.access = super.getDatabase();
        this.layout = (TableLayout)findViewById(R.id.tablelayout);
        this.spin = (Spinner)findViewById(R.id.spin);

        this.status = Status.CLOSED;

        addListener();
        this.searchLogs();
        populateCategories();
    }


    /**
     * Adds listeners to components.
     */
    private void addListener()
    {
        this.listen = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                rowClick(v);
            }
        };

        ((RadioGroup)findViewById(R.id.grpRad)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedID)
            {
                switch(checkedID)
                {
                    case R.id.radCancel:
                        status = Status.CANCELED;
                        break;
                    case R.id.radClose:
                        status = Status.CLOSED;
                        break;
                }
            }
        });

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
        this.categories = this.access.getAllIncomeCategories(); // Get categories
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
     * Open GUI for new split.
     * @param view
     */
    public void openNewSplit(View view)
    {
        Intent intent = new Intent(this, NewSplit.class);
        startActivity(intent);
    }


    /**
     * Update the selected split.
     * @param view
     */
    public void updateSplit(View view)
    {
        if(checkInputs())
        {
            this.currentSplit.setStatus(this.status);
            boolean suc = this.access.updateSplitStatus(this.currentSplit);


            if (suc)
            {
                if(this.currentSplit.getStatus() == Status.CLOSED) // Make income record
                {
                    double amount = this.currentSplit.getRecord().getAmount();
                    String comment = "Finished split";
                    Date date = new Date();

                    this.access.newRecord(amount, comment, date, this.currentCat); // Create income record
                }


                Toast t = Toast.makeText(this, "Split saved", Toast.LENGTH_SHORT);
                t.show();
            }
            else // Something failed
            {
                Toast t = Toast.makeText(this, "Split not updated", Toast.LENGTH_SHORT);
                t.show();
            }

            this.finish(); // Close activity
        }
    }


    /**
     * Check inputs for validity.
     * @return If inputs match format
     */
    private boolean checkInputs()
    {
        if(this.currentSplit == null)
        {
            Toast t = Toast.makeText(this, "Split must be selected", Toast.LENGTH_LONG);
            t.show();
            return false;
        }

        return true;
    }


    /**
     * Handles click event in the table.
     * @param v Row that was selected
     */
    private void rowClick(View v)
    {
        resetColors();

        TableRow row = (TableRow)v;
        TextView view = (TextView)row.getChildAt(2);
        int id = Integer.parseInt(view.getText().toString());
        this.currentSplit = this.access.getSplit(id);

        row.setBackgroundColor(Color.BLUE);
    }


    /**
     * Resets the colors in the table.
     */
    private void resetColors()
    {
        int size = this.layout.getChildCount();
        TableRow row;

        for(int i = 1; i < size; i++)
        {
            row = (TableRow)this.layout.getChildAt(i);
            if ((i % 2) == 0)
                row.setBackgroundColor(Color.parseColor("#EEEEEE"));
            else
                row.setBackgroundColor(Color.LTGRAY);
        }
    }

    /**
     * Search database by current month/year and
     * fill table with most recent entries.
     */
    private void searchLogs()
    {
        // Get splits
        Split[] record = this.access.getPendingSplits();
        this.fillTable(record);
    }


    /**
     * Fill the table with pending split information.
     * @param rec Array of Splits to display
     */
    private void fillTable(Split[] rec)
    {
        layout.removeAllViews(); // Reset table

        // Add column header
        TableRow header = new TableRow(this);
        TableRow.LayoutParams p = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        header.setLayoutParams(p);

        TextView h1 = new TextView(this);
        h1.setText("Name");
        h1.setPadding(15, 15, 15 ,15);
        this.textViewParams(h1);
        header.addView(h1, 0);

        TextView h2 = new TextView(this);
        h2.setText("Amount");
        h2.setPadding(15, 15, 15 ,15);
        this.textViewParams(h2);
        header.addView(h2, 1);

        TextView h3 = new TextView(this);
        h3.setText("ID");
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
            row.setClickable(true);
            row.setOnClickListener(this.listen);

            if ((i % 2) == 0)
                row.setBackgroundColor(Color.LTGRAY);

            // Add date
            TextView t1 = new TextView(this);
            t1.setText(rec[i].getUser());
            this.textViewParams(t1);
            row.addView(t1, 0);

            // Add amount
            TextView t2 = new TextView(this);
            t2.setText(String.valueOf(rec[i].getRecord().getAmount()));
            this.textViewParams(t2);
            t2.setGravity(Gravity.RIGHT);
            row.addView(t2, 1);

            // Add category
            TextView t3 = new TextView(this);
            t3.setText(String.valueOf(rec[i].getID()));
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
}
