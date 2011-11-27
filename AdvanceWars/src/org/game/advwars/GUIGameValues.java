package org.game.advwars;

import java.io.Serializable;

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
	private boolean[] inGameMenu;
	
	public GUIGameValues()
	{
		soundOn = true;
		playerName = "";
		difficulty = "medium";
		mapName = "";
		faction = "";
		inGameMenu = new boolean[3];
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
	
	public void setInGameMenuAction (boolean endTurn, boolean saveGame, boolean quitGame)
	{
		this.inGameMenu[0] = endTurn;
		this.inGameMenu[1] = saveGame;
		this.inGameMenu[2] = quitGame;
	}
	
	public boolean getInGameMenuEndTurn ()
	{
		return this.inGameMenu[0];
	}
	
	public boolean getInGameMenuSaveGame ()
	{
		return this.inGameMenu[1];
	}
	
	public boolean getInGameMenuQuitGame ()
	{
		return this.inGameMenu[2];
	}
}
