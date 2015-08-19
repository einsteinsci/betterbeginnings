package net.einsteinsci.betterbeginnings.blocks;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.gui.BBGuiHandler;
import net.einsteinsci.betterbeginnings.register.*;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityCampfire;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockCampfire extends BlockContainer implements IBBName
{
	private static boolean isAlteringLit;
	private final boolean isLit; // strange why...

	public BlockCampfire(boolean lit)
	{
		super(Material.wood);
		setResistance(3.0F);
		setHardness(0.5F);

		if (!lit)
		{
			setCreativeTab(ModMain.tabBetterBeginnings);
		}
		else
		{
			setLightLevel(0.875f);
		}

		setUnlocalizedName(getName());

		isLit = lit;
	}

	@Override
	public String getName()
	{
		return isLit ? "campfireLit" : "campfire";
	}

	public static void updateBlockState(boolean lit, World world, BlockPos pos)
	{
		TileEntity tileEntity = world.getTileEntity(pos);
		isAlteringLit = true;

		if (lit)
		{
			world.setBlockState(pos, RegisterBlocks.campfireLit.getDefaultState(), 3);
		}
		else
		{
			world.setBlockState(pos, RegisterBlocks.campfire.getDefaultState(), 3);
		}

		isAlteringLit = false;

		if (tileEntity != null)
		{
			tileEntity.validate();
			world.setTileEntity(pos, tileEntity);
		}
	}

	public boolean isFullCube()
	{
		return false;
	}

	public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
	{
		return true;
	}

	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		TileEntityCampfire tile = (TileEntityCampfire)world.getTileEntity(pos);

		if (tile.campfireState == TileEntityCampfire.STATE_BURNING)
		{
			for (int i = 0; i < 3; i++)
			{
				double ry = rand.nextDouble() * 0.5 + 0.15;
				double rx = rand.nextDouble() * 0.4 + 0.3;
				double rz = rand.nextDouble() * 0.4 + 0.3;

				double vx = rand.nextDouble() * 0.02 - 0.01;
				double vy = rand.nextDouble() * 0.05 + 0.03;
				double vz = rand.nextDouble() * 0.02 - 0.01;
				world.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + rx, pos.getY() + ry, pos.getZ() + rz,
				                    vx, vy, vz);
			}
		}
		else if (tile.campfireState == TileEntityCampfire.STATE_DECAYING)
		{
			for (int i = 0; i < 2; i++)
			{
				double ry = rand.nextDouble() * 0.5 + 0.15;
				double rx = rand.nextDouble() * 0.4 + 0.3;
				double rz = rand.nextDouble() * 0.4 + 0.3;

				double vx = rand.nextDouble() * 0.02 - 0.01;
				double vy = rand.nextDouble() * 0.05 + 0.03;
				double vz = rand.nextDouble() * 0.02 - 0.01;
				world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.getX() + rx, pos.getY() + ry, pos.getZ() + rz,
				                    vx, vy, vz);
			}
		}
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int par3)
	{
		return Item.getItemFromBlock(RegisterBlocks.campfire);
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos)
	{
		return world.getBlockState(pos).getBlock().isReplaceable(world, pos) &&
			World.doesBlockHaveSolidTopSurface(world, pos.offset(EnumFacing.DOWN));
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side,
	                                float clickX, float clickY, float clickZ)
	{
		if (player.getHeldItem() != null)
		{
			Item item = player.getHeldItem().getItem();

			if (item == Items.flint_and_steel || item == RegisterItems.fireBow)
			{
				return false;
			}
		}

		player.openGui(ModMain.modInstance, BBGuiHandler.CAMPFIRE_ID, world, pos.getX(), pos.getY(), pos.getZ());

		return true;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
	{
		this.setBlockBounds(0.15F, 0.0F, 0.15F, 0.85F, 0.5F, 0.85F);
	}

	@Override
	public int getRenderType()
	{
		return 3;
	}

	// Drop stuff everywhere
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		if (!isAlteringLit)
		{
			TileEntity tileentity = world.getTileEntity(pos);

			if (tileentity instanceof TileEntityCampfire)
			{
				InventoryHelper.dropInventoryItems(world, pos, (TileEntityCampfire)tileentity);
				world.updateComparatorOutputLevel(pos, this);
			}
		}

		super.breakBlock(world, pos, state);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return new TileEntityCampfire();
	}
}
