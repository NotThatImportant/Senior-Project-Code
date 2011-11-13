package player;


import gameplay.Logic;
import java.util.ArrayList;
import java.util.Arrays;
import terrain.Tile;
import units.APC;
import units.AntiAir;
import units.Artillery;
import units.Bomber;
import units.ChopperA;
import units.ChopperB;
import units.FighterJet;
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

	Logic log;
	int size;

	enum types {
		ANTIAIR, APC, ARTILLERY, BATTLESHIP, BOMBER, CHOPPERA, CHOPPERB,
		CRUISER, FIGHTERJET, HEAVYTANK, INFANTRY, LANDER, MECH, MEDTANK,
		MISSILE, RECON, ROCKETS, SUB, TANK
	};

	public AI(String pN, int pNum, char fact) {
		super(pN, pNum, fact);	
	}

	public void startTurn(){
		boolean endTurn = false;

		//determine possible actions
		availableMove = canIMove();
		availableAttack = canIAttack();
		availablePurchase = canIBuy();
		availableCapture = canICapture();

		//Loop through possible actions
		while(endTurn == false){

			if(availableMove){
				//move units
				if(availableCapture){
					capture();
				}
				if(availableAttack){
					attack();
				}
				//if no other captures or attacks but still moves
				moveCloserToEnemies();
			}
			
			if(availablePurchase){
				//buy a unit
			}

			if(availablePurchase)
				prodUnits();

			//if no more possible moves
			endTurn = true;
		}
	}

	protected void moveCloserToEnemies() {
		boolean notDone = true;
		ArrayList<Unit> toMove = getPossibleMoves();
		ArrayList<Unit> hasMoved = new ArrayList<Unit>();
		int count = 0; 
		Tile[][] tBoard = log.getTBoard();
		int hx = 0;
		int hy = 0;

		do { 
			//pmoves represents the + and - of where the unit can move 
			//at its current X and Y location!
			char[][] pMoves = log.getMoves(toMove.get(count));

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
			int bestMove = 999;
			//this method will move each until closer to the HQ
			for (int r = 0; r < pMoves.length; r++) {
				for (int c = 0; c < pMoves.length; c++) {
					if(pMoves[r][c] == 'x'){
						if(getDistance(r, c,hx,hy) <= bestMove){
							closestX = r;
							closestY = c;
							bestMove = getDistance(closestX, closestY, hx, hy);
						}
					}
				}
			}
			
			log.moveUnit(toMove.get(count), closestX, closestY);
			hasMoved.add(toMove.get(count));
			
			
			count++;
		} while (notDone);
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
				}
			}
			else if(potentialUnitsToAttack.size() == 1){
				if(damageToUnit.get(0) >= ATTACK_DAMAGE_THRESHOLD){
					log.battle(currentUnit, potentialUnitsToAttack.get(0), currentUnit.getOwner());
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
	 * AI decides what units to create
	 ***********************************************************************/
	protected ArrayList<Unit> prodUnits() {
		Tile[][] map = log.getTBoard();
		ArrayList<Unit> wantToBuild = new ArrayList<Unit>();
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
		return canBuild;
	}

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
	protected int getUncapturedBuildings(){
		Tile[][] mapBoard = log.getTBoard();
		int num = 0;

		for(int i = 0; i < log.getSize(); i++)
			for(int j = 0; j < log.getSize(); j++){
				if(mapBoard[i][j].getType() == 'b' &&mapBoard[i][j].getOwner()==-1){
					num++;
				}
			}


		return num;
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

	/** Move unit
	 * If unit can't attack or capture:
	 * 1. find x,y coordinates of nearest enemy
	 * 2. move unit into same row or column as enemy
	 */

}
