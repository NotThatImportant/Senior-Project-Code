package player;

public class NewTurn {
	
	private boolean availableMove;
	private boolean availableAttack;
	private boolean availablePurchase;
	private boolean availableCapture;
	
	public NewTurn(){
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
		//if possibleBuys.lenght > 0{
		//buyAvailable = true;
		
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
