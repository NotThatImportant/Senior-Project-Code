package ai;

public class Infantry extends Unit{

	private char type = 'i';
	private boolean owner;
	private int move = 3;
	private int attack;
	private int fuel = -1;
	private int ammo = -1;
	private int atkRange = 1;
	private int[] movement;
	
	public Infantry(boolean owner){
		movement = new int[-1,0,1];
	}
}
