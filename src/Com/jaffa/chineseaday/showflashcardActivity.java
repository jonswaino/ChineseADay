package Com.jaffa.chineseaday;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class showflashcardActivity extends Activity {

	private final int MAX_SERIES = 100;
	
	DbHelper dbHelper;
	SQLiteDatabase db;
	ViewFlipper thisflipper;
	List<CharacterEntry> series;
	List<Integer> randomSeries;
	int currentCard = 1;
	
	@Override
	public void onDestroy()
	{
		db.close();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		series = new ArrayList<CharacterEntry>();
		randomSeries = new ArrayList<Integer>(MAX_SERIES);
		
		dbHelper = new DbHelper(getApplicationContext());
		db = dbHelper.getReadableDatabase();
				
		setContentView(R.layout.showflashcard);		

		Button btnNext = (Button) findViewById(R.id.nextcard);
		Button btnPrevious = (Button) findViewById(R.id.previouscard);

		Button btnFlipCard = (Button) findViewById(R.id.flipcard);
		Button btnKnowCard = (Button) findViewById(R.id.knowcard);
		Button btnWrong = (Button) findViewById(R.id.wrong);
		Button btnCorrect = (Button) findViewById(R.id.correct);
		Button btnUnsure = (Button) findViewById(R.id.unsure);
		Button btnBack = (Button) findViewById(R.id.back);

		thisflipper = (ViewFlipper) findViewById(R.id.flashcardflipper);
		// thisflipper.setInAnimation(AnimationUtils.loadAnimation(this,
		// android.R.anim.fade_in));
		// thisflipper.setOutAnimation(AnimationUtils.loadAnimation(this,
		// android.R.anim.fade_out));
	
		LoadInCharacterSeries();
		GenerateRandomSeries();
		
		
		ShowCharacter();
		
		
		/// Click listeners
		btnNext.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				NextCharacter();
				ShowCharacter();
			}
		});
		
		btnPrevious.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				PreviousCharacter();
				ShowCharacter();
			}
		});
		
		btnFlipCard.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				thisflipper.showNext();
			}
		});

		btnBack.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent homeActivity = new Intent(getApplicationContext(),
						ChineseADayActivity.class);
				startActivity(homeActivity);
			}
		});

		btnKnowCard.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Context context = getApplicationContext();
				CharSequence text = "Added to your learnt list!";
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
		});

		btnUnsure.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				thisflipper.showNext();
			}
		});
		btnCorrect.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				thisflipper.showNext();
			}
		});
		btnWrong.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				thisflipper.showNext();
			}
		});
	}
	
	private int GenerateRandomValue(int minValue, int maxValue)
	{
		Random r = new Random();
		int randomValue = r.nextInt(maxValue-minValue + 1) + minValue;
		
		return randomValue;
	}
	
	private void LoadInCharacterSeries() {
		
		int startSeries;
		int endSeries;
		
		startSeries = 1; endSeries = MAX_SERIES;
		
		String whereClause = String.format("rowid >= %d and rowid <= %d", startSeries, endSeries);
		Cursor cursor = db.query(DbHelper.DB_CHARTABLE, null, whereClause, null, null,null,null);
		startManagingCursor(cursor);
		
		while (cursor.moveToNext())
		{
			CharacterEntry entry = new CharacterEntry();
			
			entry.Character = cursor.getString(dbHelper.C_CHARACTER);
			entry.Definition = cursor.getString(dbHelper.C_DEFINITION);
			entry.Pinyin = cursor.getString(dbHelper.C_PINYIN);
			
			series.add(entry);
		}
		
		int foo = 1;
	}
	
//	private String GetCharacterValue() {
//		
//		Cursor cursor = db.query(DbHelper.DB_CHARTABLE, null, whereClause, null, null,null,null);
//		startManagingCursor(cursor);
//			
//		String character = null;
//		cursor.moveToFirst();
//		
//		character = cursor.getString(DbHelper.C_CHARACTER);
//		
//		
//		StringBuilder sbChars = new StringBuilder(character);
//		return sbChars.toString();
//	}
//	
	private void ShowCharacter() {
				
		StringBuilder sbChars = new StringBuilder();
		TextView flashcharacter = (TextView) findViewById(R.id.flashcharacter);
		
		CharacterEntry currentEntry = series.get(randomSeries.get(currentCard-1));
		flashcharacter.setText(currentEntry.Character);	
	}
	
	private void NextCharacter() {
	
		if ( currentCard < MAX_SERIES)
			currentCard++;
		else
			currentCard = 1;
	}
	
	private void PreviousCharacter() {
		if ( currentCard == 0 )
			currentCard = MAX_SERIES;			
		else
			currentCard--;
	}
	
	private void ShowRevalCharacter() {

	}
	
	private void GenerateRandomSeries() {
		
//		List<Integer> normalSeries = new ArrayList<Integer>(MAX_SERIES);
//		
//		for (int i=0; i<=MAX_SERIES;i++)
//		{			 
//			normalSeries.add(new Integer(i));	
//		}
				
		
		//boolean done = false;
		int toDo = MAX_SERIES;
		
		while (toDo>0)
		{
			int randomValue = GenerateRandomValue(1,MAX_SERIES);
			if ( !randomSeries.contains(randomValue))
			{
				toDo--;
				//normalSeries.remove(randomValue);
				randomSeries.add(new Integer(randomValue));
			}			
		}
		
	}
	
	
}
