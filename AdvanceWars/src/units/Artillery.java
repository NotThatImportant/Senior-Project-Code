package units;

import java.io.Serializable;

public class Artillery extends Unit implements Serializable
{
	
	public Artillery(int owner){
		super();
		setOwner(owner);
		
		attack = 3;
		type = TANKTYPE;
		move = 4;
		fuel = 30;
		ammo = 15;
		atkRange = 3;
		armor = 2;
		cost = 6000;
		hp = 10;
		name = "Artillery";
	}
}
