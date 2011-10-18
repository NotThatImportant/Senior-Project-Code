package dbconnector;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Default path where Android stores database file:
 *'/data/data/packagename/databases'
 * 
 * @author Sinisa Malbasa
 *
 */
public class DBConnector
{
	private int numOfColumns = 0;
	private String filepath = null;
	private Connection connection = null;
	private Statement statement = null;
	private ResultSet resultSet = null;
	private ArrayList<String> availableColumns;
	private ArrayList<String> availableTables;
	
	// Default constructor
	public DBConnector()
	{
		availableColumns = new ArrayList<String> ();
		this.filepath = System.getProperty("user.home") + File.separator + "gridwars.sqlite";
		
		setupConnector();
		getDatabaseTables();
	}
	
	public DBConnector(String filepath)
	{
		availableColumns = new ArrayList<String> ();
		this.filepath = filepath;
		
		setupConnector();
		getDatabaseTables();
	}
	
	private void setupConnector()
	{
		try
		{		
			Class.forName("org.sqlite.JDBC");
			this.connection = DriverManager.getConnection("jdbc:sqlite:" + filepath);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	// Used purely for error checking
	private void getTableColumns(String table)
	{	
		try
		{
			this.statement = this.connection.createStatement();
			ResultSet rs = this.statement.executeQuery("select * from " + table);
			ResultSetMetaData rsmd = rs.getMetaData();
			numOfColumns = rsmd.getColumnCount();
			
			for (int i = 1; i < numOfColumns; i++)
			{
				availableColumns.add(rsmd.getColumnName(i));
			}
		}
		catch (SQLException e1)
		{
			e1.printStackTrace();
		}
	}
	
	// Retrieve all tables from the supplied database
	private void getDatabaseTables()
	{
		try
		{
			availableTables = new ArrayList<String> ();
			this.statement = this.connection.createStatement();
			DatabaseMetaData dbm = this.connection.getMetaData();
			String[] types = {"TABLE"};
			ResultSet rs = dbm.getTables(null,null,"%",types);
			while (rs.next())
			{
				String tableName = rs.getString("TABLE_NAME");
				availableTables.add(tableName);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public void executeQuery(String query)
	{
		try
		{
			this.statement = this.connection.createStatement();
			this.resultSet = this.statement.executeQuery(query);
			
		    ResultSetMetaData rsmd = this.resultSet.getMetaData();
			
		    System.out.println("");
			
		    int numberOfColumns = rsmd.getColumnCount();
			
		    for (int i = 1; i <= numberOfColumns; i++)
		    {
		        if (i > 1) System.out.print(",  ");
		        String columnName = rsmd.getColumnName(i);
		        System.out.print(columnName);
		    }
		    
		    System.out.println("");
		      
		    while (this.resultSet.next())
		    {
		        for (int i = 1; i <= numberOfColumns; i++)
		        {
		          if (i > 1) System.out.print(",  ");
		          String columnValue = this.resultSet.getString(i);
		          System.out.print(columnValue);
		        }
		        System.out.println("");
		    }
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public void createTable(String table, String columns)
	{
		String create = "";
		
		create = "create table ";
		create += table + " (";
		create += columns;
		create += ");";
		
		try
		{
			this.statement = this.connection.createStatement();
			this.statement.executeUpdate(create);
			getDatabaseTables();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public void deleteTable(String table)
	{
		// If table doesn't exist
		if (!availableTables.contains(table.toUpperCase()))
		{
			System.out.println("Table '" + table + "' does not exist in this database and cannot be dropped.");
			return;
		}
		
		String drop = "drop table if exists ";
		drop += table;
		
		try
		{
			this.statement = this.connection.createStatement();
			this.statement.executeUpdate(drop);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public void insertData(String table, String data)
	{
		// If table doesn't exist
		if (!availableTables.contains(table.toUpperCase()))
		{
			System.out.println("Table '" + table + "' does not exist in this database.");
			return;
		}
		
		String[] dataSplit = Pattern.compile(", ").split(data);
		int numOfEntries = dataSplit.length;
		
		getTableColumns(table);
		
		// If number of entries doesn't match number of columns
		if (numOfEntries != numOfColumns)
		{
			System.out.println("Number of data entries and table columns do not match.");
			return;
		}
		
		String insert = "insert into " + table + " values (";
		
		for (int i = 0; i < numOfEntries; i++)
		{
			insert += "?";
			
			if ((i+1) < numOfEntries)
				insert += ",";
			else
				insert += "";
		}
		
		insert += ");";
		
		try
		{
			this.statement = this.connection.createStatement();
			PreparedStatement prepStat = this.connection.prepareStatement(insert);
			
			for (int i = 0; i < numOfEntries; i++)
			{
				prepStat.setString(i+1, dataSplit[i]);
			}
			
			prepStat.addBatch();
			this.connection.setAutoCommit(false); 
	        prepStat.executeBatch(); 
	        this.connection.setAutoCommit(true); 
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public void deleteData(String table, String column, String value)
	{
		// If table doesn't exist
		if (!availableTables.contains(table.toUpperCase()))
		{
			System.out.println("Table '" + table + "' does not exist in this database. Data cannot be deleted.");
			return;
		}
		
		String delete = "delete from " + table + " where " + column + " = '" + value + "'";
		
		getTableColumns(table);
		
		// If column doesn't exist
		if (!availableColumns.contains(column))
		{
			System.out.println("Column does not exist. Data cannot be deleted.");
			return;
		}
		
		try
		{
			this.statement = this.connection.createStatement();
			int result = this.statement.executeUpdate(delete);
			
			if(result == 1)
			{
					System.out.println("Row is deleted.");
			}
			else
			{
					System.out.println("Row is not deleted.");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public void printTable(String table)
	{
		// If table doesn't exist
		if (!availableTables.contains(table.toUpperCase()))
		{
			System.out.println("Table '" + table + "' does not exist in this database and cannot be printed.");
			return;
		}
		
		try
		{
			this.statement = this.connection.createStatement();
			this.resultSet = this.statement.executeQuery("select * from " + table);
			
		    ResultSetMetaData rsmd = this.resultSet.getMetaData();
			
		    System.out.println("");
			
		    int numberOfColumns = rsmd.getColumnCount();
			
		    for (int i = 1; i <= numberOfColumns; i++)
		    {
		        if (i > 1) System.out.print(",  ");
		        String columnName = rsmd.getColumnName(i);
		        System.out.print(columnName);
		    }
		    
		    System.out.println("");
		      
		    while (this.resultSet.next())
		    {
		        for (int i = 1; i <= numberOfColumns; i++)
		        {
		          if (i > 1) System.out.print(",  ");
		          String columnValue = this.resultSet.getString(i);
		          System.out.print(columnValue);
		        }
		        System.out.println("");
		    }
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	// Close connection to the database
	public void close()
	{
		try
		{
			this.connection.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
