package net.einsteinsci.betterbeginnings.tileentity;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.blocks.BlockObsidianKiln;
import net.einsteinsci.betterbeginnings.inventory.ContainerObsidianKiln;
import net.einsteinsci.betterbeginnings.register.recipe.KilnRecipes;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityObsidianKiln extends TileEntity implements IUpdatePlayerListBox, ISidedInventory,
		IInteractionObject
{
	public static final int SLOT_INPUT = 0;
	public static final int SLOT_FUEL = 1;
	public static final int SLOT_OUTPUT = 2;

	public static final int smeltTime = 250;
	private static final int[] slotsTop = new int[] {SLOT_INPUT};
	private static final int[] slotsBottom = new int[] {SLOT_OUTPUT};
	private static final int[] slotsSides = new int[] {SLOT_FUEL, SLOT_INPUT};

	public ItemStack[] kilnStacks = new ItemStack[3];

	public int kilnBurnTime;
	public int currentBurnTime;

	public int kilnCookTime;

	private String kilnName;

	public TileEntityObsidianKiln()
	{
		super();
	}

	public void setBlockName(String string)
	{
		kilnName = string;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);

		// ItemStacks
		NBTTagList tagList = tagCompound.getTagList("Items", 10);
		kilnStacks = new ItemStack[getSizeInventory()];

		for (int i = 0; i < tagList.tagCount(); ++i)
		{
			NBTTagCompound itemTag = tagList.getCompoundTagAt(i);
			byte slot = itemTag.getByte("Slot");

			if (slot >= 0 && slot < kilnStacks.length)
			{
				kilnStacks[slot] = ItemStack.loadItemStackFromNBT(itemTag);
			}
		}

		// Burn Time & Cook Time
		kilnBurnTime = tagCompound.getShort("BurnTime");
		kilnCookTime = tagCompound.getShort("CookTime");
		currentBurnTime = getItemBurnTime(kilnStacks[SLOT_FUEL]);

		// stacked = tagCompound.getInteger("Stacked");

		if (tagCompound.hasKey("CustomName", 8))
		{
			kilnName = tagCompound.getString("CustomName");
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);

		tagCompound.setShort("BurnTime", (short)kilnBurnTime);
		tagCompound.setShort("CookTime", (short)kilnCookTime);
		// tagCompound.setInteger("Stacked", stacked);
		NBTTagList tagList = new NBTTagList();

		for (int i = 0; i < kilnStacks.length; ++i)
		{
			if (kilnStacks[i] != null)
			{
				NBTTagCompound itemTag = new NBTTagCompound();
				kilnStacks[i].writeToNBT(itemTag);
				itemTag.setByte("Slot", (byte)i);
				tagList.appendTag(itemTag);
			}
		}

		tagCompound.setTag("Items", tagList);
		if (hasCustomName())
		{
			tagCompound.setString("CustomName", kilnName);
		}
	}

	@Override
	public int getSizeInventory()
	{
		return kilnStacks.length;
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
	public ItemStack getStackInSlot(int i)
	{
		return kilnStacks[i];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount)
	{
		if (kilnStacks[slot] != null)
		{
			ItemStack stack;
			if (kilnStacks[slot].stackSize <= amount)
			{
				stack = kilnStacks[slot];
				kilnStacks[slot] = null;
				return stack;
			}
			else
			{
				stack = kilnStacks[slot].splitStack(amount);

				if (kilnStacks[slot].stackSize == 0)
				{
					kilnStacks[slot] = null;
				}

				return stack;
			}
		}
		else
		{
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		if (kilnStacks[slot] != null)
		{
			ItemStack stack = kilnStacks[slot];
			kilnStacks[slot] = null;
			return stack;
		}
		else
		{
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		kilnStacks[slot] = stack;

		if (stack != null && stack.stackSize > getInventoryStackLimit())
		{
			stack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
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
	public void openInventory(EntityPlayer player)
	{
	}

	@Override
	public void closeInventory(EntityPlayer player)
	{
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack)
	{
		return slot != SLOT_OUTPUT && (slot == SLOT_FUEL || isItemFuel(stack));
	}

	@Override
	public String getCommandSenderName()
	{
		return hasCustomName() ? kilnName : "container.obsidianKiln";
	}

	@Override
	public int getField(int id)
	{
		return 0;
	}

	@Override
	public boolean hasCustomName()
	{
		return kilnName != null && kilnName.length() > 0;
	}

	@Override
	public void setField(int id, int value)
	{
	}

	@Override
	public IChatComponent getDisplayName()
	{
		return new ChatComponentText(getCommandSenderName());
	}

	@Override
	public int getFieldCount()
	{
		return 0;
	}

	@Override
	public void clear()
	{
		for (int i = 0; i < kilnStacks.length; i++)
		{
			kilnStacks[i] = null;
		}
	}

	@Override
	public void update()
	{
		if (!worldObj.isRemote)
		{
			boolean flag = kilnBurnTime > 0;
			boolean flag1 = false;

			if (kilnBurnTime > 0)
			{
				--kilnBurnTime;
			}

			if (kilnBurnTime == 0 && canSmelt())
			{
				currentBurnTime = kilnBurnTime = getItemBurnTime(kilnStacks[1]);

				if (kilnBurnTime > 0)
				{
					flag1 = true;
					if (kilnStacks[SLOT_FUEL] != null)
					{
						--kilnStacks[SLOT_FUEL].stackSize;

						if (kilnStacks[SLOT_FUEL].stackSize == 0)
						{
							kilnStacks[SLOT_FUEL] = kilnStacks[SLOT_FUEL].getItem()
								.getContainerItem(kilnStacks[SLOT_FUEL]);
						}
					}
				}
			}

			if (isBurning() && canSmelt())
			{
				++kilnCookTime;
				if (kilnCookTime == smeltTime)
				{
					kilnCookTime = 0;
					smeltItem();
					flag1 = true;
				}
			}
			else
			{
				kilnCookTime = 0;
			}

			if (flag != kilnBurnTime > 0)
			{
				flag1 = true;
				BlockObsidianKiln.updateBlockState(kilnBurnTime > 0, worldObj, pos);
			}

			if (flag1)
			{
				markDirty();
			}
		}
	}

	private boolean canSmelt()
	{
		if (kilnStacks[SLOT_INPUT] == null)
		{
			return false;
		}
		else
		{
			ItemStack stack = KilnRecipes.smelting().getSmeltingResult(kilnStacks[SLOT_INPUT]);
			if (stack == null)
			{
				return false;
			}

			if (kilnStacks[SLOT_OUTPUT] == null)
			{
				return true;
			}
			if (!kilnStacks[SLOT_OUTPUT].isItemEqual(stack))
			{
				return false;
			}

			int result = kilnStacks[SLOT_OUTPUT].stackSize + stack.stackSize;
			return result <= getInventoryStackLimit() && result <= kilnStacks[SLOT_OUTPUT].getMaxStackSize();
		}
	}

	public boolean isBurning()
	{
		return kilnBurnTime > 0;
	}

	public void smeltItem()
	{
		if (canSmelt())
		{
			ItemStack itemStack = KilnRecipes.smelting().getSmeltingResult(kilnStacks[0]);

			if (kilnStacks[SLOT_OUTPUT] == null)
			{
				kilnStacks[SLOT_OUTPUT] = itemStack.copy();
			}
			else if (kilnStacks[SLOT_OUTPUT].getItem() == itemStack.getItem())
			{
				kilnStacks[SLOT_OUTPUT].stackSize += itemStack.stackSize;
			}

			--kilnStacks[SLOT_INPUT].stackSize;

			if (kilnStacks[SLOT_INPUT].stackSize <= 0)
			{
				kilnStacks[SLOT_INPUT] = null;
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public int getCookProgressScaled(int progress)
	{
		return kilnCookTime * progress / smeltTime;
	}

	@SideOnly(Side.CLIENT)
	public int getBurnTimeRemainingScaled(int time)
	{
		if (currentBurnTime <= 0)
		{
			currentBurnTime = smeltTime;
		}

		return kilnBurnTime * time / currentBurnTime;
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






}

