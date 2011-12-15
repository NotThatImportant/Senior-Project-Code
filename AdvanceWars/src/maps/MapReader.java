package maps;

import java.io.*;

import terrain.Tile;


public class MapReader implements Serializable
{
	private Tile[][] map;
	private int size;
	private String[]line;
	private File file;
	
	public MapReader(String pFileName) {
		int c = 0;
		
		try {
			file = new File("/data/data/org.game.advwars/maps/" + pFileName);
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
	
	// Test constructor
	public MapReader(){
		size = 30;
		map = new Tile[size][size];
	}
	
	public Tile[][] testMap(){
		
		for(int r = 0; r < size; r++)
			for(int c = 0; c < size; c++){
				map[r][c] = new Tile('g');
				map[r][c].setX(r);
				map[r][c].setY(c);
			}
		
		
		map[3][0] = new Tile('h');
		map[3][0].setX(3);
		map[3][0].setY(0);
		
		
		map[27][28] = new Tile('H');
		map[27][28].setX(27);
		map[27][28].setY(28);
		
		
		map[5][0] = new Tile('q');
		map[5][0].setX(5);
		map[5][0].setY(0);
		
		
		map[23][23] = new Tile('Q');
		map[23][23].setX(23);
		map[23][23].setY(23);
		
		return map;
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
