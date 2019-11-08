package budget101;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import budget101.Data.Category;
import budget101.Data.Enum.Money;
import budget101.Database.DatabaseAccess;


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

    // Locals to save
    private Money money;
    private boolean alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_gui);

        this.RGroup = findViewById(R.id.radioGroup);
        this.txtLimit = findViewById(R.id.limitText);
        this.RGroup2 = findViewById(R.id.radiogroup2);

        this.addListeners(); // Add action listeners

        this.money = Money.EXPENSE;
        this.access = this.getDatabase(); // Get database object
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

        if(this.checkInputs(name, limitText)) // Verify input
        {
            if (limitText.equals(""))
                limit = 0.0;
            else
                limit = Double.parseDouble(limitText);

            Category c = access.newCategory(name, alarm, limit, money); // Save to database

            if (c != null) {
                Toast t = Toast.makeText(this, name + " saved", Toast.LENGTH_SHORT);
                t.show();
            } else // Something failed
            {
                Toast t = Toast.makeText(this, name + " not saved", Toast.LENGTH_SHORT);
                t.show();
            }

            this.finish(); // Close activity
        }
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
