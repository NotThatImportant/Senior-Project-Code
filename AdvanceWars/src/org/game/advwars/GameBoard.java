package org.game.advwars;

import android.app.Activity;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.Display;
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
        
        gameBoardView.setOnTouchListener(this);
    }
	
	/*@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
			x = (int) event.getX() / 3;
			y = (int) event.getY() / 3;
		
			gameBoardView.setTile(1, x, y);
			gameBoardView.redrawBoard();
        }
		
		return true;
	}*/

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		RectF position = new RectF();
		
		
		// Get screen dimensions in pixels, should be 400x240
		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();
		
		
		x = (int) (width / event.getX());
		y = (int) (height / event.getY());

		if (x < gameBoardView.getXTileCount() && y < gameBoardView.getYTileCount())
		{
			gameBoardView.setTile(1, x, y);
			gameBoardView.redrawBoard();
			v.invalidate();
			
			return true;
		}
		
		return false;
	}
}