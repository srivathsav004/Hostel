package com.example.hostel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "Hostel.db";

    public DBHelper(Context context) {
        super(context,DBNAME,null,2);
    }
    @Override
    public void onCreate(SQLiteDatabase MYDB) {
        MYDB.execSQL("CREATE TABLE IF NOT EXISTS admin (username TEXT PRIMARY KEY, name TEXT, password TEXT, hostel TEXT)");
        MYDB.execSQL("CREATE TABLE IF NOT EXISTS student (username TEXT PRIMARY KEY, name TEXT, email TEXT, registration_number TEXT, password TEXT, hostel TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase MYDB, int i, int j) {
        MYDB.execSQL("drop table if exists admin");
        MYDB.execSQL("DROP TABLE IF EXISTS student");
        onCreate(MYDB);
    }
    public Boolean insertAdminData(String username,String name,String password,String hostel)
    {
        SQLiteDatabase MYDB=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("username",username);
        contentValues.put("name", name);
        contentValues.put("password", password);
        contentValues.put("hostel", hostel);

        long result = MYDB.insert("admin", null, contentValues);
        MYDB.close();
        if (result==-1) return false;
        else
            return true;
    }

    public Boolean insertStudentData(String username, String name, String email, String registrationNumber, String password, String hostel) {
        SQLiteDatabase MYDB = this.getWritableDatabase();

        try {
            Log.d("StudentSignupActivity", "insertStudentData called");
            Log.d("StudentSignupActivity", "insertStudentData called with: username=" + username + ", name=" + name + ", email=" + email + ", registrationNumber=" + registrationNumber + ", password=" + password + ", hostel=" + hostel);

            ContentValues contentValues = new ContentValues();
            contentValues.put("username", username);
            contentValues.put("name", name);
            contentValues.put("email", email);
            contentValues.put("registration_number", registrationNumber);
            contentValues.put("password", password);
            contentValues.put("hostel", hostel);

            long result = MYDB.insert("student", null, contentValues);

            if (result != -1) {
                Log.d("StudentSignupActivity", "Data inserted successfully into student table.");
            } else {
                Log.e("StudentSignupActivity", "Failed to insert data into student table.");
            }

            return result != -1;
        } catch (Exception e) {
            Log.e("StudentSignupActivity", "Error in insertStudentData: " + e.getMessage());
            return false;
        } finally {
            MYDB.close();
        }
    }


    public Boolean checkadminnamepassword(String username,String password)
    {
        SQLiteDatabase MYDB=this.getWritableDatabase();
        Cursor cursor=MYDB.rawQuery("select * from admin where username=? and password=?",new String[]{username,password});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }

    public Boolean checkStudentnamepassword(String username,String password)
    {
        SQLiteDatabase MYDB=this.getWritableDatabase();
        Cursor cursor=MYDB.rawQuery("select * from student where username=? and password=?",new String[]{username,password});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }
    public String getHostelForAdminUsername(String username) {
        SQLiteDatabase MYDB = this.getWritableDatabase();
        String[] columns = {"hostel"};
        Cursor cursor = MYDB.query("admin", columns, "username=?", new String[]{username}, null, null, null);
        String hostel = null;

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex("hostel");
            if (columnIndex >= 0) {
                hostel = cursor.getString(columnIndex);
            }
        }
        cursor.close();
        return hostel;
    }

    public String getHostelForStudentUsername(String username) {
        SQLiteDatabase MYDB = this.getWritableDatabase();
        String[] columns = {"hostel"};
        Cursor cursor = MYDB.query("student", columns, "username=?", new String[]{username}, null, null, null);
        String hostel = null;

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex("hostel");
            if (columnIndex >= 0) {
                hostel = cursor.getString(columnIndex);
            }
        }
        cursor.close();
        return hostel;
    }
}
