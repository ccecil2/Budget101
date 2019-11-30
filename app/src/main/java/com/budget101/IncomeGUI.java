package com.budget101;

import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.budget101.Data.Category;
import com.budget101.Data.Income;
import com.budget101.Data.Enum.Money;
import com.budget101.Database.DatabaseAccess;
import java.util.ArrayList;

import static com.budget101.Data.Enum.Money.INCOME;


/*
 * Displays the information needed from the user
 * to input income.
 */
public class IncomeGUI extends MainActivity {
    // GUI components used from activity XML
    private DatabaseAccess access; //use this to get access to categories
    private TextView txtNote;
    private TextView txtAmount;
    private TextView txtDate;
    private Spinner spin;

    Income currentIncome;
    Category currentCat;
    Category[] incomeCat;
    Category[] categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_gui);

        this.txtNote = findViewById(R.id.note_txt);
        this.txtAmount = findViewById(R.id.amount_txt);
        this.txtDate = findViewById(R.id.date_txt);
        this.spin = findViewById(R.id.spinner);

        this.addListeners(); // Add action listeners

        //this.money = Money.EXPENSE;
        this.access = this.getDatabase(); // Get database object
        this.populateCategories(); // Fill spinner
    }


    /*
     * Add listeners to components. don't need this
     */
    private void addListeners() {


        this.spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                int slot = parent.getSelectedItemPosition();

                if(slot > 0)
                    currentCat = incomeCat[slot - 1];
                else
                    currentCat = null;

                //populateForm(currentCat);
            }

            public void onNothingSelected(AdapterView<?> parent)
            {
                currentCat = null;
                //populateFormEmpty();
            }
        });
    }


    /*
     * Populate the spinner with categories.
     */
    private void populateCategories()
    {
        this.categories = this.access.getAllCategories(); // Get categories
        ArrayList<String> list = new ArrayList<String>(10);
        //list.add("New"); // Add black entry

        for(int i = 0; i < this.categories.length; i++)
        {
            //shows only expense categories in spinner
            if(this.categories[i].getMoney().equals(INCOME)) {
                this.incomeCat=this.categories;
                list.add(this.categories[i].getName()); // Add names to list
            }
        }

        ArrayAdapter<String> ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        this.spin.setAdapter(ad);
    }



    /*
     * Verifies monetary format.
     * @param str String to check
     * @return True if of correct format, else, false//no need
     *//*
    public boolean regex(String str)
    {
        String valid = "([1-9]\\d{0,2})|(([1-9]\\d*)?\\d)(\\.\\d\\d)?$"; // Valid regex
        return str.matches(valid);
    }*/


    /*
     * Take information from elements and create an expense.
     *
     * @param view
     */
    public void saveExpense(View view)
    {
        //String name = ((TextView) findViewById(R.id.category_text)).getText().toString();
        //String limitText = ((TextView) findViewById(R.id.limitText)).getText().toString();
        String name = currentCat.getName().toString();
        double limit = currentCat.getLimit();
        String noteText = ((TextView) findViewById(R.id.note_txt)).getText().toString();
        String amountText = ((TextView) findViewById(R.id.amount_txt)).getText().toString();
        String dateText = ((TextView) findViewById(R.id.date_txt)).getText().toString();
        double amount;
        boolean suc;

        if(this.checkInputs(amountText, dateText)) // Verify input
        {
            amount = Double.parseDouble(amountText);

            Toast t = Toast.makeText(this, name + " saved", Toast.LENGTH_SHORT);
            t.show();

            this.finish(); // Close activity
        }
    }



    /*
     * Verifies input in components.
     * @param amount_txt
     * @param date_txt
     * @return True if inputs pass requirements
     */
    public boolean checkInputs(String amount_txt, String date_txt)
    {

        if (date_txt.equals(""))
        {
            Toast t = Toast.makeText(this, "No date entered", Toast.LENGTH_LONG);
            t.show();
            return false;
        }
        if (amount_txt.equals(""))
        {
            Toast t = Toast.makeText(this, "No expense amount", Toast.LENGTH_LONG);
            t.show();
            return false;
        }




        return true; // Verified inputs
    }
}