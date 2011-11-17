package org.game.advwars;

import dataconnectors.DBAndroidCreator;
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import org.apache.commons.logging.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * Startup class
 * @author Sinisa Malbasa
 *
 */
public class AdvanceWarsActivity extends Activity implements OnClickListener
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Set up click listeners for all buttons
        View previousPlayerName = findViewById(R.id.previous_player_name);
        previousPlayerName.setOnClickListener(this);
        View newPlayerName = findViewById(R.id.new_player_name);
        newPlayerName.setOnClickListener(this);
        
        //Create DB if it doesn't exist
        DBAndroidCreator db = new DBAndroidCreator();

        //Create the dumb map so it will stop crashing....
        File file = new File("/data/data/org.game.advwars/maps/", "MapName.txt");

           file.mkdirs();
            android.util.Log.w("Warn", "File does not exist... creating " + file.getAbsolutePath());
            FileOutputStream fOut = null;
            try {


                file.createNewFile();


                fOut = openFileOutput(file.getName(), MODE_WORLD_READABLE);
                OutputStreamWriter osw = new OutputStreamWriter(fOut);


               osw.write("wwwwwwwwww\nwwwwwwwwww\nwwwwwwwwww\nwwwwwwwwww\nwwwwwwwwww\nwwwwwwwwww\nwwwwwwwwww\nwwwwwwwwww\nwwwwwwwwww\nwwwwwwwwww\n");


               osw.flush();
               osw.close();

            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }



        android.util.Log.w("Warn", "Does file exist?  " + file.exists());
    }

    public void onClick(View v)
    {
    	switch (v.getId())
    	{
    	case R.id.previous_player_name:
    		Intent i = new Intent(this, SelectPlayerName.class);
    		startActivity(i);
    		break;
    	case R.id.new_player_name:
    		Intent i2 = new Intent(this, EnterPlayerName.class);
    		startActivity(i2);
    		break;
    	}
    }
}