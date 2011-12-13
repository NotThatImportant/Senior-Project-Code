package org.game.advwars;

import dataconnectors.SaveGUIData;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SinglePlayer extends Activity implements OnClickListener
{
	private GUIGameValues ggvGlobal = new GUIGameValues();
	private SaveGUIData sd = new SaveGUIData();
	
	protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_player);
        
        // Set up click listeners for all buttons
        View tutorial = findViewById(R.id.tutorial);
        tutorial.setOnClickListener(this);
        View quickMatch = findViewById(R.id.quick_match);
        quickMatch.setOnClickListener(this);
        View campaign = findViewById(R.id.campaign);
        campaign.setOnClickListener(this);
    }

	@Override
	public void onClick(View v)
	{
		ggvGlobal = sd.loadGGVData();
		
		switch (v.getId())
    	{
    	case R.id.tutorial:
    		Intent i = new Intent(this, UnderConstruction.class);
    		startActivity(i);
    		break;
    	case R.id.quick_match:
    		ggvGlobal.setAIOn(true);
    		sd.saveGGVData(ggvGlobal);
    		Intent i2 = new Intent(this, FactionSelection.class);
    		startActivity(i2);
    		break;
    	case R.id.campaign:
    		Intent i3 = new Intent(this, UnderConstruction.class);
    		startActivity(i3);
    		break;
    	}
	}
}