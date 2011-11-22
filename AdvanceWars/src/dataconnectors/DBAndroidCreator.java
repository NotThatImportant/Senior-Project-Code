package dataconnectors;

import java.io.File;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Class that is used to create SQLite database for Advance Wars game
 * 
 * @author Sinisa Malbasa
 *
 */
public class DBAndroidCreator
{
	private String dbPath;
	private String dbName;
	private SQLiteDatabase db;
	
	public DBAndroidCreator()
	{
		try
		{
			dbName = "advwarslocal";
			dbPath = "/data/data/org.game.advwars/databases/" + dbName + ".sqlite";
			createDatabaseDirectory();
			db = SQLiteDatabase.openOrCreateDatabase(dbPath, null);
			createDefaultTables();
			closeDB();
		}
		catch (Exception ex)
		{
			Log.e("AdvWars", ex.toString());
		}
	}
	
	public DBAndroidCreator(String databaseName)
	{
		try
		{
			this.dbName = databaseName;
			this.dbPath = "/data/data/org.game.advwars/databases/" + dbName + ".sqlite";
			createDatabaseDirectory();
			db = SQLiteDatabase.openOrCreateDatabase(dbPath, null);
			createDefaultTables();
			closeDB();
		}
		catch (Exception ex)
		{
			Log.e("AdvWars", ex.toString());
		}
	}
	
	private void createDatabaseDirectory()
	{
		// Create database directory if it doesn't exit already
		File databaseDirectory = new File("/data/data/org.game.advwars/databases/");
		databaseDirectory.mkdirs();
	}
	
	private void createDefaultTables()
	{
		// Create default database tables, add as necessary
		this.db.execSQL("create table if not exists PlayerSettings (_id integer primary key autoincrement, Player_Name text not null, Difficulty text not null, Sound_On integer not null);");
		this.db.execSQL("create table if not exists Scores (_id integer primary key autoincrement, Player_Name text not null, Wins integer not null, Losses integer not null, Experience integer not null);");
	}
	
	private void closeDB()
	{
		this.db.close();
	}
}
