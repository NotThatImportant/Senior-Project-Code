package player;


import gameplay.Logic;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

import terrain.Tile;
import units.APC;
import units.AntiAir;
import units.Artillery;
import units.Bomber;
import units.Chopper;
import units.FighterJet;
import units.HeavyTank;
import units.Infantry;
import units.Mech;
import units.MedTank;
import units.Recon;
import units.Rockets;
import units.Tank;
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

	private int[] unitTracker;

	private ArrayList<String> moveLogger; 
	String lastAction;

	Logic log;
	int size;

	enum types {
		ANTIAIR, APC, ARTILLERY, BOMBER, CHOPPER, FIGHTERJET, HEAVYTANK, INFANTRY, MECH, MEDTANK,
		MISSILE, RECON, ROCKETS, TANK
	};

	// Default Constructor
	public AI(String pN, int pNum, char fact) {
		super(pN, pNum, fact);	

		lastAction = "";
		moveLogger = new ArrayList<String>();
		unitTracker = new int[types.values().length];

	}
	
	// Test Constructor
	public AI(String pN, int pNum, char fact, boolean test){
		super(pN, pNum, fact);	



		lastAction = "";
		moveLogger = new ArrayList<String>();
		unitTracker = new int[types.values().length];

		test();
		
	}

	/***************************************************************
	 * This is the method that does all of the moving/attacking/stuff
	 * for the AI. This and getBoard are the only ones that really
	 * need to be called for the AI to do something
	 * 
	 ***************************************************************/
	public void startTurn(){
		moveLogger.clear();

		//determine possible actions
		availableMove = canIMove();
		availableAttack = canIAttack();
		availablePurchase = canIBuy();
		availableCapture = canICapture();

		ArrayList<Unit> toMove = getPossibleMoves();
		if(availableCapture){
			capture();
		}
		if(availableAttack){
			attack();
		}


		//Loop through possible actions
		for(Unit actionUnit: toMove){

			// If it's possible to attack at the beginning of the turn, then do it
			if(availableAttack){
				attack();
			}
			if(availableCapture){
				capture();
			}

			//move units
			boolean moved = false;

			// Printing out preliminary move info and moves board
			System.out.println("Before Move");
			System.out.println(actionUnit.getName() +": " );
			System.out.println("Starting Location: " + actionUnit.getX() + " " + actionUnit.getY());
			char[][] pMoves = log.getMoves(actionUnit);
			for(int i = 0; i < log.getSize(); i++){
				if(i < 10){
					System.out.print(i + "  ");
				}else{
					System.out.print(i + " ");
				}
				for(int j = 0; j < log.getSize(); j++)
					System.out.print(pMoves[i][j] + " ");
				System.out.println();
			}
			
			// We have more econ, let's be aggressive
			if(countEconBuildings() >0){
				if(moveCloserToEnemies(actionUnit, false) == true){
					moved = true;
				}				

				if(!moved){
					if(countUncapturedBuildings() > 0)
						moved = moveToUncaptured(actionUnit, true, false);
					else
						moved = moveToUncaptured(actionUnit, false, false);
				}

				if(!moved){
					moved = moveCloserToEnemies(actionUnit, true);
				}
				if(!moved){
					if(countUncapturedBuildings() > 0)
						moved = moveToUncaptured(actionUnit, true, false);
					else
						moved = moveToUncaptured(actionUnit, false, false);
				}
			}
			// We don't have more econ
			else{

				if(moveToUncaptured(actionUnit, false, false)){

					moved = true;
				}
				if(!moved){
					moved = moveCloserToEnemies(actionUnit, false);

				}
				if(!moved){
					moved = moveToUncaptured(actionUnit, true, true);
				}
				if(!moved){
					moved = moveCloserToEnemies(actionUnit, true);
				}
			}

			if(!moved){

				moved = moveCloserToEnemies(actionUnit, false);
			}
			if(!moved){
				moved = moveCloserToEnemies(actionUnit, true);

			}

			// Printing out where the unit went 
			System.out.println(actionUnit.getName() +": " );
			pMoves = log.getMoves(actionUnit);
			for(int i = 0; i < log.getSize(); i++){
				if(i < 10){
					System.out.print(i + "  ");
				}else{
					System.out.print(i + " ");
				}
				for(int j = 0; j < log.getSize(); j++)
					System.out.print(pMoves[i][j] + " ");
				System.out.println();
			}

			//moveToUncaptured(actionUnit, true, false);

		}
		if(availableCapture){
			capture();
		}
		if(availableAttack){
			attack();
		}
		if(availablePurchase){
			ArrayList<Unit> unitsToBuild = prodUnits();

			System.out.println("Units to build: ");
			for(Unit a: unitsToBuild){
				System.out.println("\t" +a.getName());
			}

			/// Find me a production building!
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
				lastAction = "produce,"+bU.getName()+","+t.getX()+","+t.getY();
				moveLogger.add(lastAction);
				if(success){
					counter++;
					String unitType = bU.getName();
					if(unitType.equalsIgnoreCase("Anti-Air")){
						unitTracker[types.ANTIAIR.ordinal()]++;
					}else if(unitType.equalsIgnoreCase("Artillery")){
						unitTracker[types.ARTILLERY.ordinal()]++;
					}else if(unitType.equalsIgnoreCase("Bomber")){
						unitTracker[types.BOMBER.ordinal()]++;
					}else if(unitType.equalsIgnoreCase("CHOPPER")){
						unitTracker[types.CHOPPER.ordinal()]++;
					}else if(unitType.equalsIgnoreCase("Fighter Jet")){
						unitTracker[types.FIGHTERJET.ordinal()]++;
					}else if(unitType.equalsIgnoreCase("Heavy Tank")){
						unitTracker[types.HEAVYTANK.ordinal()]++;
					}else if(unitType.equalsIgnoreCase("Infantry")){
						unitTracker[types.INFANTRY.ordinal()]++;
					}else if(unitType.equalsIgnoreCase("Mech")){
						unitTracker[types.MECH.ordinal()]++;
					}else if(unitType.equalsIgnoreCase("MedTank")){
						unitTracker[types.MEDTANK.ordinal()]++;
					}else if(unitType.equalsIgnoreCase("Missile")){
						unitTracker[types.MISSILE.ordinal()]++;
					}else if(unitType.equalsIgnoreCase("Recon")){
						unitTracker[types.RECON.ordinal()]++;
					}else if(unitType.equalsIgnoreCase("Rockets")){
						unitTracker[types.ROCKETS.ordinal()]++;
					}else if(unitType.equalsIgnoreCase("Tank")){
						unitTracker[types.TANK.ordinal()]++;
					}

					if(counter >= emptyProdBldg.size()){
						break;
					}
				}
			}
		}
	}

	/***************************************************************
	 * Count the number of uncaptured buildings on the map
	 * 
	 * @return int number of uncaptured buildings
	 ***************************************************************/
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
	 * Unimplemented method.  Returns whether or not a specified 
	 * position has a friendly unit nearby.  This was going to be
	 * used so that the AI's units would move in packs.  Sort of 
	 * unpractical, I guess
	 * 
	 * @return boolean whether or not has a friendly nearby 
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
	 *  Method to determine the coordinates of the enemy's HQ
	 * 
	 * @return int[] coordinates
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
		int moveX = 0, moveY = 0;
		boolean moved = false;

		int[] temp = new int[2];
		temp[0] = pUnit.getX();
		temp[1] = pUnit.getY();

		temp = moveTowardLocation(pUnit, moveX, moveY, true,  false);
		boolean valid = false;
		if(temp[0] != -1 && temp[1] != -1){
			valid = true;
		}

		if(temp[0] != moveX && temp[1] != moveY && valid){
			foundSafe = true;
		}
		moveX = temp[0];
		moveY = temp[1];


		if(foundSafe == true && valid){
			lastAction = "move,"+pUnit.getName()+","+pUnit.getX()+","+pUnit.getY()+","+moveX+","+moveY;
			log.moveUnit(pUnit, moveX, moveY);
			moveLogger.add(lastAction);
			moved = true;
		}
		else if(desperation == true && valid){
			log.moveUnit(pUnit, moveX, moveY);
			lastAction = "move,"+pUnit.getName()+","+pUnit.getX()+","+pUnit.getY()+","+moveX+","+moveY;
			moveLogger.add(lastAction);
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
						retVal = false;
					}
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

		return retVal;
	}

	/***************************************************************
	 * This method is used for moving a specified unit as close as it
	 * possibly can to a desired location.  
	 * Can also specify if the location is a base and if they're
	 * desperate to get there.
	 * 
	 * If it's a base, you don't want a unit that can't capture to 
	 * go to that location.
	 * 
	 * If desperate is set to true, the safety of the desired spot
	 * will be disregarded
	 * 
	 * @return int[] coordinates
	 ***************************************************************/
	protected int[] moveTowardLocation(Unit pUnit, int dX, int dY, boolean isBase,  boolean desperation){

		//int origX = pUnit.getX(), origY = pUnit.getY();
		char[][] pMoves = log.getMoves(pUnit);

		int[] retVal = new int[2];
		Unit[][] unitBoard = log.getUB();

		retVal[0] = pUnit.getX();
		retVal[1] = pUnit.getY();

		int moveDistance = 0;

		int bestMove = 999;
		//this method will move each until closer to the specified position
		l1: for (int r = 0; r < pMoves.length; r++) {
			for (int c = 0; c < pMoves.length; c++) {
				if(pMoves[r][c] == 'x' && unitBoard[r][c] == null){
					// Making progress
					if(getDistance(r, c,dX,dY) < bestMove && getDistance(pUnit.getX(),pUnit.getY(), r, c) <= pUnit.getMove()){ 
						bestMove = getDistance(r,c,dX,dY);
						// Is the spot you want
						if(r == dX && c == dY){
							// If it's a base and it's safe
							if(isBase == true && isItSafe(r,c) == true && pUnit.getType()==Unit.INFANTRYTYPE){
								retVal[0] = r;
								retVal[1] = c;
								bestMove = getDistance(r, c, dX, dY);
								System.out.println("Best move so far: " + retVal[0] + " " + retVal[1] + "\nDistance: " + bestMove);
								moveDistance = getDistance(pUnit.getX(), pUnit.getY(), r, c);
								System.out.println("Move Distance : " + moveDistance);
								break l1;
							}

							// It's a base, it's not safe, but you're desperate
							else if(isBase==true && isItSafe(r,c) == false && desperation == true &&pUnit.getType() == Unit.INFANTRYTYPE){
								System.out.println("What 2");
								retVal[0] = r;
								retVal[1] = c;
								bestMove = getDistance(r, c, dX, dY);
								System.out.println("Best move so far: " + retVal[0] + " " + retVal[1] + "\nDistance: " + bestMove);
								moveDistance = getDistance(pUnit.getX(), pUnit.getY(), r, c);
								System.out.println("Move Distance : " + moveDistance);
								break l1;
							}
							// It's not a base, but it's safe
							else if(isBase == false && isItSafe(r,c) == true){
								retVal[0] = r;
								retVal[1] = c;
								bestMove = getDistance(r, c, dX, dY);
								System.out.println("Best move so far: " + retVal[0] + " " + retVal[1] + "\nDistance: " + bestMove);
								moveDistance = getDistance(pUnit.getX(), pUnit.getY(), r, c);
								System.out.println("Move Distance : " + moveDistance);
								break l1;
							}
							else if(isBase == false && isItSafe(r,c) == false && desperation == true){
								retVal[0] = r;
								retVal[1] = c;
								bestMove = getDistance(r, c, dX, dY);
								System.out.println("Best move so far: " + retVal[0] + " " + retVal[1] + "\nDistance: " + bestMove);
								moveDistance = getDistance(pUnit.getX(), pUnit.getY(), r, c);
								System.out.println("Move Distance : " + moveDistance);
								break l1;
							}
						}

						// Not the spot you want
						else{
							System.out.println("Closer Spot values: " + r + " " + c);
							if(isItSafe(r,c) == true){
								System.out.println("Safe values: " + r + " " + c);
								retVal[0] = r;
								retVal[1] = c;
								bestMove = getDistance(r, c, dX, dY);
								System.out.println("Best move so far: " + retVal[0] + " " + retVal[1] + "\nDistance: " + bestMove);
								moveDistance = getDistance(pUnit.getX(), pUnit.getY(), r, c);
								System.out.println("Move Distance : " + moveDistance);
							}
							else if(isItSafe(r,c) == false && desperation == true){
								retVal[0] = r;
								retVal[1] = c;
								bestMove = getDistance(r, c, dX, dY);
								System.out.println("Best move so far: " + retVal[0] + " " + retVal[1] + "\nDistance: " + bestMove);
								moveDistance = getDistance(pUnit.getX(), pUnit.getY(), r, c);
								System.out.println("Move Distance : " + moveDistance);
							}
							else{
								retVal[0] = r;
								retVal[1] = c;
								bestMove = getDistance(r, c, dX, dY);
								System.out.println("Best move so far: " + retVal[0] + " " + retVal[1] + "\nDistance: " + bestMove);
								moveDistance = getDistance(pUnit.getX(), pUnit.getY(), r, c);
								System.out.println("Move Distance : " + moveDistance);
							}
						}
					}
				}
			}

		}
		return retVal;

	}


	/***************************************************************
	 * Moves the unit closer to the enemy's HQ
	 * For the AI this means a is the hQ and b is the unit
	 * Ultimately uses moveTowardLocation method.
	 ***************************************************************/
	protected boolean moveCloserToEnemies(Unit pUnit, boolean desperation) {
		Tile[][] tBoard = log.getTBoard();

		int hx = 0;
		int hy = 0;
		//Sets hx and hy which gets the location of enemies HQ
		loop1: for (int r = 0; r < log.getSize(); r++) {
			for (int c = 0; c < log.getSize(); c++) {
				if (tBoard[r][c].getType() == 'h') {
					hx = r;
					hy = c;
					break loop1;
				}
			}
		}

		boolean foundSafe = false;
		int moveX = 0, moveY = 0;
		boolean moved = false;

		int[] temp = new int[2];
		temp[0] = pUnit.getX();
		temp[1] = pUnit.getY();


		temp = moveTowardLocation(pUnit, hx, hy, true,  false);
		boolean valid = false;
		if(temp[0] != -1 && temp[1] != -1){
			valid = true;
		}
		if(temp[0] != moveX && temp[1] != moveY && valid){
			foundSafe = true;
		}
		moveX = temp[0];
		moveY = temp[1];


		if(foundSafe == true && valid){
			lastAction = "move,"+pUnit.getName()+","+pUnit.getX()+","+pUnit.getY()+","+moveX+","+moveY;
			log.moveUnit(pUnit, moveX, moveY);
			moveLogger.add(lastAction);
			moved = true;
		}
		else if(desperation == true && valid){
			log.moveUnit(pUnit, moveX, moveY);
			lastAction = "move,"+pUnit.getName()+","+pUnit.getX()+","+pUnit.getY()+","+moveX+","+moveY;
			moveLogger.add(lastAction);
			moved = true;
		}
		
		return moved;
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
		char[][] moves = new char[log.getSize()][log.getSize()];

		for(Unit currentUnit: unitsWithAttacks){
			if(currentUnit.getHasAttacked() == false){

				ArrayList<Unit> potentialUnitsToAttack = new ArrayList<Unit>();
				ArrayList<Integer> damageToUnit = new ArrayList<Integer>();

				int currUnitXPosition = currentUnit.getX();
				int currUnitYPosition = currentUnit.getY();
				int movement = currentUnit.getMove();


				//Top
				for(int r = 1; r<movement && currUnitXPosition-r >= 0; r++){
					if(uBoard[currUnitXPosition-r][currUnitYPosition] != null &&
							uBoard[currUnitXPosition-r][currUnitYPosition].getOwner() != getPNum()){
						Unit unitToAttack = uBoard[currUnitXPosition-r][currUnitYPosition];
						int potentialDamage = log.damage(currentUnit, unitToAttack);
						potentialUnitsToAttack.add(unitToAttack);
						damageToUnit.add(potentialDamage);
					}
					for(int j = r; j < movement-r && currUnitYPosition+j < log.getSize() && currUnitYPosition-j >= 0; j++){
						// Checking Right
						if(uBoard[currUnitXPosition-r][currUnitYPosition+j] != null &&
								uBoard[currUnitXPosition-r][currUnitYPosition+j].getOwner() != getPNum()){
							Unit unitToAttack = uBoard[currUnitXPosition-r][currUnitYPosition+j];
							int potentialDamage = log.damage(currentUnit, unitToAttack);
							potentialUnitsToAttack.add(unitToAttack);
							damageToUnit.add(potentialDamage);
						}
						// Checking Left
						if(uBoard[currUnitXPosition-r][currUnitYPosition-j] != null &&
								uBoard[currUnitXPosition-r][currUnitYPosition-j].getOwner() != getPNum()){
							Unit unitToAttack = uBoard[currUnitXPosition-r][currUnitYPosition-j];
							int potentialDamage = log.damage(currentUnit, unitToAttack);
							potentialUnitsToAttack.add(unitToAttack);
							damageToUnit.add(potentialDamage);
						}
					}
				}

				// Bottom
				for(int r = 1; r<movement && currUnitXPosition+r<log.getSize(); r++){
					if(uBoard[currUnitXPosition+r][currUnitYPosition] != null &&
							uBoard[currUnitXPosition+r][currUnitYPosition].getOwner() != getPNum()){
						Unit unitToAttack = uBoard[currUnitXPosition+r][currUnitYPosition];
						int potentialDamage = log.damage(currentUnit, unitToAttack);
						potentialUnitsToAttack.add(unitToAttack);
						damageToUnit.add(potentialDamage);
					}
					for(int j = r; j < movement-r && currUnitYPosition+j < log.getSize() && currUnitYPosition-j >= 0; j++){
						// Checking Right
						if(uBoard[currUnitXPosition+r][currUnitYPosition+j] != null &&
								uBoard[currUnitXPosition+r][currUnitYPosition+j].getOwner() != getPNum()){
							Unit unitToAttack = uBoard[currUnitXPosition+r][currUnitYPosition+j];
							int potentialDamage = log.damage(currentUnit, unitToAttack);
							potentialUnitsToAttack.add(unitToAttack);
							damageToUnit.add(potentialDamage);
						}
						// Checking Left
						if(uBoard[currUnitXPosition+r][currUnitYPosition-j] != null &&
								uBoard[currUnitXPosition+r][currUnitYPosition-j].getOwner() != getPNum()){
							Unit unitToAttack = uBoard[currUnitXPosition+r][currUnitYPosition-j];
							int potentialDamage = log.damage(currentUnit, unitToAttack);
							potentialUnitsToAttack.add(unitToAttack);
							damageToUnit.add(potentialDamage);
						}
					}
				}

				// Left
				for(int c = 1; c<movement && currUnitYPosition-c >= 0; c++){
					if(uBoard[currUnitXPosition][currUnitYPosition-c] != null &&
							uBoard[currUnitXPosition][currUnitYPosition-c].getOwner() != getPNum()){
						Unit unitToAttack = uBoard[currUnitXPosition][currUnitYPosition-c];
						int potentialDamage = log.damage(currentUnit, unitToAttack);
						potentialUnitsToAttack.add(unitToAttack);
						damageToUnit.add(potentialDamage);
					}
					for(int j = c; j < movement-c && currUnitXPosition+j < log.getSize() && currUnitXPosition-j >= 0; j++){
						// Checking Below
						if(uBoard[currUnitXPosition+j][currUnitYPosition-c] != null &&
								uBoard[currUnitXPosition+j][currUnitYPosition-c].getOwner() != getPNum()){
							Unit unitToAttack = uBoard[currUnitXPosition+j][currUnitYPosition-c];
							int potentialDamage = log.damage(currentUnit, unitToAttack);
							potentialUnitsToAttack.add(unitToAttack);
							damageToUnit.add(potentialDamage);
						}
						// Checking Above
						if(uBoard[currUnitXPosition-j][currUnitYPosition-c] != null &&
								uBoard[currUnitXPosition-j][currUnitYPosition-c].getOwner() != getPNum()){
							Unit unitToAttack = uBoard[currUnitXPosition-j][currUnitYPosition-c];
							int potentialDamage = log.damage(currentUnit, unitToAttack);
							potentialUnitsToAttack.add(unitToAttack);
							damageToUnit.add(potentialDamage);
						}
					}
				}

				// Right
				for(int c = 1; c<movement && currUnitYPosition+c < log.getSize(); c++){
					if(uBoard[currUnitXPosition][currUnitYPosition+c] != null &&
							uBoard[currUnitXPosition][currUnitYPosition+c].getOwner() != getPNum()){
						Unit unitToAttack = uBoard[currUnitXPosition][currUnitYPosition+c];
						int potentialDamage = log.damage(currentUnit, unitToAttack);
						potentialUnitsToAttack.add(unitToAttack);
						damageToUnit.add(potentialDamage);
					}
					for(int j = c; j < movement-c && currUnitXPosition+j < log.getSize() && currUnitXPosition-j >= 0; j++){
						// Checking Below
						if(uBoard[currUnitXPosition+j][currUnitYPosition+c] != null &&
								uBoard[currUnitXPosition+j][currUnitYPosition+c].getOwner() != getPNum()){
							Unit unitToAttack = uBoard[currUnitXPosition+j][currUnitYPosition+c];
							int potentialDamage = log.damage(currentUnit, unitToAttack);
							potentialUnitsToAttack.add(unitToAttack);
							damageToUnit.add(potentialDamage);
						}
						// Checking Above
						if(uBoard[currUnitXPosition-j][currUnitYPosition+c] != null &&
								uBoard[currUnitXPosition-j][currUnitYPosition+c].getOwner() != getPNum()){
							Unit unitToAttack = uBoard[currUnitXPosition-j][currUnitYPosition+c];
							int potentialDamage = log.damage(currentUnit, unitToAttack);
							potentialUnitsToAttack.add(unitToAttack);
							damageToUnit.add(potentialDamage);
						}
					}
				}

				// Old Attack code.  Doesn't account for actual attack range
				//		
				//			//look North
				//			if(currUnitXPosition - 1 >=0)
				//				if(uBoard[currUnitXPosition -1][currUnitYPosition] != null && uBoard[currUnitXPosition -1][currUnitYPosition].getOwner() != getPNum()){
				//					Unit unitToAttack = uBoard[currUnitXPosition -1][currUnitYPosition];
				//					int potentialDamage = log.damage(currentUnit, unitToAttack);
				//					potentialUnitsToAttack.add(unitToAttack);
				//					damageToUnit.add(potentialDamage);
				//				}
				//			//look South
				//			if(currUnitXPosition + 1 < log.getSize())
				//				if(uBoard[currUnitXPosition +1][currUnitYPosition] != null && uBoard[currUnitXPosition +1][currUnitYPosition].getOwner() != getPNum()){
				//					Unit unitToAttack = uBoard[currUnitXPosition +1][currUnitYPosition];
				//					int potentialDamage = log.damage(currentUnit, unitToAttack);
				//					potentialUnitsToAttack.add(unitToAttack);
				//					damageToUnit.add(potentialDamage);
				//				}
				//			//look East
				//			if(currUnitYPosition+1 <log.getSize())
				//				if(uBoard[currUnitXPosition][currUnitYPosition +1] != null && uBoard[currUnitXPosition][currUnitYPosition +1].getOwner() != getPNum()){
				//					Unit unitToAttack = uBoard[currUnitXPosition][currUnitYPosition -1];
				//					int potentialDamage = log.damage(currentUnit, unitToAttack);
				//					potentialUnitsToAttack.add(unitToAttack);
				//					damageToUnit.add(potentialDamage);
				//				}
				//			//look West
				//			if(currUnitYPosition-1 >= 0)
				//				if(uBoard[currUnitXPosition][currUnitYPosition -1] != null && uBoard[currUnitXPosition][currUnitYPosition -1].getOwner() != getPNum()){
				//					Unit unitToAttack = uBoard[currUnitXPosition][currUnitYPosition -1];
				//					int potentialDamage = log.damage(currentUnit, unitToAttack);
				//					potentialUnitsToAttack.add(unitToAttack);
				//					damageToUnit.add(potentialDamage);
				//				}

				for(Unit b:potentialUnitsToAttack){
					for(Unit c:potentialUnitsToAttack){
						if(b.getX() == c.getX() &&
							b.getY() == c.getY() &&
							b != c){
							potentialUnitsToAttack.remove(c);
						}
					}
				}
				//potentialUnitsToAttack
				
				// Testing purposes.  Prints out who we can attack.
				if(potentialUnitsToAttack.size() >= 1){
					System.out.println();
					System.out.println(currentUnit.getName() +": Units to Attack:");
				}
				for(Unit a: potentialUnitsToAttack){
					System.out.println(a.getName());
					System.out.println(a.getX() + " " + a.getY());
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
						lastAction = "attack,"+currentUnit.getName()+","+bestUnitToAttack.getName() + ", " + bestPotentialDamageInflicted;
						currentUnit.setHasAttacked(true);
						moveLogger.add(lastAction);
					}
				}
				else if(potentialUnitsToAttack.size() == 1){
					if(damageToUnit.get(0) >= ATTACK_DAMAGE_THRESHOLD){
						log.battle(currentUnit, potentialUnitsToAttack.get(0), currentUnit.getOwner());
						lastAction = "attack,"+currentUnit.getName()+","+potentialUnitsToAttack.get(0).getName()+", " + damageToUnit.get(0);
						currentUnit.setHasAttacked(true);
						moveLogger.add(lastAction);
					}
				}
				else {
					//no attack possible, do nothing
				}
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

	/***************************************************************
	 * Mutator for sending the logic to the AI.
	 * For some reason, it's named getLogic instead of setLogic
	 * This caused problems... But we're all good now
	 * 
	 ***************************************************************/
	public void getLogic(Logic pLog) {
		log = pLog;
		size = log.getSize();
	}

	/***************************************************************
	 * Finds all the units belonging to the AI that are allowed to
	 * make moves.  
	 * 
	 * @return ArrayList<Unit> units that are able to move
	 ***************************************************************/
	protected ArrayList<Unit> getPossibleMoves() {
		Unit[][] uBoard = log.getUB();

		ArrayList<Unit> uWm = new ArrayList<Unit>();

		//search unit board for units
		for(int row = 0; row < log.getSize(); row++) {
			for (int col = 0; col < log.getSize(); col++) {
				//checks if current tile isn't empty and if I own the unit
				if (uBoard[row][col] != null && uBoard[row][col].getOwner() == getPNum()) {
					//checks to see if the unit has already moved this turn
					if(!uBoard[row][col].getHasMoved()){
						uWm.add(uBoard[row][col]); //adds unit to our available moves
					}
				}
			}
		}
		return uWm;


	}

	/***************************************************************
	 * Returns the list of units that are able to attack.  In other
	 * words, units that haven't attacked yet
	 * 
	 * @return ArrayList<Unit> units that can attack
	 ***************************************************************/
	protected ArrayList<Unit> getPossibleAttacks() {
		Unit[][] uBoard = log.getUB();

		ArrayList<Unit> unitsWithAttacks = new ArrayList<Unit>();

		//search unit board for units
		for(int row = 0; row < log.getSize(); row++) {
			for (int col = 0; col < log.getSize(); col++) {
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

	/***************************************************************
	 * Returns the list of units that are able to capture
	 * 
	 * @return ArrayList<Unit> units that can capture
	 ***************************************************************/
	protected ArrayList<Unit> getPossibleCaptures() {
		Unit[][] uBoard = log.getUB();
		Tile[][] tBoard = log.getTBoard();

		ArrayList<Unit> unitsWithCaptures = new ArrayList<Unit>();

		//search unit board for units
		for(int row = 0; row < log.getSize(); row++) {
			for (int col = 0; col < log.getSize(); col++) {
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

		int most = -1, secondMost = -1, thirdMost = -1, fourthMost = -1;
		for (int r = 0; r < log.getSize(); r++) {
			for (int c = 0; c < log.getSize(); c++) {
				if ((map[r][c].getType() == 'q' || map[r][c].getType() == 'Q') &&
						map[r][c].getOwner() == playNum) {


					int[] unitsToBuild = counterEnemyUnits();

					for(int i = 0; i < unitsToBuild.length; i++){
						if(most == -1){
							most = i;
						}

						if((unitsToBuild[i] > unitsToBuild[most]) &&
								unitTracker[i] <= 3){
							fourthMost = thirdMost;
							thirdMost = secondMost;
							secondMost = most;
							most = i;
						}
					}

					//Need more econ buildings to get ahead.
					if(countEconBuildings() <= 0 &&
							unitTracker[types.INFANTRY.ordinal()] < 3 &&
							unitTracker[types.MECH.ordinal()] < 3){
						fourthMost = secondMost;
						thirdMost = most;
						most = types.INFANTRY.ordinal();
						secondMost = types.MECH.ordinal();
					}

					if(most >= 0)
						wantToBuild.add(createMeAUnit(most));

					if(secondMost >= 0)
						wantToBuild.add(createMeAUnit(secondMost));

					if(thirdMost >= 0)
						wantToBuild.add(createMeAUnit(thirdMost));

					if(fourthMost >= 0)
						wantToBuild.add(createMeAUnit(fourthMost));
				}


			}
		}
		System.out.println("AI Producing Units.  Wishlist:");
		for(Unit u: wantToBuild){
			System.out.println("\t" +u.getName());
		}

		if(wantToBuild.size() == 0 && log.getP2().getCash() >= 3000){
			wantToBuild.add(createMeAUnit(types.MECH.ordinal()));
		}
		if(wantToBuild.size() == 0 && log.getP2().getCash() >= 1000){
			wantToBuild.add(createMeAUnit(types.INFANTRY.ordinal()));
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
			return new Bomber(-1);
		}else if(type == types.CHOPPER.ordinal()){
			return new Chopper(-1);
		}else if(type == types.FIGHTERJET.ordinal()){
			return new FighterJet(-1);
		}else if(type == types.HEAVYTANK.ordinal()){
			return new HeavyTank(-1);
		}else if(type == types.INFANTRY.ordinal()){
			return new Infantry(-1);
		}else if(type == types.MECH.ordinal()){
			return new Mech(-1);
		}else if(type == types.MEDTANK.ordinal()){
			return new MedTank(-1);
		}else if(type == types.RECON.ordinal()){
			return new Recon(-1);
		}else if(type == types.ROCKETS.ordinal()){
			return new Rockets(-1);
		}else if(type == types.TANK.ordinal()){
			return new Tank(-1);
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

						if(unitType.equalsIgnoreCase("Anti-Air")){
							counters[types.TANK.ordinal()]++;
							counters[types.MEDTANK.ordinal()]++;
							counters[types.HEAVYTANK.ordinal()]++;
						}else if(unitType.equalsIgnoreCase("Artillery")){
							counters[types.TANK.ordinal()]++;
							counters[types.MEDTANK.ordinal()]++;
							counters[types.HEAVYTANK.ordinal()]++;
							counters[types.ARTILLERY.ordinal()]++;
							counters[types.BOMBER.ordinal()]++;
							counters[types.ROCKETS.ordinal()]++;
							counters[types.MECH.ordinal()]++;
						}else if(unitType.equalsIgnoreCase("Bomber")){
							counters[types.ANTIAIR.ordinal()]++;
							counters[types.MISSILE.ordinal()]++;
							counters[types.FIGHTERJET.ordinal()]++;
						}else if(unitType.equalsIgnoreCase("CHOPPER")){
							counters[types.ANTIAIR.ordinal()]++;
							counters[types.MISSILE.ordinal()]++;
							counters[types.FIGHTERJET.ordinal()]++;
						}else if(unitType.equalsIgnoreCase("Fighter Jet")){
							counters[types.ANTIAIR.ordinal()]++;
							counters[types.MISSILE.ordinal()]++;
							counters[types.FIGHTERJET.ordinal()]++;
						}else if(unitType.equalsIgnoreCase("Heavy Tank")){
							counters[types.BOMBER.ordinal()]++;
							counters[types.HEAVYTANK.ordinal()]++;
						}else if(unitType.equalsIgnoreCase("Infantry")){
							counters[types.RECON.ordinal()]++;
							counters[types.ANTIAIR.ordinal()]++;
							counters[types.TANK.ordinal()]++;
							counters[types.MEDTANK.ordinal()]++;
							counters[types.HEAVYTANK.ordinal()]++;
							counters[types.ARTILLERY.ordinal()]++;
							counters[types.BOMBER.ordinal()]++;
						}else if(unitType.equalsIgnoreCase("Mech")){
							counters[types.ANTIAIR.ordinal()]++;
							counters[types.TANK.ordinal()]++;
							counters[types.MEDTANK.ordinal()]++;
							counters[types.HEAVYTANK.ordinal()]++;
							counters[types.ARTILLERY.ordinal()]++;
							counters[types.ROCKETS.ordinal()]++;
							counters[types.BOMBER.ordinal()]++;
						}else if(unitType.equalsIgnoreCase("MedTank")){
							counters[types.HEAVYTANK.ordinal()]++;
							counters[types.MEDTANK.ordinal()]++;
							counters[types.ROCKETS.ordinal()]++;
							counters[types.BOMBER.ordinal()]++;
						}else if(unitType.equalsIgnoreCase("Missile")){
							counters[types.MECH.ordinal()]++;
							counters[types.TANK.ordinal()]++;
							counters[types.MEDTANK.ordinal()]++;
							counters[types.HEAVYTANK.ordinal()]++;
							counters[types.ARTILLERY.ordinal()]++;
							counters[types.ROCKETS.ordinal()]++;
							counters[types.BOMBER.ordinal()]++;
						}else if(unitType.equalsIgnoreCase("Recon")){
							counters[types.MECH.ordinal()]++;
							counters[types.ANTIAIR.ordinal()]++;
							counters[types.TANK.ordinal()]++;
							counters[types.MEDTANK.ordinal()]++;
							counters[types.HEAVYTANK.ordinal()]++;
							counters[types.ARTILLERY.ordinal()]++;
							counters[types.ROCKETS.ordinal()]++;
							counters[types.BOMBER.ordinal()]++;
						}else if(unitType.equalsIgnoreCase("Rockets")){
							counters[types.MECH.ordinal()]++;
							counters[types.TANK.ordinal()]++;
							counters[types.MEDTANK.ordinal()]++;
							counters[types.HEAVYTANK.ordinal()]++;
							counters[types.ARTILLERY.ordinal()]++;
							counters[types.ROCKETS.ordinal()]++;
							counters[types.BOMBER.ordinal()]++;
						}else if(unitType.equalsIgnoreCase("Tank")){
							counters[types.TANK.ordinal()]++;
							counters[types.MEDTANK.ordinal()]++;
							counters[types.HEAVYTANK.ordinal()]++;
							counters[types.ARTILLERY.ordinal()]++;
							counters[types.ROCKETS.ordinal()]++;
							counters[types.BOMBER.ordinal()]++;
							counters[types.MECH.ordinal()]++;
						}


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

	/***************************************************************
	 * Returns whether or not I can afford ANY units
	 * 
	 * 
	 ***************************************************************/
	protected boolean canIBuy(){
		boolean buyAvailable = false;
		//find buys
		if(findUnitsICanAfford().size() > 0){
			buyAvailable = true;
		}

		return buyAvailable;
	}

	/***************************************************************
	 * Returns whether or not I can capture ANY building
	 * 
	 * 
	 ***************************************************************/
	protected boolean canICapture(){
		boolean captureAvailable = false;
		//find captures
		unitsWithCaptures = getPossibleCaptures();
		if(unitsWithCaptures.size() > 0){
			captureAvailable = true;
		}
		return captureAvailable;
	}

	/***************************************************************
	 * Testing method.
	 * Creates units and runs through AI turns
	 ***************************************************************/
	private void test(){
		boolean finished = false;


		// creating test Logic - NECESSARY
		log = new Logic(this);

		// create some random units
		log.setUnit(1, 5, new Infantry(0));
		log.setUnit(10, 8, new MedTank(0));
		log.setUnit(13, 3, new Mech(0));
		log.setUnit(21, 18, new HeavyTank(0));

		log.setUnit(29, 27, new MedTank(1));
		log.setUnit(27, 26, new Infantry(1));
		log.setUnit(23, 28, new HeavyTank(1));
		log.setUnit(28, 28, new Rockets(1));

		Unit m = new Rockets(1);
		log.setUnit(0, 5, m);
		char[][] t = log.getMoves(m);
		for(int i = 0; i < log.getSize(); i++){
			if(i < 10){
				System.out.print(i + "  ");
			}else{
				System.out.print(i + " ");
			}
			for(int j = 0; j < log.getSize(); j++)
				System.out.print(t[i][j] + " ");
			System.out.println();
		}

		lastAction = "Game started";
		System.out.println(lastAction);
		moveLogger.clear();
		System.out.println("Enemy unit count: " + countEnemyUnits());
		System.out.println("AI unit count: " + countEnemyUnits());
		Scanner scan = new Scanner(System.in);
		while(!finished){
			startTurn();
			if(moveLogger.isEmpty())
				System.out.println("move list is empty.");
			for(String s: moveLogger){
				System.out.println(s);
				scan.nextLine();
			}
			System.out.println("Enemy unit count: " + countEnemyUnits());
			System.out.println("AI unit count: " + countMyUnits());
			log.unitNewTurn(getPNum());
			System.out.println("Player One takes a turn");
			log.econDay(this);
			scan.nextLine();
		}
	}

	/***************************************************************
	 * Method used for returning the number of enemy units
	 * 
	 * 
	 ***************************************************************/
	private int countEnemyUnits(){
		int count = 0;
		Unit[][] temp = log.getUB();

		for(int i = 0; i < temp.length; i++)
			for(int j = 0; j < temp[0].length; j++){
				if(temp[i][j] != null && temp[i][j].getOwner() != getPNum()){
					count++;
				}
			}
		return count;
	}
	
	/***************************************************************
	 * Method used for returning the number of AI units
	 * 
	 * 
	 ***************************************************************/
	private int countMyUnits(){
		int count = 0;
		Unit[][] temp = log.getUB();

		for(int i = 0; i < temp.length; i++)
			for(int j = 0; j < temp[0].length; j++){
				if(temp[i][j] != null && temp[i][j].getOwner() == getPNum()){
					count++;
				}
			}
		return count;
	}
	
	/***************************************************************
	 * Returns the Unit Board to the Controller
	 * 
	 * 
	 ***************************************************************/
	public Unit[][] getNewUBoard(){
		return log.getUB();
	}
	
	/***************************************************************
	 * Returns the Terrain Board to the Controller
	 * 
	 * 
	 ***************************************************************/
	public Tile[][] getNewTBoard(){
		return log.getTBoard();
	}


	/***************************************************************
	 * Returns the list of actions back to the controller
	 * 
	 * 
	 ***************************************************************/
	public ArrayList<String> getActions(){
		return moveLogger;
	}

	public static void main(String[] args){
		new AI("Herp, Derp", 1, 'b', true);
	}



}
