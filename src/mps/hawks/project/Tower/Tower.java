package mps.hawks.project.Tower;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import mps.hawks.project.Projectile.Projectile;

public abstract class Tower {
	public int blockRadius = 5;
	public int towerConstructionCost = 10;
	public int towerUpgradeCost = 10;
	public int currentUpgradeLevel = 1;
	public int currentDamage = 1;
	public Location towerLocation = null;
	public Player towerOwner = null;
	public Projectile launchable = null;
	public boolean canShootProjectile = true;
	
	public ArrayList<Location> towerBlocks = new ArrayList<Location>();
	public ArrayList<Entity> towerProjectiles = new ArrayList<Entity>();
	public HashMap<Integer, Integer> possibleUpgrades = new HashMap<Integer, Integer>();
	
	public Tower(Player towerOwner, Location towerSpawnLocation) {
		this.towerLocation = towerSpawnLocation;
		this.towerOwner = towerOwner;
	}
	
	public abstract void upgradeTower(int upgradeId);
	
	public abstract void constructTower(Player p, Location loc);
	public abstract void destructTower();
	
	public abstract void shootProjectile(Location toLocation, Projectile projectileType);
}
