package units;

import java.io.Serializable;

public class APC extends Unit implements Serializable
{
	public APC(int owner){
		super();
		setOwner(owner);
		
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
