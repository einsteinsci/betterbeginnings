package net.einsteinsci.betterbeginnings.blocks;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.gui.BBGuiHandler;
import net.einsteinsci.betterbeginnings.register.IBBName;
import net.einsteinsci.betterbeginnings.register.RegisterBlocks;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityKiln;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockKiln extends BlockContainer implements IBBName
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	private static boolean isLit;
	private final boolean isLit2; // strange why...

	@SideOnly(Side.CLIENT)
	static final class SwitchEnumFacing
	{
		static final int[] facings = new int[EnumFacing.values().length];

		static
		{
			try
			{
				facings[EnumFacing.WEST.ordinal()] = 1;
			}
			catch (NoSuchFieldError var4)
			{
			}

			try
			{
				facings[EnumFacing.EAST.ordinal()] = 2;
			}
			catch (NoSuchFieldError var3)
			{
			}

			try
			{
				facings[EnumFacing.NORTH.ordinal()] = 3;
			}
			catch (NoSuchFieldError var2)
			{
			}

			try
			{
				facings[EnumFacing.SOUTH.ordinal()] = 4;
			}
			catch (NoSuchFieldError var1)
			{
			}
		}
	}

	public BlockKiln(boolean lit)
	{
		super(Material.rock);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		isLit2 = lit;

		if (!lit)
		{
			setCreativeTab(ModMain.tabBetterBeginnings);
		}

		setUnlocalizedName(getName());

		setHardness(2.0f);
		setResistance(10.0f);
	}

	@Override
	public String getName()
	{
		return isLit2 ? "kilnLit" : "kiln";
	}

	public static void updateBlockState(boolean flag, World world, BlockPos pos)
	{
		IBlockState iblockstate = world.getBlockState(pos);
		TileEntity tileentity = world.getTileEntity(pos);
		isLit = true;

		if (flag)
		{
			world.setBlockState(pos, RegisterBlocks.kilnLit.getDefaultState()
					.withProperty(FACING, iblockstate.getValue(FACING)), 3);
		}
		else
		{
			world.setBlockState(pos, RegisterBlocks.kiln.getDefaultState()
					.withProperty(FACING, iblockstate.getValue(FACING)), 3);
		}

		isLit = false;

		if (tileentity != null)
		{
			tileentity.validate();
			world.setTileEntity(pos, tileentity);
		}
	}

	public int getMetaFromState(IBlockState state)
	{
		return ((EnumFacing)state.getValue(FACING)).getIndex();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		if (isLit2)
		{
			EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
			double d0 = (double)pos.getX() + 0.5D;
			double d1 = (double)pos.getY() + rand.nextDouble() * 6.0D / 16.0D;
			double d2 = (double)pos.getZ() + 0.5D;
			double d3 = 0.52D;
			double d4 = rand.nextDouble() * 0.6D - 0.3D;

			switch (SwitchEnumFacing.facings[enumfacing.ordinal()])
			{
				case 1:
					world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
					world.spawnParticle(EnumParticleTypes.FLAME, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
					break;
				case 2:
					world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
					world.spawnParticle(EnumParticleTypes.FLAME, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
					break;
				case 3:
					world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D);
					world.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D);
					break;
				case 4:
					world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D);
					world.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state)
	{
		if (!world.isRemote)
		{
			Block block = world.getBlockState(pos.offset(EnumFacing.NORTH)).getBlock();
			Block block1 = world.getBlockState(pos.offset(EnumFacing.SOUTH)).getBlock();
			Block block2 = world.getBlockState(pos.offset(EnumFacing.WEST)).getBlock();
			Block block3 = world.getBlockState(pos.offset(EnumFacing.EAST)).getBlock();
			EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);

			if (enumfacing == EnumFacing.NORTH && block.isFullBlock() && !block1.isFullBlock())
			{
				enumfacing = EnumFacing.SOUTH;
			}
			else if (enumfacing == EnumFacing.SOUTH && block1.isFullBlock() && !block.isFullBlock())
			{
				enumfacing = EnumFacing.NORTH;
			}
			else if (enumfacing == EnumFacing.WEST && block2.isFullBlock() && !block3.isFullBlock())
			{
				enumfacing = EnumFacing.EAST;
			}
			else if (enumfacing == EnumFacing.EAST && block3.isFullBlock() && !block2.isFullBlock())
			{
				enumfacing = EnumFacing.WEST;
			}

			world.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);
		}
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(RegisterBlocks.kiln);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side,
	                                float clickX, float clickY, float clickZ)
	{
		player.openGui(ModMain.modInstance, BBGuiHandler.KILN_ID, world, pos.getX(), pos.getY(), pos.getZ());

		return true;
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
	                                 int meta, EntityLivingBase placer)
	{
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer,
	                            ItemStack stack)
	{
		world.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);

		if (stack.hasDisplayName())
		{
			TileEntity teBase = world.getTileEntity(pos);
			if (teBase instanceof TileEntityKiln)
			{
				TileEntityKiln tileEntity = (TileEntityKiln)teBase;
				tileEntity.setBlockName(stack.getDisplayName());
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, BlockPos pos)
	{
		return Item.getItemFromBlock(RegisterBlocks.kiln);
	}

	protected BlockState createBlockState()
	{
		return new BlockState(this, FACING);
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
		if (!isLit)
		{
			TileEntity tileentity = world.getTileEntity(pos);

			if (tileentity instanceof TileEntityKiln)
			{
				InventoryHelper.dropInventoryItems(world, pos, (TileEntityKiln)tileentity);
				world.updateComparatorOutputLevel(pos, this);
			}
		}

		super.breakBlock(world, pos, state);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int par2)
	{
		return new TileEntityKiln();
	}
}
