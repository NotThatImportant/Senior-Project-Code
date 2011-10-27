package player;
import static org.junit.Assert.*;

import java.util.ArrayList;

import gameplay.Logic;

import org.junit.Test;

import terrain.Tile;
import units.HeavyTank;
import units.Infantry;
import units.MedTank;
import units.Missile;
import units.Unit;

public class AI_FunctionalTest {
	
	Logic testLogic;
	
	public void createBaseTestConditions(){
		String aiName = "Roger";
		String playerName = "Matt";
		int aiNum = 1;
		int playerNum = 2;
		char aiFaction = 'r';
		char playerFaction = 'b';
		String mapName = "map1";
		
		testLogic = new Logic(mapName, aiFaction, playerFaction, aiName, playerName, true);
		AI ai = (AI)testLogic.getP2();
		ai.getLogic(testLogic);

	}
	
	@Test
	public void test_moveCloserToEnemies(){
//		createBaseTestConditions();
//		AI ai = (AI)testLogic.getP2();
//		ai.moveCloserToEnemies();
//		
//		ArrayList<Unit> toMove = new ArrayList<Unit>();
//		Tile[][] tBoard = testLogic.getTBoard();
		
		//check board
		//move unit
		//check board
		
//		assertEquals();
	}
	
	@Test
	public void test_canIMove(){
		boolean expectedResult = false;
		createBaseTestConditions();
		AI ai = (AI)testLogic.getP2();
		assertEquals(ai.canIMove(), expectedResult);
	}
	
	@Test
	public void test_canIBuy(){
		boolean expectedResult = true;
		createBaseTestConditions();
		AI ai = (AI)testLogic.getP2();
		assertEquals(ai.canIBuy(), expectedResult);
	}
	
	@Test
	public void test_canIAttack(){
		boolean expectedResult = false;
		createBaseTestConditions();
		AI ai = (AI)testLogic.getP2();
		assertEquals(ai.canIAttack(), expectedResult);
	}
	
	@Test
	public void test_canICapture(){
		boolean expectedResult = false;
		createBaseTestConditions();
		AI ai = (AI)testLogic.getP2();
		assertEquals(ai.canICapture(), expectedResult);
	}
	
	
	@Test
	public void test_getPossibleMoves(){
		createBaseTestConditions();
		AI ai = (AI) testLogic.getP2();
		ArrayList<Unit> testUnits = new ArrayList<Unit>();
		
		Infantry inf = new Infantry(ai.getPNum());
		HeavyTank heav = new HeavyTank(ai.getPNum());
		MedTank med = new MedTank(ai.getPNum());
		
		testLogic.setUnit(0, 0, inf);
		testLogic.setUnit(2, 4, heav);
		testLogic.setUnit(5, 2, med);
		
		testLogic.setUnit(1, 1, new Infantry(1));
		
		ai.getLogic(testLogic);
		testUnits.add(inf);
		testUnits.add(heav);
		testUnits.add(med);
		assertEquals(ai.getPossibleMoves(),testUnits);
		
	}

}
