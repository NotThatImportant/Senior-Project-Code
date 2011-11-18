package gameplay;

import java.util.ArrayList;

import player.*;
import units.*;
import terrain.Tile;
import maps.MapReader;

/****************************************************************************
 * This class contains all of the game logic. It will control the economy, 
 * how the units moves, how battles are calculated, etc. This can be called
 * the "brain" of the game, it's where all of the parts are used in order to
 * actually use it. 
 * 
 * @author ZuluXtreme 73
 ***************************************************************************/

public class Logic {

	private final int BINCOME = 1000; //income you get in addition to the base
	private final int BASEINCOME = 3000; //income you get everyday

	private final int PLAYER1 = 0; //player 1's number
	private final int PLAYER2 = 1; //player 2's number

	private ArrayList<Player> playerList; //array list containing two Player's
	private Unit[][] unitBoard; //board containing all of the units 
	private Tile[][] tBoard; //board that is the terrain of the map
	private char[][] moves; //board that shows all of the possible moves 
	private int mapSize; //current map size
	private MapReader mr; //map reader, reads in the map name and makes it
	private AI herpDerp; //AI

	/************************************************************************
	 * The constructor takes in a lot of the basic information, though 
	 * its actual use isn't important to play except for the map name and
	 * at a later point the player's factions. Here you set the mapSize to
	 * the same as the map that was created by the map reader. The players 
	 * are created and put into the array list and lastly you create all
	 * of the other boards. 
	 * 
	 * @param mapName Name of the map you want to play
	 * @param p1Fact Player 1's faction
	 * @param p2Fact Player 2's faction
	 * @param p1Name Player 1's name
	 * @param p2Name Player 2's name
	 ***********************************************************************/

	public Logic(String mapName, char p1Fact, char p2Fact, String p1Name, 
			String p2Name, boolean wantAI){

		mr = new MapReader(mapName);

		mapSize = mr.getSize();
		createBoards();
		playerList = new ArrayList<Player>();
		Player p1 = new Player(p1Name, 1, p1Fact);
		Player p2 = null;
		//if (wantAI == true) 
		//	herpDerp = new AI(p2Name, 2, 'r');
		//else 
		//	p2 = new Player(p2Name, 2, p2Fact);


		playerList.add(p1);

		if (wantAI == true) 
			playerList.add(herpDerp);
		else
			playerList.add(p2);
	}

	public Logic(String mapName, char p1Fact, char p2Fact, String p1Name, 
			String p2Name, AI pAI){

		mr = new MapReader(mapName);

		mapSize = mr.getSize();
		createBoards();
		playerList = new ArrayList<Player>();
		Player p1 = new Player(p1Name, 1, p1Fact);
		Player p2 = null;
		//if (wantAI == true) 
		//	herpDerp = new AI(p2Name, 2, 'r');
		//else 
		//	p2 = new Player(p2Name, 2, p2Fact);

		herpDerp = pAI;
		playerList.add(p1);


		playerList.add(herpDerp);

	}



	/************************************************************************
	 * 
	 * This method creates all of the boards that will be used for the game. 
	 * It also fills in the unit and the moves board, setting them so they 
	 * are empty. 
	 * 
	 ************************************************************************/

	protected void createBoards(){
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

	/************************************************************************
	 * Returns the terrain board
	 * 
	 * @return
	 ***********************************************************************/
	public Tile[][] getTBoard() {
		return tBoard;
	}

	/************************************************************************
	 * This method returns the unit board. Used exclusively to test the 
	 * board and make sure that a unit is properly placed and that the 
	 * board has actually been created. 
	 * 
	 * @return Returns the unit board
	 ***********************************************************************/

	public Unit[][] getUB() {
		return unitBoard;
	}

	/************************************************************************
	 * Returns the size of the map. 
	 * 
	 * @return Returns the size of the map
	 ***********************************************************************/

	public int getSize() {
		return mapSize;
	}

	/************************************************************************
	 * 
	 * 
	 * @param u
	 * @return
	 ***********************************************************************/

	public char[][] getMoves(Unit u) {
		move(u);

		return moves;
	}

	/************************************************************************
	 * 
	 * 
	 * @param p
	 ************************************************************************/
	public void captureBuilding(Player p, int pX, int pY) {
		if (tBoard[pX][pY].getType() == 'p' || 
				tBoard[pX][pY].getType() == 'b' && 
				tBoard[pX][pY].getOwner() != p.getPNum()) {

			if (tBoard[pX][pY].getBC() == p.getPNum()) 
				tBoard[pX][pY].setOwner(p.getPNum());
			else 
				tBoard[pX][pY].setBC(p.getPNum());

		}
		unitBoard[pX][pY].setStartedCapture(Boolean.TRUE);
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
		int hBonus = tBoard[p1.getX()][p1.getY()].getHeight();

		if (p1.getName().equals("Mech")) {
			Mech m = new Mech(0);
			damage = m.getBonusDmg(p2);
			damage += p1.getAttack() + hBonus;
		} else 
			damage = p1.getAttack() + hBonus;

		neg = p2.getArmor() + tBoard[p2.getX()][p2.getY()].getHeight();

		int tD = damage - neg;
		return tD;
	}

	private int changeHP(int dmg, Unit p) {
		int hp = p.getHP() - dmg;
		p.setHP(hp);
		return hp;
	}

	public int battle(Unit p1, Unit p2, int attackFirst) {
		int damage = 0;
		int cdamage = 0;


		if (attackFirst == PLAYER1) { //if player1 attacks first
			if (p1.getAmmo() > 0) {
				damage = damage(p1, p2); //player 1 deals damage
				changeHP(damage, p2); //damage is inflicted on player 2
				p1.setAmmo(p1.getAmmo() - 1);
			}

			if (p2.getHP() > 0) { //if player2 unit is alive
				if (p2.getAmmo() > 0) {
					cdamage = damage(p2, p1); //counter damage
					changeHP(cdamage, p1); //damage is inflicted on player 1
					p2.setAmmo(p2.getAmmo() - 1);
				}
			} else { //if player2 unit is dead
				p2 = null;
				playerList.get(PLAYER2).setNumUnits(playerList.get(PLAYER2).getNumBuild() -1);
				return 1;
			}
			damage = 0;
			cdamage = 0;
			p1.setHasAttacked(true);
			return 0;
		} else {
			if (p2.getAmmo() > 0 ) {
				damage = damage(p2, p1);
				changeHP(damage, p1);
				p2.setAmmo(p2.getAmmo() - 1);
			}
			if (p1.getHP() > 0) {
				if (p1.getAmmo() > 0) {
					cdamage = damage(p1, p2);
					changeHP(cdamage, p2);
					p1.setAmmo(p1.getAmmo() - 1);
				}
			} else {
				p1 = null;
				playerList.get(PLAYER1).setNumUnits(playerList.get(PLAYER1).getNumBuild() -1);
				return 1;
			}
			p2.setHasAttacked(true);
			return 0;
		}

	}

	public Tile getTile(int x, int y) {
		return tBoard[x][y];
	}

	public boolean produceUnit(Player p, Unit pU, Tile pT) {
		int x = pT.getX();
		int y = pT.getY();
		boolean retVal = false;

		if (unitBoard[x][y] == null) {
			if(p.getCash() >= pU.getCost()){
				unitBoard[x][y] = pU;
				p.setCash(p.getCash() - pU.getCost());
				p.setNumUnits(p.getNumUnits()+1);
				unitBoard[x][y].setX(x);
				unitBoard[x][y].setY(y);
				retVal = true;
			}
		}

		return retVal;
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

	/************************************************************************
	 * This method is to be used after the move method so you don't need to 
	 * do checks. After that you look at the move board (via GUI) and you
	 * can select where to move (the pX and pY), and this method moves the 
	 * unit there and removes it where it's from.
	 * 
	 * @param pUnit The unit to be moved
	 * @param pX The X where the unit is moving too
	 * @param pY The Y where the unit is moving too
	 ***********************************************************************/

	public void moveUnit(Unit pUnit, int pX, int pY) {
		int oX = pUnit.getX();
		int oY = pUnit.getY();

		unitBoard[oX][oY] = null;

		unitBoard[pX][pY] = pUnit;

		pUnit.setHasMoved(true);
	}

	private void move(Unit pUnit){
		int movement = pUnit.getMove();

		if(tBoard[pUnit.getX()][pUnit.getY()].getType() == 'm'){
			movement-=2;
		}
		//Top
		for(int r = 1; r<=movement && pUnit.getX()-r >= 0; r++){
			if(possibleMove(pUnit, pUnit.getX()-r, pUnit.getY())==false){
				break;
			}
			else{
				moves[pUnit.getX()-r][pUnit.getY()] = 'x';
				for(int j = 1; j < movement && pUnit.getY()+j < mapSize && pUnit.getY()-j >= 0; j++){
					if(possibleMove(pUnit, pUnit.getX()-r, pUnit.getY()+j)==true){
						moves[pUnit.getX()-r][pUnit.getY()+j] = 'x';
					}
					if(possibleMove(pUnit, pUnit.getX()-r, pUnit.getY()-j)==true){
						moves[pUnit.getX()-r][pUnit.getY()-j] = 'x';
					}
				}
			}
		}
		//Bottom
		for(int r = 1; r<movement && pUnit.getX()+r<mapSize; r++){
			if(possibleMove(pUnit, pUnit.getX()+r, pUnit.getY())==false){
				break;
			}
			else{
				moves[pUnit.getX()+r][pUnit.getY()] = 'x';
				// Checking Left
				for(int j = 1; j < movement && pUnit.getY()+j<mapSize && pUnit.getY()-j>=0; j++){
					if(possibleMove(pUnit,pUnit.getX()+r, pUnit.getY()+j)==true){
						moves[pUnit.getX()+r][pUnit.getY()+j] = 'x';
					}
					if(possibleMove(pUnit,pUnit.getX()+r, pUnit.getY()-j)== true){
						moves[pUnit.getX()+r][pUnit.getY()-j] = 'x';
					}
				}
			}
		}
		//Left
		for(int c = 1; c<movement && pUnit.getY()-c>=0; c++){
			if(possibleMove(pUnit, pUnit.getX(), pUnit.getY()-c)==false){
				break;
			}
			else{
				moves[pUnit.getX()][pUnit.getY()-c] = 'x';
				// Checking below
				for(int j = 1; j < movement && pUnit.getX()+j<mapSize && pUnit.getX()-j>=0; j++){
					if(possibleMove(pUnit, pUnit.getX()+j, pUnit.getY()-c)==true){
						moves[pUnit.getX()+j][pUnit.getY()-c] = 'x';
					}
					if(possibleMove(pUnit,pUnit.getX()-j, pUnit.getY()-c)==true){
						moves[pUnit.getX()-j][pUnit.getY()-c] = 'x';
					}
				}

			}
		}
		//Right
		for(int c = 1; c<movement && pUnit.getY()+c<mapSize; c++){
			if(possibleMove(pUnit, pUnit.getX(), pUnit.getY()+c)==false){
				break;
			}
			else{
				moves[pUnit.getX()][pUnit.getY()+c] = 'x';
				// Checking Below
				for(int j = 1; j < movement && pUnit.getX()+j<mapSize && pUnit.getX()-j>=0; j++){
					if(possibleMove(pUnit,pUnit.getX()+j, pUnit.getY()+c)==true){
						moves[pUnit.getX()+j][pUnit.getY()+c] = 'x';
					}
					if(possibleMove(pUnit,pUnit.getX()-j, pUnit.getY()+c)==true){
						moves[pUnit.getX()-j][pUnit.getY()+c] = 'x';
					}
				}
			}
		}

		for(int i = 0; i < mapSize; i++){
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

		//pUnit.setHasMoved(true);
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
		System.out.println(x +" " + y);
		if((tBoard[x][y].getType()=='m' && pUnit.getType()!='i' )) {
			retval = false;
		}

		return retval;
	}

	public void setUnit(int x, int y, Unit pUnit){
		unitBoard[x][y] = pUnit;
	}

}