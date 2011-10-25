package org.game.advwars;

import android.app.Activity;
import android.os.Bundle;

public class GameBoard extends Activity
{
	private GameBoardView gameBoardView;
	
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameboard_layout);
        
        gameBoardView = (GameBoardView) findViewById(R.id.gameboard);
        gameBoardView.update();
    }
}