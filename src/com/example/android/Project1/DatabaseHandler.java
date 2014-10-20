package com.example.android.Project1;



	import java.sql.SQLException;
	import java.util.ArrayList;
	import java.util.List;

	

	import android.content.ContentValues;
	import android.content.Context;
	import android.database.Cursor;
	import android.database.sqlite.SQLiteDatabase;
	import android.database.sqlite.SQLiteOpenHelper;


	public class DatabaseHandler extends SQLiteOpenHelper  {

	    // Database Version
	    private static final int DATABASE_VERSION = 1;
	 
	    // Database Name
	    private static final String DATABASE_NAME = "GrindCare.db";
	 
	    // Session table name
	    private static final String TABLE_SESSIONS = "session_data";
	 
	    // Session Table Columns names
	    private static final String KEY_ID = "session_id";
	    private static final String KEY_OBJ_DATA = "obj_data";
	    private static final String KEY_SUBJ_RATING = "subj_rating";
	    private static final String KEY_NOTES = "notes";
	    private static final String KEY_DATE_TIME = "date_time";
	    
		public DatabaseHandler(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			//File path = context.getDatabasePath(DATABASE_NAME);
			//System.out.println("DatabaseHandler called: " + path.getAbsolutePath());
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			System.out.println("DatabaseHandler called onCreate: ");
	        String CREATE_SESSION_TABLE = "CREATE TABLE " + TABLE_SESSIONS + "("
	                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + 
	        		KEY_OBJ_DATA + " BLOB,"+ 
	                KEY_SUBJ_RATING + " STRING, " + 
	        		KEY_NOTES + " STRING," + 
	                KEY_DATE_TIME + " STRING" + ")";
	        db.execSQL(CREATE_SESSION_TABLE);	
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	        // Drop older table if existed
	        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSIONS);
	 
	        // Create tables again
	        onCreate(db);
			
		}
		
		/**
	     * All CRUD(Create, Read, Update, Delete) Operations
		 * @throws SQLException 
	     */
	 
	    // Adding new session
	    void addSession(SessionData session) throws SQLException {
	        SQLiteDatabase db = this.getWritableDatabase();
	 
	        ContentValues values = new ContentValues();
	        //byte[] blobAsBytes = session.getObjData().getBytes(1, (int)session.getObjData().length()); //Blob to byte array

	        values.put(KEY_OBJ_DATA, session.getObjData()); 
	        values.put(KEY_SUBJ_RATING, session.getSubjRating());
	        values.put(KEY_NOTES, session.getNotes());
	        values.put(KEY_DATE_TIME, session.getDateTime());
	 
	        // Inserting Row
	        db.insert(TABLE_SESSIONS, null, values);
	        db.close(); // Closing database connection
	    }
	 
	    // Getting single contact
	    SessionData getSession(int id) {
	        SQLiteDatabase db = this.getReadableDatabase();
	 
	        Cursor cursor = db.query(TABLE_SESSIONS, new String[] { KEY_ID,
	        		KEY_OBJ_DATA, KEY_SUBJ_RATING, KEY_NOTES, KEY_DATE_TIME }, KEY_ID + "=?",
	                new String[] { String.valueOf(id) }, null, null, null, null);
	        if (cursor != null)
	            cursor.moveToFirst();
	 
	        //byte[] blobAsBytes = cursor.getBlob(1);

	        SessionData sessionData = new SessionData(cursor.getInt(0), cursor.getBlob(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
	        // return contact
	        cursor.close();
	        db.close();
	        
	        return sessionData;
	    }
	     
	    // Getting All Sessions
	    public List<SessionData> getAllSessionData() {
	        List<SessionData> sessionDataArray = new ArrayList<SessionData>();
	        // Select All Query
	        String selectQuery = "SELECT  * FROM " + TABLE_SESSIONS;
	 
	        SQLiteDatabase db = this.getWritableDatabase();
	        Cursor cursor = db.rawQuery(selectQuery, null);
	 
	        // looping through all rows and adding to list
	        if (cursor.moveToFirst()) {
	            do {
	            	SessionData sessionData = new SessionData();
	            	sessionData.setId(Integer.parseInt(cursor.getString(0)));
	            	sessionData.setObjData(cursor.getBlob(1));
	                sessionData.setSubjRating(cursor.getString(2));
	                sessionData.setNotes(cursor.getString(3));
	                sessionData.setDateTime(cursor.getString(4));
	                // Adding session to list
	                sessionDataArray.add(sessionData);
	            } while (cursor.moveToNext());
	        }
	 
	        cursor.close();
	        db.close();
	        // return session list
	        return sessionDataArray;
	    }
	 
	    // Updating single session
	    public int updateSession(SessionData session) {
	        SQLiteDatabase db = this.getWritableDatabase();
	 
	        ContentValues values = new ContentValues();
	        values.put(KEY_OBJ_DATA, session.getObjData()); 
	        values.put(KEY_SUBJ_RATING, session.getSubjRating());
	        values.put(KEY_NOTES, session.getNotes());
	        values.put(KEY_DATE_TIME, session.getDateTime());
	 
	        // updating row
	        return db.update(TABLE_SESSIONS, values, KEY_ID + " = ?",
	                new String[] { String.valueOf(session.getId()) });
	        
	    }
	 
	    // Deleting single session
	    public void deleteSession(SessionData session) {
	        SQLiteDatabase db = this.getWritableDatabase();
	        db.delete(TABLE_SESSIONS, KEY_ID + " = ?",
	                new String[] { String.valueOf(session.getId()) });
	        db.close();
	    }
	 
	 
	    // Getting sessions Count
	    public int getSessionsCount() {
	        String countQuery = "SELECT  * FROM " + TABLE_SESSIONS;
	        SQLiteDatabase db = this.getReadableDatabase();
	        Cursor cursor = db.rawQuery(countQuery, null);
	        int count = cursor.getCount();
	        cursor.close();
	        db.close();
	        // return count
	        return count;
	    }

		@Override
		public synchronized void close() {
			// TODO Auto-generated method stub
			super.close();
		}
		
	    // Getting last 30 sessions
	    public List<SessionData> getLast30Sessions() {
	        List<SessionData> sessionDataArray = new ArrayList<SessionData>();
	        int i=0;

	        String selectQuery = "SELECT  * FROM " + TABLE_SESSIONS + " ORDER BY " + KEY_ID + " DESC LIMIT 30;";
	 
	        SQLiteDatabase db = this.getWritableDatabase();
	        Cursor cursor = db.rawQuery(selectQuery, null);
	        cursor.moveToFirst();
	        int numberOfSessions = cursor.getCount();
		 
	        // looping through all rows and adding to list
	        if (cursor.moveToLast()) {
	            do {
	            	if (i < numberOfSessions) {	//this is required when database contains less than 30 records. MoveToprevious just keeps going
	                	SessionData sessionData = new SessionData();
	                	sessionData.setId(Integer.parseInt(cursor.getString(0)));
	                	sessionData.setObjData(cursor.getBlob(1));
	                    sessionData.setSubjRating(cursor.getString(2));
	                    sessionData.setNotes(cursor.getString(3));
	                    sessionData.setDateTime(cursor.getString(4));	
	                    sessionDataArray.add(sessionData);
					}
	                i++;  
	            } while (cursor.moveToPrevious() || i < 30);
	        }
	        cursor.close();
	        db.close();
	        return sessionDataArray;	// return session list
	    }


}
