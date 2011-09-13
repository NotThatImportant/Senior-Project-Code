package terrain;


/**
 * Discuss how to produce units
 *
 */
public class Tile {

	protected int height;
	protected char type;
	
	public Tile (char pType) {
		type = pType;
		switch (type) {
			case 'g': height = 0;break;
			case 'm': height = 2;break;
			case 'r': height = 0;break;
			default: height = 0;break;
		}
	}
	
	public char getType() {
		return type;
	}
	
	public int getHeight() {
		return height;
	}
	
}
