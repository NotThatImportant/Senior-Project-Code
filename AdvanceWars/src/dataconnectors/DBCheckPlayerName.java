package dataconnectors;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Class that checks validity of entered player name. This is important so
 * that we don't end up with same, yet multiple player names in the database.
 * 
 * @author Sinisa Malbasa
 *
 */
public class DBCheckPlayerName
{
	private String dbPath;
	private SQLiteDatabase db;
	
	public DBCheckPlayerName()
	{
		this.dbPath = "/data/data/org.game.advwars/databases/advwarslocal.sqlite";
		this.db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
	}
	
	public boolean playerNameIsValid(String playerName)
	{
		boolean validPlayerName = false;
		ArrayList<String> playerNames = new ArrayList<String>();
		
		try
        {
        	Cursor cursor = db.rawQuery("select Player_Name from PlayerSettings", null);
        	int playerNameIndex = cursor.getColumnIndexOrThrow("Player_Name");

            if(cursor.moveToFirst())
            {
                do
                {
                    playerNames.add(cursor.getString(playerNameIndex));
                }
                while(cursor.moveToNext());
            }
            
            cursor.close();
        }
        finally
        {
            db.close();
        }
		
		if (!playerNames.contains(playerName))
			validPlayerName = true;
		
		return validPlayerName;
	}
	
	public boolean areThereAnyPlayerNamesSaved()
	{
		boolean thereArePlayerNames = false;
		ArrayList<String> playerNames = new ArrayList<String>();
		
		try
        {
        	Cursor cursor = db.rawQuery("select Player_Name from PlayerSettings", null);

            if(cursor.moveToFirst())
            {
                do
                {
                    playerNames.add(cursor.getString(0));
                }
                while(cursor.moveToNext());
            }
            
            cursor.close();
        }
        finally
        {
            db.close();
        }
		
		if (!playerNames.isEmpty())
			thereArePlayerNames = true;
		
		return thereArePlayerNames;
	}
}
