package net.einsteinsci.betterbeginnings.blocks;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityCampfire;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCampfire extends BlockContainer
{

	public BlockCampfire(Boolean lit)
	{
		super(Material.rock);
		this.setResistance(5.0F);
		this.setHardness(2.0F);
		this.setBlockTextureName(ModMain.MODID + ":CampfireImg");
		if (!lit)
		{
			//this.setCreativeTab(ModMain.tabBetterBeginnings);
			this.setBlockName("Campfire");
		}
		else
		{
			this.setBlockName("Campfire Lit");
		}
	}

	public boolean renderAsNormalBlock()
	{
		return false;
	}

	public int getRenderType()
	{
		return -1;
	}

	public boolean isOpaqueCube()
	{
		return false;
	}

	/**
	 * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
	 */
	public boolean canPlaceBlockAt(World p_149742_1_, int p_149742_2_, int p_149742_3_, int p_149742_4_)
	{
		return p_149742_1_.getBlock(p_149742_2_, p_149742_3_, p_149742_4_)
				.isReplaceable(p_149742_1_, p_149742_2_, p_149742_3_, p_149742_4_) && World
				.doesBlockHaveSolidTopSurface(p_149742_1_, p_149742_2_, p_149742_3_ - 1, p_149742_4_);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return new TileEntityCampfire();
	}


}
