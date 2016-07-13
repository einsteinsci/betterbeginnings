package net.einsteinsci.betterbeginnings.register;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.entity.projectile.EntityThrownKnife;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class RegisterEntities 
{
	public static void register()
	{
		registerEntity(EntityThrownKnife.class, "thrownKnife", 0);
	}
	
	private static void registerEntity(Class<? extends Entity> entityClass, String entityName, int id)
	{
		EntityRegistry.registerModEntity(entityClass, entityName, id, ModMain.modInstance, 80, 1, true);
	}
}
