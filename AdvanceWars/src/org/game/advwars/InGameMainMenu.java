package org.game.advwars;

import java.util.ArrayList;

import dataconnectors.SaveGUIData;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class InGameMainMenu extends Activity implements OnClickListener
{
	private GUIGameValues ggv = new GUIGameValues();
	private SaveGUIData sd = new SaveGUIData();
	
	protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_game_main_menu);
        
        Bundle extras = getIntent().getExtras();
        this.ggv = (GUIGameValues) extras.getSerializable("ggv");
        
        // Set up click listeners for all buttons
        View endTurn = findViewById(R.id.end_turn);
        endTurn.setOnClickListener(this);
        View saveGame = findViewById(R.id.save_game);
        saveGame.setOnClickListener(this);
        View quitGame = findViewById(R.id.quit_game);
        quitGame.setOnClickListener(this);
    }

	@Override
	public void onClick(View v)
	{
		GUIGameValues tempGGV = sd.loadGGVData();
		
		switch (v.getId())
    	{
    	case R.id.quit_game:
    		tempGGV.setInGameMainMenuAction(false, false, true);
    		sd.saveGGVData(tempGGV);
    		this.finish();
    		break;
    	}
	}
}
