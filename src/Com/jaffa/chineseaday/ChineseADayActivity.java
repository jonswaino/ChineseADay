package Com.jaffa.chineseaday;

import java.io.IOException;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ChineseADayActivity extends ListActivity {
    /** Called when the activity is first created. */
	DbHelper myDbHelper;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                
        myDbHelper = new DbHelper(this);
 
        try { 
        	myDbHelper.createDataBase(); 
	 	} catch (IOException ioe) {	 
	 		throw new Error("Unable to create database");	 
	 	}
 
	 	try {
	 
	 		myDbHelper.openDataBaseForWrite();
	 
	 	}catch(SQLException sqle){
	 
	 		throw sqle;
	 
	 	}
        		
	 	// create decks menu
		String[] from = new String[] { DbHelper.C_CAPTION };
		int[] to = new int[] { R.id.label };
		
		Cursor decksCursor = myDbHelper.GetDecksMenu();
		SimpleCursorAdapter deckAdapter = new SimpleCursorAdapter(this, R.layout.rowlayout, decksCursor,from, to);
		setListAdapter(deckAdapter);
		
    }
	
    
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
				
		//int deckType = myDbHelper.GetDeckType((int)id);
		//DeckSeries series = myDbHelper.GetDeckSeries((int)id);
		Cursor cursor = myDbHelper.GetDeckSeries((int)id);
		int deckType = cursor.getInt(cursor.getColumnIndex("enabled"));
		int minseries = cursor.getInt(cursor.getColumnIndex("minseries"));
		int maxseries = cursor.getInt(cursor.getColumnIndex("maxseries"));			
		
		switch ( deckType )
		{
		case 0:
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("market://details?id=com.android.example"));
			startActivity(intent);
			break;
			
		case 1:
			
			
			Intent startCardsIntent = new Intent(this, showflashcardActivity.class);		
			
			// get the id of the menu being clicked and set the min/max series
			// for the deck to be displayed.		
			Bundle b = new Bundle(); 
			b.putInt("minseries", minseries);
			b.putInt("maxseries", maxseries);		

			startCardsIntent.putExtras(b);
			startActivity(startCardsIntent);
			break;
			
		}
		

	}
}