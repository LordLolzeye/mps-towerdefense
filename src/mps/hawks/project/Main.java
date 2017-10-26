package mps.hawks.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import mps.hawks.project.Enemies.EnemyEvents;
import mps.hawks.project.Enemies.RegisterEnemyEntities;
import mps.hawks.project.PlayerEvents.PlayerEvents;
import mps.hawks.project.Projectile.ProjectileEvents;
import mps.hawks.project.Tower.Tower;
import mps.hawks.project.Tower.TowerEvents;

public class Main extends JavaPlugin {

	public HashMap<Player, Integer> playerMoney = new HashMap<Player, Integer>();
	public HashMap<Player, ArrayList<Tower>> playerTowers = new HashMap<Player, ArrayList<Tower>>();
	public static Main self;
	public static int remainingLives = 10;
	
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
	    pm.registerEvents(new ProjectileEvents(this), this);
    }

    @Override
    public void onDisable() {
    	RegisterEnemyEntities.unregisterEntities();
    	
    	// Remove spawned entities
    	for(UUID key : EnemyEvents.aliveEntities.keySet()) {
    		if(!EnemyEvents.aliveEntities.get(key).isDead()) {
    			EnemyEvents.aliveEntities.get(key).remove();
    			
    			EnemyEvents.aliveEntities.remove(key);
    		}
    	}
    	
    	// Remove created turrets
    	for(Player p : playerTowers.keySet()) {
    		ArrayList<Tower> currentPlayerTowers = playerTowers.get(p);
    		
    		for(Tower t : currentPlayerTowers) {
    			for(Location l : t.towerBlocks) {
    				l.getBlock().setType(Material.AIR);
    			}
    		}
    	}

    	for(Player p : Bukkit.getOnlinePlayers()) {
    		p.kickPlayer("[MPS] Server is shutting down!");
    	}
    	
    }
	
}