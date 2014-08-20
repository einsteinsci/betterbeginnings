package net.einsteinsci.betterbeginnings.gui;

import cpw.mods.fml.common.network.IGuiHandler;
import net.einsteinsci.betterbeginnings.inventory.*;
import net.einsteinsci.betterbeginnings.tileentity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Some issues with this. Please note the server doesn't register items or blocks. At all.
 */
public class BBGuiHandler implements IGuiHandler
{
	public static final int KILN_ID = 0;
	public static final int SIMPLEWORKBENCH_ID = 1;
	public static final int DOUBLEWORKBENCH_ID = 2;
	public static final int BRICKOVEN_ID = 3;
	public static final int SMELTER_ID = 4;
	public static final int REPAIRTABLE_ID = 500;
	public static final int INFUSIONREPAIR_ID = 5;
	public static final int OBSIDIANKILN_ID = 6;

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
			TileEntityRepairTable repair = (TileEntityRepairTable)world.getTileEntity(x, y, z);
			return new ContainerRepairTable(player.inventory, repair);
		}

		if (ID == INFUSIONREPAIR_ID)
		{
			return new ContainerInfusionRepair(player.inventory, world, x, y, z);
		}

		if (ID == OBSIDIANKILN_ID)
		{
			TileEntityObsidianKiln obsKiln = (TileEntityObsidianKiln)world.getTileEntity(x, y, z);
			return new ContainerObsidianKiln(player.inventory, obsKiln);
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
			TileEntityRepairTable repair = (TileEntityRepairTable)world.getTileEntity(x, y, z);
			return new GuiRepairTable(player.inventory, repair);
		}

		if (ID == INFUSIONREPAIR_ID)
		{
			return new GuiInfusionRepair(player.inventory, world, x, y, z);
		}

		if (ID == OBSIDIANKILN_ID)
		{
			TileEntityObsidianKiln obsKiln = (TileEntityObsidianKiln)world.getTileEntity(x, y, z);
			return new GuiObsidianKiln(player.inventory, obsKiln);
		}

		return null;
	}
}
