package net.einsteinsci.betterbeginnings.blocks;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.gui.BBGuiHandler;
import net.einsteinsci.betterbeginnings.register.IBBName;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockInfusionRepairStation extends Block implements IBBName
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
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side,
	                                float clickX, float clickY, float clickZ)
	{
		player.openGui(ModMain.modInstance, BBGuiHandler.INFUSIONREPAIR_ID, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}
}
