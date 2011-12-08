package units;

import java.io.Serializable;

public class MedTank extends Unit implements Serializable
{
	public MedTank(int owner){
		super();
		setOwner(owner);
		
		attack = 4;
		type = TANKTYPE;
		move = 6;
		fuel = 30;
		ammo = 12;
		atkRange = 1;
		armor = 4;
		cost = 14000;
		hp = 10;
		name = "Medium Tank";
	}
}
