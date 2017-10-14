package mps.hawks.project.Tower;

import java.util.ArrayList;

import org.bukkit.Location;

import mps.hawks.project.Projectile.Projectile;
import mps.hawks.project.Tower.Upgrade.UpgradeCost;

public abstract class Tower {
	public int blockRadius = 5;
	public int towerConstructionCost = 10;
	
	public ArrayList<UpgradeCost> upgradeCosts = new ArrayList<UpgradeCost>();
	
	public abstract boolean shootProjectile(Location toLocation, Projectile projectileType);	
}
