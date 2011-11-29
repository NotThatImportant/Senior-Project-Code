package org.game.advwars;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class InGameMenu extends Activity implements OnClickListener
{
	protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_game_menu);
        
        // Set up click listeners for all buttons
        View move = findViewById(R.id.move_unit);
        move.setOnClickListener(this);
        View attack = findViewById(R.id.attack_unit);
        attack.setOnClickListener(this);
        View capture = findViewById(R.id.capture_building);
        capture.setOnClickListener(this);
        View createUnit = findViewById(R.id.create_unit);
        createUnit.setOnClickListener(this);
        View unitInfo = findViewById(R.id.unit_info);
        unitInfo.setOnClickListener(this);
    }

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
    	{
    	case R.id.move_unit:
    		this.finish();
    		break;
    	case R.id.attack_unit:
    		this.finish();
    		break;
    	case R.id.capture_building:
    		this.finish();
    		break;
    	case R.id.create_unit:
    		this.finish();
    		break;
    	case R.id.unit_info:
    		this.finish();
    		break;
    	}
	}
}
