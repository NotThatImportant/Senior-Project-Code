package maps;

import java.io.*;

import terrain.Tile;


public class MapReader {
	private Tile[][] map;
	private int size;
	private String[]line;
	private File file;
	
	public MapReader(String pFileName) {
		int c = 0;
		
		try {
			file = new File("maps/" + pFileName + ".txt");
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			String strLine;
			strLine = br.readLine();
			
			c++; 
			size = strLine.length();
			line = new String[size];
			line[0] = strLine;
			map = new Tile[size][size];
			
			while ((strLine = br.readLine()) != null) {
				line[c] = strLine;
				c++;
			}
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	public Tile[][] createMap() {
		for (int r = 0; r < size; r++) {
			for (int i = 0; i < size; i++) {
				Tile t = new Tile(line[r].charAt(i));
				map[r][i] = t;
				map[r][i].setX(r);
				map[r][i].setY(i);
			}
		}
		return map;
	}
	
	public int getSize() {
		return size; 
	}
}
