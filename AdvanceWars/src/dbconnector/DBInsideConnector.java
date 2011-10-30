package dbconnector;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Class that is used to open SQLite database on Android devices.
 * 
 * @author Sinisa Malbasa
 *
 */
public class DBInsideConnector
{
	private String dbPath;
	private SQLiteDatabase db;
	
	public DBInsideConnector()
	{
		this.dbPath = "/data/data/org.game.advwars/databases/gridwars.sqlite";
		this.db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
	}
	
	public DBInsideConnector(String packageName, String databaseName)
	{
		this.dbPath = "/data/data/" + packageName+ "/databases/" + databaseName;
		this.db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
	}
	
	public void insertRawData(String table, String data)
	{
		String insertQuery = "insert into " + table + " values (" + data + ");";
		
		try
		{
			db.execSQL(insertQuery);
		}
		catch (SQLException ex)
		{
			Log.e("AdvWars", ex.toString());
		}
	}
	
	public void insertData(String table, ContentValues data)
	{
		try
		{
			db.insertOrThrow(table, null, data);
		}
		catch (SQLException ex)
		{
			Log.e("AdvWars", ex.toString());
		}
	}
	
	public void updateData(String table, ContentValues data)
	{
		try
		{
			db.update(table, data, null, null);
		}
		catch (SQLException ex)
		{
			Log.e("AdvWars", ex.toString());
		}
	}
	
	public void deleteRawData(String table, String column, String value)
	{
		String deleteQuery = "delete from " + table + " where " + column + " = '" + value + "'";
		
		try
		{
			db.execSQL(deleteQuery);
		}
		catch (SQLException ex)
		{
			Log.e("AdvWars", ex.toString());
		}
	}
	
	public void createTable(String table, String columns)
	{
		String createQuery = "";
		createQuery = "create table ";
		createQuery += table + " (";
		createQuery += columns;
		createQuery += ");";
		
		try
		{
			db.execSQL(createQuery);
		}
		catch (SQLException ex)
		{
			Log.e("AdvWars", ex.toString());
		}
	}
	
	public void deleteTable(String table)
	{
		String dropQuery = "drop table if exists" + table;
		
		try
		{
			db.execSQL(dropQuery);
		}
		catch (SQLException ex)
		{
			Log.e("AdvWars", ex.toString());
		}
	}
	
	public void executeQuery(String query)
	{
		try
		{
			db.execSQL(query);
		}
		catch (SQLException ex)
		{
			Log.e("AdvWars", ex.toString());
		}
	}
	
	public SQLiteDatabase getDB()
	{
		return db;
	}
	
	public void closeDB()
	{
		db.close();
	}
}