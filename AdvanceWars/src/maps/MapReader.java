package maps;

import java.io.*;

import terrain.Tile;


public class MapReader {
	private Tile[][] map;
	private int size;
	private String[]line;
	private File file;
	private boolean test = false;

	public MapReader(String pFileName) {
		int c = 0;
		if(pFileName.equalsIgnoreCase("test")){
			test = true;
		}

		if(test==false){
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
		else{
			size = 30;
			map = new Tile[size][size];
			createMap();
		}
	}

	private void testMap(){
		for(int i = 0; i < size; i++)
			for(int j = 0; j < size; j++){
				map[i][j] = new Tile('g');
				map[i][j].setX(i);
				map[i][j].setY(j);
			}



		map[0][15] = new Tile('m');
		map[0][15].setX(0);
		map[0][15].setY(15);

		// P1 HQ
		map[5][0] = new Tile('h');
		map[5][0].setX(5);
		map[5][0].setY(0);

		map[8][1] = new Tile('q');
		map[8][1].setX(8);
		map[8][1].setY(1);

		map[3][2] = new Tile('x');
		map[3][2].setX(3);
		map[3][2].setY(2);

		map[15][24] = new Tile('b');
		map[15][24].setX(15);
		map[15][24].setY(24);


		// P2 (AI) HQ
		map[29][29] = new Tile('H');
		map[29][29].setX(29);
		map[29][29].setY(29);

		map[25][28] = new Tile('Q');
		map[25][28].setX(25);
		map[25][28].setY(28);

		map[28][26] = new Tile('X');
		map[28][26].setX(28);
		map[28][26].setY(26);

		map[20][20] = new Tile('p');
		map[20][20].setX(20);
		map[20][20].setY(20);


	}

	public Tile[][] createMap() {
		if(test)
			testMap();
		else{

			for (int r = 0; r < size; r++) {
				for (int i = 0; i < size; i++) {
					Tile t = new Tile(line[r].charAt(i));
					map[r][i] = t;
					map[r][i].setX(r);
					map[r][i].setY(i);
				}
			}
		}
		return map;
	}

	public int getSize() {
		return size; 
	}
}
