package net.einsteinsci.noobcraft.gui;

import net.einsteinsci.noobcraft.inventory.ContainerBrickOven;
import net.einsteinsci.noobcraft.inventory.ContainerDoubleWorkbench;
import net.einsteinsci.noobcraft.inventory.ContainerKiln;
import net.einsteinsci.noobcraft.inventory.ContainerSimpleWorkbench;
import net.einsteinsci.noobcraft.tileentity.TileEntityBrickOven;
import net.einsteinsci.noobcraft.tileentity.TileEntityKiln;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class NoobCraftGuiHandler implements IGuiHandler
{
	public static final int KILN_ID = 0;
	public static final int SIMPLEWORKBENCH_ID = 1;
	public static final int DOUBLEWORKBENCH_ID = 2;
	public static final int BRICKOVEN_ID = 3;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == KILN_ID)
		{
			TileEntityKiln tileEntityKiln = (TileEntityKiln) world.getTileEntity(x, y, z);
			return new ContainerKiln(player.inventory, tileEntityKiln);
		}
		
		if (ID == SIMPLEWORKBENCH_ID)
		{
			return new ContainerSimpleWorkbench(player.inventory, world, x, y, z);
		}
		
		if (ID == DOUBLEWORKBENCH_ID)
		{
			return new ContainerDoubleWorkbench(player.inventory, world, x, y, z);
		}
		
		if (ID == BRICKOVEN_ID)
		{
			TileEntityBrickOven brickOven = (TileEntityBrickOven) world.getTileEntity(x, y, z);
			return new ContainerBrickOven(player.inventory, brickOven);
		}
		
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == KILN_ID)
		{
			TileEntityKiln tileEntityKiln = (TileEntityKiln) world.getTileEntity(x, y, z);
			return new GuiKiln(player.inventory, tileEntityKiln);
		}
		
		if (ID == SIMPLEWORKBENCH_ID)
		{
			return new GuiSimpleWorkbench(player.inventory, world, x, y, z);
		}
		
		if (ID == DOUBLEWORKBENCH_ID)
		{
			return new GuiDoubleWorkbench(player.inventory, world, x, y, z);
		}
		
		if (ID == BRICKOVEN_ID)
		{
			TileEntityBrickOven brickOven = (TileEntityBrickOven) world.getTileEntity(x, y, z);
			return new GuiBrickOven(player.inventory, brickOven);
		}
		
		return null;
	}
	
}
