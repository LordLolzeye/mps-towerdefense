package mps.hawks.project.Enemies;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

import mps.hawks.project.Main;

public class EnemyEvents implements Listener {
	public Main plugin;
	
	public static HashMap<UUID, Entity> aliveEntities = new HashMap<UUID, Entity>();
	
	public EnemyEvents(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onEnemySpawn(EntitySpawnEvent e) {
		if(e.getEntityType() == EntityType.PLAYER) {
			return;
		}
		
		aliveEntities.put(e.getEntity().getUniqueId(), e.getEntity());
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		if(e.getEntityType() == EntityType.PLAYER) {
			return;
		}
		
		if(aliveEntities.containsKey(e.getEntity().getUniqueId())) {
			aliveEntities.remove(e.getEntity().getUniqueId());
		}
	}
	
}
