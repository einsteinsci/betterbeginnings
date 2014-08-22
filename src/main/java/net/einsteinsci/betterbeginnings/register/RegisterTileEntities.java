package net.einsteinsci.betterbeginnings.register;

import cpw.mods.fml.common.registry.GameRegistry;
import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.tileentity.*;

public class RegisterTileEntities
{
	public static void register()
	{
		GameRegistry.registerTileEntity(TileEntityKiln.class, ModMain.MODID + ":TileEntityKiln");
		GameRegistry.registerTileEntity(TileEntityBrickOven.class, ModMain.MODID + ":TileEntityBrickOven");
		GameRegistry.registerTileEntity(TileEntitySmelter.class, ModMain.MODID + ":TileEntitySmelter");
		GameRegistry.registerTileEntity(TileEntityCampfire.class, ModMain.MODID + ":TileEntityCampfire");
		GameRegistry.registerTileEntity(TileEntityObsidianKiln.class, ModMain.MODID + ":TileEntityObsidianKiln");
		GameRegistry.registerTileEntity(TileEntityNetherBrickOven.class, ModMain.MODID + ":TileEntityNetherBrickOven");
		GameRegistry.registerTileEntity(TileEntityEnderSmelter.class, ModMain.MODID + ":TileEntityEnderSmelter");
	}
}
