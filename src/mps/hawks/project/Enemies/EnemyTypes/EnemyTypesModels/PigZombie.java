package mps.hawks.project.Enemies.EnemyTypes.EnemyTypesModels;
import com.google.common.collect.Sets;

import mps.hawks.project.Enemies.EnemyTypes.PathFinderGoals.PathfinderGoalWalkToFinish;

import java.lang.reflect.Field;
import net.minecraft.server.v1_12_R1.EntityPigZombie;
import net.minecraft.server.v1_12_R1.GenericAttributes;
import net.minecraft.server.v1_12_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_12_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_12_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_12_R1.World;
import org.bukkit.Location;

public class PigZombie extends EntityPigZombie {
	public double attackDmg;
	public double health;
	public double speed;

	public PigZombie(World world, double atDmg, double hp, double entitySpeed, Location spawnLocation)
	{
		super(world);
		try
		{
			Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
			bField.setAccessible(true);
			Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
			cField.setAccessible(true);
			bField.set(this.goalSelector, Sets.newLinkedHashSet());
			bField.set(this.targetSelector, Sets.newLinkedHashSet());
			cField.set(this.goalSelector, Sets.newLinkedHashSet());
			cField.set(this.targetSelector, Sets.newLinkedHashSet());
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
		
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true, new Class[0]));
		this.goalSelector.a(2, new PathfinderGoalWalkToFinish(this, spawnLocation, 1.0D, 12.0D));
		
		this.health = hp;
		this.attackDmg = atDmg;
		this.speed = entitySpeed;
	}

	protected void initAttributes()
	{
		super.initAttributes();
		
		getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(15.0D);
	}
}
