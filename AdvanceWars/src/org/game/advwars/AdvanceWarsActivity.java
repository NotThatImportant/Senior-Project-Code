package org.game.advwars;

import dataconnectors.DBAndroidCreator;
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Startup class
 * @author Sinisa Malbasa
 *
 */
public class AdvanceWarsActivity extends Activity implements OnClickListener
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Set up click listeners for all buttons
        View previousPlayerName = findViewById(R.id.previous_player_name);
        previousPlayerName.setOnClickListener(this);
        View newPlayerName = findViewById(R.id.new_player_name);
        newPlayerName.setOnClickListener(this);
        
        //Create DB if it doesn't exist
        DBAndroidCreator db = new DBAndroidCreator();
    }
    
    public void onClick(View v)
    {
    	switch (v.getId())
    	{
    	case R.id.previous_player_name:
    		Intent i = new Intent(this, SelectPlayerName.class);
    		startActivity(i);
    		break;
    	case R.id.new_player_name:
    		Intent i2 = new Intent(this, EnterPlayerName.class);
    		startActivity(i2);
    		break;
    	}
    }
}