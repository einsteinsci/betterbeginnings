package net.einsteinsci.betterbeginnings.tileentity;

import net.einsteinsci.betterbeginnings.register.recipe.SmelterRecipeHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class TileEntitySmelterBase extends TileEntitySpecializedFurnace implements IInteractionObject
{
	public static final int INPUT = 0;
	public static final int FUEL = 1;
	public static final int OUTPUT = 2;
	public static final int GRAVEL = 3;

	public static final int[] SLOTS_TOP = new int[] {GRAVEL, INPUT};
	public static final int[] SLOTS_BOTTOM = new int[] {OUTPUT};
	public static final int[] SLOTS_SIDES = new int[] {FUEL, GRAVEL, INPUT};

	public TileEntitySmelterBase()
	{
		super(4);
	}
	public TileEntitySmelterBase(int slots)
	{
		super(slots);
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		if (side == EnumFacing.DOWN)
		{
			return SLOTS_BOTTOM;
		}
		else if (side == EnumFacing.UP)
		{
			return SLOTS_TOP;
		}

		return SLOTS_SIDES;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing side)
	{
		return isItemValidForSlot(slot, stack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side)
	{
		if (slot == OUTPUT)
		{
			return true;
		}
		else if (stack.getItem() instanceof ItemBucket)
		{
			return true;
		}
		else if (side != EnumFacing.UP)
		{
			return true;
		}

		return false;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack)
	{
		if (stack == null || slot == OUTPUT)
		{
			return false;
		}

		if (slot == GRAVEL && stack.getItem() == Item.getItemFromBlock(Blocks.gravel))
		{
			return true;
		}

		if (slot == FUEL && isItemFuel(stack))
		{
			return true;
		}

		return slot == INPUT;
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
				currentItemBurnLength = burnTime = getItemBurnTime(specialFurnaceStacks[FUEL]);

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
	public boolean canSmelt()
	{
		if (specialFurnaceStacks[INPUT] == null || specialFurnaceStacks[GRAVEL] == null)
		{
			return false;
		}
		else
		{
			ItemStack stack = SmelterRecipeHandler.instance().getSmeltingResult(specialFurnaceStacks[INPUT]);
			int gravelNeeded = SmelterRecipeHandler.instance().getGravelCount(specialFurnaceStacks[INPUT]);

			if (stack == null)
			{
				return false;
			}

			if (gravelNeeded > specialFurnaceStacks[GRAVEL].stackSize)
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
	public void smeltItem()
	{
		if (canSmelt())
		{
			ItemStack itemStack = SmelterRecipeHandler.instance().getSmeltingResult(specialFurnaceStacks[INPUT]);

			if (specialFurnaceStacks[OUTPUT] == null)
			{
				specialFurnaceStacks[OUTPUT] = itemStack.copy();
			}
			else if (specialFurnaceStacks[OUTPUT].getItem() == itemStack.getItem())
			{
				specialFurnaceStacks[OUTPUT].stackSize += itemStack.stackSize;
			}

			int gravelUsed = SmelterRecipeHandler.instance().getGravelCount(specialFurnaceStacks[INPUT]);

			--specialFurnaceStacks[INPUT].stackSize;

			if (specialFurnaceStacks[INPUT].stackSize <= 0)
			{
				specialFurnaceStacks[INPUT] = null;
			}
			specialFurnaceStacks[GRAVEL].stackSize -= gravelUsed;

			if (specialFurnaceStacks[GRAVEL].stackSize <= 0)
			{
				specialFurnaceStacks[GRAVEL] = null;
			}
		}
	}

	public abstract void updateBlockState();

	public static int getItemBurnTimeStatic(ItemStack itemStack)
	{
		if (itemStack == null)
		{
			return 0;
		}
		else
		{
			Item item = itemStack.getItem();

			if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.air)
			{
				Block block = Block.getBlockFromItem(item);
			}

			// Charcoal
			if (item == Items.coal && itemStack.getItemDamage() == 1)
			{
				return 1600;
			}

			// Blaze Rods and Lava are valid fuel sources for a Smelter.
			if (item == Items.blaze_rod)
			{
				return 600;
			}
			if (item == Items.lava_bucket)
			{
				return 7200;
			}

			return GameRegistry.getFuelValue(itemStack);
		}
	}

	public int getItemBurnTime(ItemStack stack)
	{
		return getItemBurnTimeStatic(stack);
	}

	public static boolean isItemFuelStatic(ItemStack stack)
	{
		return getItemBurnTimeStatic(stack) > 0;
	}

	public boolean isItemFuel(ItemStack stack)
	{
		return getItemBurnTime(stack) > 0;
	}
}
