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

		for (int i = 0; i < commands.size(); i++)
		{
			if(commands.get(i).toUpperCase().equals("MOVE"))
			{
				final Button moveUnit = (Button) findViewById(R.id.move_unit);
				moveUnit.setVisibility(Button.VISIBLE);
			}
			if(commands.get(i).toUpperCase().equals("ATTACK"))
			{
				final Button attackUnit = (Button) findViewById(R.id.attack_unit);
				attackUnit.setVisibility(Button.VISIBLE);
			}
			if(commands.get(i).toUpperCase().equals("CAPTURE"))
			{
				final Button captureBuilding = (Button) findViewById(R.id.capture_building);
				captureBuilding.setVisibility(Button.VISIBLE);
			}
			if(commands.get(i).toUpperCase().equals("UNITINFO"))
			{
				final Button uInfo = (Button) findViewById(R.id.unit_info);
				uInfo.setVisibility(Button.VISIBLE);
			}
		}

		sd.saveGGVData(ggv);
	}

	@Override
	public void onClick(View v)
	{
		// MOVE = 0;
		// ATTACK = 1;
		// CAPTURE = 2;

		GUIGameValues tempGGV = new GUIGameValues();

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
			Intent i = new Intent(this, UnitInfoScreen.class);
			startActivity(i);
			break;
		}
	}
}
