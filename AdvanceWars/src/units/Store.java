package units;

import java.io.Serializable;
import java.util.ArrayList;

public class Store implements Serializable
{

	ArrayList<Unit> toBuyGround;
	ArrayList<Unit> toBuyAir;

	
	public Store() {
		toBuyGround = new ArrayList<Unit>();
		toBuyAir = new ArrayList<Unit>();
		
		groundUnits();
//		airUnits();
	}
	
	private void groundUnits() {
		toBuyGround.add(new AntiAir(-1));
		toBuyGround.add(new APC(-1));
		toBuyGround.add(new Artillery(-1));
		toBuyGround.add(new HeavyTank(-1));
		toBuyGround.add(new Infantry(-1));
		toBuyGround.add(new Mech(-1));
		toBuyGround.add(new MedTank(-1));
		toBuyGround.add(new Recon(-1));
		toBuyGround.add(new Rockets(-1));
		toBuyGround.add(new Tank(-1));
	}
	
	public Unit whatUnit(String requestedUnitName) {
		Unit desiredUnit = null; //rightOne
		
		//for (int i = 0; i < toBuyGround.size(); i++) {
		//	if (toBuyGround.get(i).getName().equals(name)) 
		//		rightOne = toBuyGround.get(i);
		//}
		if (requestedUnitName.equals("Anti-Air"))
			desiredUnit = new AntiAir(-1);
		else if (requestedUnitName.equals("APC"))
			desiredUnit = new APC(-1);
		else if (requestedUnitName.equals("Artillery"))
			desiredUnit = new Artillery(-1);
		else if (requestedUnitName.equals("Heavy Tank"))
			desiredUnit = new HeavyTank(-1);
		else if (requestedUnitName.equals("Infantry"))
			desiredUnit = new Infantry(-1);
		else if (requestedUnitName.equals("Mech"))
			desiredUnit = new Mech(-1);
		else if (requestedUnitName.equals("Medium Tank"))
			desiredUnit = new MedTank(-1);
		else if (requestedUnitName.equals("Recon"))
			desiredUnit = new Recon(-1);
		else if (requestedUnitName.equals("Rockets"))
			desiredUnit = new Rockets(-1);
		else 
			desiredUnit = new Tank(-1);
		
		return desiredUnit;
	}
	
	/*This will give you a list of the units you can buy with ground units*/
	public ArrayList<Unit> buyGroundUnit(int cash) {
		ArrayList<Unit> canBuy = new ArrayList<Unit>();
		
		for (int i = 0; i < toBuyGround.size(); i++) {
			if (cash >= toBuyGround.get(i).getCost())
				canBuy.add(toBuyGround.get(i));
		}
		
		return canBuy;
	}
	
	public ArrayList<Unit> buyAirUnit(int cash) {
		ArrayList<Unit> canBuy = new ArrayList<Unit>();
		
		for (int i = 0; i < toBuyAir.size(); i++) {
			if (cash >= toBuyAir.get(i).getCost())
				canBuy.add(toBuyGround.get(i));
		}
		
		return canBuy;
	}

//	public ArrayList<Unit> buyAir() {
//		return toBuyAir;
//	}
	
//	private void airUnits() {
//		toBuyAir.add(new Bomber());
//		toBuyAir.add(new ChopperA());
//		toBuyAir.add(new ChopperB());
//		toBuyAir.add(new FighterJet(-1));
//	}
}
