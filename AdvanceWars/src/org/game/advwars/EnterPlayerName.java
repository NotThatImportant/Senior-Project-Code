package org.game.advwars;

import dbconnector.DBConnector;
import dbconnector.DBInsideConnector;
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
		DBInsideConnector dbc = new DBInsideConnector();
		playerName = newPlayerName.getText().toString();
		dbc.insertRawData("PlayerSettings", "'medium', 'true', 1, " + "'" + playerName + "'");
		dbc.deleteRawData("PlayerSettings", "PID", "0");
		dbc.closeDB();

		// 'sa' overrides input and proceeds regardless of value validity
		if (playerName.equals("sa"))
		{
			enterPlayerGGV.setPlayerName(playerName);
			Intent i = new Intent(this, MainMenu.class);
			i.putExtra("ggv", enterPlayerGGV);
			startActivity(i);
		}
		else if (!playerName.equals(""))
		{
			enterPlayerGGV.setPlayerName(playerName);
			Intent i2 = new Intent(this, MainMenu.class);
			i2.putExtra("ggv", enterPlayerGGV);
			startActivity(i2);
		}
		else
		{
			enterPlayerGGV.setPlayerName(playerName);
			Intent i3 = new Intent(this, InvalidPlayerNameEntered.class);
			i3.putExtra("ggv", enterPlayerGGV);
			startActivity(i3);
		}
	}
}