package com.example.theguardian;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Class that sets up database to store all the Guardian articles that are saved in favorites
 * @author Emrah Kinay
 * @version 1.0
 */
public class TheGuardianDbOpener extends SQLiteOpenHelper {

    /**
     * Database name
     */
    protected final static String DATABASE_NAME = "TheGuardian";

    /**
     * Database version number
     */
    protected final static int VERSION_NUM = 4;

    /**
     * Table name for The Guardian articles
     */
    public final static String TABLE_NAME = "GUARDIAN_ARTICLES";

    /**
     * Column name for article id
     */
    public final static String COL_ID = "_id";
    /**
     * Column name for article title
     */
    public final static String COL_TITLE = "TITLE";

    /**
     * Column name fora\ article  url
     */
    public final static String COL_URL = "URL";

    /**
     * Column name for article section name
     */
    public final static String COL_SECTION_NAME = "SECTION_NAME";

    /**
     * Column name for article text
     */
    public final static String COL_BODY_TEXT = "BODY_TEXT";

    /**
     * Column name for article date
     */
    public final static String COL_DATE = "DATE";

    /**
     * Column name for article image url
     */
    public final static String COL_IMAGE_URL = "IMAGE_URL";

    /**
     * Parameterized constructor that creates database object with the database name and version number
     * @param ctx
     */
    public TheGuardianDbOpener(Context ctx){
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    /**
     * Takes an SQLiteDatabase object and creates tables if there is no column in the database
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + COL_ID +  " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TITLE + " TEXT,"
                + COL_URL  + " TEXT,"
                + COL_SECTION_NAME  + " TEXT,"
                + COL_BODY_TEXT + " TEXT,"
                + COL_IMAGE_URL + " TEXT,"
                + COL_DATE  + " TEXT);");
    }

    /**
     * Takes a database object and two integer numbers, upgrades database if the given number for new version is greater than the given number for old version
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * Takes a database object and two integer numbers, downgrades database if the given number for new version is less than the given number for old version
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
