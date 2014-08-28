package net.einsteinsci.betterbeginnings.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.gui.BBGuiHandler;
import net.einsteinsci.betterbeginnings.register.RegisterBlocks;
import net.einsteinsci.betterbeginnings.register.achievement.RegisterAchievements;
import net.einsteinsci.betterbeginnings.util.BlockUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Random;

public class BlockDoubleWorkbench extends Block
{
	private final Random random = new Random();

	@SideOnly(Side.CLIENT)
	private IIcon top;
	@SideOnly(Side.CLIENT)
	private IIcon topIfNorth;
	@SideOnly(Side.CLIENT)
	private IIcon topIfWest;
	@SideOnly(Side.CLIENT)
	private IIcon topIfEast;
	@SideOnly(Side.CLIENT)
	private IIcon topIfSouth;

	@SideOnly(Side.CLIENT)
	private IIcon westNorthIfSolo;

	@SideOnly(Side.CLIENT)
	private IIcon westIfNorth;
	@SideOnly(Side.CLIENT)
	private IIcon northIfWest;

	@SideOnly(Side.CLIENT)
	private IIcon southIfEast;
	@SideOnly(Side.CLIENT)
	private IIcon eastIfSouth;

	public BlockDoubleWorkbench()
	{
		super(Material.wood);
		setStepSound(soundTypeWood);
		setBlockName("doubleWorkbench");
		setCreativeTab(ModMain.tabBetterBeginnings);

		// Not sure how this works...
		setBlockTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));
		setHardness(2.0f);
		setResistance(5.0f);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int face, int meta)
	{
		switch (face)
		{
			case BlockUtil.Top:
				if (meta == BlockUtil.North)
				{
					return topIfNorth;
				}
				else if (meta == BlockUtil.West)
				{
					return topIfWest;
				}
				else if (meta == BlockUtil.East)
				{
					return topIfEast;
				}
				else if (meta == BlockUtil.South)
				{
					return topIfSouth;
				}
				else
				{
					return top;
				}
			case BlockUtil.Bottom:
				return Blocks.planks.getBlockTextureFromSide(face);
			case BlockUtil.North:
				if (meta == BlockUtil.West)
				{
					return northIfWest;
				}
				else if (meta == BlockUtil.East)
				{
					return westIfNorth;
				}
				else
				{
					return westNorthIfSolo;
				}
			case BlockUtil.West:
				if (meta == BlockUtil.South)
				{
					return northIfWest;
				}
				else if (meta == BlockUtil.North)
				{
					return westIfNorth;
				}
				else
				{
					return westNorthIfSolo;
				}
			case BlockUtil.South:
				if (meta == BlockUtil.East)
				{
					return southIfEast;
				}
				else if (meta == BlockUtil.West)
				{
					return eastIfSouth;
				}
				else
				{
					return blockIcon;
				}
			case BlockUtil.East:
				if (meta == BlockUtil.South)
				{
					return eastIfSouth;
				}
				if (meta == BlockUtil.North)
				{
					return southIfEast;
				}
				else
				{
					return blockIcon;
				}
			default:
				return top;
		}
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int face)
	{
		if (BlockUtil.getBlockFromDirection(world, x, y, z, BlockUtil.North) == RegisterBlocks.doubleWorkbench)
		{
			world.setBlockMetadataWithNotify(x, y, z - 1, 0, 3);
		}

		if (BlockUtil.getBlockFromDirection(world, x, y, z, BlockUtil.South) == RegisterBlocks.doubleWorkbench)
		{
			world.setBlockMetadataWithNotify(x, y, z + 1, 0, 3);
		}

		if (BlockUtil.getBlockFromDirection(world, x, y, z, BlockUtil.West) == RegisterBlocks.doubleWorkbench)
		{
			world.setBlockMetadataWithNotify(x - 1, y, z, 0, 3);
		}

		if (BlockUtil.getBlockFromDirection(world, x, y, z, BlockUtil.East) == RegisterBlocks.doubleWorkbench)
		{
			world.setBlockMetadataWithNotify(x + 1, y, z, 0, 3);
		}

		super.breakBlock(world, x, y, z, block, face);
	}

	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
	{
		int l = 0;

		if (world.getBlock(x - 1, y, z) == this)
		{
			++l;
		}

		if (world.getBlock(x + 1, y, z) == this)
		{
			++l;
		}

		if (world.getBlock(x, y, z - 1) == this)
		{
			++l;
		}

		if (world.getBlock(x, y, z + 1) == this)
		{
			++l;
		}

		return l > 1 ? false : adjacentToSame(world, x - 1, y, z) ? false : adjacentToSame(world, x + 1, y, z) ? false
				: adjacentToSame(world, x, y, z - 1) ? false : !adjacentToSame(world, x, y, z + 1);
	}

	private boolean adjacentToSame(World world, int x, int y, int z)
	{
		return world.getBlock(x, y, z) != this ? false : world.getBlock(x - 1, y, z) == this ? true : world.getBlock(
				x + 1, y, z) == this ? true : world.getBlock(x, y, z - 1) == this ? true
				: world.getBlock(x, y, z + 1) == this;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int face, float posX,
	                                float posY, float posZ)
	{
		if (world.getBlockMetadata(x, y, z) == 0)
		{
			player.openGui(ModMain.modInstance, BBGuiHandler.SIMPLEWORKBENCH_ID, world, x, y, z);
			return true;
		}
		else
		{
			player.openGui(ModMain.modInstance, BBGuiHandler.DOUBLEWORKBENCH_ID, world, x, y, z);
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

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
	{
		boolean isDouble = false;

		if (BlockUtil.getBlockFromDirection(world, x, y, z, BlockUtil.North) == RegisterBlocks.doubleWorkbench)
		{
			world.setBlockMetadataWithNotify(x, y, z, BlockUtil.North, 3);
			world.setBlockMetadataWithNotify(x, y, z - 1, BlockUtil.South, 3);
			isDouble = true;
		}

		if (BlockUtil.getBlockFromDirection(world, x, y, z, BlockUtil.South) == RegisterBlocks.doubleWorkbench)
		{
			world.setBlockMetadataWithNotify(x, y, z, BlockUtil.South, 3);
			world.setBlockMetadataWithNotify(x, y, z + 1, BlockUtil.North, 3);
			isDouble = true;
		}

		if (BlockUtil.getBlockFromDirection(world, x, y, z, BlockUtil.West) == RegisterBlocks.doubleWorkbench)
		{
			world.setBlockMetadataWithNotify(x, y, z, BlockUtil.West, 3);
			world.setBlockMetadataWithNotify(x - 1, y, z, BlockUtil.East, 3);
			isDouble = true;
		}

		if (BlockUtil.getBlockFromDirection(world, x, y, z, BlockUtil.East) == RegisterBlocks.doubleWorkbench)
		{
			world.setBlockMetadataWithNotify(x, y, z, BlockUtil.East, 3);
			world.setBlockMetadataWithNotify(x + 1, y, z, BlockUtil.West, 3);
			isDouble = true;
		}

		if (isDouble)
		{
			if (entity instanceof EntityPlayer)
			{
				RegisterAchievements.achievementGet((EntityPlayer)entity, "doubleWorkbench");
			}
		}
	}

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
	}
}
