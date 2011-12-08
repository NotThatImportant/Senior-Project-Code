package org.game.advwars;

import java.util.ArrayList;

import dataconnectors.SaveGUIData;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class InGameMenu extends Activity implements OnClickListener
{
	private GUIGameValues ggv = new GUIGameValues();
	private ArrayList<String> commands;
	private SaveGUIData sd = new SaveGUIData();

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.in_game_menu);

		Bundle extras = getIntent().getExtras();
		this.ggv = (GUIGameValues) extras.getSerializable("ggv");
		sd.saveGGVData(ggv);

		commands = ggv.getAvailableCommands();

		// Set up click listeners for all buttons
		View move = findViewById(R.id.move_unit);
		move.setOnClickListener(this);
		View attack = findViewById(R.id.attack_unit);
		attack.setOnClickListener(this);
		View capture = findViewById(R.id.capture_building);
		capture.setOnClickListener(this);
		View unitInfo = findViewById(R.id.unit_info);
		unitInfo.setOnClickListener(this);
		
		// Convert commands to upper case for compatibility reasons
		for (int i = 0; i < commands.size(); i++)
			commands.set(i, commands.get(i).toUpperCase());

		// Disable buttons based on commands that are passed
		if(!commands.contains("MOVE"))
		{
			final Button moveUnit = (Button) findViewById(R.id.move_unit);
			moveUnit.setEnabled(false);
		}
		else
		{
			final Button moveUnit = (Button) findViewById(R.id.move_unit);
			moveUnit.setEnabled(true);
		}

		if(!commands.contains("ATTACK"))
		{
			final Button attackUnit = (Button) findViewById(R.id.attack_unit);
			attackUnit.setEnabled(false);
		}
		else
		{
			final Button attackUnit = (Button) findViewById(R.id.attack_unit);
			attackUnit.setEnabled(true);
		}

		if(!commands.contains("CAPTURE"))
		{
			final Button captureBuilding = (Button) findViewById(R.id.capture_building);
			captureBuilding.setEnabled(false);
		}
		else
		{
			final Button captureBuilding = (Button) findViewById(R.id.capture_building);
			captureBuilding.setEnabled(true);
		}

		if(!commands.contains("UNITINFO"))
		{
			final Button uInfo = (Button) findViewById(R.id.unit_info);
			uInfo.setEnabled(false);
		}
		else
		{
			final Button uInfo = (Button) findViewById(R.id.unit_info);
			uInfo.setEnabled(true);
		}

		sd.saveGGVData(ggv);
	}

	@Override
	public void onClick(View v)
	{
		// MOVE = 0;
		// ATTACK = 1;
		// CAPTURE = 2;

		GUIGameValues tempGGV = sd.loadGGVData();

		switch (v.getId())
		{
		case R.id.move_unit:
			tempGGV.setSelectedCommand(0);
			sd.saveGGVData(tempGGV);
			this.finish();
			break;
		case R.id.attack_unit:
			tempGGV.setSelectedCommand(1);
			sd.saveGGVData(tempGGV);
			this.finish();
			break;
		case R.id.capture_building:
			tempGGV.setSelectedCommand(2);
			sd.saveGGVData(tempGGV);
			this.finish();
			break;
		case R.id.unit_info:
			tempGGV.setSelectedCommand(3);
			sd.saveGGVData(tempGGV);
			Intent i = new Intent(this, UnitInfoScreen.class);
			startActivity(i);
			break;
		}
	}
}
