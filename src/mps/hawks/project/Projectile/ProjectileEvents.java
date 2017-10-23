package mps.hawks.project.Projectile;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import mps.hawks.project.Main;
import mps.hawks.project.Enemies.EnemyEvents;
import mps.hawks.project.Tower.Tower;

public class ProjectileEvents implements Listener {

	public Main plugin;
	
	public ProjectileEvents(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void projectileHitEvent(EntityDamageByEntityEvent e) {
		e.setDamage(0);
		
		for(Player p : plugin.playerTowers.keySet()) {
			for(Tower t : plugin.playerTowers.get(p)) {
				boolean found = false;
				for(Entity proj : t.towerProjectiles) {
					if(proj.getUniqueId().equals(e.getDamager().getUniqueId())) {
						int currentEntityHealth = EnemyEvents.aliveEntitiesHealth.get(e.getEntity().getUniqueId());
						
						System.out.println(currentEntityHealth);
						if(currentEntityHealth - t.currentDamage > 0) {
							EnemyEvents.aliveEntitiesHealth.put(e.getEntity().getUniqueId(), currentEntityHealth - t.currentDamage);
						} else {
							e.getEntity().remove();
							
							EnemyEvents.aliveEntities.remove(e.getEntity().getUniqueId());
							EnemyEvents.aliveEntitiesHealth.remove(e.getEntity().getUniqueId());
							plugin.playerMoney.put(t.towerOwner, plugin.playerMoney.get(t.towerOwner) + 1);
						}
						
						found = true;
					}
				}
				
				if(found) {
					t.towerProjectiles.remove(e.getDamager());
					e.getDamager().remove();
					
					break;
				}
			}
		}
		
		System.out.println(e.getEntityType());
		System.out.println(e.getDamager());
	}

}
