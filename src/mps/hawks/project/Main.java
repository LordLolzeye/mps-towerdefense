package mps.hawks.project;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import mps.hawks.project.Enemies.EnemyEvents;
import mps.hawks.project.Enemies.RegisterEnemyEntities;

public class Main extends JavaPlugin {

	@Override
    public void onEnable() {
		// Register Entities
		RegisterEnemyEntities.registerEntities();
		
		// Register Events
	    PluginManager pm = getServer().getPluginManager();
	    pm.registerEvents(new EnemyEvents(this), this);
    }

    @Override
    public void onDisable() {
    	RegisterEnemyEntities.unregisterEntities();
    }
	
}