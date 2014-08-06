package net.einsteinsci.noobcraft.gui;

import net.einsteinsci.noobcraft.inventory.*;
import net.einsteinsci.noobcraft.tileentity.TileEntityBrickOven;
import net.einsteinsci.noobcraft.tileentity.TileEntityKiln;
import net.einsteinsci.noobcraft.tileentity.TileEntitySmelter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class NoobCraftGuiHandler implements IGuiHandler
{
	public static final int KILN_ID = 0;
	public static final int SIMPLEWORKBENCH_ID = 1;
	public static final int DOUBLEWORKBENCH_ID = 2;
	public static final int BRICKOVEN_ID = 3;
	public static final int SMELTER_ID = 4;
	public static final int REPAIRTABLE_ID = 5;
	
	public static final int KILNSTACKED_ID = 5;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == KILN_ID)
		{
			TileEntityKiln tileEntityKiln = (TileEntityKiln)world.getTileEntity(x, y, z);
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
			TileEntityBrickOven brickOven = (TileEntityBrickOven)world.getTileEntity(x, y, z);
			return new ContainerBrickOven(player.inventory, brickOven);
		}
		
		if (ID == SMELTER_ID)
		{
			TileEntitySmelter smelter = (TileEntitySmelter)world.getTileEntity(x, y, z);
			return new ContainerSmelter(player.inventory, smelter);
		}
		
		if (ID == REPAIRTABLE_ID)
		{
			return new ContainerRepairTable(player.inventory, world, x, y, z);
		}
		
		/*
		 * if (ID == KILNSTACKED_ID) { TileEntityKiln kilnOne = (TileEntityKiln)world.getTileEntity(x, y, z);
		 * 
		 * if (kilnOne.stacked == 1) { TileEntityKiln kilnTop = (TileEntityKiln)world.getTileEntity(x, y + 1, z); return
		 * new ContainerKilnStacked(player.inventory, kilnTop, kilnOne); } else if (kilnOne.stacked == -1) {
		 * TileEntityKiln kilnBottom = (TileEntityKiln)world.getTileEntity(x, y - 1, z); return new
		 * ContainerKilnStacked(player.inventory, kilnOne, kilnBottom); } }
		 */
		
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == KILN_ID)
		{
			TileEntityKiln tileEntityKiln = (TileEntityKiln)world.getTileEntity(x, y, z);
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
			TileEntityBrickOven brickOven = (TileEntityBrickOven)world.getTileEntity(x, y, z);
			return new GuiBrickOven(player.inventory, brickOven);
		}
		
		if (ID == SMELTER_ID)
		{
			TileEntitySmelter smelter = (TileEntitySmelter)world.getTileEntity(x, y, z);
			return new GuiSmelter(player.inventory, smelter);
		}
		
		if (ID == REPAIRTABLE_ID)
		{
			return new GuiRepairTable(player.inventory, world, x, y, z);
		}
		
		/*
		 * if (ID == KILNSTACKED_ID) { TileEntityKiln kilnOne = (TileEntityKiln)world.getTileEntity(x, y, z);
		 * 
		 * if (kilnOne.stacked == 1) { TileEntityKiln kilnTop = (TileEntityKiln)world.getTileEntity(x, y + 1, z); return
		 * new GuiKilnStacked(player.inventory, kilnTop, kilnOne); } else if (kilnOne.stacked == -1) { TileEntityKiln
		 * kilnBottom = (TileEntityKiln)world.getTileEntity(x, y - 1, z); return new GuiKilnStacked(player.inventory,
		 * kilnOne, kilnBottom); } }
		 */
		
		return null;
	}
	
}
