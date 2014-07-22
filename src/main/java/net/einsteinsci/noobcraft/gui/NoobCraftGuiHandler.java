package net.einsteinsci.noobcraft.gui;

import net.einsteinsci.noobcraft.inventory.ContainerKiln;
import net.einsteinsci.noobcraft.tileentity.TileEntityKiln;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class NoobCraftGuiHandler implements IGuiHandler 
{
	public static final int KILN_ID = 0;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) 
	{
		if (ID == KILN_ID)
		{
			TileEntityKiln tileEntityKiln = (TileEntityKiln)world.getTileEntity(x, y, z);
			return new ContainerKiln(player.inventory, tileEntityKiln);
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) 
	{
		if (ID == KILN_ID)
		{	
			TileEntityKiln tileEntityKiln = (TileEntityKiln)world.getTileEntity(x, y, z);
			return new GuiKiln(player.inventory, tileEntityKiln);
		}
		
		return null;
	}

}
