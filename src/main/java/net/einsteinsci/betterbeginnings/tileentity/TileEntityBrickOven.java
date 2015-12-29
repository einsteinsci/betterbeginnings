package net.einsteinsci.betterbeginnings.tileentity;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.blocks.BlockBrickOven;
import net.einsteinsci.betterbeginnings.inventory.ContainerBrickOven;
import net.einsteinsci.betterbeginnings.register.recipe.BrickOvenRecipeHandler;
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

public class TileEntityBrickOven extends TileEntity implements ISidedInventory, IUpdatePlayerListBox, IInteractionObject
{
	public static final int FUEL = 0;
	public static final int OUTPUT = 1;
	public static final int INPUTSTART = 2;
	public static final int COOKTIME = 120;
	private static final int[] slotsInput = new int[] {FUEL};
	private static final int[] slotsOutput = new int[] {OUTPUT};
	public int ovenBurnTime;
	public int currentItemBurnLength;
	public int ovenCookTime;
	private ItemStack[] ovenStacks = new ItemStack[11];
	private String ovenName;

	public TileEntityBrickOven()
	{
		super();
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);

		// ItemStacks
		NBTTagList tagList = tagCompound.getTagList("Items", 10);
		ovenStacks = new ItemStack[getSizeInventory()];

		for (int i = 0; i < tagList.tagCount(); ++i)
		{
			NBTTagCompound itemTag = tagList.getCompoundTagAt(i);
			byte slot = itemTag.getByte("Slot");

			if (slot >= 0 && slot < ovenStacks.length)
			{
				ovenStacks[slot] = ItemStack.loadItemStackFromNBT(itemTag);
			}
		}

		// Burn Time & Cook Time
		ovenBurnTime = tagCompound.getShort("BurnTime");
		ovenCookTime = tagCompound.getShort("CookTime");
		currentItemBurnLength = getItemBurnTime(ovenStacks[1]);

		if (tagCompound.hasKey("CustomName"))
		{
			ovenName = tagCompound.getString("CustomName");
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);

		tagCompound.setShort("BurnTime", (short)ovenBurnTime);
		tagCompound.setShort("CookTime", (short)ovenCookTime);
		NBTTagList tagList = new NBTTagList();

		for (int i = 0; i < ovenStacks.length; ++i)
		{
			if (ovenStacks[i] != null)
			{
				NBTTagCompound itemTag = new NBTTagCompound();
				ovenStacks[i].writeToNBT(itemTag);
				itemTag.setByte("Slot", (byte)i);
				tagList.appendTag(itemTag);
			}
		}

		tagCompound.setTag("Items", tagList);

		if (hasCustomName())
		{
			tagCompound.setString("CustomName", ovenName);
		}
	}

	@Override
	public int getSizeInventory()
	{
		return ovenStacks.length;
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
			ItemStack clone = itemStack.copy();

			if (item instanceof ItemCoal)
			{
				clone.setItemDamage(0);
			}

			return TileEntityKiln.getItemBurnTime(itemStack);
		}
	}

	@Override
	public ItemStack getStackInSlot(int i)
	{
		return ovenStacks[i];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount)
	{
		if (ovenStacks[slot] != null)
		{
			ItemStack stack;
			if (ovenStacks[slot].stackSize <= amount)
			{
				stack = ovenStacks[slot];
				ovenStacks[slot] = null;
				return stack;
			}
			else
			{
				stack = ovenStacks[slot].splitStack(amount);

				if (ovenStacks[slot].stackSize == 0)
				{
					ovenStacks[slot] = null;
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
		if (ovenStacks[slot] != null)
		{
			ItemStack stack = ovenStacks[slot];
			ovenStacks[slot] = null;
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
		ovenStacks[slot] = stack;

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
	public void openInventory(EntityPlayer playerIn)
	{
	}

	@Override
	public void closeInventory(EntityPlayer playerIn)
	{
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack)
	{
		return slot == OUTPUT ? false : slot == FUEL ? isItemFuel(stack) : true;
	}

	@Override
	public String getCommandSenderName()
	{
		return hasCustomName() ? ovenName : "container.brickoven";
	}

	@Override
	public int getField(int id)
	{
		return 0;
	}

	@Override
	public boolean hasCustomName()
	{
		return ovenName != null && ovenName.length() > 0;
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
		for (int i = 0; i < ovenStacks.length; i++)
		{
			ovenStacks[i] = null;
		}
	}

	@Override
	public void update()
	{
		if (!worldObj.isRemote)
		{
			boolean flag = ovenBurnTime > 0;
			boolean flag1 = false;

			if (ovenBurnTime > 0)
			{
				--ovenBurnTime;
			}

			if (ovenBurnTime == 0 && canSmelt())
			{
				currentItemBurnLength = ovenBurnTime = getItemBurnTime(ovenStacks[FUEL]);

				if (ovenBurnTime > 0)
				{
					flag1 = true;
					if (ovenStacks[FUEL] != null)
					{
						--ovenStacks[FUEL].stackSize;

						if (ovenStacks[FUEL].stackSize == 0)
						{
							ovenStacks[FUEL] = ovenStacks[FUEL].getItem().getContainerItem(ovenStacks[FUEL]);
						}
					}
				}
			}

			if (isBurning() && canSmelt())
			{
				++ovenCookTime;
				if (ovenCookTime == COOKTIME)
				{
					ovenCookTime = 0;
					smeltItem();
					flag1 = true;
				}
			}
			else
			{
				ovenCookTime = 0;
			}

			if (flag != ovenBurnTime > 0)
			{
				flag1 = true;
				BlockBrickOven.updateBlockState(ovenBurnTime > 0, worldObj, pos);
			}

			if (flag1)
			{
				markDirty();
			}
		}
	}

	private boolean canSmelt()
	{
		boolean empty = true;
		for (int i = INPUTSTART; i < ovenStacks.length; ++i)
		{
			if (ovenStacks[i] != null)
			{
				empty = false;
				break;
			}
		}

		if (empty)
		{
			return false;
		}
		else
		{
			ItemStack stack = BrickOvenRecipeHandler.instance().findMatchingRecipe(this);
			if (stack == null)
			{
				return false;
			}

			if (ovenStacks[OUTPUT] == null)
			{
				return true;
			}
			if (!ovenStacks[OUTPUT].isItemEqual(stack))
			{
				return false;
			}

			int result = ovenStacks[OUTPUT].stackSize + stack.stackSize;
			return result <= getInventoryStackLimit() && result <= ovenStacks[OUTPUT].getMaxStackSize();
		}
	}

	public boolean isBurning()
	{
		return ovenBurnTime > 0;
	}

	public void smeltItem()
	{
		if (canSmelt())
		{
			ItemStack itemStack = BrickOvenRecipeHandler.instance().findMatchingRecipe(this);

			if (ovenStacks[OUTPUT] == null)
			{
				ovenStacks[OUTPUT] = itemStack.copy();
			}
			else if (ovenStacks[OUTPUT].getItem() == itemStack.getItem())
			{
				ovenStacks[OUTPUT].stackSize += itemStack.stackSize;
			}

			for (int i = INPUTSTART; i < ovenStacks.length; ++i)
			{
				ItemStack stack = ovenStacks[i];

				if (stack != null)
				{
					ItemStack containerItem = null;

					if (ovenStacks[i].getItem().hasContainerItem(ovenStacks[i]))
					{
						containerItem = ovenStacks[i].getItem().getContainerItem(ovenStacks[i]);
					}

					--ovenStacks[i].stackSize;

					if (ovenStacks[i].stackSize <= 0)
					{
						ovenStacks[i] = null;
					}

					if (containerItem != null)
					{
						ovenStacks[i] = containerItem;
					}
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public int getCookProgressScaled(int progress)
	{
		return ovenCookTime * progress / COOKTIME;
	}

	@SideOnly(Side.CLIENT)
	public int getBurnTimeRemainingScaled(int time)
	{
		if (currentItemBurnLength == 0)
		{
			currentItemBurnLength = COOKTIME;
		}

		return ovenBurnTime * time / currentItemBurnLength;
	}

	public void setBlockName(String displayName)
	{
		ovenName = displayName;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		if (side == EnumFacing.DOWN)
		{
			return slotsOutput;
		}
		else
		{
			return slotsInput;
		}
	}

	@Override
	public boolean canInsertItem(int par1, ItemStack stack, EnumFacing side)
	{
		return isItemValidForSlot(par1, stack);
	}

	public static boolean isItemFuel(ItemStack itemStack)
	{
		return getItemBurnTime(itemStack) > 0;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side)
	{
		return side == EnumFacing.DOWN;
	}

	public ItemStack getStackInRowAndColumn(int row, int column)
	{
		return getStackInSlot(INPUTSTART + row + column * 3);
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		return new ContainerBrickOven(playerInventory, this);
	}

	@Override
	public String getGuiID()
	{
		return ModMain.MODID + ":brickOven";
	}
}