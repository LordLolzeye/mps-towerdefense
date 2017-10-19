package mps.hawks.project.Projectile;

import org.bukkit.Location;

public abstract class Projectile {
	public float damage = 1.0f;
	public float speed = 1.0f;
	
	public abstract void shootProjectile(Location fromLoc, Location toLoc);
}
