package net.einsteinsci.betterbeginnings.blocks;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.gui.BBGuiHandler;
import net.einsteinsci.betterbeginnings.register.IBBName;
import net.einsteinsci.betterbeginnings.register.RegisterBlocks;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityObsidianKiln;
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

public class BlockObsidianKiln extends BlockContainer implements IBBName
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	private static boolean isLit;
	private final boolean isLit2; // strange why...

	public BlockObsidianKiln(boolean lit)
	{
		super(Material.rock);

		if (lit)
		{
			setLightLevel(0.875F);
		}
		else
		{
			setLightLevel(0F);
			setCreativeTab(ModMain.tabBetterBeginnings);
		}

		setUnlocalizedName(getName());

		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		setHardness(2.0f);
		setResistance(10.0f);

		isLit2 = lit;
	}

	@Override
	public String getName()
	{
		return isLit2 ? "obsidianKilnLit" : "obsidianKiln";
	}

	public static void updateBlockState(boolean lit, World world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		TileEntity tileEntity = world.getTileEntity(pos);
		isLit = true;

		if (lit)
		{
			world.setBlockState(pos, RegisterBlocks.obsidianKilnLit.getDefaultState()
					.withProperty(FACING, state.getValue(FACING)), 3);
		}
		else
		{
			world.setBlockState(pos, RegisterBlocks.obsidianKiln.getDefaultState()
					.withProperty(FACING, state.getValue(FACING)), 3);
		}

		isLit = false;

		if (tileEntity != null)
		{
			tileEntity.validate();
			world.setTileEntity(pos, tileEntity);
		}
	}

	public int getMetaFromState(IBlockState state)
	{
		return ((EnumFacing)state.getValue(FACING)).getIndex();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, BlockPos pos, IBlockState state, Random random)
	{
		if (isLit2)
		{
			float f = pos.getX() + 0.5f;
			float f1 = pos.getY() + random.nextFloat() * 6.0f / 16.0f;
			float f2 = pos.getZ() + 0.5f;

			float f3 = 0.5f;
			float f4 = random.nextFloat() * 0.6f - 0.2f;

			if (state.getValue(FACING) == EnumFacing.WEST)
			{
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, f - f3, f1, f2 + f4, 0.0F, 0.0F, 0.0F);
				world.spawnParticle(EnumParticleTypes.FLAME, f - f3, f1, f2 + f4, 0.0F, 0.0F, 0.0F);
			}
			else if (state.getValue(FACING) == EnumFacing.EAST)
			{
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, f + f3, f1, f2 + f4, 0.0F, 0.0F, 0.0F);
				world.spawnParticle(EnumParticleTypes.FLAME, f + f3, f1, f2 + f4, 0.0F, 0.0F, 0.0F);
			}
			else if (state.getValue(FACING) == EnumFacing.NORTH)
			{
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, f + f4, f1, f2 - f3, 0.0F, 0.0F, 0.0F);
				world.spawnParticle(EnumParticleTypes.FLAME, f + f4, f1, f2 - f3, 0.0F, 0.0F, 0.0F);
			}
			else if (state.getValue(FACING) == EnumFacing.SOUTH)
			{
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, f + f4, f1, f2 + f3, 0.0F, 0.0F, 0.0F);
				world.spawnParticle(EnumParticleTypes.FLAME, f + f4, f1, f2 + f3, 0.0F, 0.0F, 0.0F);
			}
		}
	}

	@SideOnly(Side.CLIENT)
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
	public Item getItemDropped(IBlockState state, Random rand, int par3)
	{
		return Item.getItemFromBlock(RegisterBlocks.obsidianKiln);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side,
	                                float clickX, float clickY, float clickZ)
	{
		player.openGui(ModMain.modInstance, BBGuiHandler.OBSIDIANKILN_ID, world, pos.getX(), pos.getY(), pos.getZ());

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
			if (teBase instanceof TileEntityObsidianKiln)
			{
				TileEntityObsidianKiln tileEntity = (TileEntityObsidianKiln)teBase;
				tileEntity.setBlockName(stack.getDisplayName());
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, BlockPos pos)
	{
		return Item.getItemFromBlock(RegisterBlocks.obsidianKiln);
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

			if (tileentity instanceof TileEntityObsidianKiln)
			{
				InventoryHelper.dropInventoryItems(world, pos, (TileEntityObsidianKiln)tileentity);
				world.updateComparatorOutputLevel(pos, this);
			}
		}

		super.breakBlock(world, pos, state);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int par2)
	{
		return new TileEntityObsidianKiln();
	}
}
