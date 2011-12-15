package org.game.advwars;

import java.util.ArrayList;
import java.util.HashMap;

import dataconnectors.DBAndroidConnector;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**********************************************************************************************
 * 
 * This class shows scores of various players in a well-organized table.
 * 
 * @author Sinisa Malbasa
 *
 *********************************************************************************************/
public class LeaderBoard extends Activity
{
	private int index = 0;
	private ArrayList<String> playerNames = new ArrayList<String>();
	private ArrayList<String> playerWins = new ArrayList<String>();
	private ArrayList<String> playerLosses = new ArrayList<String>();
	private ArrayList<String> playerXP = new ArrayList<String>();
	
	protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leader_board);
        
        // Get player name, wins, losses, and XP from database
        getPlayerInfo();
        
        ListView list = (ListView) findViewById(R.id.scores);

        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("PlayerName", "Player Name");
        map.put("Wins", "Wins");
        map.put("Losses", "Losses");
        map.put("XP", "Experience");
        mylist.add(map);
        
        while (index < playerNames.size())
        {
        	map = new HashMap<String, String>();
            map.put("PlayerName", playerNames.get(index));
            map.put("Wins", playerWins.get(index));
            map.put("Losses", playerLosses.get(index));
            map.put("XP", playerXP.get(index));
            mylist.add(map);
            
            index++;
        }

        ListAdapter mSchedule = new SimpleAdapter(this, mylist, R.layout.leader_board,
                    new String[] {"PlayerName", "Wins", "Losses", "XP"}, new int[] {R.id.player_name, R.id.wins, R.id.losses, R.id.xp});
        list.setAdapter(mSchedule);
    }
	
	private void getPlayerInfo()
	{
		DBAndroidConnector db = new DBAndroidConnector();
        SQLiteDatabase myDataBase = db.getDB();
		
		try
        {
        	Cursor cursor = myDataBase.rawQuery("select Player_Name from Scores", null);

            if(cursor.moveToFirst())
            {
                do
                {
                    playerNames.add(cursor.getString(0));
                }
                while(cursor.moveToNext());
            }
            
        	cursor = myDataBase.rawQuery("select Wins from Scores", null);

            if(cursor.moveToFirst())
            {
                do
                {
                    playerWins.add(cursor.getString(0));
                }
                while(cursor.moveToNext());
            }
            
        	cursor = myDataBase.rawQuery("select Losses from Scores", null);

            if(cursor.moveToFirst())
            {
                do
                {
                    playerLosses.add(cursor.getString(0));
                }
                while(cursor.moveToNext());
            }
            
        	cursor = myDataBase.rawQuery("select Experience from Scores", null);

            if(cursor.moveToFirst())
            {
                do
                {
                    playerXP.add(cursor.getString(0));
                }
                while(cursor.moveToNext());
            }
            
            cursor.close();

        }
        finally
        {
            myDataBase.close();
        }
	}
}