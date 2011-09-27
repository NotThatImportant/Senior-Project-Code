package player;

import gameplay.Logic;
import units.Unit;
import terrain.Tile;

public class AI extends Player{

	Logic log;
	int size;
	
	public AI(String pN, int pNum, char fact) {
		super("Herp Derp 4000", 2, fact);	
	}
	
	public void getLogic(Logic pLog) {
		log = pLog;
		size = log.getSize();
	}
	
	public void moveUnits() {
		Unit[][] uBoard = log.getUB();
		
		for(int r = 0; r < size; r++) {
			for (int c = 0; c < size; c++) {
				if (uBoard[r][c] != null) {
					
				}
			}
		}
	}
	
	public void prodUnits() {
		Tile[][] map = log.getTBoard();
		
		for (int r = 0; r < size; r++) {
			for (int c = 0; c < size; c++) {
				
			}
		}
	}
}
