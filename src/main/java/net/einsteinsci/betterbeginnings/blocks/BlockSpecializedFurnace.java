package net.einsteinsci.betterbeginnings.blocks;

import java.util.Random;

import net.einsteinsci.betterbeginnings.register.IBBName;
import net.einsteinsci.betterbeginnings.tileentity.TileEntitySpecializedFurnace;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockSpecializedFurnace extends BlockContainer implements IBBName
{
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
			{ }

			try
			{
				facings[EnumFacing.EAST.ordinal()] = 2;
			}
			catch (NoSuchFieldError var3)
			{ }

			try
			{
				facings[EnumFacing.NORTH.ordinal()] = 3;
			}
			catch (NoSuchFieldError var2)
			{ }

			try
			{
				facings[EnumFacing.SOUTH.ordinal()] = 4;
			}
			catch (NoSuchFieldError var1)
			{ }
		}
	}

	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	protected boolean isLit;

	protected BlockSpecializedFurnace(Material materialIn) 
	{
		super(materialIn);
		// TODO Auto-generated constructor stub
	}
	
	// Drop stuff everywhere
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		if(!(world.getBlockState(pos).getBlock() instanceof BlockSpecializedFurnace))
		{
			TileEntity tileentity = world.getTileEntity(pos);

			if (tileentity instanceof TileEntitySpecializedFurnace)
			{
				InventoryHelper.dropInventoryItems(world, pos, (TileEntitySpecializedFurnace)tileentity);
				world.updateComparatorOutputLevel(pos, this);
			}
		}

		super.breakBlock(world, pos, state);
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

		super.onBlockAdded(world, pos, state);
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
			if (teBase instanceof TileEntitySpecializedFurnace)
			{
				TileEntitySpecializedFurnace tileEntity = (TileEntitySpecializedFurnace)teBase;
				tileEntity.setBlockName(stack.getDisplayName());
			}
		}
	}

	protected BlockState createBlockState()
	{
		return new BlockState(this, FACING);
	}
	
	public int getMetaFromState(IBlockState state)
	{
		return ((EnumFacing)state.getValue(FACING)).getIndex();
	}
	
	@Override
	public int getRenderType()
	{
		return 3;
	}

	public EnumParticleTypes getFlameParticle()
	{
		return EnumParticleTypes.FLAME;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		if (isLit)
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
					world.spawnParticle(getFlameParticle(), d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
					break;
				case 2:
					world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
					world.spawnParticle(getFlameParticle(), d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
					break;
				case 3:
					world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D);
					world.spawnParticle(getFlameParticle(), d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D);
					break;
				case 4:
					world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D);
					world.spawnParticle(getFlameParticle(), d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D);
			}
		}
	}
}
