package dbconnector;

import static android.provider.BaseColumns._ID;
import static dbconnector.Constants.TABLE_NAME;
import static dbconnector.Constants.PLAYERNAME;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseData extends SQLiteOpenHelper
{
	private static final String DATABASE_NAME = "gridwars.sqlite";
	private static final int DATABASE_VERSION = 1;

	public DatabaseData(Context ctx)
	{
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + _ID + "INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ PLAYERNAME + " TEXT NOT NULL);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

}