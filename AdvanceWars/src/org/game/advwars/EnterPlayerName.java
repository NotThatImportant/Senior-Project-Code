package org.game.advwars;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Button;

public class EnterPlayerName extends Activity implements OnClickListener
{
	private EditText newPlayerName = null;
	private String playerName = null;
	
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
		playerName = newPlayerName.getText().toString();
		
		// 'sa' overrides input and proceeds regardless of value validity
		if (playerName.equals("sa"))
		{
			Intent i = new Intent(this, MainMenu.class);
			startActivity(i);
		}
		else
		{
			Intent i2 = new Intent(this, UnderConstruction.class);
			startActivity(i2);
		}
	}
}