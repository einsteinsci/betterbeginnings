package net.einsteinsci.betterbeginnings.blocks;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.register.IBBName;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityInfusionRepair;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockInfusionRepairStation extends BlockContainer implements IBBName
{
	public BlockInfusionRepairStation()
	{
		super(Material.rock);

		setCreativeTab(ModMain.tabBetterBeginnings);
		setHardness(2.0f);
		setResistance(6000);

		setUnlocalizedName(getName());
	}

	@Override
	public String getName()
	{
		return "infusionRepairStation";
	}

	@Override
	public int getRenderType()
	{
		return 3;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean isFullCube()
	{
		return true;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side,
	                                float clickX, float clickY, float clickZ)
	{
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileEntityInfusionRepair)
		{
			TileEntityInfusionRepair infusionRepair = (TileEntityInfusionRepair)tile;
			infusionRepair.onClick(player);
		}
		//player.openGui(ModMain.modInstance, BBGuiHandler.INFUSIONREPAIR_ID, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	//public static void updateBlockState(boolean repairing, World world, BlockPos pos)
	//{
	//	IBlockState iblockstate = world.getBlockState(pos);
	//	TileEntity tileentity = world.getTileEntity(pos);
	//
	//	//world.setBlockState(pos, iblockstate.withProperty(REPAIRING, repairing), 3);
	//
	//	if (tileentity != null)
	//	{
	//		tileentity.validate();
	//		world.setTileEntity(pos, tileentity);
	//	}
	//}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		// TODO: particles
	}

	// Drop stuff everywhere
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		TileEntity tileentity = world.getTileEntity(pos);

		if (tileentity instanceof TileEntityInfusionRepair)
		{
			InventoryHelper.dropInventoryItems(world, pos, (TileEntityInfusionRepair)tileentity);
			world.updateComparatorOutputLevel(pos, this);
		}

		super.breakBlock(world, pos, state);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int par2)
	{
		return new TileEntityInfusionRepair();
	}
}
