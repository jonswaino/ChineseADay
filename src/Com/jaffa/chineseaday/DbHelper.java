package Com.jaffa.chineseaday;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {
	private static final String TAG = "DbHelper";
	
	static final int DB_VERSION = 4;
	static final String DB_NAME = "caddata.sqlite"; // + DB_VERSION;
	
	static final String DB_CHARTABLE = "characters";
	static final String DB_DECKTABLE = "decks";
	
	static final int C_CHARACTER = 1;
	static final int C_PINYIN = 2;
	static final int C_DEFINITION = 3;
	
	static final String C_CAPTION = "caption";
	static final int C_MINSERIES = 2;
	static final int C_MAXSERIES = 3;
	static final int C_ENABLED = 1;
		
	private static String DB_PATH = "";
	private Context myContext;
	private SQLiteDatabase myDataBase;

	public DbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.myContext = context;

		DB_PATH = "/data/data/"
				+ context.getApplicationContext().getPackageName()
				+ "/databases/";			
	}

	public DbHelper open() throws SQLException {
		//myDataBase = getWritableDatabase();
		myDataBase =  getWritableDatabase();
		
		Log.d(TAG, "DbHelper Opening Version: " +  this.myDataBase.getVersion());
	    return this;
	}
	
	@Override
	public synchronized void close() {

		if (myDataBase != null)
			myDataBase.close();

		super.close();

	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "onCreate called");

		try {			
			createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if ( newVersion > oldVersion)
		{
			Log.d(TAG, "New database version exists for upgrade.");			
			try {
				
				copyDataBase();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
	}

	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();

		if (!dbExist) {			

			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
		
		openDataBaseForRead();
	}
	
	public void AddCharacterToLearntList(int charId)
	{		
		ContentValues values = new ContentValues();
		
		values.put("read",1);		
	    String charArg = Integer.toString(charId);	  
	    myDataBase.update(DB_CHARTABLE, values, "id = ?", new String[] {charArg });	    		
	}
	
	public Cursor GetCharacterSeries(int startSeries) 
	{		
		String query = "select * from characters as c " +
		               "inner join decks as d on c.id between d.minseries and d.maxseries " +
		               "where d.enabled = 1 and d.minseries = " + startSeries;
		
		return myDataBase.rawQuery(query, null);
	}
	
	public Cursor GetLearntCharacters() 
	{
		String whereClause = "read = 1";                                                          
		return myDataBase.query(DbHelper.DB_CHARTABLE, null, whereClause, null, null,null,null);
	}
	
	public Cursor GetTopDecksMenu()
	{
		String query = "select distinct top.rowid _id, top.caption, top.minseries, top.maxseries from decks top " +
					   "inner join decks subdeck on subdeck.minseries >= top.minseries and subdeck.minseries <= top.maxseries where top.topmenu = 1";		
		
		return myDataBase.rawQuery(query, null);
	}
	public Cursor GetDecksMenu()
	{		
		String query = "select rowid _id, minseries || ' to ' || maxseries as caption, minseries, maxseries from decks " +
					   "order by minseries asc";
		
		return myDataBase.rawQuery(query, null);	
	}	
	
	public int GetDeckType(int minSeries)
	{
		String query = "select enabled from decks where rowid = " + minSeries; 				   
	
		Cursor cursor = myDataBase.rawQuery(query, null);
		cursor.moveToNext();
		
		return cursor.getInt(cursor.getColumnIndex("enabled"));
	}	  
	
	public Cursor GetDeckSeries(int minSeries)
	{
		String query = "select enabled, minseries, maxseries from decks where rowid = " + minSeries; 				   
	
		Cursor cursor = myDataBase.rawQuery(query, null);
		cursor.moveToNext();
		return cursor;
	}	
    
	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY
							| SQLiteDatabase.NO_LOCALIZED_COLLATORS);
			Log.d(TAG, "db exists");
		} catch (SQLiteException e) {
			// database does't exist yet.
			Log.d(TAG, "db doesn't exist");

		}

		if (checkDB != null) {
			checkDB.close();			
		}

		return checkDB != null ? true : false;
	}


	private void copyDataBase() throws IOException {

		Log.d(TAG, "Copying database...");
		
		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DB_NAME);

		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[2048];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
		
		myDataBase.setVersion(DB_VERSION);
	}

	public void openDataBaseForRead() throws SQLException {

		// Open the database
		String myPath = DB_PATH + DB_NAME;		
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,	SQLiteDatabase.OPEN_READONLY);

		Log.d(TAG, "DbHelper Opening Readable Version: " +  myDataBase.getVersion());
	}
	
	public void openDataBaseForWrite() throws SQLException {

		// Open the database
		String myPath = DB_PATH + DB_NAME;		
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,	SQLiteDatabase.OPEN_READWRITE | SQLiteDatabase.NO_LOCALIZED_COLLATORS );
		Log.d(TAG, "DbHelper Opening Writeable Version: " +  myDataBase.getVersion());
	}

	
	
}
