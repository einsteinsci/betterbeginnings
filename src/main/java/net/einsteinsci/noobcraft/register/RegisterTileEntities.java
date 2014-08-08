package net.einsteinsci.noobcraft.register;

import net.einsteinsci.noobcraft.ModMain;
import net.einsteinsci.noobcraft.tileentity.TileEntityBrickOven;
import net.einsteinsci.noobcraft.tileentity.TileEntityCampfire;
import net.einsteinsci.noobcraft.tileentity.TileEntityKiln;
import net.einsteinsci.noobcraft.tileentity.TileEntitySmelter;
import cpw.mods.fml.common.registry.GameRegistry;

public class RegisterTileEntities
{
	public static void register()
	{
		GameRegistry.registerTileEntity(TileEntityKiln.class, ModMain.MODID + ":TileEntityKiln");
		GameRegistry.registerTileEntity(TileEntityBrickOven.class, ModMain.MODID + ":TileEntityBrickOven");
		GameRegistry.registerTileEntity(TileEntitySmelter.class, ModMain.MODID + ":TileEntitySmelter");
		GameRegistry.registerTileEntity(TileEntityCampfire.class, ModMain.MODID + ":TileEntityCampfire");
	}
}
