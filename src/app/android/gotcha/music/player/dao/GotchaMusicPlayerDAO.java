package app.android.gotcha.music.player.dao;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import app.android.gotcha.music.player.constants.GotchaMusicPlayerConstants.CONFIG_VALUE_FROM_DATABASE;
import app.android.gotcha.music.player.util.OpenHelper;

public class GotchaMusicPlayerDAO {
	 private static final String DATABASE_NAME = "gotchaconfig.db";
	   private static final int DATABASE_VERSION = 1;
	   private static final String TABLE_NAME = "gotchaconfigtable";
	   private Context context;
	   private SQLiteDatabase db;
	   private SQLiteStatement insertStmt;
	   private static final String INSERT = "insert into " + TABLE_NAME + "(configurationkey, configurationvalue) values (?,?)";
	   public GotchaMusicPlayerDAO(Context context) {
	      this.context = context;
	      OpenHelper openHelper = new OpenHelper(this.context, DATABASE_NAME, DATABASE_VERSION, TABLE_NAME);
	      try
	      {
	    	  this.db = openHelper.getWritableDatabase();
	          this.insertStmt = this.db.compileStatement(INSERT);
	      }
	      catch(Exception ex)
	      {
	    	  Log.e("GotchaMusicPlayerDAO","Exception in constructor "+ex.toString());
	    	  ex.printStackTrace();
	      }
	     
	   }
	   public boolean setConfigurationValue(CONFIG_VALUE_FROM_DATABASE configKey, String value) {
		   boolean isSuccess = false;
		   long newRowId = -1;
		   String configurationValue = getConfigurationValue(configKey);
		   if("".equals(configurationValue)) {
			   Log.i("GotchaMusicPlayerDAO", "Inside Insertion block. First time msg");
			   this.insertStmt.bindString(1, configKey.toString());
			   this.insertStmt.bindString(2, value);
			   newRowId = this.insertStmt.executeInsert();
		   } else {
			   Log.i("GotchaMusicPlayerDAO", "Inside Update block.");
			   ContentValues myValues=new ContentValues();
			   myValues.put("configurationvalue", value);
			   newRowId = this.db.update(TABLE_NAME, myValues, "configurationkey = ?", new String[]{configKey.toString()});
		   }
		   isSuccess = newRowId != -1;	// -1 indicates insertion failed		
		   return isSuccess;		   
	   }
	   
	   public String getConfigurationValue(CONFIG_VALUE_FROM_DATABASE configurationkey){
		   String configurationValue = "";
		   try {
			      Cursor cursor = this.db.query(TABLE_NAME, new String[] { "configurationvalue" },
			      "configurationkey = ?", new String[] {configurationkey.toString()}, null, null, "id asc");
			      if (cursor.moveToFirst()) {
			         do {
			        	 configurationValue = cursor.getString(0);// parameterIndex starts at 0
			        	
			         } while (cursor.moveToNext());
			      }
			      if (cursor != null && !cursor.isClosed()) {
			         cursor.close();
			      }
			     } catch(Exception ex) {
			    	 Log.w("GotchaMusicPlayerDAO", ex.fillInStackTrace());
			     }
		   return configurationValue;
	   }
	   
	   public Map<CONFIG_VALUE_FROM_DATABASE, String> getAllconfigurationValues() {
		   Map<CONFIG_VALUE_FROM_DATABASE, String> configurations = new  HashMap<CONFIG_VALUE_FROM_DATABASE, String>();
		   try {
			      Cursor cursor = this.db.query(TABLE_NAME, new String[] { "configurationkey","configurationvalue" },
			      null, null, null, null, "id asc");
			      if (cursor.moveToFirst()) {
			         do {
			        	 String configurationKey = cursor.getString(0);// parameterIndex starts at 0
			        	 String configurationValue = cursor.getString(1);
			        	 configurations.put(CONFIG_VALUE_FROM_DATABASE.valueOf(configurationKey), configurationValue);
			         } while (cursor.moveToNext());
			      }
			      if (cursor != null && !cursor.isClosed()) {
			         cursor.close();
			      }
			     } catch(Exception ex) {
			    	 Log.w("GotchaMusicPlayerDAO", ex.fillInStackTrace());
			     }
		   return configurations;
	   }
	   
	   
}
