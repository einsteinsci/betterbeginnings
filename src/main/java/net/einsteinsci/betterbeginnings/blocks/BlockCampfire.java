package net.einsteinsci.betterbeginnings.blocks;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.gui.BBGuiHandler;
import net.einsteinsci.betterbeginnings.register.RegisterBlocks;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityCampfire;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.Random;

public class BlockCampfire extends BlockContainer
{
	private static boolean isAlteringLit;
	private final boolean isLit; // strange why...
	private final Random random = new Random();

	public BlockCampfire(boolean lit)
	{
		super(Material.rock);
		setResistance(5.0F);
		setHardness(2.0F);
		setBlockTextureName(ModMain.MODID + ":CampfireImg");
		setBlockBounds(0.1F, 0.0F, 0.1F, 1.0F, 0.5F, 1.0F);

		if (!lit)
		{
			setCreativeTab(ModMain.tabBetterBeginnings);
			setBlockName("campfire");
		}
		else
		{
			setBlockName("campfireLit");
		}

		isLit = isAlteringLit;
	}

	public static void updateBlockState(boolean lit, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		isAlteringLit = true;

		if (lit)
		{
			world.setBlock(x, y, z, RegisterBlocks.campfireLit);
		}
		else
		{
			world.setBlock(x, y, z, RegisterBlocks.campfire);
		}

		isAlteringLit = false;

		if (tileEntity != null)
		{
			tileEntity.validate();
			world.setTileEntity(x, y, z, tileEntity);
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
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
	{
		return world.getBlock(x, y, z)
				.isReplaceable(world, x, y, z) && World
				.doesBlockHaveSolidTopSurface(world, x, y - 1, z);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float clickX,
	                                float clickY, float clickZ)
	{
		player.openGui(ModMain.modInstance, BBGuiHandler.CAMPFIRE_ID, world, x, y, z);

		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return new TileEntityCampfire();
	}
}
