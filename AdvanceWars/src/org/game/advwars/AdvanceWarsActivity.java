package org.game.advwars;

import maps.MapCreator;
import dataconnectors.DBAndroidCreator;
import dataconnectors.DBCheckPlayerName;
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
        
        //Create map directory if it doesn't exist
        MapCreator mc = new MapCreator();
        
        // Add maps here...
        mc.newMap("Map1", "mmmmmmmmmm\nmmmmmmbmmm\nmmmmmmrmmm\nmmhgggrggm\nmggrrrrggm\nmxgrgggggm\nmggrggggXm\nmggrgggHgm\nmmmbmmmmmm\nmmmmmmmmmm", true);
        mc.newMap("Map2", "mmmmmmmmmm\nmmmmmmbmmm\nmmmmmmrmmm\nmmggggrggm\nmggrrrrggm\nmggrgggggm\nmggrgggggm\nmggrgggggm\nmmmbmmmmmm\nmmmmmmmmmm", true);
    }

    public void onClick(View v)
    {
    	boolean noPlayerNames = true;
    	DBCheckPlayerName cpn = new DBCheckPlayerName();
    	
    	if (cpn.areThereAnyPlayerNamesSaved())
    		noPlayerNames = false;
    	
    	switch (v.getId())
    	{
    	case R.id.previous_player_name:
    		if (!noPlayerNames)
    		{
    			Intent i = new Intent(this, SelectPlayerName.class);
    			startActivity(i);
    			break;
    		}
    		else
    		{
    			Intent i2 = new Intent(this, InvalidPlayerNameSelected.class);
    			startActivity(i2);
    			break;
    		}
    	case R.id.new_player_name:
    		Intent i3 = new Intent(this, EnterPlayerName.class);
    		startActivity(i3);
    		break;
    	}
    }
}