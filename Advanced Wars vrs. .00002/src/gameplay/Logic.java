package gameplay;

import units.Unit;
import terrain.Terrain;

public class Logic {

	private Unit[][] unitBoard;
	private Terrain[][] tBoard;
	private char[][] moves;
	private int mapSize;

	private int p1UnitCount, p2UnitCount;

	public Logic(int pMapSize){
		mapSize = pMapSize;
		createBoards(mapSize);    

	}


	private void createBoards(int pSize){
		unitBoard =  new Unit[pSize][pSize];
		tBoard = new Terrain[pSize][pSize];
		moves = new char[pSize][pSize];
		
		for(int i = 0; i < pSize; i++)
			for(int j = 0; j < pSize; j++)
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



}
