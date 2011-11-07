package org.game.advwars;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class Settings extends Activity implements OnClickListener, OnCheckedChangeListener
{
	protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
        View changePlayerName = findViewById(R.id.change_player_name);
        changePlayerName.setOnClickListener(this);
        
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