package com.budget101;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.budget101.Data.Enum.Money;
import com.budget101.Data.Record;
import com.budget101.Data.Split;
import com.budget101.Database.DatabaseAccess;

import java.util.Calendar;
import java.util.Date;

public class NewSplit extends SplitGUI
{
    private DatabaseAccess access;
    private View.OnClickListener listen;
    private Record currentRec;
    private TableLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_split);

        this.access = super.getDatabase();
        this.layout = (TableLayout)findViewById(R.id.tablelayout);
        addListener();
        this.searchLogs();
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
    }


    /**
     * Create a new split.
     * @param view
     */
    public void createSplit(View view)
    {
        String name = ((TextView)findViewById(R.id.txtName)).getText().toString();

        // Check inputs
        if(checkInputs(name))
        {
            Split s = this.access.newSplit(name, this.currentRec);

            if (s != null)
            {
                Toast t = Toast.makeText(this, "Split saved", Toast.LENGTH_SHORT);
                t.show();
            }
            else // Something failed
            {
                Toast t = Toast.makeText(this, "Split not saved", Toast.LENGTH_SHORT);
                t.show();
            }

            this.finish(); // Finish
        }
    }


    /**
     * Check inputs for validity.
     * @param name Name of user
     * @return If inputs match format
     */
    private boolean checkInputs(String name)
    {
        if(name.length() == 0)
        {
            Toast t = Toast.makeText(this, "Name is needed", Toast.LENGTH_LONG);
            t.show();
            return false;
        }

        if(this.currentRec == null)
        {
            Toast t = Toast.makeText(this, "Record must be selected", Toast.LENGTH_LONG);
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
        this.currentRec = this.access.getRecord(id);

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
        Date d = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int month = c.get(Calendar.MONTH); // Get month
        int year = c.get(Calendar.YEAR); // Get year

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
        h3.setText("ID");
        this.textViewParams(h3);
        h3.setPadding(15, 15, 15 ,15);
        header.addView(h3, 2);

        layout.addView(header);

        // Fill table layout
        for(int i = rec.length - 1; i >= 0; i--)
        {
            if(rec[i].getCategory().getMoney() == Money.EXPENSE) // Only include expenses
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
                t3.setText(String.valueOf(rec[i].getID()));
                this.textViewParams(t3);
                row.addView(t3, 2);

                layout.addView(row);
            }
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
