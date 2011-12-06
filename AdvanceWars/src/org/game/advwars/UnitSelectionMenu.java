package org.game.advwars;

import java.util.ArrayList;
import dataconnectors.SaveGUIData;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class UnitSelectionMenu extends Activity implements OnClickListener
{
	private GUIGameValues ggv = new GUIGameValues();
	private ArrayList<String> commands;
	private SaveGUIData sd = new SaveGUIData();

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.unit_selection_menu);

		Bundle extras = getIntent().getExtras();
		this.ggv = (GUIGameValues) extras.getSerializable("ggv");

		commands = ggv.getAvailableCommands();

		// Set up click listeners for all buttons
		View infantry = findViewById(R.id.infantry);
		infantry.setOnClickListener(this);

		View mech = findViewById(R.id.mech);
		mech.setOnClickListener(this);

		View recon = findViewById(R.id.recon);
		recon.setOnClickListener(this);

		View artillery = findViewById(R.id.artillery);
		artillery.setOnClickListener(this);

		View antiAir = findViewById(R.id.anti_air);
		antiAir.setOnClickListener(this);

		View tank = findViewById(R.id.tank);
		tank.setOnClickListener(this);

		View chopper = findViewById(R.id.chopper);
		chopper.setOnClickListener(this);

		View rockets = findViewById(R.id.rockets);
		rockets.setOnClickListener(this);

		View medTank = findViewById(R.id.med_tank);
		medTank.setOnClickListener(this);

		View fighterJet = findViewById(R.id.fighter_jet);
		fighterJet.setOnClickListener(this);

		View heavyTank = findViewById(R.id.heavy_tank);
		heavyTank.setOnClickListener(this);

		View bomber = findViewById(R.id.bomber);
		bomber.setOnClickListener(this);

		// Convert commands to upper case for compatibility reasons
		for (int i = 0; i < commands.size(); i++)
			commands.set(i, commands.get(i).toUpperCase());

		// Disable buttons based on commands that are passed
		if(!commands.contains("INFANTRY"))
		{
			final Button infantryButton = (Button) findViewById(R.id.infantry);
			infantryButton.setEnabled(false);
		}
		else
		{
			final Button infantryButton = (Button) findViewById(R.id.infantry);
			infantryButton.setEnabled(true);
		}
		
		if(!commands.contains("MECH"))
		{
			final Button mechButton = (Button) findViewById(R.id.mech);
			mechButton.setEnabled(false);
		}
		else
		{
			final Button mechButton = (Button) findViewById(R.id.mech);
			mechButton.setEnabled(true);
		}
		
		if(!commands.contains("RECON"))
		{
			final Button reconButton = (Button) findViewById(R.id.recon);
			reconButton.setEnabled(false);
		}
		else
		{
			final Button reconButton = (Button) findViewById(R.id.recon);
			reconButton.setEnabled(true);
		}
		
		if(!commands.contains("ARTILLERY"))
		{
			final Button artilleryButton = (Button) findViewById(R.id.artillery);
			artilleryButton.setEnabled(false);
		}
		else
		{
			final Button artilleryButton = (Button) findViewById(R.id.artillery);
			artilleryButton.setEnabled(true);
		}
		
		if(!commands.contains("ANTI-AIR"))
		{
			final Button antiAirButton = (Button) findViewById(R.id.anti_air);
			antiAirButton.setEnabled(false);
		}
		else
		{
			final Button antiAirButton = (Button) findViewById(R.id.anti_air);
			antiAirButton.setEnabled(true);
		}
		
		if(!commands.contains("TANK"))
		{
			final Button tankButton = (Button) findViewById(R.id.tank);
			tankButton.setEnabled(false);
		}
		else
		{
			final Button tankButton = (Button) findViewById(R.id.tank);
			tankButton.setEnabled(true);
		}
		
		if(!commands.contains("CHOPPER"))
		{
			final Button chopperButton = (Button) findViewById(R.id.chopper);
			chopperButton.setEnabled(false);
		}
		else
		{
			final Button chopperButton = (Button) findViewById(R.id.chopper);
			chopperButton.setEnabled(true);
		}
		
		if(!commands.contains("ROCKET"))
		{
			final Button rocketsButton = (Button) findViewById(R.id.rockets);
			rocketsButton.setEnabled(false);
		}
		else
		{
			final Button rocketsButton = (Button) findViewById(R.id.rockets);
			rocketsButton.setEnabled(true);
		}
		
		if(!commands.contains("MEDIUM TANK"))
		{
			final Button mediumTankButton = (Button) findViewById(R.id.med_tank);
			mediumTankButton.setEnabled(false);
		}
		else
		{
			final Button mediumTankButton = (Button) findViewById(R.id.med_tank);
			mediumTankButton.setEnabled(true);
		}
		
		if(!commands.contains("FIGHTER JET"))
		{
			final Button fighterJetButton = (Button) findViewById(R.id.fighter_jet);
			fighterJetButton.setEnabled(false);
		}
		else
		{
			final Button fighterJetButton = (Button) findViewById(R.id.fighter_jet);
			fighterJetButton.setEnabled(true);
		}
		
		if(!commands.contains("HEAVY TANK"))
		{
			final Button heavyTankButton = (Button) findViewById(R.id.heavy_tank);
			heavyTankButton.setEnabled(false);
		}
		else
		{
			final Button heavyTankButton = (Button) findViewById(R.id.heavy_tank);
			heavyTankButton.setEnabled(true);
		}
		
		if(!commands.contains("BOMBER"))
		{
			final Button bomberButton = (Button) findViewById(R.id.bomber);
			bomberButton.setEnabled(false);
		}
		else
		{
			final Button bomberButton = (Button) findViewById(R.id.bomber);
			bomberButton.setEnabled(true);
		}
	}

	@Override
	public void onClick(View v)
	{
		/*
		 * Infantry
		 * Mech
		 * Anti-Air
		 * APC
		 * Artillery
		 * Heavy Tank
		 * Medium Tank
		 * Recon
		 * Rocket
		 * Tank
		 * Bomber
		 * Chopper
		 * Fighter Jet
		 */
		
		GUIGameValues tempGGV = new GUIGameValues();

		switch (v.getId())
		{
		case R.id.infantry:
			tempGGV.setSelectedUnit("Infantry");
			sd.saveGGVData(tempGGV);
			this.finish();
			break;
		case R.id.mech:
			tempGGV.setSelectedUnit("Mech");
			sd.saveGGVData(tempGGV);
			this.finish();
			break;
		case R.id.recon:
			tempGGV.setSelectedUnit("Recon");
			sd.saveGGVData(tempGGV);
			this.finish();
			break;
		case R.id.artillery:
			tempGGV.setSelectedUnit("Artillery");
			sd.saveGGVData(tempGGV);
			this.finish();
			break;
		case R.id.anti_air:
			tempGGV.setSelectedUnit("Anti-Air");
			sd.saveGGVData(tempGGV);
			this.finish();
			break;
		case R.id.tank:
			tempGGV.setSelectedUnit("Tank");
			sd.saveGGVData(tempGGV);
			this.finish();
			break;
		case R.id.chopper:
			tempGGV.setSelectedUnit("Chopper");
			sd.saveGGVData(tempGGV);
			this.finish();
			break;
		case R.id.rockets:
			tempGGV.setSelectedUnit("Rocket");
			sd.saveGGVData(tempGGV);
			this.finish();
			break;
		case R.id.med_tank:
			tempGGV.setSelectedUnit("Medium Tank");
			sd.saveGGVData(tempGGV);
			this.finish();
			break;
		case R.id.fighter_jet:
			tempGGV.setSelectedUnit("Fighter Jet");
			sd.saveGGVData(tempGGV);
			this.finish();
			break;
		case R.id.heavy_tank:
			tempGGV.setSelectedUnit("Heavy Tank");
			sd.saveGGVData(tempGGV);
			this.finish();
			break;
		case R.id.bomber:
			tempGGV.setSelectedUnit("Bomber");
			sd.saveGGVData(tempGGV);
			this.finish();
			break;
		}
	}
}
