package gameplay;

import java.util.ArrayList;

import player.Player;

import units.*;
import terrain.Tile;
import maps.MapReader;

//TODO
public class Logic {

	private final int BINCOME = 1000;
	private final int BASEINCOME = 3000;

	private final int PLAYER1 = 0;
	private final int PLAYER2 = 1;

	private ArrayList<Player> playerList;
	private Unit[][] unitBoard;
	private Tile[][] tBoard;
	private char[][] moves;
	private int mapSize;
	private MapReader mr;

	public Logic(String mapName, char p1Fact, char p2Fact, String p1Name, 
			String p2Name){
		mr = new MapReader(mapName);
		createBoards();    
		mapSize = mr.getSize();
		playerList = new ArrayList();
		Player p1 = new Player(p1Name, 1, p1Fact);
		Player p2 = new Player(p2Name, 2, p2Fact);
		playerList.add(p1);
		playerList.add(p2);
	}


	private void createBoards(){
		unitBoard =  new Unit[mapSize][mapSize];
		tBoard = mr.createMap();
		moves = new char[mapSize][mapSize];

		for(int i = 0; i < mapSize; i++)
			for(int j = 0; j < mapSize; j++)
				moves[i][j] = '-';
	}

	/*public boolean move(Unit pUnit, int destX, int destY){
		boolean returnVal = false;

		if (canMove(destX, destY)) {
			pUnit.setX(destX);
			pUnit.setY(destY);
		}


	}*/

	public boolean canMove(Unit pUnit, int pX, int pY) {

		int desiredX = pX;
		int desiredY = pY;

		int currX = pUnit.getX();
		int currY = pUnit.getY();



		return false;
	}

	public char[][] setUpMoves(Unit pUnit) {
		int movement = pUnit.getMove();

		int x = pUnit.getX();
		int y = pUnit.getY();

		Tile[][] tempMap = tBoard;
		char[][] moves = new char[mr.getSize()][mr.getSize()];

		char type = pUnit.getType();
		char tileType = tempMap[x][y].getType();

		//This loop searches the left side to see if pUnit can move
		for (int c = 0; c < movement || y - c > 0; c++) {
			tileType = tempMap[x][y-c].getType();
			if (unitBoard[x][y-c] == null) {
				if (tileType == 'm') {
					if ( (type == 'i' || type == 'a'))
						moves[x][y-c] = '-';
				} else if (tileType != 'w')
					moves[x][y-c] = '-';
				else 
					break;
			} else 
				break;
		}


		//This loop searches the right side to see if pUnit can move
		for (int c = 0; c < movement || y + c < mr.getSize(); c++) {
			tileType = tempMap[x][y+c].getType();
			if (unitBoard[x][y+c] == null) {
				if (tileType == 'm') {
					if ( (type == 'i' || type == 'a'))
						moves[x][y+c] = '-';
				} else if (tileType != 'w')
					moves[x][y+c] = '-';
				else 
					break;
			} else 
				break;
		}

		//This loop searches the top side to see if pUnits can move
		for (int r = 0; r < movement || x + r < mr.getSize(); r++) {
			tileType = tempMap[x+r][y].getType();
			if (unitBoard[x+r][y] == null) {
				if (tileType == 'm') {
					if ( (type == 'i' || type == 'a'))
						moves[x+r][y] = '-';
				} else if (tileType != 'w')
					moves[x+r][y] = '-';
				else 
					break;
			} else 
				break;
		}
		
		//This loop searches the bottom side to see if pUnits can move
		for (int r = 0; r < movement || x - r > 0; r++) {
			tileType = tempMap[x-r][y].getType();
			if (unitBoard[x-r][y] == null) {
				if (tileType == 'm') {
					if ( (type == 'i' || type == 'a'))
						moves[x-r][y] = '-';
				} else if (tileType != 'w')
					moves[x-r][y] = '-';
				else 
					break;
			} else 
				break;
		}

		int sub = 1;

		//This loop searches the top right side to see if pUnit can move
		for (int r = 0; r < movement - 1 || x + r < mr.getSize(); r++) {
			for (int c = 0; c < movement - sub || y + c < mr.getSize(); c++) {
				tileType = tempMap[x+r][y+c].getType();
				if (unitBoard[x+r][y+c] == null) {
					if (tileType == 'm') {
						if ( (type == 'i' || type == 'a'))
							moves[x+r][y+c] = '-';
					} else if (tileType != 'w')
						moves[x+r][y+c] = '-';
					else 
						break;
				} else 
					break;
			}
			sub++;
		}

		return moves;
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

	public void econDay(Player p) {
		int econ = p.getNumBuild() * BINCOME + BASEINCOME;
		p.setCash(p.getCash() + econ);
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
			if (mP2.getHP() <= 0) {
				//TODO: Take off of grid as it DIED :(
				playerList.get(PLAYER2).setNumUnits(playerList.get(PLAYER2).
						getNumUnits()- 1);
				unitBoard[mP2.getX()][mP2.getY()] = null;
			} else {
				cDamage = (mP2.getAttack() + mP2.getBonus() + mp2HB) - 
				(mP1.getArmor() + mp1HB);

				mP1.setHP(mP1.getHP() - cDamage);
				if (mP1.getHP() <= 0) {
					//TODO: take off grid
					playerList.get(PLAYER1).setNumUnits(playerList.get(PLAYER1).
							getNumUnits()- 1);;
							unitBoard[mP1.getX()][mP1.getY()] = null;
				}
			}
		} else {
			damage = (mP2.getAttack() + mP2.getBonus() + mp2HB) - 
			(mP1.getArmor() + mp1HB);
			mP2.setHP(mP1.getHP() - damage);
			if (mP1.getHP() <= 0) {
				//TODO: Take off of grid as it DIED :( 
				playerList.get(PLAYER1).setNumUnits(playerList.get(PLAYER1).
						getNumUnits()- 1);
				unitBoard[mP1.getX()][mP1.getY()] = null;
			} else {
				cDamage = (mP1.getAttack() + mP1.getBonus() + mp1HB) - 
				(mP2.getArmor() + mp2HB);

				mP2.setHP(mP2.getHP() - cDamage);
				if (mP2.getHP() <= 0) {
					//TODO: take off grid
					playerList.get(PLAYER2).setNumUnits(playerList.get(PLAYER2).
							getNumUnits()- 1);
					unitBoard[mP2.getX()][mP2.getY()] = null;
				}
			}
		}

		modifiedUnits[PLAYER1] = mP1;
		modifiedUnits[PLAYER2] = mP2;
		return modifiedUnits;
	}

	public void produceUnit(Player p, Unit pU, Tile pT) {
		int x = pT.getX();
		int y = pT.getY();

		if (unitBoard[x][y] == null) {
			unitBoard[x][y] = pU;
			p.setCash(p.getCash() - pU.getCost());
			p.setNumUnits(p.getNumUnits()+1);
		}	
	}

	public boolean didWin(Player p) {

		if (p.getPNum() == PLAYER1) {
			if (playerList.get(PLAYER2).getNumUnits() <= 0)  {
				return true;
			}
		}
		else {
			if (playerList.get(PLAYER2).getNumUnits() <= 0)
				return true;
		}

		return false;
	}
}
