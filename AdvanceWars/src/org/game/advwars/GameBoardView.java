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

public class GameBoardView extends View
{
	protected static int mTileSize;
    protected static int mXTileCount;
    protected static int mYTileCount;
    private static int mXOffset;
    private static int mYOffset;
    private Bitmap[] mTileArray;
    private int[][] mTileGrid;
    private final Paint mPaint = new Paint();
	
    public GameBoardView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GameBoardView);
        mTileSize = a.getInt(R.styleable.GameBoardView_tileSize, 12);
        initSnakeView();
        a.recycle();
    }

    public GameBoardView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GameBoardView);
        mTileSize = a.getInt(R.styleable.GameBoardView_tileSize, 12);
        initSnakeView();
        a.recycle();
    }
    
    private void initSnakeView()
    {
        setFocusable(true);

        Resources r = this.getContext().getResources();
        
        resetTiles(4);
        loadTile(0, r.getDrawable(R.drawable.grass));
    	
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

        mTileGrid = new int[mXTileCount][mYTileCount];
        clearTiles();
    }
    
    public void loadTile(int key, Drawable tile)
    {
        Bitmap bitmap = Bitmap.createBitmap(mTileSize, mTileSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        tile.setBounds(0, 0, mTileSize, mTileSize);
        tile.draw(canvas);
        
        mTileArray[key] = bitmap;
    }
    
    public void clearTiles()
    {
        for (int x = 0; x < mXTileCount; x++) {
            for (int y = 0; y < mYTileCount; y++) {
                setTile(0, x, y);
            }
        }
    }
    
    public void setTile(int tileindex, int x, int y)
    {
        mTileGrid[x][y] = tileindex;
    }
    
    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        for (int x = 0; x < mXTileCount; x += 1) {
            for (int y = 0; y < mYTileCount; y += 1) {
                if (mTileGrid[x][y] > 0) {
                    canvas.drawBitmap(mTileArray[mTileGrid[x][y]], 
                    		mXOffset + x * mTileSize,
                    		mYOffset + y * mTileSize,
                    		mPaint);
                }
            }
        }

    }
    
    public void update()
    {
          clearTiles();
          //updateWalls();
    }
}
