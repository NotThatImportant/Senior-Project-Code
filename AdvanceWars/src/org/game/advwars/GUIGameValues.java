package org.game.advwars;

import java.io.Serializable;
import java.util.ArrayList;

import controller.Controller;

/**
 * 
 * Keeps track of all game values that need to be accessed or stored by the GUI
 * 
 * @author Sinisa Malbasa
 *
 */
public class GUIGameValues implements Serializable
{
	private boolean soundOn;
	private String playerName;
	private String difficulty;
	private String mapName;
	private String faction;
	private boolean[] inGameMainMenu;
	private ArrayList<String> commands;
	private ArrayList<String> unitInfo;
	private int selectedCommand;
	private String selectedUnit;
	private Controller c;
	private char[][] movement;
	private boolean aiOn;
	private int money;
	
	public GUIGameValues()
	{
		soundOn = true;
		playerName = null;
		difficulty = "medium";
		mapName = null;
		faction = null;
		commands = null;
		unitInfo = null;
		inGameMainMenu = new boolean[3];
		selectedCommand = -1;
		selectedUnit = null;
		c = null;
		movement = null;
		aiOn = false;
		money = -1;
	}
	
	public void setPlayerName (String playerName)
	{
		this.playerName = playerName;
	}
	
	public String getPlayerName ()
	{
		// Check return values
		if (playerName.equals(""))
			return "VALUE_NOT_SET";
		else
			return this.playerName;
	}
	
	public void setSound (boolean soundOn)
	{
		this.soundOn = soundOn;
	}
	
	public boolean getSound ()
	{
		return this.soundOn;
	}
	
	public void setDifficulty (String difficulty)
	{
		this.difficulty = difficulty;
	}
	
	public String getDifficulty ()
	{
		return this.difficulty;
	}
	
	public void setMap (String map)
	{
		this.mapName = map;
	}
	
	public String getMap ()
	{
		return this.mapName;
	}
	
	public void setFaction (String faction)
	{
		this.faction = faction;
	}
	
	public String getFaction ()
	{
		return this.faction;
	}
	
	public String getAIFaction ()
	{
		if(faction.equalsIgnoreCase("blue"))
			return "red";
		if(faction.equalsIgnoreCase("red"))
			return this.faction;
		return "";
	}
	
	public void setInGameMainMenuAction (boolean endTurn, boolean saveGame, boolean quitGame)
	{
		this.inGameMainMenu[0] = endTurn;
		this.inGameMainMenu[1] = saveGame;
		this.inGameMainMenu[2] = quitGame;
	}
	
	public boolean getInGameMenuEndTurn ()
	{
		return this.inGameMainMenu[0];
	}
	
	public boolean getInGameMenuSaveGame ()
	{
		return this.inGameMainMenu[1];
	}
	
	public boolean getInGameMenuQuitGame ()
	{
		return this.inGameMainMenu[2];
	}
	
	public void setAvailableCommands (ArrayList<String> commands)
	{
		this.commands = commands;
	}
	
	public ArrayList<String> getAvailableCommands ()
	{
		return this.commands;
	}
	
	public void setSelectedCommand (int command)
	{
		this.selectedUnit = null;
		this.selectedCommand = command;
	}
	
	public int getSelectedCommand ()
	{
		return this.selectedCommand;
	}
	
	public void setSelectedUnit (String unit)
	{
		this.selectedCommand = -1;
		this.selectedUnit = unit;
	}
	
	public String getSelectedUnit ()
	{
		return this.selectedUnit;
	}
	
	/*
	 * This method should never be used because it defeats the purpose of
	 * modular design. All variables should be passed individually, not
	 * as a controller.
	 */
	public void setController (Controller controller)
	{
		this.c = controller;
	}
	
	/*
	 * This method should never be used because it defeats the purpose of
	 * modular design. All variables should be retrieved individually, not
	 * as a controller.
	 */
	public Controller getController ()
	{
		return this.c;
	}
	
	public void setUnitInfo (ArrayList<String> unitInfo)
	{
		this.unitInfo = unitInfo;
	}
	
	public ArrayList<String> getUnitInfo ()
	{
		return this.unitInfo;
	}
	
	public void setMovement(char[][] moves)
	{
		movement = moves;
	}
	
	public char[][] getMovement()
	{
		return movement;
	}
	
	public void setAIOn (boolean aiOn)
	{
		this.aiOn = aiOn;
	}
	
	public boolean isAIOn ()
	{
		return this.aiOn;
	}
	
	public void setMoney (int money)
	{
		this.money = money;
	}
	
	public int getMoney ()
	{
		return this.money;
	}
}