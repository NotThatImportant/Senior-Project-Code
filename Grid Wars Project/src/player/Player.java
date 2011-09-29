package player;

import java.util.ArrayList;

import units.*;

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
	
	public ArrayList<Unit> findUnitsICanAfford(){
		ArrayList<Unit> unitsICanAfford = new ArrayList<Unit>();
		ArrayList<Unit> unitList = findAllUnits();
		
		for (int i = 0; i < unitList.size(); i++) {
			if (unitList.get(i).getCost() <= munnys) {
				unitsICanAfford.add(unitList.get(i));
			}
		}
		
		
		return unitsICanAfford;
	}
	
	public ArrayList<Unit> findAllUnits(){
		ArrayList<Unit> allUnits = new ArrayList<Unit>();
		allUnits.add(new AntiAir(-1));
		allUnits.add(new APC(-1));
		allUnits.add(new Artillery(-1));
//		allUnits.add(new Bomber(-1));
//		allUnits.add(new ChopperA(-1));
//		allUnits.add(new ChopperB(-1));
//		allUnits.add(new FighterJet(-1));
		allUnits.add(new HeavyTank(-1));
		allUnits.add(new Infantry(-1));
		allUnits.add(new Mech(-1));
		allUnits.add(new MedTank(-1));
		allUnits.add(new Recon(-1));
		allUnits.add(new Rockets(-1));
		allUnits.add(new Tank(-1));
		
		return allUnits;
	}
}
