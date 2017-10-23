package mps.hawks.project.Tower.TowerTypes;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import mps.hawks.project.Main;
import mps.hawks.project.Projectile.Projectile;
import mps.hawks.project.Projectile.ProjectileTypes.Arrow;
import mps.hawks.project.Tower.Tower;
import net.md_5.bungee.api.ChatColor;

public class ArrowTower extends Tower {
	private HashMap<Location, Material> towerBlockPositions = new HashMap<Location , Material>();
	
	public int currentUpgradeLevel;
	
	public ArrowTower(Player owner, Location towerSpawnLocation) {
		super(owner, towerSpawnLocation);
		
		towerConstructionCost = 10;
		towerUpgradeCost = 20;
		currentUpgradeLevel = 1;
		currentDamage = 10;
		blockRadius = 7; // can attack at max 7 blocks (xyz)
		launchable = new Arrow();
		
		if(Main.self.playerMoney.containsKey(towerOwner)) {
			if(Main.self.playerMoney.get(towerOwner) >= towerConstructionCost) {
				this.constructTower(towerOwner, towerSpawnLocation);
			} else {
				towerOwner.sendMessage(ChatColor.AQUA + "[MPS]" + ChatColor.DARK_RED + "You do not have enough gold to construct this tower!");
				towerOwner.sendMessage(ChatColor.AQUA + "[MPS]" + ChatColor.GRAY + "Construction cost: " + ChatColor.GOLD + towerConstructionCost);
				towerOwner.sendMessage(ChatColor.AQUA + "[MPS]" + ChatColor.GRAY + "You have: " + ChatColor.GOLD + Main.self.playerMoney.get(owner));
			}
		}
	}

	@Override
	public boolean canUpgradeTower() {
		if(currentUpgradeLevel + 1 > 3) {
			towerOwner.sendMessage(ChatColor.AQUA + "[MPS]" + ChatColor.DARK_RED + "This tower is already max level!");
			
			return false;
		}
		
		if(!Main.self.playerMoney.containsKey(towerOwner)) {
			towerOwner.sendMessage(ChatColor.AQUA + "[MPS]" + ChatColor.DARK_RED + "Unknown error has occured, please contact an administrator!");
			
			return false;
		}
		
		if((Main.self.playerMoney.get(towerOwner)) < towerUpgradeCost) {
			towerOwner.sendMessage(ChatColor.AQUA + "[MPS]" + ChatColor.DARK_RED + "You do not have enough gold to upgrade this tower!");
			
			return false;
		}
		
		return true;
	}

	@Override
	public void upgradeTower() {
		int playerMoney = Main.self.playerMoney.get(towerOwner);
		Main.self.playerMoney.put(towerOwner, playerMoney - towerUpgradeCost);
		
		towerUpgradeCost *= 2;
		currentUpgradeLevel++;
		
		towerOwner.sendMessage(ChatColor.AQUA + "[MPS]" + ChatColor.GREEN + "Tower has been upgraded!");
		towerOwner.sendMessage(ChatColor.AQUA + "[MPS]" + ChatColor.GRAY + "Current Tower Level: " + currentUpgradeLevel);
		towerOwner.sendMessage(ChatColor.AQUA + "[MPS]" + ChatColor.GRAY + "Required for next level: " + ChatColor.GOLD + towerUpgradeCost + " gold " + ChatColor.GRAY + " !");
	}

	@Override
	public void constructTower(Player p, Location loc) {		
		towerBlockPositions.put(new Location(Bukkit.getWorld("world"), loc.add(0, 0, 0).getX(), loc.add(0, 1, 0).getY(), loc.add(0, 0, 0).getZ()), Material.MOSSY_COBBLESTONE);
		towerBlockPositions.put(new Location(Bukkit.getWorld("world"), loc.add(0, 0, 0).getX(), loc.add(0, 1, 0).getY(), loc.add(0, 0, 0).getZ()), Material.MOSSY_COBBLESTONE);
		towerBlockPositions.put(new Location(Bukkit.getWorld("world"), loc.add(0, 0, 0).getX(), loc.add(0, 1, 0).getY(), loc.add(0, 0, 0).getZ()), Material.ANVIL);
		
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
			towerProjectiles.add(projectileType.shootProjectile(towerLocation.clone().add(0, 1, 0), toLocation));
			canShootProjectile = false;
			
			new BukkitRunnable() {
				
				@Override
				public void run() {
					canShootProjectile = true;	
				}
				
			}.runTaskLaterAsynchronously(Main.self, 40);
		}
	}

}
