package units;

public class Rockets extends Unit{
	
	public Rockets(int owner){
		super();
		setOwner(owner);
		
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
	}
}
