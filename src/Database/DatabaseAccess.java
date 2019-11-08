package budget101.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import budget101.Data.Category;
import budget101.Data.Enum.Money;
import budget101.Data.Enum.Status;
import budget101.Data.Record;
import budget101.Data.Split;
import budget101.Data.User;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;

/**
 * Used to access database budget.db.
 */
public final class DatabaseAccess {
    private SQLiteDatabase db;
    private DatabaseHelper dataHelper;


    /**
     * Contains connection and functionality with budget.db.
     * Close connection with close() when finished.
     */
    public DatabaseAccess(Context context) {
        this.dataHelper = new DatabaseHelper(context);
        this.db = this.dataHelper.getWritableDatabase();
    }


    /**
     * Close the database.
     */
    public void close() {
        this.dataHelper.close();
    }


    /**
     * Set the SQLite database object.
     *
     * @param data - Sets the database object
     */
    public void setDatabaseCon(SQLiteDatabase data) {
        this.db = data;
    }


    /**
     * Saves passed input to database.
     * On success, a new Record object will be returned
     * or null if failed to save.
     *
     * @param amount  - Dollar amount
     * @param comment - User comment
     * @param date    - Date of record
     * @param cat     - Category for record
     * @return New Record object or null if failed
     */
    public Record newRecord(double amount, String comment, Date date, Category cat) {
        ContentValues insert = new ContentValues();
        insert.put("amount", amount);
        insert.put("comment", comment);
        insert.put("date", date.getTime());
        insert.put("FK_Category", cat.getID());

        int r = (int) this.db.insert("Record", null, insert);
        Record rec;

        if (r == -1)
            rec = null;
        else
            rec = new Record(r, amount, cat, date, comment);

        return rec;
    }


    /**
     * Saves passed input to database.
     * On success, a new Category object will be returned
     * or null if failed to save.
     *
     * @param name  - Name of category
     * @param alarm - Alarm on/off
     * @param m     - Money object
     * @return New Category object or null if failed
     */
    public Category newCategory(String name, boolean alarm, double limit, Money m) {
        ContentValues insert = new ContentValues();
        insert.put("name", name);
        insert.put("alarm", Category.getAlarmInt(alarm));
        insert.put("max", limit);
        insert.put("type", Money.toInt(m));

        int r = (int) this.db.insert("Category", null, insert);
        Category c;

        if (r == -1)
            c = null;
        else
            c = new Category(r, name, alarm, limit, m);

        return c;
    }


    /**
     * Saves passed input to database.
     * On success, a new Category object will be returned
     * or null if failed to save.
     * Pass null to image if no image is to be saved.
     *
     * @param name     - Username
     * @param password - Hashed password
     * @param image    - Bitmap image or null
     * @return New User object or null if failed
     */
    public User newUser(String name, String password, Bitmap image)
    {
        ContentValues insert = new ContentValues();
        insert.put("userName", name);
        insert.put("password", password);
        if(image != null) // Allow no image
            insert.put("picture", this.toByteArray(image));

        int r = (int) this.db.insert("User", null, insert);
        User u;

        if (r == -1)
            u = null;
        else
            u = new User(name, password, image);

        return u;
    }


    /**
     * Converts a Bitmap into a byte[].
     *
     * @param image - Bitmap to convert
     * @return byte[] of bitmap
     */
    private byte[] toByteArray(Bitmap image) {
        if (image == null)
            return null;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }


    /**
     * Converts a byte[] into a Bitmap object.
     *
     * @param image - Array to convert
     * @return Bitmap object or null
     */
    private Bitmap toBitmap(byte[] image)
    {
        if(image == null || image.length == 0)
            return null;

        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }


    /**
     * Returns an array of all categories in the database.
     * Array will be zero length if no categories are in
     * the database.
     *
     * @return Array of category objects
     */
    public Category[] getAllCategories() {
        String[] col = {"ID", "name", "alarm", "max", "type"};

        Cursor cursor = this.db.query("Category", col, null, null, null, null, null, null);

        Category[] cat = new Category[cursor.getCount()]; // Get row count
        for (int index = 0; cursor.moveToNext(); index++)
        {
            cat[index] = new Category(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getDouble(3), cursor.getInt(4));
        }

        cursor.close();

        return cat;
    }


    /**
     * Checks database for given username and returns
     * a filled User object if found. Null if not found.
     * @param username - Username to check
     * @return User object or null
     */
    public User getUser(String username)
    {
        if(username == null || username.equals(""))
            return null;

        String[] col = {"userName", "password", "picture"};
        String select = "userName = ?";
        String[] args = {username};
        Cursor cursor = this.db.query("User", col, select, args, null, null, null);

        User user = null;
        if(cursor.moveToFirst())
        {
            user = new User(cursor.getString(0), cursor.getString(1), toBitmap(cursor.getBlob(2)));
        }

        cursor.close();

        return user;
    }


    /**
     * Returns a record object with given ID.
     * Returns null if ID is not found.
     * @param ID - ID of record
     * @return Record object or null
     */
    public Record getRecord(int ID)
    {
        String[] col = {"ID", "amount", "comment", "date", "FK_Category"};
        String sel = "ID = ?";
        String[] args = {Integer.toString(ID)};

        Cursor cursor = this.db.query("Record", col, sel, args, null, null, null);

        Record r = null;
        if(cursor.moveToFirst())
        {
            Category c = this.getCategory(cursor.getInt(4)); // Get category with ID from database
            Date d = new Date(cursor.getLong(3)); // Time is long

            r = new Record(cursor.getInt(0), cursor.getDouble(1), c, d, cursor.getString(2));
        }

        cursor.close();

        return r;
    }


    /**
     * Returns all records in month and year of given variables.
     * Month uses 0-11 for Jan-Dec.
     * Will return an array with no elements if no
     * records are found.
     * @param month - Month of records needed
     * @param year - Year of records needed
     * @return - Array of Record objects in month, may be empty
     */
    public Record[] getRecordByDate(int month, int year)
    {
        // Make calendars
        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        // Set first date
        cal.set(year, month, 1, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);

        // Set second date
        cal2.set(year, month + 1, 1, 0, 0, 0);
        cal2.set(Calendar.MILLISECOND, 0);

        Date d1 = cal.getTime();
        Date d2 = cal2.getTime();

        return getRecordByDate(cal.getTimeInMillis(), cal2.getTimeInMillis()); // Get records
    }


    /**
     * Returns all records between t1 and t2 as [t1, t2).
     * @param t1 - First of current month
     * @param t2 - First of next month
     * @return Array of Record objects
     */
    private Record[] getRecordByDate(long t1, long t2)
    {
        String[] col = {"ID", "amount", "comment", "date", "FK_Category"};
        String sel = "date >= ? AND date < ?";
        String[] args = {Long.toString(t1), Long.toString(t2)};

        Cursor cursor = this.db.query("Record", col, sel, args, null, null, "date ASC", null);

        Record[] recs = new Record[cursor.getCount()]; // Set to row count
        for(int i = 0; cursor.moveToNext(); i++)
        {
            Category c = this.getCategory(cursor.getInt(4)); // Get category with ID from database
            Date d = new Date(cursor.getLong(3)); // Time is long

            recs[i] = new Record(cursor.getInt(0), cursor.getDouble(1), c, d, cursor.getString(2));
        }

        cursor.close();

        return recs;
    }


    /**
     * Returns a category with given ID.
     * Null if ID is not found.
     * @param ID - ID of category
     * @return - Category object or null
     */
    public Category getCategory(int ID)
    {
        String[] col = {"ID", "name", "alarm", "max", "type"};
        String selection = "ID = ?";
        String[] args = {Integer.toString(ID)};

        Cursor cursor = this.db.query("Category", col, selection, args, null, null, null, null);

        Category cat;
        if(cursor.moveToFirst())
        {
            cat = new Category(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getDouble(3), cursor.getInt(4));
        }
        else
            cat = null;

        cursor.close();

        return cat;
    }


    /**
     * Creates a new split record in the database.
     * Record must already be in the database and not null.
     * Status is set to Status.PENDING
     * @param user - Name of other user in split
     * @param record - Record tied to split
     * @return - Split object or null
     */
    public Split newSplit(String user, Record record)
    {ContentValues insert = new ContentValues();
        insert.put("otherUser", user);
        insert.put("status", Status.toInt(Status.PENDING));
        insert.put("FK_Record", record.getID());

        int r = (int)this.db.insert("Split", null, insert);
        Split split;

        if(r == -1)
            split = null;
        else
            split = new Split(r, user, Status.PENDING, record);

        return split;
    }


    // Get split based on month, status, ID, or all splits

    /**
     * Returns all splits that have pending status.
     * @return Array of splits
     */
    public Split[] getPendingSplits()
    {
        String[] col = {"ID", "otherUser", "status", "FK_Record"};
        String selection = "status = ?";
        String[] args = {Integer.toString(Status.toInt(Status.PENDING))}; // Find pending status only

        Cursor cursor = this.db.query("Split", col, selection, args, null, null, null, null);

        Split[] splits = new Split[cursor.getCount()];

        for(int i = 0; cursor.moveToNext(); i++)
        {
            Record r = this.getRecord(cursor.getInt(3));
            Status s = Status.toStatus(cursor.getInt(2));

            splits[i] = new Split(cursor.getInt(0), cursor.getString(1), s, r);
        }

        cursor.close();

        return splits;
    }


    /**
     * Updates split status with status in object.
     * @param split - Split to update
     * @return Boolean on success of update
     */
    public boolean updateSplitStatus(Split split)
    {
        if(split == null)
            return false;

        ContentValues vals = new ContentValues();
        vals.put("status", Status.toInt(split.getStatus()));
        String[] args = {Integer.toString(split.getID())};

        int r = this.db.update("Split", vals, "ID = ?", args);
        boolean suc;

        if(r == 1) // One updated row
            suc = true;
        else
            suc = false;

        return suc;
    }


    /**
     * Updates the user's password and picture from given object.
     * @param user - User to update
     * @return Boolean on success of update
     */
    public boolean updateUser(User user)
    {
        if(user == null)
            return false;

        ContentValues vals = new ContentValues();
        vals.put("password", user.getPassword());
        if(user.getPicture() != null)
            vals.put("picture", toByteArray(user.getPicture()));

        String[] args = {user.getName()};

        int r = this.db.update("User", vals, "userName = ?", args);
        boolean suc;

        if(r == 1) // One updated row
            suc = true;
        else
            suc = false;

        return suc;
    }


    /**
     * Updates the alarm and limit for given category object.
     * @param cat - Category to update
     * @return Boolean on success of update
     */
    public boolean updateCategory(Category cat)
    {
        if(cat == null)
            return false;

        ContentValues vals = new ContentValues();
        vals.put("alarm", Category.getAlarmInt(cat.isAlarm()));
        vals.put("max", cat.getLimit());

        String[] args = {Integer.toString(cat.getID())};

        int r = this.db.update("Category", vals, "ID = ?", args);
        boolean suc;

        if(r == 1) // One updated row
            suc = true;
        else
            suc = false;

        return suc;
    }
}
