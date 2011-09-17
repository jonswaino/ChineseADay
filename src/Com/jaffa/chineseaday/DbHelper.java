package Com.jaffa.chineseaday;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {
	private static final String TAG= "DbHelper";
	
	static final String DB_NAME = "caddata.sqlite";
	static final int DB_VERSION = 1;
	static final String DB_CHARTABLE = "characters";
	static final int C_CHARACTER = 1;
	static final int C_PINYIN = 2;
	static final int C_DEFINITION = 3;
	private static String DB_PATH = "";
	
	private Context myContext;
	 	
	public DbHelper(Context context)
	{
		super(context,  DB_NAME, null, DB_VERSION);
		this.myContext = context;
				
	    DB_PATH = "/data/data/" + context.getApplicationContext().getPackageName() + "/databases/";
	    
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "onCreate called");
				
		try {
			Log.d(TAG, "Copying database...");
			copyDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

	public void createDataBase() throws IOException{

	    boolean dbExist = checkDataBase();

	    if(dbExist){
	        //do nothing - database already exist
	    }else{

	        //By calling this method and empty database will be created into the default system path
	           //of your application so we are gonna be able to overwrite that database with our database.
	        this.getWritableDatabase();
	    }

	}
	
	public boolean checkDataBase(){

	    SQLiteDatabase checkDB = null;

	    try{
	        String myPath = DB_PATH + DB_NAME;
	        checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY | SQLiteDatabase.NO_LOCALIZED_COLLATORS);
	        Log.d(TAG, "db exists");
	        }
	    catch(SQLiteException e){
	         //database does't exist yet.
	    	Log.d(TAG, "db doesn't exist");
	    		    	
	     }

	     if(checkDB != null){
	         checkDB.close();
	     }

	     return checkDB != null ? true : false;
	}
	
	private void copyDataBase() throws IOException{
		 
    	//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(DB_NAME);
 
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DB_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close(); 
    }
}
