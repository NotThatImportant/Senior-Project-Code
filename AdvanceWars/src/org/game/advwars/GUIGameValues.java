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
	private int selectedCommand;
	private Controller c;
	
	public GUIGameValues()
	{
		soundOn = true;
		playerName = "";
		difficulty = "medium";
		mapName = "";
		faction = "";
		inGameMainMenu = new boolean[3];
		selectedCommand = -1;
		c = null;
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
		this.selectedCommand = command;
	}
	
	public int getSelectedCommand ()
	{
		return this.selectedCommand;
	}
	
	public void setController (Controller controller)
	{
		this.c = controller;
	}
	
	public Controller getController ()
	{
		return this.c;
	}
}
