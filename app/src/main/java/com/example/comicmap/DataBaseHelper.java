package com.example.comicmap;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataBaseHelper extends SQLiteOpenHelper {
    // varaible declaration
    public static String TAG = "DatabaseHelper";
    private static String DB_NAME = "comike_location.db";//file name in assets
    private SQLiteDatabase sqliteDatabase;
    private final Context myContext;
    private String DatabasePath;

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
        DatabasePath = "/data/data/" + myContext.getPackageName() + "/databases/";
    }

    /**
     * Method to create the database inside application
     *
     * @throws IOException
     */
    private void createDatabase() throws IOException {
        try {

            // check if the database exists
            boolean dbExist = checkDatabase();
            if (!dbExist) {
                // database is not present copy databse
                this.getReadableDatabase();
                try {
                    copyDatabse();
                } catch (IOException e) {
                    // TODO: handle exception
                    // String ex = e.getMessage();
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each
     * time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDatabase() {
        SQLiteDatabase checkDB = null;
        try {
            try {
                File file = new File(DatabasePath + DB_NAME);
                if (file.exists()) {
                    checkDB = SQLiteDatabase.openDatabase(DatabasePath
                            + DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
                } else {
                    return false;
                }
            } catch (SQLException e) {
                Log.d("exploit", "DB exception");
            }
            if (checkDB != null) {
                checkDB.close();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return checkDB != null;
    }

    /**
     * Copies your database from your local assets-folder to the just created
     * empty database in the system folder, from where it can be accessed and
     * handled. This is done by tranfering bytestream.
     * */
    private void copyDatabse() throws IOException {
        try {
            // Open your local db as the input stream
            InputStream myInput = myContext.getAssets().open(DB_NAME);
            // Path to the just created empty db
            String outFileName = DatabasePath + DB_NAME;
            // Open the empty db as the output stream

            OutputStream myOutput = new FileOutputStream(outFileName);
            // transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024 * 2];
            int length;

            while ((length = myInput.read(buffer)) > 0) {
                try {
                    myOutput.write(buffer, 0, length);
                } catch (Exception e) {  }
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        try {
            createDatabase();
        } catch (IOException e) { e.printStackTrace(); }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

/**
 * Other Methods to insert,delete, update,select in Database
 **/

}
