package net.einsteinsci.noobcraft.gui;

import net.einsteinsci.noobcraft.inventory.ContainerBrickOven;
import net.einsteinsci.noobcraft.inventory.ContainerCampfire;
import net.einsteinsci.noobcraft.inventory.ContainerDoubleWorkbench;
import net.einsteinsci.noobcraft.inventory.ContainerKiln;
import net.einsteinsci.noobcraft.inventory.ContainerRepairTable;
import net.einsteinsci.noobcraft.inventory.ContainerSimpleWorkbench;
import net.einsteinsci.noobcraft.inventory.ContainerSmelter;
import net.einsteinsci.noobcraft.tileentity.TileEntityBrickOven;
import net.einsteinsci.noobcraft.tileentity.TileEntityCampfire;
import net.einsteinsci.noobcraft.tileentity.TileEntityKiln;
import net.einsteinsci.noobcraft.tileentity.TileEntitySmelter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

/** Some issues with this. Please note the server doesn't register items or blocks. At all. */
public class NoobCraftGuiHandler implements IGuiHandler
{
	public static final int KILN_ID = 0;
	public static final int SIMPLEWORKBENCH_ID = 1;
	public static final int DOUBLEWORKBENCH_ID = 2;
	public static final int BRICKOVEN_ID = 3;
	public static final int SMELTER_ID = 4;
	public static final int REPAIRTABLE_ID = 5;
	
	public static final int KILNSTACKED_ID = 5;
	
	public static final int CAMPFIRE_ID = 6;
	
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
		
		if (ID == CAMPFIRE_ID)
		{
			TileEntityCampfire campfire = (TileEntityCampfire)world.getTileEntity(x, y, z);
			return new ContainerCampfire(player.inventory, campfire);
		}
		
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
		
		if (ID == CAMPFIRE_ID)
		{
			TileEntityCampfire campfire = (TileEntityCampfire)world.getTileEntity(x, y, z);
			return new GuiCampfire(player.inventory, campfire);
		}
		
		return null;
	}
}
