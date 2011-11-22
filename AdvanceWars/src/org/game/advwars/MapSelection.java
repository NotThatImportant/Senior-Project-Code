package org.game.advwars;

import java.io.File;

import dataconnectors.SaveGUIData;

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
	private String[] maps = null;
	private SaveGUIData sd = new SaveGUIData();
	private GUIGameValues mapSelectionGGV = new GUIGameValues();
	
	protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_selection);
        
        // Get all maps from default directory
        maps = listMaps();
        
        /*Spinner spinner = (Spinner) findViewById(R.id.map_selection_list);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.maps, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter)*/
        
        Spinner spinner = (Spinner) findViewById(R.id.map_selection_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, maps);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);       
        
        spinner.setOnItemSelectedListener(new MapSelection());
        
        Button selectMap = (Button) findViewById(R.id.select_map);
        selectMap.setOnClickListener(this);
    }

	@Override
	public void onClick(View v)
	{
		Intent i = new Intent(this, GameBoard.class);
		startActivity(i);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
	{
		mapSelectionGGV = sd.loadGGVData();
		mapName = parent.getItemAtPosition(pos).toString();
		mapSelectionGGV.setMap(mapName);
		sd.saveGGVData(mapSelectionGGV);
	    //Toast.makeText(parent.getContext(), "You selected " + mapName, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0)
	{
		// TODO Auto-generated method stub
	}
	
	private String[] listMaps()
	{
		File directory = new File("/data/data/org.game.advwars/maps/");
		String[] dirMaps = directory.list();
		
		return dirMaps;
	}
}