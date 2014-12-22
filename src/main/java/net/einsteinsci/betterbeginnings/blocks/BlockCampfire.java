package net.einsteinsci.betterbeginnings.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.gui.BBGuiHandler;
import net.einsteinsci.betterbeginnings.register.RegisterBlocks;
import net.einsteinsci.betterbeginnings.register.RegisterItems;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityCampfire;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.Random;

public class BlockCampfire extends BlockContainer
{
	private static boolean isAlteringLit;
	private final boolean isLit; // strange why...
	private final Random random = new Random();

	public BlockCampfire(boolean lit)
	{
		super(Material.rock);
		setResistance(5.0F);
		setHardness(2.0F);
		setBlockTextureName(ModMain.MODID + ":CampfireImg");
		setBlockBounds(0.1F, 0.0F, 0.1F, 1.0F, 0.5F, 1.0F);

		if (!lit)
		{
			setCreativeTab(ModMain.tabBetterBeginnings);
			setBlockName("campfire");
		}
		else
		{
			setBlockName("campfireLit");
		}

		isLit = isAlteringLit;
	}

	public static void updateBlockState(boolean lit, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		isAlteringLit = true;

		if (lit)
		{
			world.setBlock(x, y, z, RegisterBlocks.campfireLit);
		}
		else
		{
			world.setBlock(x, y, z, RegisterBlocks.campfire);
		}

		isAlteringLit = false;

		if (tileEntity != null)
		{
			tileEntity.validate();
			world.setTileEntity(x, y, z, tileEntity);
		}
	}

	public boolean renderAsNormalBlock()
	{
		return false;
	}

	public int getRenderType()
	{
		return -1;
	}

	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random rand)
	{
		TileEntityCampfire tile = (TileEntityCampfire)world.getTileEntity(x, y, z);

		if (tile.isBurning())
		{
			for (int i = 0; i < 3; i++)
			{
				double ry = rand.nextDouble() * 0.5 + 0.15;
				double rx = rand.nextDouble() * 0.4 + 0.3;
				double rz = rand.nextDouble() * 0.4 + 0.3;

				double vx = rand.nextDouble() * 0.02 - 0.01;
				double vy = rand.nextDouble() * 0.05 + 0.03;
				double vz = rand.nextDouble() * 0.02 - 0.01;
				world.spawnParticle("flame", x + rx, y + ry, z + rz, vx, vy, vz);
			}
		}
		else if (tile.isDecaying())
		{
			for (int i = 0; i < 2; i++)
			{
				double ry = rand.nextDouble() * 0.5 + 0.15;
				double rx = rand.nextDouble() * 0.4 + 0.3;
				double rz = rand.nextDouble() * 0.4 + 0.3;

				double vx = rand.nextDouble() * 0.02 - 0.01;
				double vy = rand.nextDouble() * 0.05 + 0.03;
				double vz = rand.nextDouble() * 0.02 - 0.01;
				world.spawnParticle("largesmoke", x + rx, y + ry, z + rz, vx, vy, vz);
			}
		}
	}

	@Override
	public Item getItemDropped(int par1, Random rand, int par3)
	{
		return Item.getItemFromBlock(RegisterBlocks.campfire);
	}

	/**
	 * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
	 */
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
	{
		return world.getBlock(x, y, z)
				.isReplaceable(world, x, y, z) && World
				.doesBlockHaveSolidTopSurface(world, x, y - 1, z);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float clickX,
	                                float clickY, float clickZ)
	{
		if (player.getHeldItem() != null)
		{
			Item item = player.getHeldItem().getItem();

			if (item == Items.flint_and_steel || item == RegisterItems.fireBow)
			{
				return false;
			}
		}

		player.openGui(ModMain.modInstance, BBGuiHandler.CAMPFIRE_ID, world, x, y, z);

		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return new TileEntityCampfire();
	}

	// Drop stuff everywhere
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta)
	{
		if (!isLit)
		{
			TileEntityCampfire campfire = (TileEntityCampfire)world.getTileEntity(x, y, z);
			if (campfire != null)
			{
				for (int i = 0; i < campfire.getSizeInventory(); i++)
				{
					ItemStack stack = campfire.getStackInSlot(i);

					if (stack != null)
					{
						float velX = random.nextFloat() * 0.6f + 0.1f;
						float velY = random.nextFloat() * 0.6f + 0.1f;
						float velZ = random.nextFloat() * 0.6f + 0.1f;

						while (stack.stackSize > 0)
						{
							int j = random.nextInt(21) + 10;

							j = Math.min(j, stack.stackSize);

							stack.stackSize -= j;
							EntityItem entityItem = new EntityItem(world, x + velX, y + velY, z + velZ,
							                                       new ItemStack(stack.getItem(), j,
							                                                     stack.getItemDamage()));

							if (stack.hasTagCompound())
							{
								entityItem.getEntityItem()
										.setTagCompound((NBTTagCompound)stack.getTagCompound().copy());
							}

							float f3 = 0.025f;
							entityItem.motionX = (float)random.nextGaussian() * f3;
							entityItem.motionY = (float)random.nextGaussian() * f3 + 0.3f;
							entityItem.motionX = (float)random.nextGaussian() * f3;
							world.spawnEntityInWorld(entityItem);
						}
					}
				}

				world.func_147453_f(x, y, z, block);
			}
		}
		super.breakBlock(world, x, y, z, block, meta);
	}
}
