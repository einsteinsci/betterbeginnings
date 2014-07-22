package net.einsteinsci.noobcraft.register;

import net.einsteinsci.noobcraft.NoobcraftMod;
import net.einsteinsci.noobcraft.tileentity.TileEntityKiln;
import cpw.mods.fml.common.registry.GameRegistry;

public class RegisterTileEntities 
{
	public static void register()
	{
		GameRegistry.registerTileEntity(TileEntityKiln.class, NoobcraftMod.MODID + ":TileEntityKiln");
	}
}
