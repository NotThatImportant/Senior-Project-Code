package org.game.advwars;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class MapSelection extends Activity implements OnItemSelectedListener, OnClickListener
{
	private String mapName = "";
	
	protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_selection);
        
        Spinner spinner = (Spinner) findViewById(R.id.map_selection_list);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.maps, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        
        spinner.setOnItemSelectedListener(new FactionSelection());
        
        Button selectPlayerName = (Button) findViewById(R.id.select_map);
        selectPlayerName.setOnClickListener(this);
    }

	@Override
	public void onClick(View v)
	{
		Intent i = new Intent(this, UnderConstruction.class);
		startActivity(i);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
	{
		mapName = parent.getItemAtPosition(pos).toString();
	    Toast.makeText(parent.getContext(), "You selected " + mapName, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0)
	{
		// TODO Auto-generated method stub
	}
}