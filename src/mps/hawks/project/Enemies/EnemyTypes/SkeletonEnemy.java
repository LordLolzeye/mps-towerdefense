package mps.hawks.project.Enemies.EnemyTypes;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;

import mps.hawks.project.Enemies.Enemy;
import mps.hawks.project.Enemies.EnemyTypes.EnemyTypesModels.Skeleton;

public class SkeletonEnemy extends Enemy {

	@Override
	public void spawn(Location spawnLoc, int difficultyLevel) {
		CraftWorld w = (CraftWorld) spawnLoc.getWorld();
		Chunk ch = spawnLoc.getChunk();
		if(!ch.isLoaded()) {
			ch.load();
		}
		
		Skeleton entity = new Skeleton(w.getHandle(), 1.0d, 10.0d, 1.0d, spawnLoc);
		entity.setPosition(spawnLoc.getX(), spawnLoc.getY(), spawnLoc.getZ());
		w.getHandle().addEntity(entity);
	}
	
}
