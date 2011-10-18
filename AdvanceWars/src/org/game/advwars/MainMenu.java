package org.game.advwars;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainMenu extends Activity implements OnClickListener
{
	protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        
        // Set up click listeners for all buttons
        View singlePlayer = findViewById(R.id.single_player);
        singlePlayer.setOnClickListener(this);
        View multiplayer = findViewById(R.id.multiplayer);
        multiplayer.setOnClickListener(this);
        View scores = findViewById(R.id.scores);
        scores.setOnClickListener(this);
        View settings = findViewById(R.id.settings);
        settings.setOnClickListener(this);
        View about = findViewById(R.id.about);
        about.setOnClickListener(this);
        
        // Gets player name and displays it on main menu
        GUIGameValues ggv = new GUIGameValues();
        final TextView textViewToChange = (TextView) findViewById(R.id.welcome_message);
        textViewToChange.setText("Welcome " + ggv.getPlayerName());
    }
	
    public void onClick(View v)
    {
    	switch (v.getId())
    	{
    	case R.id.single_player:
    		Intent i = new Intent(this, SinglePlayer.class);
    		startActivity(i);
    		break;
    	case R.id.multiplayer:
    		Intent i2 = new Intent(this, Multiplayer.class);
    		startActivity(i2);
    		break;
    	case R.id.scores:
    		Intent i3 = new Intent(this, UnderConstruction.class);
    		startActivity(i3);
    		break;
    	case R.id.settings:
    		Intent i4 = new Intent(this, Settings.class);
    		startActivity(i4);
    		break;
    	case R.id.about:
    		Intent i5 = new Intent(this, About.class);
    		startActivity(i5);
    		break;
    	}
    }
}
