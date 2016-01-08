package net.einsteinsci.betterbeginnings.tileentity;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.blocks.BlockObsidianKiln;
import net.einsteinsci.betterbeginnings.inventory.ContainerObsidianKiln;
import net.einsteinsci.betterbeginnings.register.recipe.KilnRecipeHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IInteractionObject;

public class TileEntityObsidianKiln extends TileEntitySpecializedFurnace implements IUpdatePlayerListBox, ISidedInventory,
		IInteractionObject
{
	public static final int SLOT_INPUT = 0;
	public static final int SLOT_FUEL = 1;
	public static final int SLOT_OUTPUT = 2;

	private static final int[] slotsTop = new int[] {SLOT_INPUT};
	private static final int[] slotsBottom = new int[] {SLOT_OUTPUT};
	private static final int[] slotsSides = new int[] {SLOT_FUEL, SLOT_INPUT};

	public TileEntityObsidianKiln()
	{
		super(3);
		processTime = 250;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		currentItemBurnLength = getItemBurnTime(specialFurnaceStacks[SLOT_FUEL]);

		// stacked = tagCompound.getInteger("Stacked");
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		// tagCompound.setInteger("Stacked", stacked);
	}

	public static int getItemBurnTime(ItemStack itemStack)
	{
		if (itemStack == null)
		{
			return 0;
		}
		else
		{
			Item item = itemStack.getItem();

			// Blaze Rods and Lava are valid fuel sources for an obsidian kiln.
			if (item == Items.blaze_rod)
			{
				return 1600;
			}
			if (item == Items.lava_bucket)
			{
				return 80000;
			}

			// All fuels from the Kiln apply here too.
			return TileEntityKiln.getItemBurnTime(itemStack);
		}
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		if (worldObj.getTileEntity(pos) != this)
		{
			return false;
		}
		else
		{
			return player.getDistanceSq((double)pos.getX() + 0.5d, (double)pos.getY() + 0.5d,
			                            (double)pos.getZ() + 0.5d) <= 64.0d;
		}
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack)
	{
		return slot != SLOT_OUTPUT && (slot == SLOT_FUEL || isItemFuel(stack));
	}
	
	@Override
	public String getCommandSenderName()
	{
		return hasCustomName() ? customName : "container.obsidianKiln";
	}

	public void update()
	{
		if (!worldObj.isRemote)
		{
			boolean flag = burnTime > 0;
			boolean flag1 = false;

			if (burnTime > 0)
			{
				--burnTime;
			}

			if (burnTime == 0 && canSmelt())
			{
				currentItemBurnLength = burnTime = getItemBurnTime(specialFurnaceStacks[1]);

				if (burnTime > 0)
				{
					flag1 = true;
					if (specialFurnaceStacks[SLOT_FUEL] != null)
					{
						--specialFurnaceStacks[SLOT_FUEL].stackSize;

						if (specialFurnaceStacks[SLOT_FUEL].stackSize == 0)
						{
							specialFurnaceStacks[SLOT_FUEL] = specialFurnaceStacks[SLOT_FUEL].getItem()
								.getContainerItem(specialFurnaceStacks[SLOT_FUEL]);
						}
					}
				}
			}

			if (isBurning() && canSmelt())
			{
				++cookTime;
				if (cookTime == processTime)
				{
					cookTime = 0;
					smeltItem();
					flag1 = true;
				}
			}
			else
			{
				cookTime = 0;
			}

			if (flag != burnTime > 0)
			{
				flag1 = true;
				BlockObsidianKiln.updateBlockState(burnTime > 0, worldObj, pos);
			}

			if (flag1)
			{
				markDirty();
			}
		}
	}

	private boolean canSmelt()
	{
		if (specialFurnaceStacks[SLOT_INPUT] == null)
		{
			return false;
		}
		else
		{
			ItemStack stack = KilnRecipeHandler.instance().getSmeltingResult(specialFurnaceStacks[SLOT_INPUT]);
			if (stack == null)
			{
				return false;
			}

			if (specialFurnaceStacks[SLOT_OUTPUT] == null)
			{
				return true;
			}
			if (!specialFurnaceStacks[SLOT_OUTPUT].isItemEqual(stack))
			{
				return false;
			}

			int result = specialFurnaceStacks[SLOT_OUTPUT].stackSize + stack.stackSize;
			return result <= getInventoryStackLimit() && result <= specialFurnaceStacks[SLOT_OUTPUT].getMaxStackSize();
		}
	}

	public void smeltItem()
	{
		if (canSmelt())
		{
			ItemStack itemStack = KilnRecipeHandler.instance().getSmeltingResult(specialFurnaceStacks[0]);

			if (specialFurnaceStacks[SLOT_OUTPUT] == null)
			{
				specialFurnaceStacks[SLOT_OUTPUT] = itemStack.copy();
			}
			else if (specialFurnaceStacks[SLOT_OUTPUT].getItem() == itemStack.getItem())
			{
				specialFurnaceStacks[SLOT_OUTPUT].stackSize += itemStack.stackSize;
			}

			--specialFurnaceStacks[SLOT_INPUT].stackSize;

			if (specialFurnaceStacks[SLOT_INPUT].stackSize <= 0)
			{
				specialFurnaceStacks[SLOT_INPUT] = null;
			}
		}
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		return side == EnumFacing.DOWN ? slotsBottom : side == EnumFacing.UP ? slotsTop : slotsSides;
	}

	@Override
	public boolean canInsertItem(int par1, ItemStack stack, EnumFacing par3)
	{
		return isItemValidForSlot(par1, stack);
	}

	public static boolean isItemFuel(ItemStack itemStack)
	{
		return getItemBurnTime(itemStack) > 0;
	}

	@Override
	public boolean canExtractItem(int par1, ItemStack stack, EnumFacing par3)
	{
		return par3 != EnumFacing.DOWN || par1 != 0 || stack.getItem() == Items.bucket;
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		return new ContainerObsidianKiln(playerInventory, this);
	}

	@Override
	public String getGuiID()
	{
		return ModMain.MODID + ":obsidianKiln";
	}

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new S35PacketUpdateTileEntity(pos, 1, tag);
	}
	
	@Override
	public void onDataPacket(NetworkManager manager, S35PacketUpdateTileEntity packet)
	{
		readFromNBT(packet.getNbtCompound());
	}




}

