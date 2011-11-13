package maps;

import java.util.Scanner;

import terrain.Tile;

class MapTest {
	
	public static void main (String[] args) {
			
			System.out.println("Please enter a map:");
			
			Scanner sc = new Scanner(System.in);
			
			System.out.println("");
			
			String mapName = sc.next();
			
			MapReader mr = new MapReader(mapName);
			
			Tile[][] map = mr.createMap();
			
			System.out.println("Printing out the map!");
			System.out.println("");
			
			for (int r = 0; r < mr.getSize(); r++) {
				for (int c = 0; c < mr.getSize(); c++) {
					System.out.print(" " + map[r][c].getType());
				}
				System.out.println("");
			}
	}

}