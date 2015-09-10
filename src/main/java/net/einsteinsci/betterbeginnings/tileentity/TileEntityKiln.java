package net.einsteinsci.betterbeginnings.tileentity;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.blocks.BlockKiln;
import net.einsteinsci.betterbeginnings.inventory.ContainerKiln;
import net.einsteinsci.betterbeginnings.items.ItemBonePickaxe;
import net.einsteinsci.betterbeginnings.items.ItemFlintHatchet;
import net.einsteinsci.betterbeginnings.items.ItemKnifeFlint;
import net.einsteinsci.betterbeginnings.register.recipe.KilnRecipes;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityKiln extends TileSpecializedFurnace implements IInteractionObject
{
	public static final int SLOT_INPUT = 0;
	public static final int SLOT_FUEL = 1;
	public static final int SLOT_OUTPUT = 2;

	public static final int smeltTime = 250;
	private static final int[] slotsTop = new int[] {SLOT_INPUT};
	private static final int[] slotsBottom = new int[] {SLOT_OUTPUT};
	private static final int[] slotsSides = new int[] {SLOT_FUEL, SLOT_INPUT};

	public int kilnBurnTime;
	public int currentBurnTime;

	public int kilnCookTime;

	private String kilnName;

	public TileEntityKiln()
	{
		super(3);
	}

	public void setBlockName(String string)
	{
		kilnName = string;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);

		// Burn Time & Cook Time
		kilnBurnTime = tagCompound.getShort("BurnTime");
		kilnCookTime = tagCompound.getShort("CookTime");
		currentBurnTime = getItemBurnTime(specialFurnaceStacks[1]);

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
		if (hasCustomName())
		{
			tagCompound.setString("CustomName", kilnName);
		}
	}

	@Override
	public int getSizeInventory()
	{
		return specialFurnaceStacks.length;
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

	@Override
	public ItemStack getStackInSlot(int i)
	{
		return specialFurnaceStacks[i];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount)
	{
		if (specialFurnaceStacks[slot] != null)
		{
			ItemStack stack;
			if (specialFurnaceStacks[slot].stackSize <= amount)
			{
				stack = specialFurnaceStacks[slot];
				specialFurnaceStacks[slot] = null;
				return stack;
			}
			else
			{
				stack = specialFurnaceStacks[slot].splitStack(amount);

				if (specialFurnaceStacks[slot].stackSize == 0)
				{
					specialFurnaceStacks[slot] = null;
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
		if (specialFurnaceStacks[slot] != null)
		{
			ItemStack stack = specialFurnaceStacks[slot];
			specialFurnaceStacks[slot] = null;
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
		specialFurnaceStacks[slot] = stack;

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
			BlockPos buf = new BlockPos(pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d);
			return player.getDistanceSq(buf) <= 64.0d;
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
		return slot != SLOT_OUTPUT && (slot != SLOT_FUEL || isItemFuel(stack));
	}

	@Override
	public String getCommandSenderName()
	{
		return hasCustomName() ? kilnName : "container.kiln";
	}

	@Override
	public boolean hasCustomName()
	{
		return kilnName != null && kilnName.length() > 0;
	}

	@Override
	public void clear()
	{
		for (int i = 0; i < specialFurnaceStacks.length; i++)
		{
			specialFurnaceStacks[i] = null;
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
				currentBurnTime = kilnBurnTime = getItemBurnTime(specialFurnaceStacks[1]);

				if (kilnBurnTime > 0)
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
				BlockKiln.updateBlockState(kilnBurnTime > 0, worldObj, pos);
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
			ItemStack stack = KilnRecipes.smelting().getSmeltingResult(specialFurnaceStacks[SLOT_INPUT]);
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

	public boolean isBurning()
	{
		return kilnBurnTime > 0;
	}

	public void smeltItem()
	{
		if (canSmelt())
		{
			ItemStack itemStack = KilnRecipes.smelting().getSmeltingResult(specialFurnaceStacks[SLOT_INPUT]);

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

	public int getCookProgressScaled(int progress)
	{
		return kilnCookTime * progress / smeltTime;
	}

	public int getBurnTimeRemainingScaled(int time)
	{
		if (currentBurnTime <= 0)
		{
			currentBurnTime = smeltTime;
		}

		return kilnBurnTime * time / currentBurnTime;
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		return new ContainerKiln(playerInventory, this);
	}

	@Override
	public String getGuiID()
	{
		return ModMain.MODID + ":kiln";
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		if (side == EnumFacing.DOWN)
		{
			return slotsBottom;
		}
		else if (side == EnumFacing.UP)
		{
			return slotsTop;
		}
		else
		{
			return slotsSides;
		}
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
	{
		return isItemValidForSlot(index, itemStackIn);
	}

	public static boolean isItemFuel(ItemStack itemStack)
	{
		return getItemBurnTime(itemStack) > 0;
	}

	@Override
	public boolean canExtractItem(int par1, ItemStack stack, EnumFacing par3)
	{
		return par3 != EnumFacing.UP || par1 != 1 || stack.getItem() == Items.bucket;
	}


}
