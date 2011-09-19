package Com.jaffa.chineseaday;

import java.io.IOException;

import android.app.ListActivity;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ChineseADayActivity extends ListActivity {
    /** Called when the activity is first created. */
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Need to call dbhelper get to force check for db
        //new DbHelper(this).getReadableDatabase();               
//        try {
//			new DbHelper(this).copyImageFile();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
        
//        try {
//			new DbHelper(this).createDataBase();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
//        try {
//			new DbHelper(this).createDataBase();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
        DbHelper myDbHelper =  new DbHelper(this);
 
        try {
 
        	myDbHelper.createDataBase();
 
	 	} catch (IOException ioe) {
	 
	 		throw new Error("Unable to create database");
	 
	 	}
 
	 	try {
	 
	 		myDbHelper.openDataBase();
	 
	 	}catch(SQLException sqle){
	 
	 		throw sqle;
	 
	 	}
        
		// Create an array of Strings, that will be put to our ListActivity
		String[] names = new String[] { 
				"100 characters",
				"200-300 characters",
				"300-400 characters",
				"400-500 characters",
				"View learnt words",
				"View Progress"};
		
		// Use your own layout and point the adapter to the UI elements which
		// contains the label
		this.setListAdapter(new ArrayAdapter<String>(this, R.layout.rowlayout,
				R.id.label, names));     
    }
    
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked
		
		//Object o = this.getListAdapter().getItem(position);
		//String keyword = o.toString();
		
		Intent startCardsIntent = new Intent(this, showflashcardActivity.class);
		startActivity(startCardsIntent);
		
	}
}