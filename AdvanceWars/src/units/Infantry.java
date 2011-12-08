package units;

import java.io.Serializable;

public class Infantry extends Unit implements Serializable
{
	
	public Infantry(int owner){
		super();
		setOwner(owner);
		
		attack = 1;
		type = INFANTRYTYPE;
		move = 5;
		fuel = 99;
		ammo = 20;
		atkRange = 1;
		armor = 1;
		cost = 1000;
		hp = 10;
		name = "Infantry";
		attackCopter = true;
	}
}
