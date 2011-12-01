package org.game.advwars;

import java.util.ArrayList;
import dataconnectors.SaveGUIData;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

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

		//commands = ggv.getAvailableCommands();

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
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.infantry:
			this.finish();
			break;
		case R.id.mech:
			this.finish();
			break;
		case R.id.recon:
			this.finish();
			break;
		case R.id.artillery:
			this.finish();
			break;
		case R.id.anti_air:
			this.finish();
			break;
		case R.id.tank:
			this.finish();
			break;
		case R.id.chopper:
			this.finish();
			break;
		case R.id.rockets:
			this.finish();
			break;
		case R.id.med_tank:
			this.finish();
			break;
		case R.id.fighter_jet:
			this.finish();
			break;
		case R.id.heavy_tank:
			this.finish();
			break;
		case R.id.bomber:
			this.finish();
			break;
		}
	}
}
