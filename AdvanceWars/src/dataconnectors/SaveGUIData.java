package dataconnectors;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.game.advwars.GUIGameValues;

import android.util.Log;

/**
 * Connector class used to save GUI values (GUIGameValues).
 * 
 * @author Sinisa Malbasa
 */
public class SaveGUIData
{
	private String filePath;
	private FileOutputStream fos;
	private FileInputStream fis;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private GUIGameValues ggv;
	
	public SaveGUIData()
	{
		filePath = "/data/data/org.game.advwars/temp/ggvdata.dat";
		fos = null;
		fis = null;
		out = null;
		in = null;
		ggv = new GUIGameValues();
		createDefaultDirectory();
	}
	
	public void saveGGVData(GUIGameValues ggv)
	{
		try
		{
			fos = new FileOutputStream(filePath);
			out = new ObjectOutputStream(fos);
			out.writeObject(ggv);
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
	
	public GUIGameValues loadGGVData()
	{
		try
		{
			fis = new FileInputStream(filePath);
			in = new ObjectInputStream(fis);
			this.ggv = (GUIGameValues) in.readObject();
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
		
		return this.ggv;
	}
	
	private void createDefaultDirectory()
	{
		File tempDirectory = new File("/data/data/org.game.advwars/temp/");
		tempDirectory.mkdirs();
	}
}
