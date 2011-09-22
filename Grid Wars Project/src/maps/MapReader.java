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
			
			//file = new File(pFileName + ".txt");
			//Need to have the android file directory
			
			/*Change C:/Users/Dan/ to a method to get root*/
			file = new File("C:/Users/Dan/" + pFileName + ".txt");
			System.out.println(file.getAbsolutePath()+ " : " + file.exists());
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			//BufferedReader br = new BufferedReader(new FileReader("C:/Users/Dan/" + pFileName + ".txt"));
			
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
			//in.close();
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
