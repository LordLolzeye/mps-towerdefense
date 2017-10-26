package mps.hawks.project.Projectile.ProjectileTypes;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import mps.hawks.project.Projectile.Projectile;

public class Fireball extends Projectile {
	public Entity shootProjectile(Location fromLoc, Location toLoc) {
		Vector vector = toLoc.toVector().subtract(fromLoc.toVector());
		
		org.bukkit.entity.Arrow projectile = fromLoc.getWorld().spawnArrow(fromLoc, vector, (float) 3, (float) 0);
		projectile.setBounce(false);
		
		return projectile;
	}
}
