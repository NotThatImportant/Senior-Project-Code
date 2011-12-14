package org.game.advwars;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import controller.Controller;
import dataconnectors.SaveGUIData;

public class GameBoardView extends View
{
	protected static int mTileSize;
	protected static int mXTileCount;
	protected static int mYTileCount;
	private static int mXOffset;
	private static int mYOffset;
	private Bitmap[] mTileArray;
	private Bitmap[] mRedUnitArray;
	//private Bitmap[] mRedBuildingArray;
	private Bitmap[] mBlueUnitArray;
	//private Bitmap[] mBlueBuildingArray;
	private Bitmap[] mUncaptBuildingArray;
	private char[][] mTileGrid;
	private int[][] mPlayer1Units;
	private int[][] mPlayer2Units;
	private int mX;
	private int mY;
	private final Paint mPaint = new Paint();
	private Canvas canvas;
	private Controller controller;
	private SaveGUIData sd = new SaveGUIData();
	private GUIGameValues ggvGlobal;

	public GameBoardView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);

		mX = 0;
		mY = 0;

		ggvGlobal = sd.loadGGVData();

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GameBoardView);
		mTileSize = a.getInt(R.styleable.GameBoardView_tileSize, 30);
		initGameBoardView();
		a.recycle();
	}

	public GameBoardView(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		mX = 0;
		mY = 0;

		ggvGlobal = sd.loadGGVData();

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GameBoardView);
		mTileSize = a.getInt(R.styleable.GameBoardView_tileSize, 30);
		initGameBoardView();
		a.recycle();
	}

	private void initGameBoardView()
	{
		setFocusable(true);

		Resources r = this.getContext().getResources();

		resetTiles(13);

		// Terrain tiles

		loadTile(0, r.getDrawable(R.drawable.grass2), mTileArray);
		loadTile(1, r.getDrawable(R.drawable.mountain), mTileArray);
		loadTile(2, r.getDrawable(R.drawable.road), mTileArray);
		loadTile(3, r.getDrawable(R.drawable.water), mTileArray);
		loadTile(4, r.getDrawable(R.drawable.selected), mTileArray);
		loadTile(5, r.getDrawable(R.drawable.blue_building), mTileArray);
		loadTile(6, r.getDrawable(R.drawable.blue_hq), mTileArray);
		loadTile(7, r.getDrawable(R.drawable.blue_factory), mTileArray);
		loadTile(8, r.getDrawable(R.drawable.red_building), mTileArray);
		loadTile(9, r.getDrawable(R.drawable.red_hq), mTileArray);
		loadTile(10, r.getDrawable(R.drawable.red_factory), mTileArray);
		loadTile(11, r.getDrawable(R.drawable.uncaptured_building), mTileArray);
		loadTile(12, r.getDrawable(R.drawable.uncaptured_factory), mTileArray);

		// Red units tiles

		mRedUnitArray = new Bitmap[9];

		loadTile(0, r.getDrawable(R.drawable.red_anti_air), mRedUnitArray);
		loadTile(1, r.getDrawable(R.drawable.red_artillery), mRedUnitArray);
		loadTile(2, r.getDrawable(R.drawable.red_tank), mRedUnitArray);
		loadTile(3, r.getDrawable(R.drawable.red_infantry), mRedUnitArray);
		loadTile(4, r.getDrawable(R.drawable.red_mech), mRedUnitArray);
		loadTile(5, r.getDrawable(R.drawable.red_tank), mRedUnitArray);
		loadTile(6, r.getDrawable(R.drawable.red_recon), mRedUnitArray);
		loadTile(7, r.getDrawable(R.drawable.red_rocket), mRedUnitArray);
		loadTile(8, r.getDrawable(R.drawable.red_tank), mRedUnitArray);

		// Blue units tiles

		mBlueUnitArray = new Bitmap[9];

		loadTile(0, r.getDrawable(R.drawable.blue_anti_air), mBlueUnitArray);
		loadTile(1, r.getDrawable(R.drawable.blue_artillery), mBlueUnitArray);
		loadTile(2, r.getDrawable(R.drawable.blue_tank), mBlueUnitArray);
		loadTile(3, r.getDrawable(R.drawable.blue_infantry), mBlueUnitArray);
		loadTile(4, r.getDrawable(R.drawable.blue_mech), mBlueUnitArray);
		loadTile(5, r.getDrawable(R.drawable.blue_tank), mBlueUnitArray);
		loadTile(6, r.getDrawable(R.drawable.blue_recon), mBlueUnitArray);
		loadTile(7, r.getDrawable(R.drawable.blue_rocket), mBlueUnitArray);
		loadTile(8, r.getDrawable(R.drawable.blue_tank), mBlueUnitArray);
	}

	public void resetTiles(int tilecount)
	{
		mTileArray = new Bitmap[tilecount];
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		mXTileCount = (int) Math.floor(w / mTileSize);
		mYTileCount = (int) Math.floor(h / mTileSize);

		mXOffset = ((w - (mTileSize * mXTileCount)) / 2);
		mYOffset = ((h - (mTileSize * mYTileCount)) / 2);
		//
		//        mXOffset = 0;
		//        mYOffset = 0;

		//mTileGrid = new int[mXTileCount][mYTileCount];
		// clearTiles();
	}

	public void setController(Controller c) {
		this.controller = c;
	}

	public Controller getController(){
		return controller;
	}


	public void loadTile(int key, Drawable tile, Bitmap[] array)
	{
		Bitmap bitmap = Bitmap.createBitmap(mTileSize, mTileSize, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap);
		tile.setBounds(0, 0, mTileSize, mTileSize);
		tile.draw(canvas);

		array[key] = bitmap;
	}

	public void clearTiles()
	{
		for (int x = 0; x < mXTileCount; x++) {
			for (int y = 0; y < mYTileCount; y++) {
				setTile('g', x, y);
			}
		}
	}

	public void setTile(char tileindex, int x, int y)
	{
		mTileGrid[x][y] = tileindex;
	}

	@Override
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);

		for (int x = mX; x < mXTileCount + mX && x < mTileGrid.length; x += 1)
		{
			for (int y = mY; y < mYTileCount + mY && y < mTileGrid[x].length; y += 1)
			{

				canvas.drawBitmap(mTileArray[getTileByType(mTileGrid[x][y])], 
						mXOffset + (x - mX) * mTileSize,
						mYOffset + (y - mY) * mTileSize,
						mPaint);

				if(mPlayer1Units[x][y] > -1)
					canvas.drawBitmap(drawUnitTile(mPlayer1Units[x][y], ggvGlobal.getFaction()), 
							mXOffset + (x - mX) * mTileSize,
							mYOffset + (y - mY) * mTileSize,
							mPaint);

				if(mPlayer2Units[x][y] > -1)
					canvas.drawBitmap(drawUnitTile(mPlayer2Units[x][y],ggvGlobal.getAIFaction()), 
							mXOffset + (x - mX) * mTileSize,
							mYOffset + (y - mY) * mTileSize,
							mPaint);
			}
		}
//		drawPossibleMoves(canvas);
	}
	
// Not working yet
//	private void drawPossibleMoves(Canvas canvas) {
//		char[][] moves = ggvGlobal.getMovement();
//
//		if (moves != null) {
//			for (int x = mX; x < mXTileCount + mX && x < mTileGrid.length; x += 1)
//			{
//				for (int y = mY; y < mYTileCount + mY && y < mTileGrid[x].length; y += 1)
//				{
//					if (moves[x][y] == 'x') {
//						canvas.drawBitmap(mTileArray[4], mXOffset + (x - mX) * mTileSize, 
//								mYOffset + (y - mY) * mTileSize, mPaint);
//					}
//				}
//			}
//		}
//	}

	private Bitmap drawUnitTile(int i, String faction) {
		if(faction.equalsIgnoreCase("blue")){
			return mBlueUnitArray[i];
		}else{
			return mRedUnitArray[i];
		}
	}

	private int getTileByType(char c)
	{
		String faction = ggvGlobal.getFaction();
		int result = 0;
		switch(c){
		case 'g' :
			result = 0;
			break;
		case 'm':
			result = 1;
			break;
		case  'r':
			result = 2;
			break;
		case 'w':
			result = 3;
			break;
		case 'h':
			if(faction.equalsIgnoreCase("blue"))
				result = 6;
			else
				result = 9;
			break;
		case 'H':
			if(faction.equalsIgnoreCase("red"))
				result = 6;
			else
				result = 9;
			break;
		case 'x':
			if(faction.equalsIgnoreCase("blue"))
				result = 5;
			else
				result = 8;
			break;
		case 'X':
			if(faction.equalsIgnoreCase("red"))
				result = 5;
			else
				result = 8;
			break;
		case 'q':
			if(faction.equalsIgnoreCase("blue"))
				result = 7;
			else
				result = 10;
			break;
		case 'Q':
			if(faction.equalsIgnoreCase("red"))
				result = 7;
			else
				result = 10;
			break;
		case 'b':
			result = 11;
			break;
		case 'p':
			result = 12;
			break;
		}
		return result;
	}

	public void update()
	{
		clearTiles();
	}

	/*private void updateWalls()
    {
        for (int x = 0; x < mXTileCount; x++) {
            setTile(0, x, 0);
            setTile(0, x, mYTileCount - 1);
        }
        for (int y = 1; y < mYTileCount - 1; y++) {
            setTile(0, 0, y);
            setTile(0, mXTileCount - 1, y);
        }
    }*/

	public void redrawBoard()
	{
		onDraw(this.canvas);
	}

	public int getXTileCount()
	{
		return mXTileCount;
	}

	public int getYTileCount()
	{
		return mYTileCount;
	}

	/*public ArrayList<String> selectPoint(float x, float y)
	{
		x = x - mXOffset;
		y = y - mYOffset;
		int tileX = (int) x/mTileSize;
		int tileY = (int) y/mTileSize;

		if(tileX < mXTileCount && tileY < mYTileCount &&
				tileX >= 0 && tileY >= 0){
			int[] coor = new int[2];
			coor[0] = tileX + mX;
			coor[1] = tileY + mY;
			return controller.selectCoordinates(coor[0],coor[1]);
		}

		return null;
	}*/

	public int[] getPoints(float x, float y)
	{
		x = x - mXOffset;
		y = y - mYOffset;
		int tileX = (int) x/mTileSize;
		int tileY = (int) y/mTileSize;

		if(tileX < mTileGrid.length && tileY < mTileGrid.length && tileX >= 0 && tileY >= 0)
		{
			int[] coor = new int[2];
			coor[0] = tileX + mX;
			coor[1] = tileY + mY;
			return coor;
		}

		return null;
	}

	public void translateBoard(float x, float y)
	{
		mX += (x/mTileSize) * 0.2;
		mY += (y/mTileSize) * 0.2;

		if(mX < 0)
			mX = 0;
		if(mX > 100)
			mX = 100;

		if(mY < 0)
			mY = 0;
		if(mY > 100)
			mY = 100;

		invalidate();
	}

	public void scaleBoard(float scale, float x, float y)
	{
		if(scale < 0.2)
			scale = 0.5f;
		if(scale > 2)
			scale = 2.0f;

		mTileSize = (int) (mTileSize * scale);

		if(mTileSize < 10)
			mTileSize = 10;
		if(mTileSize > 50)
			mTileSize = 50;

		initGameBoardView();
		onSizeChanged(getWidth(), getHeight(), getWidth(), getHeight());
		invalidate();
	}

	public void initGame()
	{
		mTileGrid = controller.getBoard();
		mPlayer1Units = controller.getConvertedUnits(0);
		mPlayer2Units = controller.getConvertedUnits(1);
		this.invalidate();
	}


}
