package gameplay;

import units.*;
import terrain.Tile;
import maps.MapReader;

public class Logic {

	private final int PLAYER1 = 0;
	private final int PLAYER2 = 1;
	
	private Unit[][] unitBoard;
	private Tile[][] tBoard;
	private char[][] moves;
	private int mapSize;
	private MapReader mr;
	
	private int p1UnitCount, p2UnitCount;

	public Logic(String mapName){
		mr = new MapReader(mapName);
		createBoards();    
		mapSize = mr.getSize();
	}


	private void createBoards(){
		unitBoard =  new Unit[mapSize][mapSize];
		tBoard = mr.createMap();
		moves = new char[mapSize][mapSize];
		
		for(int i = 0; i < mapSize; i++)
			for(int j = 0; j < mapSize; j++)
				moves[i][j] = '-';
	}

	private boolean move(Unit pUnit, int destX, int destY){
		boolean returnVal = false;
		return returnVal;

	}

	private char[][] calculateMoves(Unit pUnit){
		switch(pUnit.getType()){
		case Unit.AIRTYPE:
				for(int i = 0; i < pUnit.getMove();i++)
					break;
			break;
		case Unit.TANKTYPE:

			break;
		case Unit.INFANTRYTYPE:

			break;

		}
		
		return moves;

	}


	public char[][] getMoves(){
		return moves;
	}
	
	private int econDay() {
		int econ = 0;
		
		return econ;
	}
	
	private Unit[] battle(Unit p1, Unit p2, int attackFirst) {
		Unit[] modifiedUnits = null; 
		int damage = 0; 
		
		Unit mP1 = p1;
		Unit mP2 = p2;
		
		if (attackFirst == PLAYER1) {
			damage = (mP1.getAttack() + mP1.getBonus()) - (mP2.getArmor() + 
					tBoard[mP1.getX()][mP1.getY()].getHeight()); 
		} else {
			
		}
		
		modifiedUnits[PLAYER1] = mP1;
		modifiedUnits[PLAYER2] = mP2;
		return modifiedUnits;
	}
}
