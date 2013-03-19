package com.example.taximap.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
   private static final String DATABASE_NAME = "TicTacToe.db";
   private static final int DATABASE_VERSION = 1;
   private static final String TABLE_NAME = "Accounts";
   private Context context;
   private SQLiteDatabase db;
   private SQLiteStatement insertStmt;
   private static final String INSERT = "insert into " + TABLE_NAME + "(name, password) values (?, ?)" ;
   
   public DatabaseHelper(Context context) {
      this.context = context;
      TicTacToeOpenHelper openHelper = new TicTacToeOpenHelper(this.context);
      this.db = openHelper.getWritableDatabase();
      this.insertStmt = this.db.compileStatement(INSERT);
   }

   public long insert(String name, String password) {
      this.insertStmt.bindString(1, name);		// bind name to the first value
      this.insertStmt.bindString(2, password);
      return this.insertStmt.executeInsert();
   }
   public void deleteAll() {

      this.db.delete(TABLE_NAME, null, null);
   }
  
   public List<String> selectAll(String username, String password) {
      List<String> list = new ArrayList<String>();
      Cursor cursor = this.db.query(TABLE_NAME, new String[] { "name", "password" }, "name = '"+ username +"' AND password= '"+ password+"'", null, null, null, "name desc");
      if (cursor.moveToFirst()) {
        do {
        	 list.add(cursor.getString(0));
        	 list.add(cursor.getString(1));
         } while (cursor.moveToNext()); 
      }
      if (cursor != null && !cursor.isClosed()) {
         cursor.close();
      }
      return list;
   }
   
   private static class TicTacToeOpenHelper extends SQLiteOpenHelper {
	   TicTacToeOpenHelper(Context context) {
    	  super(context, DATABASE_NAME, null, DATABASE_VERSION);
      }

      @Override
      public void onCreate(SQLiteDatabase db) {
         db.execSQL("CREATE TABLE " + TABLE_NAME + "(id INTEGER PRIMARY KEY, name TEXT, password TEXT)");
      }

      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

         Log.w("Example", "Upgrading database; this will drop and recreate the tables.");
         db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
         onCreate(db);
      }
   }
}
