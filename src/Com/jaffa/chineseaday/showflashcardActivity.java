package Com.jaffa.chineseaday;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class showflashcardActivity extends Activity {

	private final int MAX_SERIES = 100;

	private final String TAG = "showflashcardActivity";
	
	DbHelper dbHelper;
	SQLiteDatabase db;
	ViewFlipper thisflipper;
	List<CharacterEntry> series;
	List<Integer> randomSeries;
	
	int MaxCard = MAX_SERIES;
	int MinSeries;
	int currentCard = 1;
	int charsLearnt = 0;
	
	// controls
	ProgressBar bar;
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		dbHelper.close();
		
		db.close();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		Bundle b = getIntent().getExtras(); 
		MinSeries = b.getInt("minseries", 1);
		
		series = new ArrayList<CharacterEntry>();
		randomSeries = new ArrayList<Integer>(MaxCard);
		
		dbHelper = new DbHelper(getApplicationContext());
		dbHelper.openDataBaseForWrite();
		//db = dbHelper.getReadableDatabase();
				
		setContentView(R.layout.showflashcard);		

		// setup progress bar
		bar = (ProgressBar) findViewById(R.id.progressBar);
		bar.setMax(MaxCard);
			
		
		Button btnNext = (Button) findViewById(R.id.nextcard);
		Button btnPrevious = (Button) findViewById(R.id.previouscard);

		Button btnFlipCard = (Button) findViewById(R.id.flipcard);
		Button btnKnowCard = (Button) findViewById(R.id.knowcard);
		Button btnWrong = (Button) findViewById(R.id.wrong);
		Button btnCorrect = (Button) findViewById(R.id.correct);
		Button btnUnsure = (Button) findViewById(R.id.unsure);
		Button btnBack = (Button) findViewById(R.id.back);
		
		TextView flashcharacter = (TextView) findViewById(R.id.revealCharacter);
		TextView pinyin = (TextView) findViewById(R.id.revealPinyin);
		TextView definition = (TextView) findViewById(R.id.revealDefinition);

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
				ShowRevealCharacter();
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
				
				AddToLearntList();
			}
		});

		btnUnsure.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ReturnToInitialView();
			}
		});
		btnCorrect.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AddToLearntList();
				ReturnToInitialView();
			}
		});
		btnWrong.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ReturnToInitialView();
			}
		});
	}
	
	private void AddToLearntList()
	{
		CharSequence text = "Added to your learnt list!";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(getApplicationContext(), text, duration);
		toast.show();
		

		dbHelper.AddCharacterToLearntList(series.get(currentCard).Id);
		
		// now remove character from list
		series.remove(currentCard);
		randomSeries.remove(currentCard);
		
		MaxCard--;								
		charsLearnt++;
		
		ShowCharacter();
	}
	
	private void ReturnToInitialView()
	{		
		thisflipper.showNext();
	
		NextCharacter();
		ShowCharacter();
	}
	
	private int GenerateRandomValue(int minValue, int maxValue)
	{
		Random r = new Random();
		int randomValue = r.nextInt(maxValue-minValue + 1) + minValue;
		
		return randomValue;
	}
	
	private void LoadInCharacterSeries() {
						
		Cursor cursor = dbHelper.GetCharacterSeries(MinSeries);
		startManagingCursor(cursor);
		
		while (cursor.moveToNext())	{
			CharacterEntry entry = new CharacterEntry();
			
			entry.Character = cursor.getString(dbHelper.C_CHARACTER);
			entry.Definition = cursor.getString(dbHelper.C_DEFINITION);
			entry.Pinyin = cursor.getString(dbHelper.C_PINYIN);
			
			series.add(entry);
		}			
	}
	

	private void ShowCharacter() {
				
		StringBuilder sbChars = new StringBuilder();
		TextView flashcharacter = (TextView) findViewById(R.id.flashcharacter);
		
		CharacterEntry currentEntry = series.get(randomSeries.get(currentCard-1));
		flashcharacter.setText(currentEntry.Character);	
		
		// num left to learn
		bar.setProgress(charsLearnt+1);
	}
	
	  public static String getCharPinyin(char c) {
	        HanyuPinyinOutputFormat hanyuPinyinFormat = new HanyuPinyinOutputFormat();
	        	        
	        hanyuPinyinFormat.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);
	        String[] pinyinArray = null;
	        try {
	            pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c,
	                    hanyuPinyinFormat);
	        } catch (BadHanyuPinyinOutputFormatCombination e) {
	            e.printStackTrace();
	        }
	        if (pinyinArray != null) {
	            return pinyinArray[0];
	        }
	        return "";
	    }
	  
	private void ShowRevealCharacter() {
		
		StringBuilder sbChars = new StringBuilder();
		TextView flashcharacter = (TextView) findViewById(R.id.revealCharacter);
		TextView pinyin = (TextView) findViewById(R.id.revealPinyin);
		TextView definition = (TextView) findViewById(R.id.revealDefinition);
		
		CharacterEntry currentEntry = series.get(randomSeries.get(currentCard-1));
		flashcharacter.setText(currentEntry.Character);
		
		//pinyin.setText(currentEntry.Pinyin);
		Character chineseChar = currentEntry.Character.charAt(0);
		pinyin.setText(getCharPinyin(chineseChar));
		definition.setText(currentEntry.Definition);					
	}
	
	private void NextCharacter() {
			
		if ( currentCard < MaxCard)
			currentCard++;
		else
			currentCard = 1;
		
		Log.d(TAG, "Next char idx now: " + currentCard);
	}
	
	private void PreviousCharacter() {
		
		if ( currentCard == 1 )
			currentCard = MaxCard;			
		else
			currentCard--;
		
		Log.d(TAG, "Prev char idx now: " + currentCard);
	}
		
	private void GenerateRandomSeries() {

		int toDo = MaxCard;
		
		while (toDo>0)
		{
			int randomValue = GenerateRandomValue(1,MaxCard);
			if ( !randomSeries.contains(randomValue))
			{
				toDo--;		
				randomSeries.add(new Integer(randomValue));
			}			
		}
		
	}
	
}
