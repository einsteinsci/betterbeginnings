package net.einsteinsci.betterbeginnings.blocks;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.gui.BBGuiHandler;
import net.einsteinsci.betterbeginnings.register.IBBName;
import net.einsteinsci.betterbeginnings.register.RegisterBlocks;
import net.einsteinsci.betterbeginnings.register.achievement.RegisterAchievements;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockDoubleWorkbench extends Block implements IBBName
{
	public static final PropertyDirection CONNECTION = PropertyDirection.create("connection");

	public BlockDoubleWorkbench()
	{
		super(Material.wood);
		setStepSound(soundTypeWood);
		setCreativeTab(ModMain.tabBetterBeginnings);
		setDefaultState(blockState.getBaseState().withProperty(CONNECTION, EnumFacing.UP));

		// Not sure how this works...
		setHardness(2.0f);
		setResistance(5.0f);

		setUnlocalizedName(getName());
	}

	@Override
	public String getName()
	{
		return "doubleWorkbench";
	}

	public int getMetaFromState(IBlockState state)
	{
		return ((EnumFacing)state.getValue(CONNECTION)).getIndex();
	}

	@Override
	public int getRenderType()
	{
		return 3;
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state)
	{
		IBlockState north = world.getBlockState(pos.north());
		if (north.getBlock().equals(RegisterBlocks.doubleWorkbench))
		{
			world.setBlockState(pos, state.withProperty(CONNECTION, EnumFacing.NORTH), 3);
			world.setBlockState(pos.north(), north.withProperty(CONNECTION, EnumFacing.SOUTH), 3);
		}

		IBlockState south = world.getBlockState(pos.south());
		if (south.getBlock().equals(RegisterBlocks.doubleWorkbench))
		{
			world.setBlockState(pos, state.withProperty(CONNECTION, EnumFacing.SOUTH), 3);
			world.setBlockState(pos.south(), south.withProperty(CONNECTION, EnumFacing.NORTH), 3);
		}

		IBlockState east = world.getBlockState(pos.east());
		if (east.getBlock().equals(RegisterBlocks.doubleWorkbench))
		{
			world.setBlockState(pos, state.withProperty(CONNECTION, EnumFacing.EAST), 3);
			world.setBlockState(pos.east(), east.withProperty(CONNECTION, EnumFacing.WEST), 3);
		}

		IBlockState west = world.getBlockState(pos.west());
		if (west.getBlock().equals(RegisterBlocks.doubleWorkbench))
		{
			world.setBlockState(pos, state.withProperty(CONNECTION, EnumFacing.WEST), 3);
			world.setBlockState(pos.west(), west.withProperty(CONNECTION, EnumFacing.EAST), 3);
		}
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		IBlockState north = world.getBlockState(pos.north());
		if (north.getBlock().equals(RegisterBlocks.doubleWorkbench))
		{
			world.setBlockState(pos.north(), north.withProperty(CONNECTION, EnumFacing.UP), 3);
		}
		IBlockState south = world.getBlockState(pos.south());
		if (south.getBlock().equals(RegisterBlocks.doubleWorkbench))
		{
			world.setBlockState(pos.south(), south.withProperty(CONNECTION, EnumFacing.UP), 3);
		}
		IBlockState east = world.getBlockState(pos.east());
		if (east.getBlock().equals(RegisterBlocks.doubleWorkbench))
		{
			world.setBlockState(pos.east(), east.withProperty(CONNECTION, EnumFacing.UP), 3);
		}
		IBlockState west = world.getBlockState(pos.west());
		if (west.getBlock().equals(RegisterBlocks.doubleWorkbench))
		{
			world.setBlockState(pos.west(), west.withProperty(CONNECTION, EnumFacing.UP), 3);
		}
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos)
	{
		if (world.getBlockState(pos.north()).getBlock().equals(this) &&
				world.getBlockState(pos.north()).getValue(CONNECTION) != EnumFacing.UP)
		{
			return false;
		}
		if (world.getBlockState(pos.south()).getBlock().equals(this) &&
				world.getBlockState(pos.south()).getValue(CONNECTION) != EnumFacing.UP)
		{
			return false;
		}
		if (world.getBlockState(pos.east()).getBlock().equals(this) &&
				world.getBlockState(pos.east()).getValue(CONNECTION) != EnumFacing.UP)
		{
			return false;
		}
		if (world.getBlockState(pos.west()).getBlock().equals(this) &&
				world.getBlockState(pos.west()).getValue(CONNECTION) != EnumFacing.UP)
		{
			return false;
		}

		return true;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing face,
	                                float posX, float posY, float posZ)
	{
		if (state.getValue(CONNECTION).equals(EnumFacing.UP))
		{
			player.openGui(ModMain.modInstance, BBGuiHandler.SIMPLEWORKBENCH_ID, world,
			               pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
		else
		{
			player.openGui(ModMain.modInstance, BBGuiHandler.DOUBLEWORKBENCH_ID, world,
			               pos.getX(), pos.getY(), pos.getZ());
			return true;
		}

		// if (world.isRemote)
		// {
		// return true;
		// }
		// else
		// {
		// // player.displayGUIWorkbench(x, y, z);
		// return true;
		// }
	}

	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		if (placer instanceof EntityPlayer)
		{
			RegisterAchievements.achievementGet((EntityPlayer)placer, "doubleWorkbench");
		}

		return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
	}

	protected BlockState createBlockState()
	{
		return new BlockState(this, CONNECTION);
	}

	/*
	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister iconregister)
	{
		blockIcon = iconregister.registerIcon(ModMain.MODID + ":workbenchSide");
		top = iconregister.registerIcon(ModMain.MODID + ":workbenchTop");
		westNorthIfSolo = iconregister.registerIcon(ModMain.MODID + ":workbenchFront");

		westIfNorth = iconregister.registerIcon(ModMain.MODID + ":workbenchWestIfNorth");
		northIfWest = iconregister.registerIcon(ModMain.MODID + ":workbenchNorthIfWest");
		eastIfSouth = iconregister.registerIcon(ModMain.MODID + ":workbenchEastIfSouth");
		southIfEast = iconregister.registerIcon(ModMain.MODID + ":workbenchSouthIfEast");

		topIfNorth = iconregister.registerIcon(ModMain.MODID + ":workbenchTopIfNorth");
		topIfWest = iconregister.registerIcon(ModMain.MODID + ":workbenchTopIfWest");
		topIfEast = iconregister.registerIcon(ModMain.MODID + ":workbenchTopIfEast");
		topIfSouth = iconregister.registerIcon(ModMain.MODID + ":workbenchTopIfSouth");
	} */
}
