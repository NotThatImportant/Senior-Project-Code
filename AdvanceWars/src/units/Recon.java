package units;

import java.io.Serializable;

public class Recon extends Unit implements Serializable
{
	public Recon(int owner){
		super();
		setOwner(owner);
		
		attack = 1;
		type = TANKTYPE;
		move = 8;
		fuel = 40;
		ammo = 15;
		atkRange = 1;
		armor = 2;
		cost = 4000;
		hp = 10;
		name = "Recon";
		attackCopter = true;
	}
}
