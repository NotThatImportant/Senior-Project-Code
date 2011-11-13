package units;

public class Mech extends Unit{

	public Mech(int owner){
		super();
		setOwner(owner);
		
		attack = 1;
		type = INFANTRYTYPE;
		move = 3;
		fuel = 99;
		ammo = 5;
		atkRange = 1;
		armor = 1;
		cost = 3000;
		hp = 10;
		bonus = true;
		name = "Mech";
	}
	
	public int getBonusDmg(Unit e) {		
		int nBonus = 0;
		if (e.getType() == TANKTYPE)
			nBonus = 2;
		
		return nBonus;
	}
}
