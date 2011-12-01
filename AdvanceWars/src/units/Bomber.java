package units;

public class Bomber extends Unit
{
	public Bomber(int owner){
		this.owner = owner;
		
		attack = 4;
		type = AIRTYPE;
		move = 6;
		fuel = 30;
		ammo = 0;
		atkRange = 0;
		armor = 3;
		cost = 26000;
		hp = 10;
		name = "Bomber";
		attackCopter = true;
		attackPlane = true;
		bonus = true;
	}
	
	public int getAttack(Unit e) {		
		int tAttack = attack;
		if (e.getType() == INFANTRYTYPE || e.getType() == TANKTYPE)
			tAttack = 0; 
		
		return tAttack;
	}
}
