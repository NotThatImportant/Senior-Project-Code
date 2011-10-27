package org.game.advwars;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class FactionSelection extends Activity implements OnItemSelectedListener, OnClickListener
{
	private String faction = "";
	
	protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faction_selection);
        
        Spinner spinner = (Spinner) findViewById(R.id.faction_selection_list);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.factions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        
        spinner.setOnItemSelectedListener(new FactionSelection());
        
        Button selectPlayerName = (Button) findViewById(R.id.select_faction);
        selectPlayerName.setOnClickListener(this);
    }

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
	{
		faction = parent.getItemAtPosition(pos).toString();
	    Toast.makeText(parent.getContext(), "You selected " + faction, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onClick(View v)
	{
		Intent i = new Intent(this, MapSelection.class);
		startActivity(i);
	}
}