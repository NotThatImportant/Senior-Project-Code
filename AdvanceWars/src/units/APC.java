package units;

public class APC extends Unit{
	public APC(int owner){
		this.owner = owner;
		
		attack = 0;
		type = TANKTYPE;
		move = 6;
		fuel = 30;
		ammo = 5;
		atkRange = 0;
		armor = 2;
		cost = 5000;
		hp = 10;
		name = "APC";
	}
	
	public int supply() {
		return ammo;
	}
}
