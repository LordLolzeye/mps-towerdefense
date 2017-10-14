package mps.hawks.project.Enemies.EnemyTypes.PathFinderGoals;

import net.minecraft.server.v1_12_R1.PathfinderGoal;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import mps.hawks.project.Main;
import mps.hawks.project.Utils.ReflectUtil;

@SuppressWarnings("rawtypes")
public class PathfinderGoalWalkToFinish extends PathfinderGoal {
	private double speed;
	private double range;
	private Location spawnLoc;
	private boolean returnToSpawn;
	private Object entity;
	private Object navigation;
	private boolean hasGoal;
	private Location achieveLocation;
	private int tid = -1;
	private ReflectUtil.RefClass netEntityReflect = ReflectUtil.getRefClass("{nms}.EntityInsentient");
	private ReflectUtil.RefClass navReflect = ReflectUtil.getRefClass("{nms}.Navigation");
	private ReflectUtil.RefClass pathEntityReflect = ReflectUtil.getRefClass("{nms}.PathEntity");

	public PathfinderGoalWalkToFinish(Object entity, Location loc, double speed, double range)
	{
		this.entity = entity;
		this.achieveLocation = loc;
		this.spawnLoc = loc.add(0.0D, 1.0D, 0.0D).getBlock().getLocation();
		this.navigation = this.netEntityReflect.findMethod(new ReflectUtil.MethodCondition[] { new ReflectUtil.MethodCondition().withName("getNavigation") }).of(this.netEntityReflect.getRealClass().cast(entity)).call(new Object[0]);
		this.speed = speed;
		this.hasGoal = false;
		if (range > 15.0D) {
			this.range = 15.0D;
		} else {
			this.range = range;
		}
	}

	public boolean a()
	{
		Location eLoc = new Location(this.achieveLocation.getWorld(), ((Double)this.netEntityReflect.getField("locX").of(this.entity).get()).doubleValue(), 
				((Double)this.netEntityReflect.getField("locY").of(this.entity).get()).doubleValue(), 
				((Double)this.netEntityReflect.getField("locZ").of(this.entity).get()).doubleValue());

		eLoc = eLoc.getBlock().getLocation();
		if (!this.hasGoal)
		{
			Object entityTarget = this.netEntityReflect.getMethod("getGoalTarget", new Object[0]).of(this.entity).call(new Object[0]);
			if (entityTarget != null) {
				return false;
			}
			if (eLoc.distance(this.spawnLoc) > 14.0D)
			{
				Location closestLoc = eLoc.clone();Location locIterator = eLoc.clone();
				double amx = closestLoc.distance(this.spawnLoc);
				for (int a = -9; a <= 9; a++) {
					for (int b = -9; b <= 9; b++)
					{
						locIterator.add(a, 0.0D, b);
						if ((!locIterator.equals(eLoc)) && 
								(locIterator.distance(this.spawnLoc) < amx))
						{
							closestLoc = locIterator.clone();
							amx = locIterator.distance(this.spawnLoc);
						}
						locIterator.subtract(a, 0.0D, b);
					}
				}
				this.returnToSpawn = false;
				this.achieveLocation = closestLoc.getBlock().getLocation();
				this.achieveLocation.setY(this.achieveLocation.getWorld().getHighestBlockYAt(this.achieveLocation));

				return true;
			}
			if (eLoc.distance(this.spawnLoc) >= this.range)
			{
				this.returnToSpawn = true;
				return true;
			}
		}
		else if ((eLoc.distance(this.spawnLoc) < 1.0D) || (eLoc.distance(this.achieveLocation) < 1.0D))
		{
			if (this.tid != -1)
			{
				Bukkit.getScheduler().cancelTask(this.tid);
				this.tid = -1;
			}
			this.hasGoal = false;
		}
		return false;
	}

	public void e()
	{
		this.hasGoal = true;

		BukkitTask taskID = new BukkitRunnable()
		{
			public void run()
			{
				PathfinderGoalWalkToFinish.this.hasGoal = false;
			}
		}.runTaskLater(Main.getProvidingPlugin(getClass()), 600L);

		this.tid = taskID.getTaskId();

		Location toLoc = this.achieveLocation;
		if (this.returnToSpawn) {
			toLoc = this.spawnLoc;
		}
		Object pathEntity = this.navReflect.findMethod(new ReflectUtil.MethodCondition[] { new ReflectUtil.MethodCondition().withName("a").withTypes(new Object[] { Double.TYPE, Double.TYPE, Double.TYPE }) })
				.of(this.navReflect.getRealClass().cast(this.navigation)).call(new Object[] { Double.valueOf(toLoc.getX()), Double.valueOf(toLoc.getY()), Double.valueOf(toLoc.getZ()) });

		this.navReflect.findMethod(new ReflectUtil.MethodCondition[] { new ReflectUtil.MethodCondition().withName("a").withTypes(new Object[] { this.pathEntityReflect.getRealClass(), Double.TYPE }) })
		.of(this.navReflect.getRealClass().cast(this.navigation)).call(new Object[] { this.pathEntityReflect.getRealClass().cast(pathEntity), Double.valueOf(this.speed) });
	}
}
