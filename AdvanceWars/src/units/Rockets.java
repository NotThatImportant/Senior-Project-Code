package units;

public class Rockets extends Unit{
	
	private boolean didMove;
	
	public Rockets(int owner){
		this.owner = owner;
		
		attack = 4;
		type = TANKTYPE;
		move = 3;
		fuel = 30;
		ammo = 5;
		atkRange = 5;
		armor = 2;
		cost = 12000;
		hp = 10;
		name = "Rocket";
		didMove = false;
	}
	
	public void madeMove() {
		didMove = true;
	}
	
	public boolean didMove() {
		boolean temp = didMove;
		didMove = false;
		return temp;
	}
}
