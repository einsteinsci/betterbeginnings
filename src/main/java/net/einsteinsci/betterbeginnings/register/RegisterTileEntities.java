package net.einsteinsci.betterbeginnings.register;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.tileentity.*;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RegisterTileEntities
{
	public static void register()
	{
		GameRegistry.registerTileEntity(TileEntityKiln.class, ModMain.MODID + ":TileEntityKiln");
		GameRegistry.registerTileEntity(TileEntityObsidianKiln.class, ModMain.MODID + ":TileEntityObsidianKiln");
		GameRegistry.registerTileEntity(TileEntityRedstoneKiln.class, ModMain.MODID + ":TileEntityRedstoneKiln");

		GameRegistry.registerTileEntity(TileEntityBrickOven.class, ModMain.MODID + ":TileEntityBrickOven");
		GameRegistry.registerTileEntity(TileEntityNetherBrickOven.class, ModMain.MODID + ":TileEntityNetherBrickOven");

		GameRegistry.registerTileEntity(TileEntitySmelter.class, ModMain.MODID + ":TileEntitySmelter");
		GameRegistry.registerTileEntity(TileEntityEnderSmelter.class, ModMain.MODID + ":TileEntityEnderSmelter");

		GameRegistry.registerTileEntity(TileEntityCampfire.class, ModMain.MODID + ":TileEntityCampfire");

		GameRegistry.registerTileEntity(TileEntityInfusionRepair.class, ModMain.MODID + ":TileEntityInfusionRepair");
	}
}
