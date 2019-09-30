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

    public void createDatabase() throws IOException {
        try {

            // check if the database exists
            boolean dbExist = checkDatabase();
            if (!dbExist) {
                // database is not present copy database
                this.getReadableDatabase();
                try {
                    copyDatabase();
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

    private void copyDatabase() throws IOException {
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
            Log.e("exploit", "database created..");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public SQLiteDatabase openDataBase() throws SQLException
    {
        //Log.v("mPath", mPath);
        return SQLiteDatabase.openDatabase(DatabasePath + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
        //mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }


}
