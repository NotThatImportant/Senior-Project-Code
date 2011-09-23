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
		mapSize = mr.getSize();
		createBoards();
		playerList = new ArrayList<Player>();
		Player p1 = new Player(p1Name, 1, p1Fact);
		Player p2 = new Player(p2Name, 2, p2Fact);
		playerList.add(p1);
		playerList.add(p2);
	}


	private void createBoards(){
		unitBoard =  new Unit[mapSize][mapSize];
		for (int r = 0; r < mapSize; r++)
			for (int c = 0; c < mapSize; c++) 
				unitBoard[r][c] = null;
		
		tBoard = mr.createMap();
		moves = new char[mapSize][mapSize];

		for(int i = 0; i < mapSize; i++)
			for(int j = 0; j < mapSize; j++)
				moves[i][j] = '-';
	}
	
	public Unit[][] getUB() {
		return unitBoard;
	}

	public int getSize() {
		return mapSize;
	}
	
	public char[][] getMoves() {
		return moves;
	}
	
	public Player getP1() {
		return playerList.get(PLAYER1);
	}
	
	public Player getP2() {
		return playerList.get(PLAYER2);
	}


	public void econDay(Player p) {
		int econ = p.getNumBuild() * BINCOME + BASEINCOME;
		p.setCash(p.getCash() + econ);
	} 
	
	public Unit getUnit(int x, int y) {
		Unit u = null;
		if (unitBoard[x][y] != null) 
			u = unitBoard[x][y];
		return u;
	}

	public int damage(Unit p1, Unit p2) {
		int damage = 0; 
		int neg = 0;
		
		damage = p1.getAttack() + p1.getBonus() + tBoard[p1.getX()][p1.getY()].getHeight();
		neg = p2.getArmor() + tBoard[p2.getX()][p2.getY()].getHeight();
		
		int tD = damage - neg;
		return tD;
	}
	
	public int changeHP(int dmg, Unit p) {
		int hp = p.getHP() - dmg;
		p.setHP(hp);
		return hp;
	}
	
	public int battle(Unit p1, Unit p2, int attackFirst) {
		int damage = 0;
		int cdamage = 0;
		
		boolean t = true;
		
		if (attackFirst == PLAYER1) { //if player1 attacks first
			t = false;
			damage = damage(p1, p2); //player 1 deals damage
			changeHP(damage, p2); //damage is inflicted on player 2
			
			if (p2.getHP() > 0) { //if player2 unit is alive
				cdamage = damage(p2, p1); //counter damage
				changeHP(cdamage, p1); //damage is inflicted on player 1
			} else { //if player2 unit is dead
				p2 = null;
				playerList.get(PLAYER2).setNumUnits(playerList.get(PLAYER2).getNumBuild() -1);
				return 1;
			}
			damage = 0;
			cdamage = 0;
			return 0;
		} else {
			if (t == false) {
				return 0;
			}
			damage = damage(p2, p1);
			changeHP(damage, p1);
			
			if (p1.getHP() > 0) {
				cdamage = damage(p1, p2);
				changeHP(cdamage, p2);
			} else {
				p1 = null;
				playerList.get(PLAYER1).setNumUnits(playerList.get(PLAYER1).getNumBuild() -1);
				return 1;
			}
			return 0;
		}
	}

	public Tile getTile(int x, int y) {
		return tBoard[x][y];
	}
	
	public void produceUnit(Player p, Unit pU, Tile pT) {
		int x = pT.getX();
		int y = pT.getY();

		if (unitBoard[x][y] == null) {
			unitBoard[x][y] = pU;
			p.setCash(p.getCash() - pU.getCost());
			p.setNumUnits(p.getNumUnits()+1);
			unitBoard[x][y].setX(x);
			unitBoard[x][y].setY(y);
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

	/******************************************************** 
	 *	Move method
	 *	Works by first going north of the player, then checking
	 *	whether or not that spot is traversable.  At each spot,
	 *	it checks movement-1 number of spots left and right.
	 *	This amount decreases by one for every space away
	 *	it is from the unit.
	 *********************************************************/
	private void move(Unit pUnit){
		int movement = pUnit.getMove();

		if(tBoard[pUnit.getX()][pUnit.getY()].getType() == 'm'){
			movement-=2;
		}
		//Top
		for(int r = pUnit.getX()-1; r<movement && r>=0; r--){
			if(possibleMove(pUnit, r, pUnit.getY())==false){
				break;
			}
			else{
				moves[r][pUnit.getY()] = 'x';
				for(int j = 1; j < movement; j++){
					if(possibleMove(pUnit,r, pUnit.getY()+j)==true){
						moves[r][pUnit.getY()+j] = 'x';
					}
				}
				for(int j = 1; j > movement; j++){
					if(possibleMove(pUnit,r, pUnit.getY()-j)==true){
						moves[r][pUnit.getY()+j] = 'x';
					}
				}
			}
		}
		//Bottom
		for(int r = pUnit.getX()+1; r<movement && r<mapSize; r++){
			if(possibleMove(pUnit, r, pUnit.getY())==false){
				break;
			}
			else{
				moves[r][pUnit.getY()] = 'x';
				// Checking Left
				for(int j = 1; j < movement; j--){
					if(possibleMove(pUnit,r, pUnit.getY()+j)==true){
						moves[r][pUnit.getY()+j] = 'x';
					}
				}
				// Checking Right
				for(int j = 1; j > movement; j++){
					if(possibleMove(pUnit,r, pUnit.getY()-j)== true){
						moves[r][pUnit.getY()-j] = 'x';
					}
				}
			}
		}
		//Left
		for(int c = pUnit.getY()-1; c<movement && c>=0; c--){
			if(possibleMove(pUnit, pUnit.getX(), c)==false){
				break;
			}
			else{
				moves[pUnit.getX()][c] = 'x';
				// Checking below
				for(int j = 1; j < movement; j++){
					if(possibleMove(pUnit, pUnit.getX()+j, c)==true){
						moves[pUnit.getX()+j][c] = 'x';
					}
				}
				// Checking above
				for(int j = 1; j > movement; j++){
					if(possibleMove(pUnit,pUnit.getX()-j, c)==true){
						moves[pUnit.getX()-j][c] = 'x';
					}
				}

			}
		}
		//Right
		for(int c = pUnit.getY()+1; c<movement && c<mapSize; c++){
			if(possibleMove(pUnit, pUnit.getX(), c)==false){
				break;
			}
			else{
				moves[pUnit.getX()][c] = 'x';
				// Checking Below
				for(int j = 1; j < movement; j--){
					if(possibleMove(pUnit,pUnit.getX()+j, c)==true){
						moves[pUnit.getX()+j][c] = 'x';
					}
				}
				// Checking Above
				for(int j = 1; j > movement; j++){
					if(possibleMove(pUnit,pUnit.getX()-j, c)==true){
						moves[pUnit.getX()-j][c] = 'x';
					}
				}
			}
		}

		for(int i = 0; i < mapSize; i++)
			for(int j= 0; j < mapSize; j++){
				boolean adj = false;
				if(moves[i][j] == 'x'){
					if(i-1 >=0 && moves[i-1][j] == 'x')
						adj = true;
					if(i+1 < mapSize && moves[i+1][j] == 'x')
						adj = true;
					if(j-1 >= 0 && moves[i][j-1] == 'x')
						adj = true;
					if(j+1 < mapSize && moves[i][j+1] == 'x')
						adj = true;

					if(adj == false)
						moves[i][j] = '-';
				}
			}
	}


/******************************************************** 
 *	possibleMove is used by the move method.  It checks for 
 *	making sure that only infantry and mech infantry can
 *	move on mountains.  
 *	Later we can change this method to account for water
 *	and water units
 *********************************************************/
private boolean possibleMove(Unit pUnit, int x, int y){
	boolean retval = true;

	if(tBoard[x][y].getType()=='m'&&pUnit.getType()!='i' ||
			tBoard[x][y].getType()=='m'&&pUnit.getType()!='m'){
		retval = false;
	}

	return retval;
}






/**private void move(unit pUnit){
		moveUnit(pUnit, pUnit.getMovement(), pUnit.getX(), pUnit.getY());
	}*/

/******************************************************** 
 *	Move Method - WITH RECURSION
 *	This is if we want to test the limits of the phone.
 *********************************************************/
/**
	private void move_Unit(unit pUnit, int movesLeft, int lastX, int lastY){
		if(movesLeft == 0){
		}
		else if(tBoard[lastX][lastY] == 'x'){
		}
		else{
			int unitX = lastX;
			int unitY = lastY;
			if(canMove(pUnit, lastX+1, lastY){
				unitBoard[lastX+1][lastY];
				move(pUnit, movesLeft-1, lastX+1, lastY);
			}
			if(canMove(pUnit, lastX-1, lastY){
				unitBoard[lastX-1][lastY];
				move(pUnit, movesLeft-1, lastX-1, lastY);
			}
			if(canMove(pUnit, lastX, lastY+1){
				unitBoard[lastX][lastY+1];
				move(pUnit, movesLeft-1, lastX, lastY+1);
			}
			if(canMove(pUnit, lastX, lastY-1){
				unitBoard[lastX][lastY-1];
				move(pUnit, movesLeft-1, lastX, lastY-1);
			}
		}
	}
 */

}