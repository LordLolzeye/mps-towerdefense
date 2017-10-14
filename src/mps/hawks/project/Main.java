package mps.hawks.project;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import mps.hawks.project.Enemies.EnemyEvents;
import mps.hawks.project.Enemies.RegisterEnemyEntities;
import mps.hawks.project.PlayerEvents.PlayerEvents;
import mps.hawks.project.Tower.Tower;
import mps.hawks.project.Tower.TowerEvents;

public class Main extends JavaPlugin {

	public HashMap<Player, Integer> playerMoney = new HashMap<Player, Integer>();
	public HashMap<Player, ArrayList<Tower>> playerTowers = new HashMap<Player, ArrayList<Tower>>();
	public static Main self;
	
	@Override
    public void onEnable() {
		Main.self = this;
		
		// Register Entities
		RegisterEnemyEntities.registerEntities();
		
		// Register Events
	    PluginManager pm = getServer().getPluginManager();
	    pm.registerEvents(new EnemyEvents(this), this);
	    pm.registerEvents(new PlayerEvents(this), this);
	    pm.registerEvents(new TowerEvents(this), this);
    }

    @Override
    public void onDisable() {
    	RegisterEnemyEntities.unregisterEntities();
    }
	
}