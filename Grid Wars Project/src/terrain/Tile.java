package terrain;


/**
 * Discuss how to produce units
 *
 */
public class Tile {

	private int height;
	private char type;
	private int owner;
	private int x, y;
	private int bC; 
	
	public Tile (char pType) {
		type = pType;
		bC = 0;
		switch (type) {
			case 'g': height = 0;break;
			case 'm': height = 2;break;
			case 'r': height = 0;break;
			case 'b': height = 3; owner = -1; break; 
			case 'p': height = 3; owner = -1; break;
			default: height = 0;break;
		}
	}

	public void setBC(int pNum) {
		bC = pNum;
	}
	
	public int getBC() {
		int oBC = bC;
		if (bC != 0)
			bC = 0;
		return oBC;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setX(int pX) {
		x = pX;
	}
	
	public void setY(int pY) {
		y = pY;
	}
	
	public char getType() {
		return type;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setOwner(int player) {
		owner = player;
	}
	
	public int getOwner() {
		return owner;
	}
	
}
