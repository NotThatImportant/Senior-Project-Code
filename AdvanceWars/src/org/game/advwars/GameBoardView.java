package org.game.advwars;

import java.util.ArrayList;
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

public class GameBoardView extends View
{
	protected static int mTileSize;
    protected static int mXTileCount;
    protected static int mYTileCount;
    private static int mXOffset;
    private static int mYOffset;
    private Bitmap[] mTileArray;
    private Bitmap[] mRedUnitArray;
    private char[][] mTileGrid;
    private int[][] mPlayer1Units;
    private int[][] mPlayer2Units;
    private int mX;
    private int mY;
    private final Paint mPaint = new Paint();
    private Canvas canvas;
    private Controller controller;
	
    public GameBoardView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        mX = 0;
        mY = 0;

        
        
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
        

        
        
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GameBoardView);
        mTileSize = a.getInt(R.styleable.GameBoardView_tileSize, 30);
        initGameBoardView();
        a.recycle();
    }
    
    private void initGameBoardView()
    {
        setFocusable(true);

        Resources r = this.getContext().getResources();
        
        resetTiles(5);
        loadTile(0, r.getDrawable(R.drawable.ground), mTileArray);
        loadTile(1, r.getDrawable(R.drawable.mountain), mTileArray);
        loadTile(2, r.getDrawable(R.drawable.road), mTileArray);
        loadTile(3, r.getDrawable(R.drawable.water), mTileArray);
        loadTile(4, r.getDrawable(R.drawable.selected), mTileArray);

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
                
            }
        }
        
        /*mPaint.setColor(Color.RED);
        canvas.drawLine(0, 0, 10, 0, mPaint);
        
        canvas.drawLine(0, 0, 0, 10, mPaint);
        
        mPaint.setColor(Color.BLUE);
        canvas.drawLine(0, 10, 0, 100, mPaint);*/

    }
    
    private int getTileByType(char c) {
		int result = 0;
		
		if(c == 'g')
			result = 0;
		else if(c == 'm')
			result = 1;
		else if (c == 'r')
			result = 2;
		else if (c == 'w')
			result = 3;
        else if (c == 'h')
            result = 4;
        else if (c == 'H')
            result = 4;
        else if (c == 'x')
        	result = 4;
        else if (c == 'X')
        	result = 4;
		
		return result;
	}

	public void update()
    {
          clearTiles();
          //updateWalls();
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

	public ArrayList<String> selectPoint(float x, float y)
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
        mPlayer1Units = controller.getConvertedUnits(1);
        mPlayer2Units = controller.getConvertedUnits(2);
	}


}
