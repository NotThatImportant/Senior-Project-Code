package units;

public class Infantry extends Unit{
	
	public Infantry(int owner){
		this.owner = owner;
		
		attack = 1;
		type = INFANTRYTYPE;
		move = 5;
		fuel = 99;
		ammo = 20;
		atkRange = 1;
		armor = 1;
		cost = 1000;
		hp = 10;
	}
}
