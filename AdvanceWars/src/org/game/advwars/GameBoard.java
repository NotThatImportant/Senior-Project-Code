package org.game.advwars;

import android.app.Activity;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

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
		// Get screen dimensions in pixels, should be 400x240
		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();
		
		
		x = (int) (event.getRawX()/12);
		y = (int) (event.getRawY()/12) -3;


		//Toast.makeText(v.getContext(), "You selected " + "x: " + event.getRawX() + "--> " + x + " y: " + event.getRawY() +"--> " + y, Toast.LENGTH_SHORT).show();
		
		
		v.getWidth();
		if (x < gameBoardView.getXTileCount() && y < gameBoardView.getYTileCount() &&
				x >= 0 && y >= 0)
		{
			
			gameBoardView.setTile(1, x, y);
			v.invalidate();
			
			return true;
		}
		
		return false;
	}
}