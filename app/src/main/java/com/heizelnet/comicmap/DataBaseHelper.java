package com.heizelnet.comicmap;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static String TAG = "DatabaseHelper";
    private String DB_PATH;
    private static String DB_NAME = "comike_location.db";//file name in assets
    private SQLiteDatabase myDataBase;
    private final Context myContext;

    public DataBaseHelper(Context context) throws IOException{
        super(context, DB_NAME, null, 1);
        this.myContext = context;
        DB_PATH = myContext.getApplicationInfo().dataDir + "/databases/";

        boolean dbexist = checkDataBase();
        if (!dbexist) {
            createDataBase();
        }
    }

    public void createDataBase() throws IOException {
            this.getReadableDatabase();
            this.close();
            try {
                copyDataBase();
                Logger.e("exploit", "Database created!");
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
    }

    private boolean checkDataBase() {
        boolean checkdb = false;
        try {
            String myPath = DB_PATH + DB_NAME;
            File dbfile = new File(myPath);
            checkdb = dbfile.exists();
        } catch (SQLiteException e) {
            Logger.e("exploit", "Database doesn't exist");
        }
        return checkdb;
    }

    private void copyDataBase() throws IOException {
        // Open your local db as the input stream
        InputStream myinput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outfilename = DB_PATH + DB_NAME;

        // Open the empty db as the output stream
        OutputStream myoutput = new FileOutputStream(DB_PATH + DB_NAME);

        // transfer byte to inputfile to outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myinput.read(buffer)) > 0) {
            myoutput.write(buffer, 0, length);
        }

        // Close the streams
        myoutput.flush();
        myoutput.close();
        myinput.close();
    }

    public SQLiteDatabase openDataBase() throws SQLException {
        // Open the database
        String mypath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);
        return myDataBase;
    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();

        super.close();
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
