package units;

public class Artillery extends Unit{
	
	private boolean didMove; 
	
	public Artillery(int owner){
		this.owner = owner;
		
		attack = 3;
		type = TANKTYPE;
		move = 4;
		fuel = 30;
		ammo = 15;
		atkRange = 3;
		armor = 2;
		cost = 5000;
		hp = 10;
		name = "Artillery";
		didMove = false;
	}
	
	public void madeMove() {
		didMove = true;
	}
	
	public boolean didMove() {
		boolean temp = didMove;
		didMove = true;
		return temp;
	}
}
