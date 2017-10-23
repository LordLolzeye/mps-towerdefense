package mps.hawks.project.Projectile;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public abstract class Projectile {
	public abstract Entity shootProjectile(Location fromLoc, Location toLoc);
}
