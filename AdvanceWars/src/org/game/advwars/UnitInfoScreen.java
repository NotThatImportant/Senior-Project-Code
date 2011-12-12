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
        	
        	final TextView unitHealth = (TextView) findViewById(R.id.unit_health);
        	unitHealth.setText(unitInfo.get(1));
        	
        	final TextView unitMoveRange = (TextView) findViewById(R.id.unit_move_range);
        	unitMoveRange.setText(unitInfo.get(2));
        	
        	final TextView unitAttack = (TextView) findViewById(R.id.unit_attack);
        	unitAttack.setText(unitInfo.get(3));
        	
        	final TextView unitAttackRange = (TextView) findViewById(R.id.unit_attack_range);
        	unitAttackRange.setText(unitInfo.get(4));
        	
        	final TextView unitArmor = (TextView) findViewById(R.id.unit_armor);
        	unitArmor.setText(unitInfo.get(5));
        	
        	final TextView unitAmmo = (TextView) findViewById(R.id.unit_ammo);
        	unitAmmo.setText(unitInfo.get(6));
        	
        	final TextView unitFuel = (TextView) findViewById(R.id.unit_fuel);
        	unitFuel.setText(unitInfo.get(7));
        }
    }
}
