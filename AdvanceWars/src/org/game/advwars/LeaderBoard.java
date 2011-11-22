package org.game.advwars;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class LeaderBoard extends Activity
{
	protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leader_board);
        
        ListView list = (ListView) findViewById(R.id.scores);
        
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("PlayerName", "Player Name");
        map.put("Wins", "Wins");
        map.put("Losses", "Losses");
        map.put("XP", "Experience");
        mylist.add(map);
        map = new HashMap<String, String>();
        map.put("PlayerName", "103(x)");
        map.put("Wins", "6:35 AM");
        map.put("Losses", "7:45 AM");
        map.put("XP", "7:40 AM");
        mylist.add(map);
        map = new HashMap<String, String>();
        map.put("PlayerName", "103(x)");
        map.put("Wins", "6:35 AM");
        map.put("Losses", "7:45 AM");
        map.put("XP", "7:40 AM");
        mylist.add(map);
        map = new HashMap<String, String>();
        map.put("PlayerName", "103(x)");
        map.put("Wins", "6:35 AM");
        map.put("Losses", "7:45 AM");
        map.put("XP", "7:40 AM");
        mylist.add(map);
        map = new HashMap<String, String>();
        map.put("PlayerName", "103(x)");
        map.put("Wins", "6:35 AM");
        map.put("Losses", "7:45 AM");
        map.put("XP", "7:40 AM");
        mylist.add(map);
        map = new HashMap<String, String>();
        map.put("PlayerName", "103(x)");
        map.put("Wins", "6:35 AM");
        map.put("Losses", "7:45 AM");
        map.put("XP", "7:40 AM");
        mylist.add(map);
        map = new HashMap<String, String>();
        map.put("PlayerName", "103(x)");
        map.put("Wins", "6:35 AM");
        map.put("Losses", "7:45 AM");
        map.put("XP", "7:40 AM");
        mylist.add(map);
        map = new HashMap<String, String>();
        map.put("PlayerName", "103(x)");
        map.put("Wins", "6:35 AM");
        map.put("Losses", "7:45 AM");
        map.put("XP", "7:40 AM");
        mylist.add(map);

        ListAdapter mSchedule = new SimpleAdapter(this, mylist, R.layout.leader_board,
                    new String[] {"PlayerName", "Wins", "Losses", "XP"}, new int[] {R.id.player_name, R.id.wins, R.id.losses, R.id.xp});
        list.setAdapter(mSchedule);
    }
}