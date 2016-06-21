package net.einsteinsci.betterbeginnings.tileentity;

import net.einsteinsci.betterbeginnings.items.ItemBonePickaxe;
import net.einsteinsci.betterbeginnings.items.ItemFlintHatchet;
import net.einsteinsci.betterbeginnings.items.ItemKnifeFlint;
import net.einsteinsci.betterbeginnings.register.recipe.KilnRecipeHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.*;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class TileEntityKilnBase extends TileEntitySpecializedFurnace implements IUpdatePlayerListBox,
	ISidedInventory, IInteractionObject
{
	public static final int SLOT_INPUT = 0;
	public static final int SLOT_FUEL = 1;
	public static final int SLOT_OUTPUT = 2;

	public static final int[] SLOTS_TOP = new int[] {SLOT_INPUT};
	public static final int[] SLOTS_BOTTOM = new int[] {SLOT_OUTPUT};
	public static final int[] SLOTS_SIDES = new int[] {SLOT_FUEL, SLOT_INPUT};

	protected TileEntityKilnBase()
	{
		super(3);
	}
	protected TileEntityKilnBase(int slots)
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
		else
		{
			return SLOTS_SIDES;
		}
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
	{
		return isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public boolean canExtractItem(int par1, ItemStack stack, EnumFacing par3)
	{
		return par3 != EnumFacing.UP || par1 != SLOT_INPUT || stack.getItem() == Items.bucket;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack)
	{
		return slot != SLOT_OUTPUT && (slot == SLOT_FUEL || isItemFuel(stack));
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
			BlockPos buf = new BlockPos(pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d);
			return player.getDistanceSq(buf) <= 64.0d;
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
				currentItemBurnLength = burnTime = getItemBurnTime(specialFurnaceStacks[SLOT_FUEL]);

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

			int size = specialFurnaceStacks[SLOT_OUTPUT].stackSize + stack.stackSize;
			return size <= getInventoryStackLimit() && size <= specialFurnaceStacks[SLOT_OUTPUT].getMaxStackSize();
		}
	}

	@Override
	public void smeltItem()
	{
		if (canSmelt())
		{
			ItemStack itemStack = KilnRecipeHandler.instance().getSmeltingResult(specialFurnaceStacks[SLOT_INPUT]);

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

				// Insert any additional block fuels here
				if (block == Blocks.wooden_slab)
				{
					return 150;
				}

				if (block.getMaterial() == Material.wood)
				{
					return 300;
				}

				if (block == Blocks.coal_block)
				{
					return 16000;
				}
			}

			// It may be "wood", but it is not fuel.
			if (item instanceof ItemKnifeFlint || item instanceof ItemBonePickaxe ||
				item instanceof ItemFlintHatchet)
			{
				return 0;
			}

			if (item instanceof ItemTool)
			{
				if (((ItemTool)item).getToolMaterialName().equals("WOOD") ||
					((ItemTool)item).getToolMaterialName().equals("noobwood"))
				{
					return 200;
				}
			}
			if (item instanceof ItemSword)
			{
				if (((ItemSword)item).getToolMaterialName().equals("WOOD") ||
					((ItemSword)item).getToolMaterialName().equals("noobwood"))
				{
					return 200;
				}
			}
			if (item instanceof ItemHoe)
			{
				if (((ItemHoe)item).getMaterialName().equals("WOOD") ||
					((ItemHoe)item).getMaterialName().equals("noobwood"))
				{
					return 200;
				}
			}
			if (item == Items.stick)
			{
				return 100;
			}
			if (item == Items.coal)
			{
				return 1600;
			}
			if (item == Item.getItemFromBlock(Blocks.sapling))
			{
				return 100;
			}

			// Blaze Rods and Lava are invalid fuel sources for a kiln.
			if (item == Items.blaze_rod)
			{
				return 0;
			}
			if (item == Items.lava_bucket)
			{
				return 0;
			}

			return GameRegistry.getFuelValue(itemStack);
		}
	}

	public static boolean isItemFuelStatic(ItemStack itemStack)
	{
		return getItemBurnTimeStatic(itemStack) > 0;
	}

	public int getItemBurnTime(ItemStack stack)
	{
		return getItemBurnTimeStatic(stack);
	}

	public boolean isItemFuel(ItemStack stack)
	{
		return getItemBurnTime(stack) > 0;
	}
}
