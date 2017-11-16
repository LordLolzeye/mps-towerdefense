package mps.hawks.project.Enemies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import mps.hawks.project.Main;
import mps.hawks.project.Enemies.EnemyTypes.CreeperEnemy;
import mps.hawks.project.Enemies.EnemyTypes.PigZombieEnemy;
import mps.hawks.project.Enemies.EnemyTypes.SkeletonEnemy;
import net.md_5.bungee.api.ChatColor;

public class EnemyEvents implements Listener {
	public Main plugin;

	public static HashMap<UUID, Entity> aliveEntities = new HashMap<UUID, Entity>();
	public static HashMap<UUID, Integer> aliveEntitiesHealth = new HashMap<UUID, Integer>();
	public static Location spawnLocation = new Location(Bukkit.getWorld("world"), -852.5, 4.0, -1207.5);
	public static Location finishLocation = new Location(Bukkit.getWorld("world"), -840, 4, -1193);
	public static ArrayList<Location> enemyRoute = new ArrayList<Location>();

	public String currentEnemy = "pigzombie";
	public int waveNumber = 1;
	public int mobNumber = 5;

	public EnemyEvents(Main plugin) {
		this.plugin = plugin;

		enemyRoute.add(new Location(Bukkit.getWorld("world"), -852, 4, -1207));
		enemyRoute.add(new Location(Bukkit.getWorld("world"), -845, 4, -1207));
		enemyRoute.add(new Location(Bukkit.getWorld("world"), -838, 4, -1207));
		enemyRoute.add(new Location(Bukkit.getWorld("world"), -837, 4, -1210));
		enemyRoute.add(new Location(Bukkit.getWorld("world"), -833, 4, -1210));
		enemyRoute.add(new Location(Bukkit.getWorld("world"), -833, 4, -1205));
		enemyRoute.add(new Location(Bukkit.getWorld("world"), -833, 4, -1201));
		enemyRoute.add(new Location(Bukkit.getWorld("world"), -837, 4, -1201));
		enemyRoute.add(new Location(Bukkit.getWorld("world"), -841, 4, -1201));
		enemyRoute.add(new Location(Bukkit.getWorld("world"), -845, 4, -1201));
		enemyRoute.add(new Location(Bukkit.getWorld("world"), -852, 4, -1201));
		enemyRoute.add(new Location(Bukkit.getWorld("world"), -854, 4, -1201)); // tp spot
		enemyRoute.add(new Location(Bukkit.getWorld("world"), -840, 4, -1193)); // last cp
		
		new BukkitRunnable() {

			@Override
			public void run() {
				switch(currentEnemy) {
				case "pigzombie":
						for(int i = 0; i < mobNumber; i++) {
							new BukkitRunnable() {
								
								@Override
								public void run() {
									new PigZombieEnemy().spawn(spawnLocation, waveNumber);
								}
								
							}.runTaskLater(plugin, 40 * i);
						}
						
						currentEnemy = "creeper";
						mobNumber += 2;
						waveNumber++;
					break;
				case "creeper":
					for(int i = 0; i < mobNumber; i++) {
						new BukkitRunnable() {
							
							@Override
							public void run() {
								new CreeperEnemy().spawn(spawnLocation, waveNumber);
							}
							
						}.runTaskLater(plugin, 40 * i);
					}
					
					currentEnemy = "skeleton";
					mobNumber += 2;
					waveNumber++;
				break;
				case "skeleton":
					for(int i = 0; i < 3; i++) {
						new BukkitRunnable() {
							
							@Override
							public void run() {
								new SkeletonEnemy().spawn(spawnLocation, waveNumber);
							}
							
						}.runTaskLater(plugin, 40 * i);
					}
					
					currentEnemy = "pigzombie";
					waveNumber++;
				break;
				default:
					System.out.println("ERROR: MOB TYPE NOT FOUND");
					break;
				}
			}

		}.runTaskTimerAsynchronously(plugin, 100, 1800);
	}
	
	@EventHandler
	public void onEnemySpawn(EntitySpawnEvent e) {
		if(e.getEntityType() == EntityType.PLAYER) {
			return;
		}
		
		if(e.getEntityType() == EntityType.SKELETON) {
			((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2147483647, 2));
		}
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(e.getEntity().isDead()) {
					this.cancel();
				}
				
				if(e.getEntity().getLocation().distance(new Location(Bukkit.getWorld("world"), -854, 4, -1201)) < 1.4) {
					e.getEntity().teleport(new Location(Bukkit.getWorld("world"), -851, 4, -1192.5));
				}
				
				if(e.getEntity().getLocation().distance(EnemyEvents.finishLocation) < 2) {
					Main.remainingLives--;
					e.getEntity().remove();
					this.cancel();
					
					if(Main.remainingLives > 0) {
						for(Player p : Bukkit.getOnlinePlayers()) {
							p.sendMessage(ChatColor.GOLD + " >> " + ChatColor.GREEN + " Remaining Lives " + ChatColor.DARK_RED + Main.remainingLives);
						}
					} else {
						for(Player p : Bukkit.getOnlinePlayers()) {
							p.sendMessage(ChatColor.GOLD + " >> " + ChatColor.DARK_RED + " GAME OVER! ");
							
							new BukkitRunnable() {
								
								@Override
								public void run() {
									
									for(Player p : Bukkit.getOnlinePlayers()) {
										p.kickPlayer("GAME OVER");
									}
									
								}
								
							}.runTaskLater(Main.self, 20 * 3);
						}
					}
				}
			}
			
		}.runTaskTimerAsynchronously(plugin, 0, 5);

		aliveEntities.put(e.getEntity().getUniqueId(), e.getEntity());
		aliveEntitiesHealth.put(e.getEntity().getUniqueId(), 20);
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		if(e.getEntityType() == EntityType.PLAYER) {
			return;
		}

		if(aliveEntities.containsKey(e.getEntity().getUniqueId())) {
			aliveEntities.remove(e.getEntity().getUniqueId());
			aliveEntitiesHealth.remove(e.getEntity().getUniqueId());
		}
	}
}
