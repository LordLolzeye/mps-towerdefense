package mps.hawks.project.Projectile.ProjectileTypes;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import mps.hawks.project.Projectile.Projectile;

public class Arrow extends Projectile {
	public float damage = 1.0f;
	public float speed = 1.0f;
	
	@Override
	public void shootProjectile(Location fromLoc, Location toLoc) {
		Vector vector = toLoc.toVector().subtract(fromLoc.toVector());
		
		fromLoc.getWorld().spawnArrow(fromLoc, vector, (float) 3, (float) 0);
	}
}
