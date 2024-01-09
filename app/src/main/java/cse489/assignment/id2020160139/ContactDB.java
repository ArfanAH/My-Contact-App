package cse489.assignment.id2020160139;

import static android.opengl.ETC1.encodeImage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ContactDB extends SQLiteOpenHelper {

  public ContactDB(Context context) {
    super(context, "ContactDatabase.db", null, 1);
    //    context.deleteDatabase("ContactDatabase.db");

  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    System.out.println("DB@OnCreate");
    String sql = "CREATE TABLE ContactTable  ("
        + "UniqueID INTEGER PRIMARY KEY,"
        + "name TEXT,"
        + "email TEXT,"
        + "homePhone TEXT,"
        + "officePhone TEXT,"
        + "image TEXT"
        + ")";
    db.execSQL(sql);
  }


  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    System.out.println("Write code to modify database schema here");
    // db.execSQL("ALTER table my_table  ......");
    // db.execSQL("CREATE TABLE  ......");
  }

  public void insertContactDB(Integer UniqueID,String name, String email, String homePhone,String officePhone, String imagePath) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues cols = new ContentValues();
    cols.put("UniqueID", UniqueID);
    cols.put("name", name);
    cols.put("email",email);
    cols.put("homePhone", homePhone);
    cols.put("officePhone", officePhone);
    cols.put("image", imagePath);
    // Add other attributes
    db.insert("ContactTable", null, cols);
    db.close();
  }

  public void updateContactDB(Integer UniqueID, String name, String email, String homePhone, String officePhone) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues cols = new ContentValues();
    cols.put("name", name);
    cols.put("email", email);
    cols.put("homePhone", homePhone);
    cols.put("officePhone", officePhone);
    db.update("ContactTable", cols, "UniqueID=?", new String[]{String.valueOf(UniqueID)});
    db.close();
  }



  public void deleteContactDB(String ID) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete("ContactTable", "UniqueID=?", new String[]{ID});
    db.close();
  }

  public Cursor selectContactDB(String query, String[] selectionArgs) {
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor res = null;
    try {
      res = db.rawQuery(query, selectionArgs);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return res;
  }

  public boolean hasRecord(Integer UniqueID) {
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery("SELECT * FROM ContactTable WHERE UniqueID=?", new String[]{String.valueOf(UniqueID)});
    boolean hasRecord = cursor.getCount() > 0;
    cursor.close();
    return hasRecord;
  }



}
