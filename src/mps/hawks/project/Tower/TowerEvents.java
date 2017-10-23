package mps.hawks.project.Tower;

import java.util.UUID;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import mps.hawks.project.Main;
import mps.hawks.project.Enemies.EnemyEvents;

public class TowerEvents implements Listener {
	public Main plugin;
	
	public TowerEvents(Main plugin) {
		this.plugin = plugin;
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				for(UUID uu : EnemyEvents.aliveEntities.keySet()) {
					Entity e = EnemyEvents.aliveEntities.get(uu);
					
					for(Player p : Main.self.playerTowers.keySet()) {
						for(Tower t : Main.self.playerTowers.get(p)) {
							if(e.getLocation().distance(t.towerLocation) <= t.blockRadius) {
								t.shootProjectile(e.getLocation(), t.launchable);
							}
						}
					}
				}
			}
			
		}.runTaskTimer(Main.self, 20, 5);
	}
	
}
