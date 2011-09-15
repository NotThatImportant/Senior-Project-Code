package player;

/**
 * 
 * Player class
 * 
 */
public class Player {
	
	private int numUnits; 
	private int playNum;
	private String playName;
	private int numBuild;
	private int cash;
	private char faction;
	
	public Player(String pN, int pNum, char fact) {
		faction = fact;
		playName = pN;
		playNum = pNum;
	}
	
	public int getPNum() {
		return playNum;
	}
	
	public String getPName() {
		return playName;
	}
	
	public char getFact() {
		return faction;
	}
	
	public int getNumUnits() {
		return numUnits;
	}
	
	public void setNumUnits(int numUnits) {
		this.numUnits = numUnits;
	}
	
	public int getNumBuild() {
		return numBuild;
	}
	
	public void setNumBuild(int numBuild) {
		this.numBuild = numBuild;
	}
	
	public int getCash() {
		return cash;
	}
	
	public void setCash(int cash) {
		this.cash = cash;
	}
}
