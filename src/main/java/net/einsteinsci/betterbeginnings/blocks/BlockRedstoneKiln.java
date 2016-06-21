package net.einsteinsci.betterbeginnings.blocks;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.gui.BBGuiHandler;
import net.einsteinsci.betterbeginnings.register.RegisterBlocks;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityRedstoneKiln;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockRedstoneKiln extends BlockSpecializedFurnace
{
	public BlockRedstoneKiln(boolean lit)
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

		isLit = lit;
	}

	@Override
	public String getName()
	{
		return isLit ? "redstoneKilnLit" : "redstoneKiln";
	}

	public static void updateBlockState(boolean lit, World world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		TileEntity tileEntity = world.getTileEntity(pos);

		if (lit)
		{
			world.setBlockState(pos, RegisterBlocks.redstoneKilnLit.getDefaultState()
				.withProperty(FACING, state.getValue(FACING)), 3);
		}
		else
		{
			world.setBlockState(pos, RegisterBlocks.redstoneKiln.getDefaultState()
				.withProperty(FACING, state.getValue(FACING)), 3);
		}

		if (tileEntity != null)
		{
			tileEntity.validate();
			world.setTileEntity(pos, tileEntity);
		}
	}

	@Override
	public EnumParticleTypes getFlameParticle()
	{
		return EnumParticleTypes.REDSTONE;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int par3)
	{
		return Item.getItemFromBlock(RegisterBlocks.redstoneKiln);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side,
		float clickX, float clickY, float clickZ)
	{
		player.openGui(ModMain.modInstance, BBGuiHandler.REDSTONEKILN_ID, world, pos.getX(), pos.getY(), pos.getZ());

		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, BlockPos pos)
	{
		return Item.getItemFromBlock(RegisterBlocks.redstoneKiln);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int par2)
	{
		return new TileEntityRedstoneKiln();
	}
}
