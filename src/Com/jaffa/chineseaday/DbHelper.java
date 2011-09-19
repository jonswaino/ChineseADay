package Com.jaffa.chineseaday;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {
	private static final String TAG = "DbHelper";

	static final String DB_NAME = "caddata.sqlite";
	static final int DB_VERSION = 1;
	static final String DB_CHARTABLE = "characters";
	static final int C_CHARACTER = 1;
	static final int C_PINYIN = 2;
	static final int C_DEFINITION = 3;
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

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "onCreate called");

		try {
			Log.d(TAG, "Copying database...");
			createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();

		if (dbExist) {
			// do nothing - database already exist
		} else {

			this.getReadableDatabase();

			try {

				copyDataBase();

			} catch (IOException e) {

				throw new Error("Error copying database");

			}

		}

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

	// public void copyImageFile() throws IOException
	// {
	// InputStream myInput = myContext.getAssets().open("cadtest.sqlite");
	//
	// // Path to the just created empty db
	// String outFileName = DB_PATH + "cadtest.sqlite";
	//
	// //Open the empty db as the output stream
	// OutputStream myOutput = new FileOutputStream(outFileName);
	//
	// //transfer bytes from the inputfile to the outputfile
	// byte[] buffer = new byte[2048];
	// int length;
	// while ((length = myInput.read(buffer))>0){
	// myOutput.write(buffer, 0, length);
	// }
	//
	// //Close the streams
	// myOutput.flush();
	// myOutput.close();
	// myInput.close();
	// }

	private void copyDataBase() throws IOException {

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
	}

	public void openDataBase() throws SQLException {

		// Open the database
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READONLY);

	}

	@Override
	public synchronized void close() {

		if (myDataBase != null)
			myDataBase.close();

		super.close();

	}
}
