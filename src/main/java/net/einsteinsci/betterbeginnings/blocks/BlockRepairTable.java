package net.einsteinsci.betterbeginnings.blocks;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.gui.BBGuiHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockRepairTable extends Block
{
	public IIcon top;
	public IIcon bottom;

	public BlockRepairTable()
	{
		super(Material.rock);
		setBlockName("repairTable");
		setBlockTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));
		setCreativeTab(ModMain.tabBetterBeginnings);
		setHardness(2.0f);
		setResistance(6000);
	}

	@Override
	public IIcon getIcon(int side, int meta)
	{
		if (side == 1)
		{
			return top;
		}
		else if (side == 0)
		{
			return bottom;
		}
		else
		{
			return blockIcon;
		}
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float clickX,
									float clickY, float clickZ)
	{
		player.openGui(ModMain.modInstance, BBGuiHandler.REPAIRTABLE_ID, world, x, y, z);
		return true;
	}

	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		blockIcon = register.registerIcon(ModMain.MODID + ":RepairTableSide");
		top = register.registerIcon(ModMain.MODID + ":RepairTableTop");
		bottom = register.registerIcon(ModMain.MODID + ":RepairTableBottom");
	}
}


// Wheee! Buffer!
