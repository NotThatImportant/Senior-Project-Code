package units;

public class Recon extends Unit{
	public Recon(int owner){
		this.owner = owner;
		
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
