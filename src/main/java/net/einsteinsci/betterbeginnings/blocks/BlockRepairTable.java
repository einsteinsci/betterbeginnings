package net.einsteinsci.betterbeginnings.blocks;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.gui.BBGuiHandler;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityRepairTable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Random;

public class BlockRepairTable extends BlockContainer
{
	private final Random random = new Random();
	public IIcon top;
	public IIcon bottom;

	public BlockRepairTable()
	{
		super(Material.rock);
		setBlockName("inputs");
		setBlockTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));
		setCreativeTab(ModMain.tabBetterBeginnings);
		setHardness(2.0f);
		setResistance(6000);
	}

	@Override
	public IIcon getIcon(int side, int meta)
	{
		if (side == 1)
		{
			return top;
		}
		else if (side == 0)
		{
			return bottom;
		}
		else
		{
			return blockIcon;
		}
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float clickX,
	                                float clickY, float clickZ)
	{
		player.openGui(ModMain.modInstance, BBGuiHandler.REPAIRTABLE_ID, world, x, y, z);
		return true;
	}

	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		blockIcon = register.registerIcon(ModMain.MODID + ":RepairTableSide");
		top = register.registerIcon(ModMain.MODID + ":RepairTableTop");
		bottom = register.registerIcon(ModMain.MODID + ":RepairTableBottom");
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta)
	{
		TileEntityRepairTable repairTable = (TileEntityRepairTable)world.getTileEntity(x, y, z);

		if (repairTable != null)
		{
			for (int i = 0; i < repairTable.getSizeInventory(); ++i)
			{
				ItemStack stack = repairTable.getStackInSlot(i);

				if (stack != null)
				{
					float velX = random.nextFloat() * 0.6f;
					float velY = random.nextFloat() * 0.6f + 0.2f;
					float velZ = random.nextFloat() * 0.6f;

					while (stack.stackSize > 0)
					{
						int j = random.nextInt(21) + 10;

						j = Math.min(j, stack.stackSize);

						stack.stackSize -= j;
						EntityItem entityItem =
								new EntityItem(world,
								               x + velX,
								               y + velY,
								               z + velZ,
								               new ItemStack(stack.getItem(), j,
								                             stack.getItemDamage()));

						if (stack.hasTagCompound())
						{
							entityItem.getEntityItem()
									.setTagCompound((NBTTagCompound)stack.getTagCompound().copy());
						}

						float f3 = 0.025f;
						entityItem.motionX = (float)random.nextGaussian() * f3;
						entityItem.motionY = (float)random.nextGaussian() * f3 + 0.1f;
						entityItem.motionX = (float)random.nextGaussian() * f3;
						world.spawnEntityInWorld(entityItem);
					}
				}
			}
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world, int par2)
	{
		return new TileEntityRepairTable();
	}
}


// Wheee! Buffer!
