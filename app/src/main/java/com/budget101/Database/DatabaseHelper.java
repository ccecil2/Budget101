package com.budget101.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Used to access database budget.db.
 */
public final class DatabaseHelper extends SQLiteOpenHelper
{
    private static final String dbName = "budget.db";
    private static final int dbVersion = 1;

    public DatabaseHelper(Context context)
    {
        super(context, dbName, null, dbVersion);
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String createUser = "CREATE TABLE IF NOT EXISTS User(" +
                "userName VARCHAR(20) PRIMARY KEY, " +
                "password VARCHAR(20) NOT NULL, " +
                "picture BLOB);";

        String createCategory = "CREATE TABLE IF NOT EXISTS Category(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name VARCHAR(20) NOT NULL, " +
                "max DOUBLE DEFAULT 0.0, " +
                "alarm INTEGER NOT NULL, " +
                "type INTEGER NOT NULL);";

        String createRecord = "CREATE TABLE IF NOT EXISTS Record(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "amount DOUBLE NOT NULL, " +
                "comment VARCHAR(50), " +
                "date INTEGER NOT NULL, " +
                "FK_Category INTEGER, " +
                "FOREIGN KEY(FK_Category) REFERENCES Category(ID) " +
                "ON UPDATE CASCADE " +
                "ON DELETE SET NULL);";

        String createSplit = "CREATE TABLE IF NOT EXISTS Split(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "otherUser VARCHAR(20), " +
                "status INTEGER NOT NULL, " +
                "FK_Record INTEGER NOT NULL, " +
                "FOREIGN KEY(FK_Record) REFERENCES Record(ID) " +
                "ON UPDATE CASCADE " +
                "ON DELETE SET NULL);";

        // Make tables
        db.execSQL(createUser);
        db.execSQL(createCategory);
        db.execSQL(createRecord);
        db.execSQL(createSplit);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE User");
        db.execSQL("DROP TABLE Split");
        db.execSQL("DROP TABLE Record");
        db.execSQL("DROP TABLE Category");
        onCreate(db);
    }
}
