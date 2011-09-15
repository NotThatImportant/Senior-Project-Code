package units;

public abstract class Unit {

	public static final char AIRTYPE = 'a'; 
	public static final char INFANTRYTYPE = 'i'; 
	public static final char TANKTYPE = 't'; 

	char type;
	boolean owner;
	int move;
	int attack;
	int fuel;
	int ammo;
	int armor;
	int atkRange;
	int[] movement;
	int x, y;
	int hp = 10;
	
	public int getHP() {
		return hp;
	}
	
	public void setHP(int pHP) {
		hp = pHP;
	}
	
	public char getType() {
		return type;
	}
	public void setType(char type) {
		this.type = type;
	}
	public boolean isOwner() {
		return owner;
	}
	public void setOwner(boolean owner) {
		this.owner = owner;
	}
	public int getMove() {
		return move;
	}
	public void setMove(int move) {
		this.move = move;
	}
	public int getAttack() {
		return attack;
	}
	public void setAttack(int attack) {
		this.attack = attack;
	}
	public int getFuel() {
		return fuel;
	}
	public void setFuel(int fuel) {
		this.fuel = fuel;
	}
	public int getAmmo() {
		return ammo;
	}
	public void setAmmo(int ammo) {
		this.ammo = ammo;
	}
	public int getArmor() {
		return armor;
	}
	public void setArmor(int armor) {
		this.armor = armor;
	}
	public int getAtkRange() {
		return atkRange;
	}
	public void setAtkRange(int atkRange) {
		this.atkRange = atkRange;
	}
	public int[] getMovement() {
		return movement;
	}
	public void setMovement(int[] movement) {
		this.movement = movement;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
}
