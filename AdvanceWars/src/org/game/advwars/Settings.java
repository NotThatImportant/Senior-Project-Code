package org.game.advwars;

import dataconnectors.DBAndroidConnector;
import dataconnectors.SaveData;
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;


public class Settings extends Activity implements OnClickListener, OnCheckedChangeListener
{
	private int soundOn;
	private String difficulty;
	private String playerName;
	private SaveData sd = new SaveData();
	private GUIGameValues ggv = new GUIGameValues();
	
	protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
        View changePlayerName = findViewById(R.id.change_player_name);
        changePlayerName.setOnClickListener(this);
        
        DBAndroidConnector db = new DBAndroidConnector();
        SQLiteDatabase myDataBase = db.getDB();
        ggv = sd.loadGGVData();
        playerName = ggv.getPlayerName();
        
        try
        {
        	Cursor cursor = myDataBase.rawQuery("select Sound_On from PlayerSettings where Player_Name = '" + playerName + "'", null);
        	int soundOnIndex = cursor.getColumnIndexOrThrow("Sound_On");
        	cursor.moveToFirst();
        	soundOn = cursor.getInt(soundOnIndex);
        	
        	// TODO
        }
        catch (Exception ex)
        {
        	Log.e("AdvWars", ex.toString());
        }
        finally
        {
            myDataBase.close();
        }
        
        RadioGroup soundRadioGroup = (RadioGroup) findViewById(R.id.sound_radio_group);
        soundRadioGroup.setOnCheckedChangeListener(this);
        
        RadioGroup difficultyRadioGroup = (RadioGroup) findViewById(R.id.difficulty_radio_group);
        difficultyRadioGroup.setOnCheckedChangeListener(this);
    }

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
    	{
    	case R.id.change_player_name:
    		Intent i = new Intent(this, EnterPlayerName.class);
    		startActivity(i);
    		break;
    	}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId)
	{
        switch (group.getCheckedRadioButtonId())
    	{
    	case R.id.sound_on:
    		Log.i("AdvWars", "Sound on");
    		break;
    	case R.id.sound_off:
    		Log.i("AdvWars", "Sound off");
    		break;
    	case R.id.easy:
    		Log.i("AdvWars", "Easy");
    		break;
    	case R.id.medium:
    		Log.i("AdvWars", "Medium");
    		break;
    	case R.id.hard:
    		Log.i("AdvWars", "Hard");
    		break;
    	}
	}
}