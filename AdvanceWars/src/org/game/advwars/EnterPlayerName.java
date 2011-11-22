package org.game.advwars;

import dataconnectors.DBAndroidConnector;
import dataconnectors.DBCheckPlayerName;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Button;

public class EnterPlayerName extends Activity implements OnClickListener
{
	private String playerName = null;
	private EditText newPlayerName = null;
	private GUIGameValues enterPlayerGGV = new GUIGameValues();
	
	protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_player_name);
        
        // Set up text box and button
        newPlayerName = (EditText) findViewById(R.id.new_player_name);
        Button changePlayerName = (Button) findViewById(R.id.enter_new_player_name);
        
        // Default text box text
        newPlayerName.setText("");
        
        changePlayerName.setOnClickListener(this);
    }

	@Override
	public void onClick(View v)
	{
		boolean cancelPlayerNameEntry = true;
		DBAndroidConnector dbc = new DBAndroidConnector();
		playerName = newPlayerName.getText().toString();
		DBCheckPlayerName checkPlayer = new DBCheckPlayerName();
		
		if(checkPlayer.playerNameIsValid(playerName))
			cancelPlayerNameEntry = false;
		
		if(playerName.length() > 15)
			cancelPlayerNameEntry = true;

		if (!playerName.equals("") && !cancelPlayerNameEntry)
		{
			enterPlayerGGV.setPlayerName(playerName);
			dbc.executeQuery("insert into 'PlayerSettings' ('Player_Name', 'Difficulty', 'Sound_On') values ('" + playerName + "', 'medium', '1');");
			dbc.executeQuery("insert into 'Scores' ('Player_Name', 'Wins', 'Losses', 'Experience') values ('" + playerName + "', '0', '0', '0');");
			dbc.closeDB();

			Intent i = new Intent(this, MainMenu.class);
			i.putExtra("ggv", enterPlayerGGV);
			startActivity(i);
		}
		else
		{
			Intent i2 = new Intent(this, InvalidPlayerNameEntered.class);
			startActivity(i2);
		}
	}
}