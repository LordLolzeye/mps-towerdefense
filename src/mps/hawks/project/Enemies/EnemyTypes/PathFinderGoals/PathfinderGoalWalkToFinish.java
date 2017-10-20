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
	private boolean hasGoal;
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
		this.hasGoal = true;
	}

	@Override
	public boolean a() {
		Location eLoc = new Location(this.achieveLocation.getWorld(), ((Double)this.netEntityReflect.getField("locX").of(this.entity).get()).doubleValue(), 
				((Double)this.netEntityReflect.getField("locY").of(this.entity).get()).doubleValue(), 
				((Double)this.netEntityReflect.getField("locZ").of(this.entity).get()).doubleValue());
		
		if(!this.hasGoal) {
			eLoc = eLoc.getBlock().getLocation();
			
			if(EnemyEvents.enemyRoute.size() > currentIndex + 1) {
				this.achieveLocation = EnemyEvents.enemyRoute.get(currentIndex + 1);

				currentIndex++;
			}
			
			return true;
		} else if(eLoc.distance(this.achieveLocation) < 1.4) {
			this.hasGoal = false;
			
			return false;
		}
		return false;
	}

	@Override
	public void e()
	{
		this.hasGoal = true;
		
		Location toLoc = this.achieveLocation;
		Object pathEntity = this.navReflect.findMethod(new ReflectUtil.MethodCondition[] { new ReflectUtil.MethodCondition().withName("a").withTypes(new Object[] { Double.TYPE, Double.TYPE, Double.TYPE }) })
				.of(this.navReflect.getRealClass().cast(this.navigation)).call(new Object[] { Double.valueOf(toLoc.getX()), Double.valueOf(toLoc.getY()), Double.valueOf(toLoc.getZ()) });

		this.navReflect.findMethod(new ReflectUtil.MethodCondition[] { new ReflectUtil.MethodCondition().withName("a").withTypes(new Object[] { this.pathEntityReflect.getRealClass(), Double.TYPE }) })
		.of(this.navReflect.getRealClass().cast(this.navigation)).call(new Object[] { this.pathEntityReflect.getRealClass().cast(pathEntity), Double.valueOf(this.speed) });
	}
}
