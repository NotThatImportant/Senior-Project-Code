package org.game.advwars;

import android.app.Activity;
import android.os.Bundle;

/**********************************************************************************************
 * 
 * Displays various options that can be set for the multiplayer portion of the game.
 * 
 * @author Sinisa Malbasa
 *
 *********************************************************************************************/
public class MultiplayerOptions extends Activity
{
	protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiplayer_options);
    }
}