package dataconnectors;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import controller.Controller;
import android.util.Log;

/**
 * Connector class used to save actual game data.
 * 
 * @author Sinisa Malbasa
 */
public class SaveGameData
{
	private String filePath;
	private FileOutputStream fos;
	private FileInputStream fis;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Controller c;
	
	public SaveGameData()
	{
		filePath = "/data/data/org.game.advwars/saves/savedata.dat";
		fos = null;
		fis = null;
		out = null;
		in = null;
		createDefaultSaveDirectory();
	}
	
	public void saveGameData(Controller controller)
	{
		try
		{
			fos = new FileOutputStream(filePath);
			out = new ObjectOutputStream(fos);
			out.writeObject(controller);
			out.close();
		}
		catch (FileNotFoundException ex)
		{
			Log.e("AdvWars", ex.toString());
		}
		catch (IOException ex)
		{
			Log.e("AdvWars", ex.toString());
		}
	}
	
	public Controller loadGameData()
	{
		try
		{
			fis = new FileInputStream(filePath);
			in = new ObjectInputStream(fis);
			this.c = (Controller) in.readObject();
			in.close();
		}
		catch (FileNotFoundException ex)
		{
			Log.e("AdvWars", ex.toString());
		}
		catch (IOException ex)
		{
			Log.e("AdvWars", ex.toString());
		}
		catch (ClassNotFoundException ex)
		{
			Log.e("AdvWars", ex.toString());
		}
		
		return this.c;
	}
	
	private void createDefaultSaveDirectory()
	{
		File tempDirectory = new File("/data/data/org.game.advwars/saves/");
		tempDirectory.mkdirs();
	}
}
