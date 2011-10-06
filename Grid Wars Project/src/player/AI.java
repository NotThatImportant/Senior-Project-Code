package player;


import gameplay.Logic;
import units.Unit;
import terrain.Tile;

public class AI extends Player{
	private boolean availableMove;
	private boolean availableAttack;
	private boolean availablePurchase;
	private boolean availableCapture;
	
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
		
		while(endTurn == false){
			//Go through possible moves
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
	
	public void moveUnits() {
		Unit[][] uBoard = log.getUB();
		
		for(int r = 0; r < size; r++) {
			for (int c = 0; c < size; c++) {
				if (uBoard[r][c] != null) {
					
				}
			}
		}
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
	
	public boolean canIMove(){
		boolean moveAvailable = false;
		//find moves
		//if possibleMoves.lenght > 0{
		//moveAvailable = true;
		
		return moveAvailable;
	}
	
	public boolean canIAttack(){
		boolean attackAvailable = false;
		//find attacks
		//if possibleAttacks.lenght > 0{
		//attackAvailable = true;
		
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
	
	
}
