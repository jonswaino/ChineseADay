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
	
	// offline added...
	/*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        String[] projection = new String[] {Browser.BookmarkColumns._ID, 
                                     Browser.BookmarkColumns.TITLE, 
                                     Browser.BookmarkColumns.URL};
        String[] displayFields = new String[] {Browser.BookmarkColumns.TITLE, 
                                     Browser.BookmarkColumns.URL};
        int[] displayViews = new int[] { android.R.id.text1, 
                                     android.R.id.text2 };

        Cursor cur = managedQuery(android.provider.Browser.BOOKMARKS_URI, 
                       projection, null, null, null);
        setListAdapter(new SimpleCursorAdapter(this, 
                       android.R.layout.simple_list_item_2, cur, 
                       displayFields, displayViews));
    }*/
    
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Intent startCardsIntent = new Intent(this, showflashcardActivity.class);
		startActivity(startCardsIntent);
		
	}
}