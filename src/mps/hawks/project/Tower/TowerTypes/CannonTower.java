package mps.hawks.project.Tower.TowerTypes;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import mps.hawks.project.Main;
import mps.hawks.project.Projectile.Projectile;
import mps.hawks.project.Projectile.ProjectileTypes.Fireball;
import mps.hawks.project.Tower.Tower;
import net.md_5.bungee.api.ChatColor;

public class CannonTower extends Tower {
	private HashMap<Location, Material> towerBlockPositions = new HashMap<Location , Material>();
	
	public int currentUpgradeLevel;
	public int timeMultiplier = 5;
	
	public CannonTower(Player owner, Location towerSpawnLocation) {
		super(owner, towerSpawnLocation);
		
		towerConstructionCost = 15;
		currentUpgradeLevel = 1;
		currentDamage = 20;
		blockRadius = 3; 
		launchable = new Fireball();
		
		if(Main.self.playerMoney.containsKey(towerOwner)) {
			if(Main.self.playerMoney.get(towerOwner) >= towerConstructionCost) {
				this.constructTower(towerOwner, towerSpawnLocation);
			} else {
				towerOwner.sendMessage(ChatColor.AQUA + "[MPS]" + ChatColor.DARK_RED + "You do not have enough gold to construct this tower!");
				towerOwner.sendMessage(ChatColor.AQUA + "[MPS]" + ChatColor.GRAY + "Construction cost: " + ChatColor.GOLD + towerConstructionCost);
				towerOwner.sendMessage(ChatColor.AQUA + "[MPS]" + ChatColor.GRAY + "You have: " + ChatColor.GOLD + Main.self.playerMoney.get(owner));
			}
		}
		
		possibleUpgrades.put(1, 30); // speed
		possibleUpgrades.put(2, 20); // damage
		possibleUpgrades.put(3, 50); // range
	}

	@Override
	public void upgradeTower(int upgradeId) {
		int playerMoney = Main.self.playerMoney.get(towerOwner);
		int towerUpgradeCost = this.possibleUpgrades.get(upgradeId);
		if(playerMoney - towerUpgradeCost < 0) {
			towerOwner.sendMessage(ChatColor.AQUA + "[MPS]" + ChatColor.DARK_RED + "You do not have enough gold to upgrade this tower!");
			return;
		}
		
		this.possibleUpgrades.put(upgradeId, towerUpgradeCost*2);
		Main.self.playerMoney.put(towerOwner, playerMoney - towerUpgradeCost);
		
		switch(upgradeId) {
		case 1:
			timeMultiplier -= 1;
			break;
		case 2:
			currentDamage += 2;
			break;
		case 3:
			blockRadius++;
			break;
		}
		
		towerOwner.sendMessage(ChatColor.AQUA + "[MPS]" + ChatColor.GREEN + "Tower has been upgraded!");
	}

	@Override
	public void constructTower(Player p, Location loc) {	
		towerBlockPositions.put(new Location(Bukkit.getWorld("world"), loc.add(0, 0, 0).getX(), loc.add(0, 1, 0).getY(), loc.add(0, 0, 0).getZ()), Material.IRON_BLOCK);
		towerBlockPositions.put(new Location(Bukkit.getWorld("world"), loc.add(0, 0, 0).getX(), loc.add(0, 1, 0).getY(), loc.add(0, 0, 0).getZ()), Material.IRON_BLOCK);
		towerBlockPositions.put(new Location(Bukkit.getWorld("world"), loc.add(0, 0, 0).getX(), loc.add(0, 1, 0).getY(), loc.add(0, 0, 0).getZ()), Material.SAPLING);
		
		for(Location tbp : towerBlockPositions.keySet()) {
			towerBlocks.add(tbp);
		}
		
		for(Location key : towerBlockPositions.keySet()) {
			Material toPlace = towerBlockPositions.get(key);
			
			Bukkit.getWorld("world").getBlockAt(key).setType(toPlace);
		}
		
		int playerMoney = Main.self.playerMoney.get(towerOwner);
		Main.self.playerMoney.put(towerOwner, playerMoney - towerConstructionCost);
		Main.self.playerTowers.get(towerOwner).add(this);
	}

	@Override
	public void destructTower() {
		for(Location key : towerBlockPositions.keySet()) {
			Bukkit.getWorld("world").getBlockAt(key).setType(Material.AIR);
		}
		
		Main.self.playerTowers.get(towerOwner).remove(this);
		
		towerOwner.sendMessage(ChatColor.AQUA + "[MPS]" + ChatColor.DARK_RED + "Your tower has been destroyed!");
	}

	@Override
	public void shootProjectile(Location toLocation, Projectile projectileType) {
		if(canShootProjectile) {
			towerProjectiles.add(projectileType.shootProjectile(towerLocation.clone().add(0, 2, 0), toLocation));
			canShootProjectile = false;
			
			new BukkitRunnable() {
				
				@Override
				public void run() {
					canShootProjectile = true;	
				}
				
			}.runTaskLaterAsynchronously(Main.self, 40 * timeMultiplier);
		}
	}

}
