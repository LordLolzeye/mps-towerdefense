package mps.hawks.project.Enemies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import mps.hawks.project.Main;
import mps.hawks.project.Enemies.EnemyTypes.PigZombieEnemy;

public class EnemyEvents implements Listener {
	public Main plugin;

	public static HashMap<UUID, Entity> aliveEntities = new HashMap<UUID, Entity>();
	public static Location spawnLocation = new Location(Bukkit.getWorld("world"), -852.5, 4.0, -1207.5);
	public static ArrayList<Location> enemyRoute = new ArrayList<Location>();

	public String currentEnemy = "pigzombie";
	public int difficultyLevel = 1;
	public int mobNumber = 1;

	public EnemyEvents(Main plugin) {
		this.plugin = plugin;

		enemyRoute.add(new Location(Bukkit.getWorld("world"), -852, 4, -1207));
		enemyRoute.add(new Location(Bukkit.getWorld("world"), -845, 4, -1207));
		enemyRoute.add(new Location(Bukkit.getWorld("world"), -845, 4, -1201));
		enemyRoute.add(new Location(Bukkit.getWorld("world"), -852, 4, -1201));
		enemyRoute.add(new Location(Bukkit.getWorld("world"), -859, 4, -1201));
		enemyRoute.add(new Location(Bukkit.getWorld("world"), -859, 4, -1193));
		enemyRoute.add(new Location(Bukkit.getWorld("world"), -854, 4, -1193));
		enemyRoute.add(new Location(Bukkit.getWorld("world"), -854, 4, -1198));
		enemyRoute.add(new Location(Bukkit.getWorld("world"), -851, 4, -1198));
		enemyRoute.add(new Location(Bukkit.getWorld("world"), -851, 4, -1193));
		enemyRoute.add(new Location(Bukkit.getWorld("world"), -840, 4, -1193));
		
		new BukkitRunnable() {

			@Override
			public void run() {
				switch(currentEnemy) {
				case "pigzombie":
						for(int i = 0; i < mobNumber; i++) {
							new BukkitRunnable() {
								
								@Override
								public void run() {
									System.out.println("spawning pig zombie entity");
									new PigZombieEnemy().spawn(spawnLocation, difficultyLevel);
								}
								
							}.runTaskLater(plugin, 40 * i);
						}
					break;
				default:
					System.out.println("ERROR: MOB TYPE NOT FOUND");
					break;
				}
			}

		}.runTaskTimerAsynchronously(plugin, 600, 1800);
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
