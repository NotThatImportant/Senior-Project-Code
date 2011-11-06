package database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class DBAndroidCreator
{
	private String dbPath;
	private String dbName;
	private SQLiteDatabase db;
	private CursorFactory cf;
	
	public DBAndroidCreator()
	{
		try
		{
			dbName = "advwarslocal";
			dbPath = "/data/data/org.game.advwars/databases/" + dbName + ".sqlite";
			db.openOrCreateDatabase(dbPath, cf);
			createDatabaseDirectory();
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
			db.openOrCreateDatabase(dbPath, cf);
			createDatabaseDirectory();
		}
		catch (Exception ex)
		{
			Log.e("AdvWars", ex.toString());
		}
	}
	
	private void createDatabaseDirectory()
	{
		File databaseDirectory = new File("/data/data/org.game.advwars/databases/");
		databaseDirectory.mkdirs();
		File outputFile = new File(databaseDirectory, dbName);
		try
		{
			FileOutputStream fos = new FileOutputStream(outputFile);
		}
		catch (FileNotFoundException ex)
		{
			Log.e("AdvWars", ex.toString());
		}
	}
	
	public SQLiteDatabase getDB()
	{
		return db;
	}
}
