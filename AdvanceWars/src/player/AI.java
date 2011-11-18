package player;


import gameplay.Logic;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import terrain.Tile;
import units.APC;
import units.AntiAir;
import units.Artillery;
import units.Bomber;
import units.ChopperA;
import units.ChopperB;
import units.FighterJet;
import units.HeavyTank;
import units.Infantry;
import units.Mech;
import units.MedTank;
import units.Rockets;
import units.Unit;

public class AI extends Player{
	private boolean availableMove;
	private boolean availableAttack;
	private boolean availablePurchase;
	private boolean availableCapture;

	//only attack a unit if it can deal at least this amount of damage
	private final Integer ATTACK_DAMAGE_THRESHOLD = 3;  

	private ArrayList<Unit> unitsWithMoves;
	private ArrayList<Unit> unitsWithAttacks;
	private ArrayList<Unit> unitsWithCaptures;

	private ArrayList<String> moveLogger; 
	String move;

	Logic log;
	int size;

	enum types {
		ANTIAIR, APC, ARTILLERY, BATTLESHIP, BOMBER, CHOPPERA, CHOPPERB,
		CRUISER, FIGHTERJET, HEAVYTANK, INFANTRY, LANDER, MECH, MEDTANK,
		MISSILE, RECON, ROCKETS, SUB, TANK
	};

	public AI(String pN, int pNum, char fact) {
		super(pN, pNum, fact);	
		
		

		move = "";
		moveLogger = new ArrayList<String>();
		
		test();

	}

	/***************************************************************
	 *  
	 * 
	 ***************************************************************/
	public void startTurn(){

		//determine possible actions
		availableMove = canIMove();
		availableAttack = canIAttack();
		availablePurchase = canIBuy();
		availableCapture = canICapture();

		ArrayList<Unit> toMove = getPossibleMoves();

		//Loop through possible actions
		for(Unit actionUnit: toMove){
			moveLogger.clear();

			if(availableMove){
				//move units
				boolean moved = false;

				// We have more econ, let's be aggressive
				if(countEconBuildings() >0){
					if(moveCloserToEnemies(actionUnit, false) == true){
						moved = true;
					}				

					if(!moved){
						if(countUncapturedBuildings() == 0)
							moved = moveToUncaptured(actionUnit, true, false);
						else
							moved = moveToUncaptured(actionUnit, false, false);
					}

					if(!moved){
						moved = moveCloserToEnemies(actionUnit, true);
					}
					if(!moved){
						if(countUncapturedBuildings() == 0)
							moved = moveToUncaptured(actionUnit, true, false);
						else
							moved = moveToUncaptured(actionUnit, false, false);
					}


				}


			}
		}
		if(availableCapture){
			capture();
		}
		if(availableAttack){
			attack();
		}
		if(availablePurchase){
			ArrayList<Unit> unitsToBuild = prodUnits();
			Tile[][] tBoard= log.getTBoard();
			Unit[][] uBoard = log.getUB();
			ArrayList<Tile> emptyProdBldg = new ArrayList<Tile>();
			for(int i = 0; i < log.getSize(); i++)
				for(int j = 0; j < log.getSize(); j++){
					if(tBoard[i][j].getType() == 'Q' && uBoard[i][j] ==null){
						emptyProdBldg.add(tBoard[i][j]);
					}
				}
			int counter = 0;
			boolean success = false;
			Tile t = emptyProdBldg.get(counter);
			for(Unit bU: unitsToBuild){
				success = log.produceUnit(this, bU, emptyProdBldg.get(counter));
				move = "produce,"+bU.getName()+","+t.getX()+","+t.getY();
				moveLogger.add(move);
				if(success)
					counter++;
			}
			
		}
	}

	private int countUncapturedBuildings(){
		char[][] temp = getUncapturedBuildings();
		int count = 0;

		for(int i = 0; i < log.getSize(); i++)
			for(int j = 0; j < log.getSize(); j++)
				if(temp[i][j] == 'x')
					count++;

		return count;

	}


	/***************************************************************
	 * 
	 * @return 
	 ***************************************************************/
	private boolean hasFriendliesNearby(int x, int y){
		boolean retVal = false;
		Unit[][] uB = log.getUB();
		int pNum = getPNum();

		if((x-1) >= 0){
			if(uB[x-1][y] != null && uB[x-1][y].getOwner()==pNum){
				retVal = true;
			}
			if((y-1) >= 0){
				if(uB[x-1][y-1] != null && uB[x-1][y-1].getOwner()==pNum){
					retVal = true;
				}
			}
			if((y+1)< log.getSize()){
				if(uB[x-1][y+1] != null && uB[x-1][y+1].getOwner()==pNum){
					retVal = true;
				}
			}
		}
		if((x-2) >= 0){
			if(uB[x-2][y] != null && uB[x-2][y].getOwner()==pNum){
				retVal = true;
			}

		}
		if((x+1) < log.getSize()){
			if(uB[x+1][y] != null && uB[x+1][y].getOwner()==pNum){
				retVal = true;
			}
			if((y-1) >= 0){
				if(uB[x-1][y-1] != null && uB[x-1][y-1].getOwner()==pNum){
					retVal = true;
				}
			}
			if((y+1)< log.getSize()){
				if(uB[x-1][y+1] != null && uB[x-1][y+1].getOwner()==pNum){
					retVal = true;
				}
			}
		}
		if((x+2) < log.getSize()){
			if(uB[x+2][y] != null && uB[x+2][y].getOwner()==pNum){
				retVal = true;
			}
		}

		if((y-1) >= 0){
			if(uB[x][y-1] != null && uB[x][y-1].getOwner()==pNum){
				retVal = true;
			}
			if((x-1) >= 0){
				if(uB[x-1][y-1] != null && uB[x-1][y-1].getOwner()==pNum){
					retVal = true;
				}
			}
			if((x+1)< log.getSize()){
				if(uB[x+1][y-1] != null && uB[x+1][y-1].getOwner()==pNum){
					retVal = true;
				}
			}
		}
		if((y-2) >= 0){
			if(uB[x][y-2] != null && uB[x][y-2].getOwner()==pNum){
				retVal = true;
			}

		}
		if((y+1) < log.getSize()){
			if(uB[x][y+1] != null && uB[x][y+1].getOwner()==pNum){
				retVal = true;
			}
			if((x-1) >= 0){
				if(uB[x-1][y-1] != null && uB[x-1][y-1].getOwner()==pNum){
					retVal = true;
				}
			}
			if((x+1)< log.getSize()){
				if(uB[x+1][y-1] != null && uB[x+1][y-1].getOwner()==pNum){
					retVal = true;
				}
			}
		}
		if((x+2) < log.getSize()){
			if(uB[x+2][y] != null && uB[x+2][y].getOwner()==pNum){
				retVal = true;
			}
		}

		return retVal;

	}



	/***************************************************************
	 * 
	 * 
	 * @return 
	 ***************************************************************/
	private boolean moveInPacks(Unit pUnit){
		char[][] moves = log.getMoves(pUnit);
		int origX, origY;
		boolean hasMoved = false;

		int[] unitLoc = getUnitLocation(pUnit);
		origX = unitLoc[0];
		origY = unitLoc[1];



		for(int i = 0; i < log.getSize(); i++)
			for(int j = 0; j < log.getSize(); j++){
				if(moves[i][j] == 'x')
					if(hasFriendliesNearby(i, j)){
						int[] hqLoc = getHQLoc();
						if(getDistance(origX, origY, hqLoc[0], hqLoc[1]) <
								getDistance(i, j, hqLoc[0], hqLoc[1])){
							log.moveUnit(pUnit, i, j);
							move = "move,"+pUnit.getName()+","+origX+","+origY+","+i+","+j;
							hasMoved = true;
						}
					}
			}

		return hasMoved;
	}

	/***************************************************************
	 * 
	 * 
	 * @return 
	 ***************************************************************/
	private int[] getHQLoc(){
		int[] retVal = new int[2];
		Tile[][] tBoard = log.getTBoard();

		l1: for(int i = 0; i < log.getSize(); i++)
			for(int j = 0; j < log.getSize(); j++){
				if((tBoard[i][j].getType() == 'H' || tBoard[i][j].getType()=='h')
						&& tBoard[i][j].getOwner() == getPNum()){
					retVal[0] = i;
					retVal[1] = j;
					break l1;
				}
			}

		return retVal;
	}


	/***************************************************************
	 * Moves the unit to an uncaptured building.
	 * The parameter 'desperation' is for the AI to specify whether
	 * it's desperate to capture a building or not.  If it is, it will
	 * ignore the fact that the building location is not safe from enemy
	 * units.
	 * If desperation is false, the AI will not move to that building
	 * location if it's not safe.  By safe, I mean not one enemy unit
	 * will be able to reach that building within a turn.  This includes
	 * the four spots N,E,S,W around the building as well.
	 * 
	 * @return ArrayList<Unit> - list of units moved.
	 ***************************************************************/
	private boolean moveToUncaptured(Unit pUnit, boolean enemyBuildings, boolean desperation){
		char[][] buildingsBoard = getUncapturedBuildings();
		boolean foundSafe = false;
		int origX = 0, origY = 0;
		boolean moved = false;

		int closest = 999;
		int[] temp = getUnitLocation(pUnit);
		l1: for(int k = 0; k < log.getSize(); k++)
			for(int u = 0; u < log.getSize(); u++){
				if(buildingsBoard[k][u] == 'x'){
					origX = k;
					origY = u;
					if(getDistance(temp[0], temp[1], origX, origY) <= closest){
						closest = getDistance(temp[0], temp[1], origX, origY);
					}
					break l1;
				}
			}


		temp = moveTowardLocation(pUnit, origX, origY, true,  false);
		if(temp[0] != origX && temp[1] != origY){
			foundSafe = true;
		}
		origX = temp[0];
		origY = temp[1];

		if(foundSafe == true){
			log.moveUnit(pUnit, origX, origY);
			moved = true;
		}
		else if(desperation == true){
			log.moveUnit(pUnit, origX, origY);
			moved = true;
		}


		return moved;
	}


	/***************************************************************
	 * This tells the AI if moving to that certain location is safe.
	 * Mainly used for deciding whether or not to go try to capture 
	 * a building, but it could be used for all other moves as well.
	 * In the chess game of war, this method should really be expanded
	 * to the number of turns it will take an enemy to reach the 
	 * specified location.  Right now it only looks one turn into the
	 * future.
	 ***************************************************************/
	private boolean isItSafe(int x, int y){
		Unit[][] unitBoard = log.getUB();
		boolean retVal = true;
		for(int i = 0; i < log.getSize(); i++)
			for(int j = 0; j < log.getSize(); j++){
				if(unitBoard[i][j] != null && unitBoard[i][j].getOwner()!=getPNum()){
					char[][] moves = log.getMoves(unitBoard[i][j]);
					if(moves[x][y] == 'x'){
						if(x > 0 && moves[x-1][y] == 'x'){
							retVal = false;
						}else if(x < log.getSize()-1 && moves[x+1][y] == 'x'){
							retVal = false;
						}else if(y > 0 && moves[x][y-1] == 'x'){
							retVal = false;
						}else if(y < log.getSize()-1 && moves[x][y+1] == 'x'){
							retVal = false;
						}
					}
				}
			}

		return retVal;
	}





	private int[] getUnitLocation(Unit pUnit){
		int[] retVal = new int[2];
		Unit[][] uB = log.getUB();

		l1: for(int i = 0; i < log.getSize(); i++)
			for(int j = 0; j < log.getSize(); j++)
				if(uB[i][j] == pUnit){
					retVal[0] = i;
					retVal[1] = j;
					break l1;
				}

		return retVal;
	}


	protected int[] moveTowardLocation(Unit pUnit, int dX, int dY, boolean isBase,  boolean desperation){
		char[][] pMoves = log.getMoves(pUnit);
		int[] retVal = new int[2];

		retVal[1] = -1;
		retVal[2] = -1;


		int closestX = 0;
		int closestY = 0;
		Unit[][] uB = log.getUB();
		for(int i = 0; i < log.getSize(); i++)
			for(int j = 0; j < log.getSize(); j++)
				if(uB[i][j] == pUnit){
					closestX = i;
					closestY = j;
				}

		boolean foundMove = false;
		int bestMove = 999;
		//this method will move each until closer to the specified position
		l1: for (int r = 0; r < pMoves.length; r++) {
			for (int c = 0; c < pMoves.length; c++) {
				if(pMoves[r][c] == 'x'){
					if(desperation==false && isItSafe(r,c))
						if(getDistance(r, c,dX,dY) <= bestMove){
							if(r == dX && c == dY){
								if(isBase == true && pUnit.getType()==Unit.INFANTRYTYPE){
									retVal[1] = r;
									retVal[2] = c;
									foundMove = true;
									closestX = r;
									closestY = c;
									bestMove = getDistance(closestX, closestY, dX, dY);
									break l1;
								}
							}
							else{
								retVal[1] = r;
								retVal[2] = c;
								foundMove = true;
								closestX = r;
								closestY = c;
								bestMove = getDistance(closestX, closestY, dX, dY);
							}


						}
						else if(desperation==true){
							if(r == dX && c == dY && isBase){
								if(isBase==true && pUnit.getType() == Unit.INFANTRYTYPE){
									retVal[1] = r;
									retVal[2] = c;
									foundMove = true;
									closestX = r;
									closestY = c;
									bestMove = getDistance(closestX, closestY, dX, dY);
									break l1;
								}
							}
							else{
								retVal[1] = r;
								retVal[2] = c;
								foundMove = true;
								closestX = r;
								closestY = c;
								bestMove = getDistance(closestX, closestY, dX, dY);
							}
						}
				}
			}
		}

		if(foundMove){
			int[] unitLoc = getUnitLocation(pUnit);
			log.moveUnit(pUnit, closestX, closestY);
			move = "move,"+pUnit.getName()+","+unitLoc[0]+","+unitLoc[1]+","+closestX+","+closestY;
			moveLogger.add(move);
		}

		return retVal;
	}


	/***************************************************************
	 * Moves the unit 
	 * For the AI this means a is the hQ and b is the unit
	 ***************************************************************/
	protected boolean moveCloserToEnemies(Unit pUnit, boolean desperation) {
		boolean hasMoved = false;
		//ArrayList<Unit> toMove = getPossibleMoves(); 
		Tile[][] tBoard = log.getTBoard();
		int hx = 0;
		int hy = 0;

		//pmoves represents the + and - of where the unit can move 
		//at its current X and Y location!
		char[][] pMoves = log.getMoves(pUnit);

		//Sets hx and hy which gets the location of enemies HQ
		for (int r = 0; r < log.getSize(); r++) {
			for (int c = 0; c < log.getSize(); c++) {
				if (tBoard[r][c].getType() == 'h') {
					hx = r;
					hy = c;
				}
			}
		}

		int closestX = 0;
		int closestY = 0;
		Unit[][] uB = log.getUB();
		for(int i = 0; i < log.getSize(); i++)
			for(int j = 0; j < log.getSize(); j++)
				if(uB[i][j] == pUnit){
					closestX = i;
					closestY = j;
				}


		boolean foundMove = false;
		int bestMove = 999;
		//this method will move each until closer to the HQ
		for (int r = 0; r < pMoves.length; r++) {
			for (int c = 0; c < pMoves.length; c++) {
				if(pMoves[r][c] == 'x'){
					if(desperation==false && isItSafe(r,c))
						if(getDistance(r, c,hx,hy) <= bestMove){
							foundMove = true;
							closestX = r;
							closestY = c;
							bestMove = getDistance(closestX, closestY, hx, hy);
						}
						else if(desperation==true){
							foundMove = true;
							closestX = r;
							closestY = c;
							bestMove = getDistance(closestX, closestY, hx, hy);
						}
				}
			}
		}
		if(foundMove){
			int[] unitLoc = getUnitLocation(pUnit);
			log.moveUnit(pUnit, closestX, closestY);
			move = "move,"+pUnit.getName()+","+unitLoc[0]+","+unitLoc[1]+","+closestX+","+closestY;
			moveLogger.add(move);
			hasMoved = true;
		}

		return hasMoved;

	}

	/***************************************************************
	 * Gets the distance from point a (x1, y1) to point b (x2, y2) 
	 * For the AI this means a is the hQ and b is the unit
	 ***************************************************************/
	protected int getDistance(int x1, int y1, int x2, int y2) {
		//while this returns a double we don't need to deal with decimals for our purposes
		int distance = (int) Math.sqrt( Math.pow((x2 - x1), 2) + Math.pow( (y2-y1), 2));

		return distance;
	}


	/**************************************************************************
	 * Method that controls the attack phase of an AI players turn
	 * The basic structure of the attack phase is as follows:
	 * -- Find enemies within attack range
	 * ----- 1. find current tile
	 * ----- 2. look at all surrounding tiles
	 * ----- 3. create an array of enemies it can attack
	 * ----- 4. compare the potential damage dealt to each
	 * ----- 5. attack enemy you do most damage too
	 *  Does not take into consideration ranged attacks
	 *  Consider for looping direction checks
	 *************************************************************************/
	protected void attack() {
		Unit[][] uBoard = log.getUB();
		for(Unit currentUnit: unitsWithAttacks){
			ArrayList<Unit> potentialUnitsToAttack = new ArrayList<Unit>();
			ArrayList<Integer> damageToUnit = new ArrayList<Integer>();

			int currUnitXPosition = currentUnit.getX();
			int currUnitYPosition = currentUnit.getY();

			//look left
			if(uBoard[currUnitXPosition -1][currUnitYPosition] != null && uBoard[currUnitXPosition -1][currUnitYPosition].getOwner() != getPNum()){
				Unit unitToAttack = uBoard[currUnitXPosition -1][currUnitYPosition];
				int potentialDamage = log.damage(currentUnit, unitToAttack);
				potentialUnitsToAttack.add(unitToAttack);
				damageToUnit.add(potentialDamage);
			}
			//look right
			if(uBoard[currUnitXPosition +1][currUnitYPosition] != null && uBoard[currUnitXPosition +1][currUnitYPosition].getOwner() != getPNum()){
				Unit unitToAttack = uBoard[currUnitXPosition +1][currUnitYPosition];
				int potentialDamage = log.damage(currentUnit, unitToAttack);
				potentialUnitsToAttack.add(unitToAttack);
				damageToUnit.add(potentialDamage);
			}
			//look up
			if(uBoard[currUnitXPosition][currUnitYPosition +1] != null && uBoard[currUnitXPosition][currUnitYPosition +1].getOwner() != getPNum()){
				Unit unitToAttack = uBoard[currUnitXPosition][currUnitYPosition -1];
				int potentialDamage = log.damage(currentUnit, unitToAttack);
				potentialUnitsToAttack.add(unitToAttack);
				damageToUnit.add(potentialDamage);
			}
			//look down
			if(uBoard[currUnitXPosition][currUnitYPosition -1] != null && uBoard[currUnitXPosition][currUnitYPosition -1].getOwner() != getPNum()){
				Unit unitToAttack = uBoard[currUnitXPosition][currUnitYPosition -1];
				int potentialDamage = log.damage(currentUnit, unitToAttack);
				potentialUnitsToAttack.add(unitToAttack);
				damageToUnit.add(potentialDamage);
			}

			//determine which unit to attack if there is more than one possible
			if(potentialUnitsToAttack.size() > 1){

				//check to see what attack would be most effective
				Unit bestUnitToAttack = potentialUnitsToAttack.get(0); //start with the first unit
				Integer bestPotentialDamageInflicted = damageToUnit.get(0);  //get first units damage

				for(int i = 1; i < potentialUnitsToAttack.size(); i++){ //start at second unit
					if(damageToUnit.get(i) >  bestPotentialDamageInflicted){
						bestUnitToAttack = potentialUnitsToAttack.get(i); //change unit to attack;
						bestPotentialDamageInflicted = damageToUnit.get(i); //change potentialDamageInflicted
					}
				}

				if(bestPotentialDamageInflicted >= ATTACK_DAMAGE_THRESHOLD){
					log.battle(currentUnit, bestUnitToAttack, currentUnit.getOwner());
					move = "attack,"+currentUnit.getName()+","+bestUnitToAttack.getName();
					moveLogger.add(move);
				}
			}
			else if(potentialUnitsToAttack.size() == 1){
				if(damageToUnit.get(0) >= ATTACK_DAMAGE_THRESHOLD){
					log.battle(currentUnit, potentialUnitsToAttack.get(0), currentUnit.getOwner());
					move = "attack,"+currentUnit.getName()+","+potentialUnitsToAttack.get(0).getName();
					moveLogger.add(move);
				}
			}
			else {
				//no attack possible, do nothing
			}

		}
	}

	/**************************************************************************
	 * Method that controls the capture phase of an AI players turn
	 * The basic structure of the capture phase is as follows:
	 * -- Find buildings within capture range
	 * ----- 1. find current tile
	 * ----- 2. look at all surrounding tiles
	 * ----- 3. create an array of buildings it can capture
	 * ----- 4. compare the potential damage dealt to each
	 * ----- 5. attack enemy you do most damage too
	 *  Does not take into consideration ranged attacks
	 *  Consider for looping direction checks
	 *  
	 *  if any of my units are positioned on a building
	 *************************************************************************/
	protected void capture(){
		Tile[][] tBoard = log.getTBoard();
		for(Unit currentUnit: unitsWithCaptures){
			ArrayList<Tile> potentialBuildingsToCapture = new ArrayList<Tile>();

			int currUnitXPosition = currentUnit.getX();
			int currUnitYPosition = currentUnit.getY();

			//look left
			if(tBoard[currUnitXPosition -1][currUnitYPosition] != null && tBoard[currUnitXPosition -1][currUnitYPosition].getOwner() != getPNum()){
				Tile buildingToCapture = tBoard[currUnitXPosition -1][currUnitYPosition];
				potentialBuildingsToCapture.add(buildingToCapture);
			}
			//look right
			if(tBoard[currUnitXPosition +1][currUnitYPosition] != null && tBoard[currUnitXPosition +1][currUnitYPosition].getOwner() != getPNum()){
				Tile buildingToCapture = tBoard[currUnitXPosition +1][currUnitYPosition];
				potentialBuildingsToCapture.add(buildingToCapture);
			}
			//look up
			if(tBoard[currUnitXPosition][currUnitYPosition +1] != null && tBoard[currUnitXPosition][currUnitYPosition +1].getOwner() != getPNum()){
				Tile buildingToCapture = tBoard[currUnitXPosition][currUnitYPosition +1];
				potentialBuildingsToCapture.add(buildingToCapture);
			}
			//look down
			if(tBoard[currUnitXPosition][currUnitYPosition -1] != null && tBoard[currUnitXPosition][currUnitYPosition -1].getOwner() != getPNum()){
				Tile buildingToCapture = tBoard[currUnitXPosition][currUnitYPosition -1];
				potentialBuildingsToCapture.add(buildingToCapture);
			}

			//determine which building to capture if there is more than one possible
			if(potentialBuildingsToCapture.size() > 1){

				//check to see what building would be best to capture
				Tile bestBuildingToCapture = potentialBuildingsToCapture.get(0); //start with the first unit

				//				for(possibleCaptures){ //start at second unit
				//					whatever determines what building to chose
				//				}

				log.captureBuilding(this, bestBuildingToCapture.getX(), bestBuildingToCapture.getY());
			}
			else if(potentialBuildingsToCapture.size() == 1){
				log.captureBuilding(this, potentialBuildingsToCapture.get(0).getX(), potentialBuildingsToCapture.get(0).getY());
			}
			else {
				//no capture possible, do nothing
			}

		}
	}

	protected void getLogic(Logic pLog) {
		log = pLog;
		size = log.getSize();
	}

	protected ArrayList<Unit> getPossibleMoves() {
		Unit[][] uBoard = log.getUB();

		ArrayList<Unit> unitsWithMoves = new ArrayList<Unit>();

		//search unit board for units
		for(int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				//checks if current tile isn't empty and if I own the unit
				if (uBoard[row][col] != null && uBoard[row][col].getOwner() == getPNum()) {
					//checks to see if the unit has already moved this turn
					if(!uBoard[row][col].getHasMoved()){
						unitsWithMoves.add(uBoard[row][col]); //adds unit to our available moves
					}
				}
			}
		}
		return unitsWithMoves;
	}

	protected ArrayList<Unit> getPossibleAttacks() {
		Unit[][] uBoard = log.getUB();

		ArrayList<Unit> unitsWithAttacks = new ArrayList<Unit>();

		//search unit board for units
		for(int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				//checks if current tile isn't empty and if I own the unit
				if (uBoard[row][col] != null && uBoard[row][col].getOwner() == getPNum()) { //must check tile type!!
					//checks to see if the unit has already attacked this turn
					if(!uBoard[row][col].getHasAttacked()){
						unitsWithAttacks.add(uBoard[row][col]); //adds unit to our available attacks
					}
				}
			}
		}
		return unitsWithAttacks;
	}

	protected ArrayList<Unit> getPossibleCaptures() {
		Unit[][] uBoard = log.getUB();
		Tile[][] tBoard = log.getTBoard();

		ArrayList<Unit> unitsWithCaptures = new ArrayList<Unit>();

		//search unit board for units
		for(int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				//checks if current tile isn't empty and if I own the unit
				if (uBoard[row][col] != null && uBoard[row][col].getOwner() == getPNum()) { //must check tile type!!
					//checks to see if the unit has already captured this turn
					if(!uBoard[row][col].getHasCaptured()){
						//Check to see whether the tile that unit is on is an unclaimed base 'b' or unclaimed production 'p'
						if(tBoard[row][col].getType() == 'b' || tBoard[row][col].getType() == 'p'){
							unitsWithCaptures.add(uBoard[row][col]); //adds unit to our available captures
						}
					}
				}
			}
		}
		return unitsWithCaptures;
	}

	/************************************************************************
	 * AI decides what units to create.
	 * It first creates a wishlist of all the units it would like to have
	 * Then goes through that list and adds the units that it has enough money
	 * to make into the canBuild list.
	 ***********************************************************************/
	protected ArrayList<Unit> prodUnits() {
		Tile[][] map = log.getTBoard();

		// AI wish list
		ArrayList<Unit> wantToBuild = new ArrayList<Unit>();

		// List of units the AI has enough money to build
		ArrayList<Unit> canBuild = new ArrayList<Unit>();

		for (int r = 0; r < size; r++) {
			for (int c = 0; c < size; c++) {
				if (map[r][c].getType() == 'p' &&
						map[r][c].getOwner() == playNum) {
					int[] unitsToBuild = counterEnemyUnits();
					int most = 0, secondMost = 0, thirdMost = 0, fourthMost = 0;
					for(int i = 0; i < unitsToBuild.length; i++){
						if(unitsToBuild[i] > most){
							fourthMost = thirdMost;
							thirdMost = secondMost;
							secondMost = most;
							most = unitsToBuild[i];
						}
					}
					wantToBuild.add(createMeAUnit(most));
					wantToBuild.add(createMeAUnit(secondMost));
					wantToBuild.add(createMeAUnit(thirdMost));
					wantToBuild.add(createMeAUnit(fourthMost));
				}
			}
		}
		
		int cash = getCash();
		for(Unit bUnit: wantToBuild){
			if(cash > bUnit.getCost()){
				canBuild.add(bUnit);
				cash -= bUnit.getCost();
			}
		}
		return canBuild;
	}


	/************************************************************************
	 * This method actually builds a certain unit specified by that parameter
	 * This is used by the prodUnits method as a way of keeping track of the 
	 * units it would like to build.  Whether or not it has enough money
	 * is not taken into consideration.  
	 * 
	 * @return Unit - The unit the AI wants to build
	 ***********************************************************************/
	private Unit createMeAUnit(int type){
		if(type==types.ANTIAIR.ordinal()){
			return new AntiAir(-1);
		}
		else if(type == types.APC.ordinal()){
			return new APC(-1);
		}else if(type == types.ARTILLERY.ordinal()){
			return new Artillery(-1);
		}else if(type == types.BOMBER.ordinal()){
			return new Bomber();
		}else if(type == types.CHOPPERA.ordinal()){
			return new ChopperA();
		}else if(type == types.CHOPPERB.ordinal()){
			return new ChopperB();
		}else if(type == types.FIGHTERJET.ordinal()){
			return new FighterJet(-1);
		}else if(type == types.HEAVYTANK.ordinal()){
			return new APC(-1);
		}else if(type == types.INFANTRY.ordinal()){
			return new APC(-1);
		}else if(type == types.MECH.ordinal()){
			return new APC(-1);
		}else if(type == types.MEDTANK.ordinal()){
			return new APC(-1);
		}else if(type == types.MISSILE.ordinal()){
			return new APC(-1);
		}else if(type == types.RECON.ordinal()){
			return new APC(-1);
		}else if(type == types.ROCKETS.ordinal()){
			return new APC(-1);
		}else if(type == types.TANK.ordinal()){
			return new APC(-1);
		}else{
			return null;
		}
	}


	/************************************************************************
	 * Counts the number of "free" buildings on the map
	 * This is used to tell the AI whether or not it should build more
	 * units to capture or not
	 * @return int num (of uncaptured buildings)
	 ***********************************************************************/
	protected char[][] getUncapturedBuildings(){
		Tile[][] mapBoard = log.getTBoard();
		char[][] openBuildings = new char[log.getSize()][log.getSize()];

		for(int i = 0; i < log.getSize(); i++)
			for(int j = 0; j < log.getSize(); j++){
				if(mapBoard[i][j].getType() == 'b' &&mapBoard[i][j].getOwner()==-1){
					openBuildings[i][j] = 'x';
				}
				else{
					openBuildings[i][j] = '-';
				}
			}


		return openBuildings;
	}

	/************************************************************************
	 * Calculates the difference in unit weights and counts
	 * retVal[0] is the difference in unit weights.  
	 * <0, Opponent army is stronger
	 * 
	 * retVal[1] is the difference in unit count
	 * <0, Opponent has more units
	 * 
	 * @return int[] retVal:
	 ***********************************************************************/
	protected int[] getUnitWeights() {

		// Element 
		int[] retVal= new int[2];
		retVal[0] = 0;
		retVal[1] = 0;

		Unit[][] unitBoard = log.getUB();

		for(int i = 0; i<log.getSize(); i++)
			for(int j = 0; j < log.getSize(); j++){
				if(unitBoard[i][j] != null){
					if(unitBoard[i][j].getOwner() == this.getPNum()){
						retVal[0] += unitBoard[i][j].getArmor();
						retVal[1]++;
					}
					else{
						retVal[0] -= unitBoard[i][j].getArmor();
						retVal[1]--;
					}
				}


			}


		return retVal;
	}

	/************************************************************************
	 * Counts the number of AI economy buildings vs the opponent's.
	 * Returns negative if AI has less, Positive if AI has more.
	 * 0 for equal
	 * 
	 * @return int - Disparity in economy buildings
	 ***********************************************************************/
	protected int countEconBuildings(){
		int retVal = 0;
		Tile[][] board = log.getTBoard();

		for(int i = 0; i < log.getSize(); i++)
			for(int j = 0; j < log.getSize(); j++){
				if(board[i][j].getType() == 'x' || board[i][j].getType() == 'X'){
					if(board[i][j].getOwner() == getPNum()){
						retVal++;
					}
					else{
						retVal--;
					}
				}
			}
		return retVal;
	}


	/************************************************************************
	 * AI looks at building units to counter enemy units.
	 * Array holds ints for each unit in alphabetical order.
	 * For every unit that the enemy has, the counter to that unit is inc'd
	 * in the corresponding array index.
	 * Example:  Enemy has fighter jet.  Anti-Air, array index of 1, is inc'd
	 * @return int[] units
	 ***********************************************************************/
	protected int[] counterEnemyUnits(){
		int[] counters = new int[19];
		Arrays.fill(counters, 0);
		Unit[][] unitBoard = log.getUB();
		String unitType;


		for(int i = 0; i<log.getSize(); i++)
			for(int j = 0; j < log.getSize(); j++)
				if(unitBoard[i][j] != null)
					if(unitBoard[i][j].getOwner() != this.getPNum()){
						unitType = unitBoard[i][j].getName();

						if(unitType == "Anti-Air"){
							counters[types.TANK.ordinal()]++;
							counters[types.MEDTANK.ordinal()]++;
							counters[types.HEAVYTANK.ordinal()]++;
							counters[types.BATTLESHIP.ordinal()]++;
							//}else if(unitType == "APC"){
							//Do we really need to counter APCs?!?!
						}else if(unitType == "Artillery"){
							counters[types.TANK.ordinal()]++;
							counters[types.MEDTANK.ordinal()]++;
							counters[types.HEAVYTANK.ordinal()]++;
							counters[types.ARTILLERY.ordinal()]++;
							counters[types.BOMBER.ordinal()]++;
							counters[types.ROCKETS.ordinal()]++;
							counters[types.MECH.ordinal()]++;
						}else if(unitType == "Bomber"){
							counters[types.ANTIAIR.ordinal()]++;
							counters[types.MISSILE.ordinal()]++;
							counters[types.FIGHTERJET.ordinal()]++;
						}else if(unitType == "CHOPPERB"){
							counters[types.ANTIAIR.ordinal()]++;
							counters[types.MISSILE.ordinal()]++;
							counters[types.CRUISER.ordinal()]++;
							counters[types.CHOPPERA.ordinal()]++;
							counters[types.FIGHTERJET.ordinal()]++;
						}else if(unitType == "CHOPPERA"){
							counters[types.ANTIAIR.ordinal()]++;
							counters[types.MISSILE.ordinal()]++;
							counters[types.CRUISER.ordinal()]++;
							counters[types.FIGHTERJET.ordinal()]++;
						}else if(unitType == "Fighter Jet"){
							counters[types.ANTIAIR.ordinal()]++;
							counters[types.MISSILE.ordinal()]++;
							counters[types.CRUISER.ordinal()]++;
							counters[types.FIGHTERJET.ordinal()]++;
						}else if(unitType == "Heavy Tank"){
							counters[types.BOMBER.ordinal()]++;
							counters[types.HEAVYTANK.ordinal()]++;
						}else if(unitType == "Infantry"){
							counters[types.RECON.ordinal()]++;
							counters[types.ANTIAIR.ordinal()]++;
							counters[types.TANK.ordinal()]++;
							counters[types.MEDTANK.ordinal()]++;
							counters[types.HEAVYTANK.ordinal()]++;
							counters[types.ARTILLERY.ordinal()]++;
							counters[types.BATTLESHIP.ordinal()]++;
							counters[types.CHOPPERA.ordinal()]++;
							counters[types.BOMBER.ordinal()]++;
						}else if(unitType == "Mech"){
							counters[types.ANTIAIR.ordinal()]++;
							counters[types.TANK.ordinal()]++;
							counters[types.MEDTANK.ordinal()]++;
							counters[types.HEAVYTANK.ordinal()]++;
							counters[types.ARTILLERY.ordinal()]++;
							counters[types.ROCKETS.ordinal()]++;
							counters[types.BATTLESHIP.ordinal()]++;
							counters[types.CHOPPERA.ordinal()]++;
							counters[types.BOMBER.ordinal()]++;
						}else if(unitType == "MedTank"){
							counters[types.HEAVYTANK.ordinal()]++;
							counters[types.MEDTANK.ordinal()]++;
							counters[types.ROCKETS.ordinal()]++;
							counters[types.BATTLESHIP.ordinal()]++;
							counters[types.BOMBER.ordinal()]++;
						}else if(unitType == "Missile"){
							counters[types.MECH.ordinal()]++;
							counters[types.TANK.ordinal()]++;
							counters[types.MEDTANK.ordinal()]++;
							counters[types.HEAVYTANK.ordinal()]++;
							counters[types.ARTILLERY.ordinal()]++;
							counters[types.ROCKETS.ordinal()]++;
							counters[types.BATTLESHIP.ordinal()]++;
							counters[types.BOMBER.ordinal()]++;
						}else if(unitType == "Recon"){
							counters[types.MECH.ordinal()]++;
							counters[types.ANTIAIR.ordinal()]++;
							counters[types.TANK.ordinal()]++;
							counters[types.MEDTANK.ordinal()]++;
							counters[types.HEAVYTANK.ordinal()]++;
							counters[types.ARTILLERY.ordinal()]++;
							counters[types.ROCKETS.ordinal()]++;
							counters[types.BATTLESHIP.ordinal()]++;
							counters[types.BOMBER.ordinal()]++;
						}else if(unitType == "Rockets"){
							counters[types.MECH.ordinal()]++;
							counters[types.TANK.ordinal()]++;
							counters[types.MEDTANK.ordinal()]++;
							counters[types.HEAVYTANK.ordinal()]++;
							counters[types.ARTILLERY.ordinal()]++;
							counters[types.ROCKETS.ordinal()]++;
							counters[types.BATTLESHIP.ordinal()]++;
							counters[types.CHOPPERA.ordinal()]++;
							counters[types.BOMBER.ordinal()]++;
						}else if(unitType == "Tank"){
							counters[types.TANK.ordinal()]++;
							counters[types.MEDTANK.ordinal()]++;
							counters[types.HEAVYTANK.ordinal()]++;
							counters[types.ARTILLERY.ordinal()]++;
							counters[types.ROCKETS.ordinal()]++;
							counters[types.BATTLESHIP.ordinal()]++;
							counters[types.CHOPPERA.ordinal()]++;
							counters[types.BOMBER.ordinal()]++;
							counters[types.MECH.ordinal()]++;
						}//else if(unitType == ""){

						//}


					}



		return counters;
	}


	/************************************************************************
	 * Update unitsWithMoves arrayList and determine if moves are available
	 * @return boolean canIMove
	 ***********************************************************************/
	protected boolean canIMove(){
		boolean moveAvailable = false;
		//find moves
		unitsWithMoves = getPossibleMoves();
		if(unitsWithMoves.size() > 0) {
			moveAvailable = true;
		}
		return moveAvailable;
	}

	/************************************************************************
	 * Update unitsWithAttacks arrayList and determine if attacks are available
	 * @return boolean canIAttack
	 ***********************************************************************/
	protected boolean canIAttack(){
		boolean attackAvailable = false;
		//find attacks
		unitsWithAttacks = getPossibleAttacks();
		if(unitsWithAttacks.size() > 0){
			attackAvailable = true;
		}
		return attackAvailable;
	}

	protected boolean canIBuy(){
		boolean buyAvailable = false;
		//find buys
		if(findUnitsICanAfford().size() > 0){
			buyAvailable = true;
		}

		return buyAvailable;
	}

	protected boolean canICapture(){
		boolean captureAvailable = false;
		//find captures
		unitsWithCaptures = getPossibleCaptures();
		if(unitsWithCaptures.size() > 0){
			captureAvailable = true;
		}
		return captureAvailable;
	}
	
	private void test(){
		boolean finished = false;
		
		// creating Logic - NECESSARY
		log = new Logic("test", 'b', 'r', "Puny Human", "Herp Derp", true);
		
		// create some random units
		log.setUnit(4, 5, new Infantry(1));
		log.setUnit(10, 8, new MedTank(1));
		log.setUnit(13, 3, new Mech(1));
		log.setUnit(21, 18, new HeavyTank(1));
		
		log.setUnit(29, 27, new MedTank(2));
		log.setUnit(27, 26, new Infantry(2));
		log.setUnit(23, 28, new HeavyTank(2));
		log.setUnit(28, 28, new Rockets(2));
		move = "Game started";
		System.out.println(move);
		Scanner scan = new Scanner(move);
		while(!finished){
			startTurn();
			for(String s: moveLogger){
				System.out.println(s);
				scan.nextLine();
			}
			
		}
	}
	
	private static void waitOneSec(){
		try {
			Thread.currentThread();
			Thread.sleep(1200);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		new AI("Herp, Derp", 2, 'b');
	}

	public ArrayList<String> getActions() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
