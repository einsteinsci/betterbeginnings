package net.einsteinsci.betterbeginnings.tileentity;

import net.einsteinsci.betterbeginnings.config.BBConfig;
import net.einsteinsci.betterbeginnings.config.json.BoosterConfig;
import net.einsteinsci.betterbeginnings.register.recipe.SmelterRecipeHandler;
import net.einsteinsci.betterbeginnings.util.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public abstract class TileEntitySmelterBase extends TileEntitySpecializedFurnace implements IInteractionObject
{
	public static final int INPUT = 0;
	public static final int FUEL = 1;
	public static final int OUTPUT = 2;
	public static final int BOOSTER = 3;

	public static final int[] SLOTS_TOP = new int[] {BOOSTER, INPUT};
	public static final int[] SLOTS_BOTTOM = new int[] {OUTPUT};
	public static final int[] SLOTS_SIDES = new int[] {FUEL, BOOSTER, INPUT};

	public static Map<ItemStack, Float> boosterRegistry = new HashMap<>();

	public static final Random RANDOM = new Random();

	private float nextPosition = 0;

	public static void registerBooster(ItemStack booster, float amount)
	{
		boosterRegistry.put(booster, amount);
	}

	public static void registerDefaultBoosters()
	{
		BoosterConfig.registerBooster(new ItemStack(Blocks.gravel), 0.0f);
		BoosterConfig.registerBooster(new ItemStack(Blocks.soul_sand), 0.3f);
		BoosterConfig.registerBooster(new ItemStack(Items.quartz), 1.0f);
		BoosterConfig.registerBooster(new ItemStack(Items.prismarine_shard), 1.5f);
		// BoosterConfig.registerBooster(new ItemStack(Items.crushed_purpur), 1.3f); // 1.9+
	}

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

		if (slot == BOOSTER && isBooster(stack))
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
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		currentItemBurnLength = getItemBurnTime(specialFurnaceStacks[FUEL]);
		nextPosition = tagCompound.getFloat("NextRandPosition");
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		tagCompound.setFloat("NextRandPosition", nextPosition);
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
							specialFurnaceStacks[FUEL] = specialFurnaceStacks[FUEL].getItem()
								.getContainerItem(specialFurnaceStacks[FUEL]);
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
					smeltItem(); // TADA!!!
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
		if (specialFurnaceStacks[INPUT] == null || specialFurnaceStacks[BOOSTER] == null)
		{
			return false;
		}
		else
		{
			ItemStack stack = SmelterRecipeHandler.instance().getSmeltingResult(specialFurnaceStacks[INPUT]);
			int boostersNeeded = SmelterRecipeHandler.instance().getBoosterCount(specialFurnaceStacks[INPUT]);

			if (stack == null)
			{
				return false;
			}

			if (boostersNeeded > specialFurnaceStacks[BOOSTER].stackSize)
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

			int resultCount = specialFurnaceStacks[OUTPUT].stackSize + getMaxNextOutputCount();
			return resultCount <= getInventoryStackLimit() && resultCount <=
				specialFurnaceStacks[OUTPUT].getMaxStackSize();
		}
	}

	@Override
	public void smeltItem()
	{
		if (canSmelt())
		{
			ItemStack result = SmelterRecipeHandler.instance().getSmeltingResult(specialFurnaceStacks[INPUT]);
			int outputCount = getNextOutputCount();

			if (specialFurnaceStacks[OUTPUT] == null)
			{
				specialFurnaceStacks[OUTPUT] = result.copy();
				specialFurnaceStacks[OUTPUT].stackSize = outputCount;
			}
			else if (specialFurnaceStacks[OUTPUT].getItem() == result.getItem())
			{
				specialFurnaceStacks[OUTPUT].stackSize += outputCount;
			}

			int gravelUsed = SmelterRecipeHandler.instance().getBoosterCount(specialFurnaceStacks[INPUT]);

			--specialFurnaceStacks[INPUT].stackSize;

			if (specialFurnaceStacks[INPUT].stackSize <= 0)
			{
				specialFurnaceStacks[INPUT] = null;
			}
			specialFurnaceStacks[BOOSTER].stackSize -= gravelUsed;

			if (specialFurnaceStacks[BOOSTER].stackSize <= 0)
			{
				specialFurnaceStacks[BOOSTER] = null;
			}

			nextPosition = RANDOM.nextFloat();
		}
	}

	// used for space checking in canSmelt(), not actual smelting
	public final int getMaxNextOutputCount()
	{
		float boost = getTotalBoost();
		int ceiling = MathUtil.roundUp(boost);
		return getOutputCountFromBoostLevel(ceiling);
	}

	public final int getNextOutputCount()
	{
		float boost = getTotalBoost();
		int floor = (int)boost;

		if (boost == (float)floor)
		{
			return getOutputCountFromBoostLevel(floor);
		}

		int ceiling = MathUtil.roundUp(boost);
		float determiner = RANDOM.nextFloat() + floor; // width is always 1

		if (determiner > boost) // It's kind of reversed if you try to picture it.
		{
			return getOutputCountFromBoostLevel(floor);
		}
		else
		{
			return  getOutputCountFromBoostLevel(ceiling);
		}
	}

	public final float getTotalBoost()
	{
		float booster = getBoostFromBooster(specialFurnaceStacks[BOOSTER]);
		if (Float.isNaN(booster))
		{
			booster = 0;
		}

		return getBaseBoosterLevel() + booster;
	}

	public final int getMinOutputCount()
	{
		int boostFloor = (int)getTotalBoost();
		return getOutputCountFromBoostLevel(boostFloor);
	}

	public final int getMaxOutputCount()
	{
		int boostCeiling = MathUtil.roundUp(getTotalBoost());
		return getOutputCountFromBoostLevel(boostCeiling);
	}

	public final int getOutputCountFromBoostLevel(int roundedBoost)
	{
		int countPerBoost = SmelterRecipeHandler.instance().getBonusPerBoost(specialFurnaceStacks[INPUT]);
		int countUnboosted = SmelterRecipeHandler.instance().getSmeltingResult(specialFurnaceStacks[INPUT]).stackSize;
		return countUnboosted + (roundedBoost - 1) * countPerBoost;
	}

	public abstract void updateBlockState();

	public abstract float getBaseBoosterLevel();

	public static float getBoostFromBooster(ItemStack stack)
	{
		if (stack == null)
		{
			return Float.NaN;
		}

		for (Map.Entry<ItemStack, Float> entry : boosterRegistry.entrySet())
		{
			ItemStack tested = entry.getKey();
			if (tested.getMetadata() == OreDictionary.WILDCARD_VALUE)
			{
				if (tested.getItem() == stack.getItem())
				{
					return entry.getValue();
				}
			}
			else
			{
				if (tested.getItem() == stack.getItem() && tested.getMetadata() == stack.getMetadata())
				{
					return entry.getValue();
				}
			}
		}

		return Float.NaN;
	}

	public static boolean isBooster(ItemStack stack)
	{
		return !Float.isNaN(getBoostFromBooster(stack));
	}

	public static int getItemBurnTimeStatic(ItemStack itemStack)
	{
		if (itemStack == null)
		{
			return 0;
		}

		Item item = itemStack.getItem();

		if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.air)
		{
			Block block = Block.getBlockFromItem(item);
		}

		// Charcoal
		if (item == Items.coal && (itemStack.getItemDamage() == 1 || BBConfig.smeltersCanUseCoal))
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
