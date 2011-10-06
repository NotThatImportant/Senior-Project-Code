package player;


import java.util.ArrayList;
import java.util.List;
import gameplay.Logic;
import units.Unit;
import terrain.Tile;

public class AI extends Player{
	private boolean availableMove;
	private boolean availableAttack;
	private boolean availablePurchase;
	private boolean availableCapture;
	
	private ArrayList<Unit> unitsWithMoves;
	private ArrayList<Unit> unitsWithAttacks;
	
	Logic log;
	int size;
	
	public AI(String pN, int pNum, char fact) {
		super("Herp Derp 4000", 2, fact);	
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
			
			if(availablePurchase){
				//buy a unit
			}
			if(availableMove){
				//move units
				if(availableCapture){
					//capture building
				}
				if(availableAttack){
					//attack closest units
				}
				//if no other captures or attacks but still moves
				//moveCloserToEnemies();
			}
			
			//if no more possible moves
			endTurn = true;
		}
	}
	
	public void getLogic(Logic pLog) {
		log = pLog;
		size = log.getSize();
	}
	
	public ArrayList<Unit> getPossibleMoves() {
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
	
	public ArrayList<Unit> getPossibleAttacks() {
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
		return unitsWithMoves;
	}
	
	public void prodUnits() {
		Tile[][] map = log.getTBoard();
		
		for (int r = 0; r < size; r++) {
			for (int c = 0; c < size; c++) {
				if (map[r][c].getType() == 'p' &&
						map[r][c].getOwner() == playNum) {
					
				}
			}
		}
	}
	
	/************************************************************************
	 * Update unitsWithMoves arrayList and determine if moves are available
	 * @return boolean canIMove
	 ***********************************************************************/
	public boolean canIMove(){
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
	public boolean canIAttack(){
		boolean attackAvailable = false;
		//find attacks
		unitsWithAttacks = getPossibleAttacks();
		if(unitsWithAttacks.size() > 0){
			attackAvailable = true;
		}
		return attackAvailable;
	}
	
	public boolean canIBuy(){
		boolean buyAvailable = false;
		//find buys
		if(findUnitsICanAfford().size() > 0){
			buyAvailable = true;
		}
		
		return buyAvailable;
	}
	
	public boolean canICapture(){
		boolean captureAvailable = false;
		//find captures
		//if possibleCaptures.lenght > 0{
		//capturesAvailable = true;
		
		return captureAvailable;
	}
	
	/** Move unit
	 * If unit can't attack or capture:
	 * 1. find x,y coordinates of nearest enemy
	 * 2. move unit into same row or column as enemy
	 */
	
}
