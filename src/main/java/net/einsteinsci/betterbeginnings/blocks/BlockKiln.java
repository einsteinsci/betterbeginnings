package net.einsteinsci.betterbeginnings.blocks;

import java.util.Random;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.gui.BBGuiHandler;
import net.einsteinsci.betterbeginnings.register.RegisterBlocks;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityKiln;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockKiln extends BlockSpecializedFurnace
{
	public BlockKiln(boolean lit)
	{
		super(Material.rock);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		isLit = lit;

		if (!lit)
		{
			setLightLevel(0.0f);
			setCreativeTab(ModMain.tabBetterBeginnings);
		}
		else
		{
			setLightLevel(0.875f);
		}

		setUnlocalizedName(getName());

		setHardness(2.0f);
		setResistance(10.0f);
	}

	@Override
	public String getName()
	{
		return isLit ? "kilnLit" : "kiln";
	}

	public static void updateBlockState(boolean flag, World world, BlockPos pos)
	{
		IBlockState iblockstate = world.getBlockState(pos);
		TileEntity tileentity = world.getTileEntity(pos);

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

		if (tileentity != null)
		{
			tileentity.validate();
			world.setTileEntity(pos, tileentity);
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
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, BlockPos pos)
	{
		return Item.getItemFromBlock(RegisterBlocks.kiln);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int par2)
	{
		return new TileEntityKiln();
	}
}
