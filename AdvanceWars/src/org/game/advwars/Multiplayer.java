package org.game.advwars;

import dataconnectors.SaveGUIData;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class Multiplayer extends Activity implements OnClickListener
{
	private GUIGameValues ggvGlobal = new GUIGameValues();
	private SaveGUIData sd = new SaveGUIData();
	
	protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiplayer);
        
        // Set up click listeners for all buttons
        View createMPGame = findViewById(R.id.create_mp_game);
        createMPGame.setOnClickListener(this);
        View connectToMPGame = findViewById(R.id.connect_to_mp_game);
        connectToMPGame.setOnClickListener(this);
        View mpOptions = findViewById(R.id.mp_options);
        mpOptions.setOnClickListener(this);
    }
	
	@Override
	public void onClick(View v)
	{
		ggvGlobal = sd.loadGGVData();
		
		switch (v.getId())
    	{
    	case R.id.create_mp_game:
    		ggvGlobal.setAIOn(false);
    		sd.saveGGVData(ggvGlobal);
    		Intent i = new Intent(this, FactionSelection.class);
    		startActivity(i);
    		break;
    	case R.id.connect_to_mp_game:
    		Intent i2 = new Intent(this, UnderConstruction.class);
    		startActivity(i2);
    		break;
    	case R.id.mp_options:
    		Intent i3 = new Intent(this, UnderConstruction.class);
    		startActivity(i3);
    		break;
    	}
	}
}
