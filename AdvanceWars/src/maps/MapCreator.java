package maps;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.commons.logging.Log;
import java.io.FileNotFoundException;

public class MapCreator
{
	private File mapsDirectory;
	
	public MapCreator()
	{
		createMapsDirectory();
	}
	
	private void createMapsDirectory()
	{
		// Create maps directory if it doesn't exit already
		this.mapsDirectory = new File("/data/data/org.game.advwars/maps/");
		mapsDirectory.mkdirs();
	}
	
	public void newMap(String mapName, String map, boolean overwrite)
	{
		File fMap = new File("/data/data/org.game.advwars/maps/" + mapName + ".txt");
		//boolean fileExists = fMap.exists();
		Writer output = null;

		if (overwrite)
		{
			try
			{ 
				output = new BufferedWriter(new FileWriter(fMap));
				output.write(map);
				output.close();
			}
			catch (Exception ex)
			{
				// Do nothing, otherwise it will crash the application
			}
		}
	}
}
