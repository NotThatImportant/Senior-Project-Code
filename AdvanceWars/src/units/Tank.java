package units;

import java.io.Serializable;

public class Tank extends Unit implements Serializable
{
	public Tank(int owner){
		super();
		setOwner(owner);
		
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
