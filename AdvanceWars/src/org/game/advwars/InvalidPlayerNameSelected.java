package org.game.advwars;

import android.app.Activity;
import android.os.Bundle;

/**********************************************************************************************
 * 
 * Error check against invalid player name selection.
 * 
 * @author Sinisa Malbasa
 *
 *********************************************************************************************/
public class InvalidPlayerNameSelected extends Activity
{
	public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invalid_user_name_selected);
    }
}