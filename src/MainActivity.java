package budget101;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.Toast;

import budget101.Data.Category;
import budget101.Data.Enum.Money;
import budget101.Data.Enum.Status;
import budget101.Data.Record;
import budget101.Data.Split;
import budget101.Data.User;
import budget101.Database.DatabaseAccess;

import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity
{
    private DatabaseAccess access;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        access = new DatabaseAccess(this);

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
        Record[] r = access.getRecordByDate(10, 2019); // Nov/2019
        Category[] c = access.getAllCategories();
        Split[] s = access.getPendingSplits();


        // Make Splits
        /*Split s1 = access.newSplit("Gary", r[0]);
        Split s2 = access.newSplit("Ed", r[1]);
        Split s3 = access.newSplit("Work", r[3]);*/
    }


    /**
     * Returns database connection object.
     * @return DatabaseAccess object
     */
    public DatabaseAccess getDatabase()
    {
        return this.access;
    }

    public void sendMessage(View view)
    {
        Intent intent = new Intent(this, CategoryGUI.class);
        startActivity(intent);
    }
}
