package player;

/**
 * 
 * Player class
 *  Austin was here....
 */
public class Player {
	
	protected int numUnits; 
	protected int playNum;
	protected String playName;
	protected int numBuild;
	protected int munnys;
	protected char faction;
	
	public Player(String pN, int pNum, char fact) {
		faction = fact;
		playName = pN;
		playNum = pNum;
		munnys = 6000;
		numBuild = 0;
		numUnits = 0;
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
	
	public void addBuilding() {
		numBuild++;
	}
	
	public int getCash() {
		return munnys;
	}
	
	public void setCash(int cash) {
		this.munnys = cash;
	}
}
