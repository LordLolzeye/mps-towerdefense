package mps.hawks.project.Enemies;

import java.lang.reflect.Field;
import java.util.Set;
import org.bukkit.entity.EntityType;

import mps.hawks.project.Enemies.EnemyTypes.EnemyTypesModels.PigZombie;
import net.minecraft.server.v1_12_R1.EntityPigZombie;
import net.minecraft.server.v1_12_R1.EntityTypes;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.RegistryMaterials;

@SuppressWarnings({ "rawtypes", "unchecked" })
public enum RegisterEnemyEntities
{
	PIGZOMBIE("PigZombie", 57, EntityType.PIG_ZOMBIE, EntityPigZombie.class, PigZombie.class);

	private String name;
	private int id;
	private EntityType entityType;
	private Class<?> nmsClass;
	private Class<?> customClass;
	private MinecraftKey key;
	private MinecraftKey oldKey;

	private RegisterEnemyEntities(String name, int id, EntityType entityType, Class<?> nmsClass, Class<?> customClass)
	{
		this.name = name;
		this.id = id;
		this.entityType = entityType;
		this.nmsClass = nmsClass;
		this.customClass = customClass;
		this.key = new MinecraftKey(name);
		this.oldKey = ((MinecraftKey)((RegistryMaterials)getPrivateStatic(EntityTypes.class, "b")).b(nmsClass));
	}

	public static void registerEntities()
	{
		RegisterEnemyEntities[] arrayOfCustomMobs;
		int j = (arrayOfCustomMobs = values()).length;
		for (int i = 0; i < j; i++)
		{
			RegisterEnemyEntities ce = arrayOfCustomMobs[i];ce.register();
		}
	}

	public static void unregisterEntities()
	{
		RegisterEnemyEntities[] arrayOfCustomMobs;
		int j = (arrayOfCustomMobs = values()).length;
		for (int i = 0; i < j; i++)
		{
			RegisterEnemyEntities ce = arrayOfCustomMobs[i];ce.unregister();
		}
	}


	private void register()
	{
		((Set)getPrivateStatic(EntityTypes.class, "d")).add(this.key);
		((RegistryMaterials)getPrivateStatic(EntityTypes.class, "b")).a(this.id, this.key, this.customClass);
	}

	private void unregister()
	{
		((Set)getPrivateStatic(EntityTypes.class, "d")).remove(this.key);
		((RegistryMaterials)getPrivateStatic(EntityTypes.class, "b")).a(this.id, this.oldKey, this.nmsClass);
	}

	public String getName()
	{
		return this.name;
	}

	public int getID()
	{
		return this.id;
	}

	public EntityType getEntityType()
	{
		return this.entityType;
	}

	public Class<?> getCustomClass()
	{
		return this.customClass;
	}

	private static Object getPrivateStatic(Class<?> clazz, String f)
	{
		try
		{
			Field field = clazz.getDeclaredField(f);
			field.setAccessible(true);
			return field.get(null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}

