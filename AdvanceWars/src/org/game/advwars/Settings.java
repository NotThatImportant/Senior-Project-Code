package org.game.advwars;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;

public class Settings extends Activity implements OnClickListener
{
	protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
        View changePlayerName = findViewById(R.id.change_player_name);
        changePlayerName.setOnClickListener(this);
        
        /*RadioGroup soundRadioGroup = (RadioGroup) findViewById(R.id.sound_radio_group);
        int checkedSoundRadioButton = soundRadioGroup.getCheckedRadioButtonId();
        
        switch (checkedSoundRadioButton)
    	{
    	case R.id.sound_on:
    		// Do nothing for now
    		break;
    	case R.id.sound_off:
    		// Do nothing for now
    		break;
    	}
        
        RadioGroup difficultyRadioGroup = (RadioGroup) findViewById(R.id.difficulty_radio_group);
        int difficultySoundRadioButton = difficultyRadioGroup.getCheckedRadioButtonId();
        
        switch (difficultySoundRadioButton)
    	{
    	case R.id.easy:
    		// Do nothing for now
    		break;
    	case R.id.medium:
    		// Do nothing for now
    		break;
    	case R.id.hard:
    		// Do nothing for now
    		break;
    	}*/
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
}