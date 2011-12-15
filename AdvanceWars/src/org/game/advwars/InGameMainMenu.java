package org.game.advwars;

import dataconnectors.SaveGUIData;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**********************************************************************************************
 * 
 * Main menu that is invoked by pressing the Menu button while on the game board. It is used
 * for ending the current turn, saving the current game, or quitting the current game.
 * 
 * @author Sinisa Malbasa
 *
 *********************************************************************************************/
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
        sd.saveGGVData(ggv);
        
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
    	case R.id.end_turn:
    		tempGGV.setInGameMainMenuAction(true, false, false);
    		sd.saveGGVData(tempGGV);
    		this.finish();
    		break;
    	case R.id.save_game:
    		tempGGV.setInGameMainMenuAction(false, true, false);
    		sd.saveGGVData(tempGGV);
    		this.finish();
    		break;
    	case R.id.quit_game:
    		tempGGV.setInGameMainMenuAction(false, false, true);
    		sd.saveGGVData(tempGGV);
    		this.finish();
    		break;
    	}
	}
}
