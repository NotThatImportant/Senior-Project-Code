package ai;

public class Terrain {

	private int height;
	private char type;
	
	public Terrain(){
		height = 0;
		type = 'g';
	}
	
	public void setType(char pType){
		type = pType;
		calculateHeight();
	}
	
	private void calculateHeight(){
		if(type == 'm')
			height = 1;
		else if (type == 'w')
			height = -1;
		else 
			height = 0;
	}
	
	
}
