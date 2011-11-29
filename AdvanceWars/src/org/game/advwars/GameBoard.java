package org.game.advwars;

import android.view.*;
import player.Player;
import controller.Controller;
import dataconnectors.SaveGUIData;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PointF;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.View.OnTouchListener;

import java.util.ArrayList;

public class GameBoard extends Activity implements OnTouchListener
{
	private MediaPlayer mp;
	private Controller c;
	private static final String TAG = "GameBoard";
	// We can be in one of these 3 states
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	static final int PRESS = 3;

	int mode = NONE;

	// Remember some things for zooming
	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;

	private GameBoardView gameBoardView;
	private GUIGameValues ggvGlobal = new GUIGameValues();
	private SaveGUIData sd = new SaveGUIData();
	private char p1Char;
	private char p2Char;
	private  ArrayList<String> commands;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gameboard_layout);
		// Tells Android to adjust the volume of music instead of the ringer
		//setVolumeControlStream(AudioManager.STREAM_MUSIC);

		// Load player values
		ggvGlobal = sd.loadGGVData();

		if (ggvGlobal.getFaction().equals("Blue"))
		{
			p1Char = 'b';
			p2Char = 'r';
		}
		else
		{
			p1Char = 'r';
			p2Char = 'b';
		}

		gameBoardView = (GameBoardView) findViewById(R.id.gameboard);
		gameBoardView.setOnTouchListener(this);

		Player p1 = new Player("Player1", 0, p1Char);
		Player p2 = new Player("Player2", 1, p2Char);
		c = new Controller(p1, p2, true, ggvGlobal.getMap());
		gameBoardView.setController(c);
		gameBoardView.initGame();

		// Background music
		/*if (mp != null)
			mp.release();

		mp = MediaPlayer.create(this, R.raw.background_music);
		mp.setLooping(true);
		mp.start();*/
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_MENU)
		{
			openMainMenuDialog();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void openMainMenuDialog()
	{
		new AlertDialog.Builder(this)
		.setTitle(R.string.app_name)
		.setItems(R.array.in_game_menu,
				new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialogInterface, int i)
			{
				if (i == 0)
				{
					// End turn
				}
				else if (i == 1)
				{
					// Save game
				}
				else if (i == 2)
				{
					//Intent intent = new Intent(this, MainMenu.class);
					//startActivity(intent);
					
					// Quit game
				}
				else
				{
					//Intent intent2 = new Intent(this, MainMenu.class);
					//startActivity(intent2);
				}
			}
		})
		.show();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		// Handle touch events here...
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			start.set(event.getX(), event.getY());
			Log.d(TAG, "mode=DRAG");
			mode = PRESS;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			oldDist = spacing(event);
			Log.d(TAG, "oldDist=" + oldDist);
			if (oldDist > 10f) {
				midPoint(mid, event);
				mode = ZOOM;
				Log.d(TAG, "mode=ZOOM");
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			if(mode == PRESS) {
				//options = gameBoardView.selectPoint(event.getX(), event.getY());
				//openContextMenu(v);
				int[] selectedPoints = gameBoardView.getPoints(event.getX(), event.getY());
				
				if (selectedPoints != null)
					commands = c.selectCoordinates(selectedPoints[0], selectedPoints[1]);
				
				if (commands != null)
				{
					ggvGlobal.setAvailableCommands(commands);
					Intent i = new Intent(this, InGameMenu.class);
					i.putExtra("ggv", ggvGlobal);
	    			startActivity(i);
	    			ggvGlobal = sd.loadGGVData();
				}
			}
			mode = NONE;
			Log.d(TAG, "mode=NONE");
			break;
		case MotionEvent.ACTION_MOVE:
			if(mode == PRESS && 
			(Math.abs(event.getX() - start.x) > 20 ||
					Math.abs(event.getY() - start.y) > 20)){
				mode = DRAG;
			}

			if (mode == DRAG) {
				// ...
				gameBoardView.translateBoard(-(event.getX() - start.x),
						-(event.getY() - start.y));
			}
			else if (mode == ZOOM) {
				float newDist = spacing(event);
				if (newDist > 10f) {
					float scale = newDist / oldDist;
					gameBoardView.scaleBoard(scale,  mid.x, mid.y);
				}
			}
			break;
		}

		//Toast.makeText(v.getContext(), "y: " +event.getY()+ " \nRawY:" + event.getRawY(), Toast.LENGTH_SHORT).show();





		v.invalidate();

		return true;
	}


	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Context Menu");
		menu.add(0, v.getId(), 0, "Action 1");
		menu.add(0, v.getId(), 0, "Action 2");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		return true;
	}


	/** Determine the space between the first two fingers */
	private float spacing(MotionEvent event) {
		// ...
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	/** Calculate the mid point of the first two fingers */
	private void midPoint(PointF point, MotionEvent event) {
		// ...
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}
}
