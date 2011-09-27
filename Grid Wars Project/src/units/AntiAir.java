package units;

public class AntiAir extends Unit{
	public AntiAir(int owner){
		this.owner = owner;
		
		attack = 2;
		type = TANKTYPE;
		move = 6;
		fuel = 30;
		ammo = 10;
		atkRange = 1;
		armor = 2;
		cost = 7000;
		hp = 10;
		name = "Anti-Air";
		bonus = true;
	}
	
	public int getBonusDmg(Unit e) {		
		int nBonus = 0;
		if (e.getType() == AIRTYPE)
			nBonus = 2;
		
		return nBonus;
	}
}
