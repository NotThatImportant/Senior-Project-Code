package units;

public class HeavyTank extends Unit{
	
	public HeavyTank(int owner){
		super();
		setOwner(owner);
		
		attack = 8;
		type = TANKTYPE;
		move = 6;
		fuel = 200;
		ammo = 5;
		atkRange = 1;
		armor = 4;
		cost = 24000;
		hp = 10;
		name = "APC";
	}
}
