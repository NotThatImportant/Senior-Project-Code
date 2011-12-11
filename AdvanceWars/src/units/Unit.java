package units;

import java.io.Serializable;

public abstract class Unit implements Serializable
{

	public static final char AIRTYPE = 'a'; 
	public static final char INFANTRYTYPE = 'i'; 
	public static final char TANKTYPE = 't'; 

	protected char type;
	protected int owner;
	protected int move;
	protected int attack;
	protected int fuel;
	protected int ammo;
	protected int armor;
	protected int atkRange;
	protected int[] movement;
	protected int x, y;
	protected int hp;
	protected int cost; 
	protected boolean bonus;
	protected String name;
	protected boolean attackCopter; 	
	protected boolean attackPlane;
	protected boolean canBuy;
	protected boolean hasMoved;
	protected boolean hasAttacked;
	protected boolean hasCaptured;
	protected boolean startedCapture;
	
	public Unit() {
		startedCapture = false;
		hasCaptured = false;
		hasAttacked = false;
		hasMoved = false;
		canBuy = false;
		attackPlane = false;
		attackCopter = true;
		bonus = false; 
		hp = 10;
	}
	
	public void setOwner(int own) {
		owner = own;
	}
	
	public boolean canBuy() {
		return canBuy;
	}
	
	public void setBuy(boolean buy) {
		canBuy = buy;
	}
	
	public boolean canAttackCopter() {
		return attackCopter;
	}
	
	public boolean canAttacKPlane() {
		return attackPlane;
	}
	
	public String getName() {
		return name;
	}
	
	public int getCost() {
		return cost;
	}
	
	public int getHP() {
		return hp;
	}
	
	public void setHP(int pHP) {
		hp = pHP;
	}
	
	public char getType() {
		return type;
	}
	public int getMove() {
		return move;
	}
	public void setMove(int move) {
		this.move = move;
	}
	public int getAttack() {
		int tA = attack * (hp / 2 );
		return tA;
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

	public boolean getBonus() {
		return bonus;
	}
	
	public int getOwner(){
		return owner;
	}
	
	public void setHasMoved(boolean hasMoved){
		this.hasMoved = hasMoved;
	}
	
	public void setHasAttacked(boolean hasAttacked){
		this.hasAttacked = hasAttacked;
	}
	
	public boolean getHasMoved(){
		return hasMoved;
	}
	
	public boolean getHasAttacked(){
		return hasAttacked;
	}
	
	public void setHasCaptured(boolean hasCaptured){
		this.hasCaptured = hasCaptured;
	}
	
	public boolean getHasCaptured(){
		return hasCaptured;
	}
	
	public void setStartedCapture(boolean startedCapture){
		this.startedCapture = startedCapture;
	}
	
	public boolean getStartedCapture(){
		return startedCapture;
	}
	
	public void justProded() {
		hasCaptured = false; 
		hasAttacked = false; 
		hasMoved = false; 
	}


}
