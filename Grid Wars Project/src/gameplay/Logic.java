package gameplay;

import units.*;
import terrain.Tile;
import maps.MapReader;

public class Logic {

	private final int PLAYER1 = 0;
	private final int PLAYER2 = 1;
	
	private final int BINCOME = 300;
	
	private Unit[][] unitBoard;
	private Tile[][] tBoard;
	private char[][] moves;
	private int mapSize;
	private MapReader mr;
	private int currPlayer;
	
	private int p1UnitCount, p2UnitCount;
	private int p1BCount, p2BCount;
	
	public Logic(String mapName){
		mr = new MapReader(mapName);
		createBoards();    
		mapSize = mr.getSize();
		currPlayer = PLAYER1;
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
	
	public int econDay(int player) {
		int econ = 0;
		
		if (player == PLAYER1) {
			econ = p1BCount * BINCOME;
		} else {
			econ = p2BCount * BINCOME;
		}
		
		return econ;
	}
	
	public Unit[] battle(Unit p1, Unit p2, int attackFirst) {
		Unit[] modifiedUnits = new Unit[2]; 
		int damage = 0; 
		int cDamage = 0;
		
		Unit mP1 = p1;
		Unit mP2 = p2;
		
		int mp2HB = tBoard[mP2.getX()][mP2.getY()].getHeight();
		int mp1HB = tBoard[mP1.getX()][mP1.getY()].getHeight();
		
		if (attackFirst == PLAYER1) {
			damage = (mP1.getAttack() + mP1.getBonus() + mp1HB) - 
					(mP2.getArmor() + mp2HB);
			mP2.setHP(mP2.getHP() - damage);
			if (mP2.getHP() < 0) {
				//TODO: Take off of grid as it DIED :(
				p2UnitCount--;
				tBoard[mP2.getX()][mP2.getY()] = null;
			} else {
				cDamage = (mP2.getAttack() + mP2.getBonus() + mp2HB) - 
					(mP1.getArmor() + mp1HB);
				
				mP1.setHP(mP1.getHP() - cDamage);
				if (mP1.getHP() < 0) {
					//TODO: take off grid
					p1UnitCount--;
					tBoard[mP1.getX()][mP1.getY()] = null;
				}
			}
		} else {
			damage = (mP2.getAttack() + mP2.getBonus() + mp2HB) - 
			(mP1.getArmor() + mp1HB);
			mP2.setHP(mP1.getHP() - damage);
			if (mP1.getHP() < 0) {
				//TODO: Take off of grid as it DIED :( 
				p1UnitCount--;
				tBoard[mP1.getX()][mP1.getY()] = null;
			} else {
				cDamage = (mP1.getAttack() + mP1.getBonus() + mp1HB) - 
					(mP2.getArmor() + mp2HB);
				
				mP2.setHP(mP2.getHP() - cDamage);
				if (mP2.getHP() < 0) {
					//TODO: take off grid
					p2UnitCount--;
					tBoard[mP2.getX()][mP2.getY()] = null;
				}
			}
		}
		
		modifiedUnits[PLAYER1] = mP1;
		modifiedUnits[PLAYER2] = mP2;
		return modifiedUnits;
	}
	
	public void produceUnit(int player, Unit pU, Tile pT) {
		int x = pT.getX();
		int y = pT.getY();
		
	}
}
