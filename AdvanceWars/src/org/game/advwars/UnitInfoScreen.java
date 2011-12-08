package org.game.advwars;

import java.util.ArrayList;

import dataconnectors.SaveGUIData;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class UnitInfoScreen extends Activity
{
	private GUIGameValues ggv = new GUIGameValues();
	private SaveGUIData sd = new SaveGUIData();
	private ArrayList<String> unitInfo = null;
	
	protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unit_info_screen);
        
        ggv = sd.loadGGVData();
        unitInfo = ggv.getUnitInfo();
        
        if (unitInfo != null)
        {
        	final TextView unitName = (TextView) findViewById(R.id.unit_name);
        	unitName.setText(unitInfo.get(0));
        }
    }
}
