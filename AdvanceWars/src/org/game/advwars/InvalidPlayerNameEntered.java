package org.game.advwars;

import android.app.Activity;
import android.os.Bundle;

/**********************************************************************************************
 * 
 * Error check against invalid player name entry.
 * 
 * @author Sinisa Malbasa
 *
 *********************************************************************************************/
public class InvalidPlayerNameEntered extends Activity
{
	public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invalid_user_name_entered);
    }
}