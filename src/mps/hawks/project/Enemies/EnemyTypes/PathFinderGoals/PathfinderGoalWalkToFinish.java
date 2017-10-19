package mps.hawks.project.Enemies.EnemyTypes.PathFinderGoals;

import net.minecraft.server.v1_12_R1.Entity;
import net.minecraft.server.v1_12_R1.PathfinderGoal;
import org.bukkit.Location;

import mps.hawks.project.Enemies.EnemyEvents;
import mps.hawks.project.Utils.ReflectUtil;

@SuppressWarnings("rawtypes")
public class PathfinderGoalWalkToFinish extends PathfinderGoal {
	private double speed;
	private int currentIndex;
	public Entity entity;
	private Object navigation;
	private Location achieveLocation;
	private ReflectUtil.RefClass netEntityReflect = ReflectUtil.getRefClass("{nms}.EntityInsentient");
	private ReflectUtil.RefClass navReflect = ReflectUtil.getRefClass("{nms}.Navigation");
	private ReflectUtil.RefClass pathEntityReflect = ReflectUtil.getRefClass("{nms}.PathEntity");

	public PathfinderGoalWalkToFinish(Entity entity, Location loc, double speed, double range)
	{
		this.entity = entity;
		this.currentIndex = 0;
		this.achieveLocation = EnemyEvents.enemyRoute.get(currentIndex); 
		this.navigation = this.netEntityReflect.findMethod(new ReflectUtil.MethodCondition[] { new ReflectUtil.MethodCondition().withName("getNavigation") }).of(this.netEntityReflect.getRealClass().cast(entity)).call(new Object[0]);
		this.speed = speed;
	}

	@Override
	public boolean a() {
		Location eLoc = new Location(this.achieveLocation.getWorld(), ((Double)this.netEntityReflect.getField("locX").of(this.entity).get()).doubleValue(), 
				((Double)this.netEntityReflect.getField("locY").of(this.entity).get()).doubleValue(), 
				((Double)this.netEntityReflect.getField("locZ").of(this.entity).get()).doubleValue());
		
		eLoc = eLoc.getBlock().getLocation();
		
		if(eLoc.distance(EnemyEvents.enemyRoute.get(currentIndex + 1)) < 0.8 ) {
			this.achieveLocation = EnemyEvents.enemyRoute.get(currentIndex + 1);
			currentIndex++;
			
			return true;
		}
		
		return false;
	}

	@Override
	public void e()
	{
		Location toLoc = this.achieveLocation;
		System.out.println("Current achievement is " + toLoc);
		Object pathEntity = this.navReflect.findMethod(new ReflectUtil.MethodCondition[] { new ReflectUtil.MethodCondition().withName("a").withTypes(new Object[] { Double.TYPE, Double.TYPE, Double.TYPE }) })
				.of(this.navReflect.getRealClass().cast(this.navigation)).call(new Object[] { Double.valueOf(toLoc.getX()), Double.valueOf(toLoc.getY()), Double.valueOf(toLoc.getZ()) });

		this.navReflect.findMethod(new ReflectUtil.MethodCondition[] { new ReflectUtil.MethodCondition().withName("a").withTypes(new Object[] { this.pathEntityReflect.getRealClass(), Double.TYPE }) })
		.of(this.navReflect.getRealClass().cast(this.navigation)).call(new Object[] { this.pathEntityReflect.getRealClass().cast(pathEntity), Double.valueOf(this.speed) });
	}
}
