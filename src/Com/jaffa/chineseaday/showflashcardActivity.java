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

	DbHelper dbHelper;
	SQLiteDatabase db;
	ViewFlipper thisflipper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
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
	
		// chinese unicode characters 4E00..U+9FFF
		
		btnNext.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {					
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
		int randomValue = r.nextInt(maxValue-minValue) + minValue;
		
		return randomValue;
	}
	
	private String GetCharacterValue() {

		

		String whereClause = String.format("id > %d", GenerateRandomValue(1, 100));
		
		Cursor cursor = db.query(DbHelper.DB_CHARTABLE, null, whereClause, null, null,null,null);
		startManagingCursor(cursor);
			
		String character = null;
		cursor.moveToFirst();
		
		character = cursor.getString(DbHelper.C_CHARACTER);
		
		
		StringBuilder sbChars = new StringBuilder(character);
		return sbChars.toString();
	}
	
	private void ShowCharacter() {
		StringBuilder sbChars = new StringBuilder();
		TextView flashcharacter = (TextView) findViewById(R.id.flashcharacter);
		
		//sbChars.append(new Character((char) charValue));
		flashcharacter.setText(GetCharacterValue());
	}
	
	private void ShowRevalCharacter() {
//		StringBuilder sbChars = new StringBuilder();
//		TextView flashcharacter = (TextView) findViewById(R.id.flashcharacter);
//		
//		sbChars.append(new Character((char) charValue));
//		flashcharacter.setText(sbChars.toString());
	}
}
