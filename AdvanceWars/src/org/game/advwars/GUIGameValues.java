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
	
	public GUIGameValues()
	{
		soundOn = true;
		playerName = "";
	}
	
	public void setPlayerName (String playerName)
	{
		this.playerName = playerName;
	}
	
	public String getPlayerName ()
	{
		// Check return values
		if (playerName.equals(""))
		{
			return "VALUE_NOT_SET";
		}
		else if (playerName.equals("sa"))
		{
			return "System Administrator";
		}
		else
		{
			return this.playerName;
		}
	}
	
	public void setSound (boolean soundOn)
	{
		this.soundOn = soundOn;
	}
	
	public boolean getSound ()
	{
		return this.soundOn;
	}
}
