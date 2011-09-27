package test;

import static org.junit.Assert.*;
import gameplay.Logic;

import maps.MapReader;

import org.junit.Test;

import units.*;

public class TestLogic {
	
	@Test
	public void createLogic() {
		Logic log = new Logic("map1", 'r','b', "Austin", "Matt", false);
		assertNotNull(log);
	}
	
	@Test
	public void testBoards() {
		Logic log = new Logic("map1", 'r','b', "Austin", "Matt", false);
		assertNotNull(log.getUB());
		
		assertNull(log.getUB()[0][0]);
	}
	
	@Test
	public void testInfo() {
		Logic log = new Logic("map1", 'r', 'b', "Austin", "Matt", false);
		MapReader mr = new MapReader("map1");
		
		assertNotNull(log.getP1());
		assertNotNull(log.getP2());
		
		assertEquals(log.getP1().getPName(), "Austin");
		assertEquals(log.getP2().getPName(), "Matt");
		
		assertEquals(log.getP1().getFact(), 'r');
		assertEquals(log.getP2().getFact(), 'b');
		
		assertEquals(log.getSize(), mr.getSize());
		
		assertEquals(log.getP1().getCash(), 6000);
		
		log.econDay(log.getP1());
		
		assertEquals(log.getP1().getCash(), 9000);
		
		log.getP1().addBuilding();
		log.econDay(log.getP1());
		
		assertEquals(log.getP1().getCash(), 13000);
		
		Infantry inf = new Infantry(1);
		Infantry inf2 = new Infantry(1);
		
		log.produceUnit(log.getP1(), inf, log.getTile(1, 1));
		
		assertEquals(log.getP1().getNumUnits(), 1);
		assertTrue(log.didWin(log.getP1()));
		
		log.produceUnit(log.getP2(), inf2, log.getTile(1,2));
		
		assertEquals(log.getP2().getNumUnits(), 1);
		
		System.out.println("Unit 1 HP: " + log.getUnit(1,1).getHP());
		System.out.println("Unit 2 HP: " + log.getUnit(1,2).getHP());
		
		System.out.println("After battle!");
		
		log.battle(log.getUnit(1, 1), log.getUnit(1, 2), 0);

		System.out.println("Unit 1 HP: " + log.getUnit(1,1).getHP());
		System.out.println("Unit 2 HP: " + log.getUnit(1,2).getHP());
		
		assertTrue(log.getUnit(1, 1).getHP() != 10);
	}
	
	@Test
	public void testMove() {
		Logic log = new Logic("map1", 'r', 'b', "Austin", "Matt", false);
		Infantry inf = new Infantry(1);
		log.produceUnit(log.getP1(), inf, log.getTile(1,1));
		
		
		
		char[][] board = log.getMoves(inf);
		
		int size = board[1].length;
		for(int i = 0; i < size; i++) {
			System.out.print(i + " ");
			for (int j = 0; j < size; j++) {
				System.out.print(board[i][j]);
			}
			System.out.println();
		}
		
		System.out.println("++++++++++++++++++++++++");
		
		Unit[][] uBoard = log.getUB();
		size = uBoard[1].length;
		
		for (int r = 0; r < size; r++) {
			System.out.print(r + " ");
			for (int c = 0; c < size; c++) {
				if (uBoard[r][c] != null)
					System.out.print(uBoard[r][c].getType());
				else
					System.out.print("-");
			}
			System.out.println();
		}
		
		System.out.println("++++++++++++++++++++++++++++");
		
		if (board[1][2] == 'x') {
			log.moveUnit(inf, 1, 2);
		} else
			log.moveUnit(inf, 1, 2);
		
		uBoard = log.getUB();
		size = uBoard[1].length;
		
		for (int r = 0; r < size; r++) {
			for (int c = 0; c < size; c++) {
				if (uBoard[r][c] != null)
					System.out.print(uBoard[r][c].getType());
				else
					System.out.print("-");
			}
			System.out.println();
		}
		
	}
	
}
