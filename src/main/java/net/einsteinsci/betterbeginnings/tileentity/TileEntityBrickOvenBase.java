package net.einsteinsci.betterbeginnings.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemCoal;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public abstract class TileEntityBrickOvenBase extends TileEntitySpecializedFurnace
{
	public static final int FUEL = 0;
	public static final int OUTPUT = 1;
	public static final int INPUTSTART = 2;

	private static final int[] SLOTS_INPUT = new int[] {FUEL};
	private static final int[] SLOTS_OUTPUT = new int[] {OUTPUT};

	public TileEntityBrickOvenBase()
	{
		super(11);
	}
	public TileEntityBrickOvenBase(int slots)
	{
		super(slots);
	}

	@Override
	public void smeltItem()
	{
		if (canSmelt())
		{
			ItemStack itemStack = findMatchingRecipe();

			if (specialFurnaceStacks[OUTPUT] == null)
			{
				specialFurnaceStacks[OUTPUT] = itemStack.copy();
			}
			else if (specialFurnaceStacks[OUTPUT].getItem() == itemStack.getItem())
			{
				specialFurnaceStacks[OUTPUT].stackSize += itemStack.stackSize;
			}

			for (int i = INPUTSTART; i < specialFurnaceStacks.length; ++i)
			{
				ItemStack stack = specialFurnaceStacks[i];

				if (stack != null)
				{
					ItemStack containerItem = null;

					if (specialFurnaceStacks[i].getItem().hasContainerItem(specialFurnaceStacks[i]))
					{
						containerItem = specialFurnaceStacks[i].getItem().getContainerItem(specialFurnaceStacks[i]);
					}

					--specialFurnaceStacks[i].stackSize;

					if (specialFurnaceStacks[i].stackSize <= 0)
					{
						specialFurnaceStacks[i] = null;
					}

					if (containerItem != null)
					{
						specialFurnaceStacks[i] = containerItem;
					}
				}
			}
		}
	}

	@Override
	public boolean canSmelt()
	{
		boolean empty = true;
		for (int i = INPUTSTART; i < specialFurnaceStacks.length; ++i)
		{
			if (specialFurnaceStacks[i] != null)
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
			ItemStack stack = findMatchingRecipe();
			if (stack == null)
			{
				return false;
			}

			if (specialFurnaceStacks[OUTPUT] == null)
			{
				return true;
			}
			if (!specialFurnaceStacks[OUTPUT].isItemEqual(stack))
			{
				return false;
			}

			int result = specialFurnaceStacks[OUTPUT].stackSize + stack.stackSize;
			return result <= getInventoryStackLimit() && result <= specialFurnaceStacks[OUTPUT].getMaxStackSize();
		}
	}

	@Override
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
				currentItemBurnLength = burnTime = getItemBurnTimeStatic(specialFurnaceStacks[FUEL]);

				if (burnTime > 0)
				{
					flag1 = true;
					if (specialFurnaceStacks[FUEL] != null)
					{
						--specialFurnaceStacks[FUEL].stackSize;

						if (specialFurnaceStacks[FUEL].stackSize == 0)
						{
							specialFurnaceStacks[FUEL] = specialFurnaceStacks[FUEL].getItem().getContainerItem(specialFurnaceStacks[FUEL]);
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
				updateBlockState();
			}

			if (flag1)
			{
				markDirty();
			}
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
		return slot == OUTPUT ? false : slot == FUEL ? isItemFuel(stack) : true;
	}

	@Override
	public boolean canInsertItem(int par1, ItemStack stack, EnumFacing side)
	{
		return isItemValidForSlot(par1, stack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side)
	{
		return side == EnumFacing.DOWN;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		if (side == EnumFacing.DOWN)
		{
			return SLOTS_OUTPUT;
		}
		else
		{
			return SLOTS_INPUT;
		}
	}

	public static int getItemBurnTimeStatic(ItemStack itemStack)
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

			return TileEntityKiln.getItemBurnTimeStatic(itemStack);
		}
	}

	public static boolean isItemFuel(ItemStack itemStack)
	{
		return getItemBurnTimeStatic(itemStack) > 0;
	}

	public abstract ItemStack findMatchingRecipe();

	public abstract void updateBlockState();

	public void updateNetwork()
	{ }

	public int getItemBurnTime(ItemStack stack, boolean flag)
	{
		return getItemBurnTimeStatic(stack);
	}
}
