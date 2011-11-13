package units;

public class Tank extends Unit{
	public Tank(int owner){
		this.owner = owner;
		
		attack = 3;
		type = TANKTYPE;
		move = 7;
		fuel = 30;
		ammo = 10;
		atkRange = 1;
		armor = 3;
		cost = 7000;
		hp = 10;
		name = "Tank";
	}
}
