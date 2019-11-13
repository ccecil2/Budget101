package budget101;

import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import budget101.Data.Category;
import budget101.Data.Enum.Money;
import budget101.Database.DatabaseAccess;
import java.util.ArrayList;


/**
 * Displays the information needed from the user
 * to create a new category.
 */
public class CategoryGUI extends MainActivity {
    // GUI components used from activity XML
    private DatabaseAccess access;
    private RadioGroup RGroup;
    private TextView txtLimit;
    private RadioGroup RGroup2;
    private Spinner spin;

    // Locals to save
    private Money money;
    private boolean alarm;
    Category[] categories;
    Category currentCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_gui);

        this.RGroup = findViewById(R.id.radioGroup);
        this.txtLimit = findViewById(R.id.limitText);
        this.RGroup2 = findViewById(R.id.radiogroup2);
        this.spin = findViewById(R.id.spinner);

        this.addListeners(); // Add action listeners

        this.money = Money.EXPENSE;
        this.access = this.getDatabase(); // Get database object
        this.populateCategories(); // Fill spinner
    }


    /**
     * Add listeners to components.
     */
    private void addListeners() {
        this.RGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedID) {
                switch (checkedID) {
                    case R.id.radioOn:
                        txtLimit.setEnabled(true);
                        alarm = true;
                        break;
                    case R.id.radioOff:
                        txtLimit.setEnabled(false);
                        txtLimit.setText("");
                        alarm = false;
                        break;
                }
            }
        });


        this.RGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedID) {
                switch (checkedID) // Save type of enum: INCOME/EXPENSE
                {
                    case R.id.radioIncome:
                        money = Money.INCOME;
                        RadioButton alarm = findViewById(R.id.radioOff);
                        alarm.setChecked(true); // Set alarm to off
                        break;
                    case R.id.radioExpense:
                        money = Money.EXPENSE;
                        break;
                }
            }
        });

        this.spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                int slot = parent.getSelectedItemPosition();

                if(slot > 0)
                    currentCat = categories[slot - 1];
                else
                    currentCat = null;

                populateForm(currentCat);
            }

            public void onNothingSelected(AdapterView<?> parent)
            {
                currentCat = null;
                populateFormEmpty();
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
        list.add("New"); // Add black entry

        for(int i = 0; i < this.categories.length; i++)
        {
            list.add(this.categories[i].getName()); // Add names to list
        }

        ArrayAdapter<String> ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        this.spin.setAdapter(ad);
    }


    /**
     * Sets the form to match category passed.
     * @param cat Values to fill form with
     */
    private void populateForm(Category cat)
    {
        if(cat == null)
            this.populateFormEmpty();
        else
        {
            ((TextView) findViewById(R.id.category_text)).setText(cat.getName());
            ((TextView) findViewById(R.id.limitText)).setText(String.valueOf(cat.getLimit()));
            RadioButton radIncome;
            RadioButton radAlarm;

            if(cat.getMoney() == Money.INCOME)
            {
                radIncome = findViewById(R.id.radioIncome);
                radIncome.setChecked(true);
            }
            else
            {
                radIncome = findViewById(R.id.radioExpense);
                radIncome.setChecked(true);
            }

            if(cat.isAlarm())
            {
                radAlarm = findViewById(R.id.radioOn);
                radAlarm.setChecked(true);
            }
            else
            {
                radAlarm = findViewById(R.id.radioOff);
                radAlarm.setChecked(true);
            }

            ((RadioButton)findViewById(R.id.radioIncome)).setEnabled(false);
            ((RadioButton)findViewById(R.id.radioExpense)).setEnabled(false);
            ((TextView)findViewById(R.id.category_text)).setEnabled(false);
        }
    }


    /**
     * Sets the form to default values.
     */
    private void populateFormEmpty()
    {
        ((RadioButton)findViewById(R.id.radioIncome)).setEnabled(true);
        ((RadioButton)findViewById(R.id.radioExpense)).setEnabled(true);
        ((TextView)findViewById(R.id.category_text)).setEnabled(true);
        ((TextView) findViewById(R.id.category_text)).setText("");
        ((TextView) findViewById(R.id.limitText)).setText("");
        ((RadioButton) findViewById(R.id.radioExpense)).setChecked(true);
        ((RadioButton) findViewById(R.id.radioOn)).setChecked(true);
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
     * Take information from elements and create a category.
     *
     * @param view
     */
    public void saveCategory(View view)
    {
        String name = ((TextView) findViewById(R.id.category_text)).getText().toString();
        String limitText = ((TextView) findViewById(R.id.limitText)).getText().toString();
        double limit;
        boolean suc;

        if(this.checkInputs(name, limitText)) // Verify input
        {
            if (limitText.equals(""))
                limit = 0.0;
            else
                limit = Double.parseDouble(limitText);

            if(this.currentCat == null) // Will be null if "New" is selected
                suc = this.newCategory(name, limit);
            else
                suc = this.updateCategory(limit);

            if (suc)
            {
                Toast t = Toast.makeText(this, name + " saved", Toast.LENGTH_SHORT);
                t.show();
            }
            else // Something failed
            {
                Toast t = Toast.makeText(this, name + " not saved", Toast.LENGTH_SHORT);
                t.show();
            }

            this.finish(); // Close activity
        }
    }


    /**
     * Creates a new category.
     * @param name Name of category
     * @param limit Limit of category
     * @return Success of operation
     */
    private boolean newCategory(String name, double limit)
    {
        Category c = this.access.newCategory(name, alarm, limit, money); // Save to database

        if(c == null)
            return false;
        else
            return true;
    }


    /**
     * Updates the category to the database.
     * @param limit New limit to set
     * @return Success of operation
     */
    private boolean updateCategory(double limit)
    {
        this.currentCat.setLimit(limit); // Update limit

        if(((RadioButton)findViewById(R.id.radioOn)).isChecked()) // Update alarm
            this.currentCat.setAlarm(true);
        else
            this.currentCat.setAlarm(false);

        boolean suc = this.access.updateCategory(this.currentCat);

        return suc;
    }


    /**
     * Verifies input in components.
     * @param name Name of category
     * @param limit Text for limit amount
     * @return True if inputs pass requirements
     */
    public boolean checkInputs(String name, String limit)
    {
        // Check category name
        if (name.length() == 0)
        {
            Toast t = Toast.makeText(this, "No category name", Toast.LENGTH_LONG);
            t.show();
            return false;
        }


        // Check limit format
        RadioButton buttonOn = findViewById(R.id.radioOn);
        if(buttonOn.isChecked() && !this.regex(limit))
        {
            Toast t = Toast.makeText(this, "Invalid input for limit", Toast.LENGTH_LONG);
            t.show();
            return false;
        }


        return true; // Verified inputs
    }
}
