package org.game.advwars;

import android.app.Activity;
import android.os.Bundle;

/**********************************************************************************************
 * 
 * Displays various information about the game, such as authors' names and copyright
 * information.
 * 
 * @author Sinisa Malbasa
 *
 *********************************************************************************************/
public class About extends Activity
{
	protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
    }
}
