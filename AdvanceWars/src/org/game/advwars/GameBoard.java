package org.game.advwars;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class GameBoard extends Activity implements OnTouchListener
{
	private int x;
	private int y;
	private GameBoardView gameBoardView;
	
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameboard_layout);
        
        gameBoardView = (GameBoardView) findViewById(R.id.gameboard);
        //gameBoardView.update();
        
        gameBoardView.setOnTouchListener(this);
    }
	
	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		x = (int) event.getX() / 3;
		y = (int) event.getY() / 3;
		
		gameBoardView.setTile(1, x, y);
		gameBoardView.redrawBoard();
		
		return true;
	}
}