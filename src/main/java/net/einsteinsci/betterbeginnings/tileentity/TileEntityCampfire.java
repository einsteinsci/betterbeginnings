package net.einsteinsci.betterbeginnings.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.einsteinsci.betterbeginnings.blocks.BlockCampfire;
import net.einsteinsci.betterbeginnings.items.ItemPan;
import net.einsteinsci.betterbeginnings.register.RegisterItems;
import net.einsteinsci.betterbeginnings.register.recipe.CampfirePanRecipes;
import net.einsteinsci.betterbeginnings.register.recipe.CampfireRecipes;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityCampfire extends TileEntity implements IInventory
{
	public static final int maxCookTime = 300;
	public static final int maxDecayTime = 400; // 20 sec

	public static final int SLOT_INPUT = 0;
	public static final int SLOT_FUEL = 2;
	public static final int SLOT_OUTPUT = 1;
	public static final int SLOT_PAN = 3;

	public ItemStack[] stacks = new ItemStack[4];

	public int cookTime;
	public int burnTime;
	public int currentItemBurnTime;
	public int decayTime;

	private String campfireName;

	public TileEntityCampfire()
	{
		super();
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);

		// ItemStacks
		NBTTagList tagList = tagCompound.getTagList("Items", 10);
		stacks = new ItemStack[getSizeInventory()];

		for (int i = 0; i < tagList.tagCount(); ++i)
		{
			NBTTagCompound itemTag = tagList.getCompoundTagAt(i);
			byte slot = itemTag.getByte("Slot");

			if (slot >= 0 && slot < stacks.length)
			{
				stacks[slot] = ItemStack.loadItemStackFromNBT(itemTag);
			}
		}

		// Burn Time & Cook Time
		burnTime = tagCompound.getShort("BurnTime");
		cookTime = tagCompound.getShort("CookTime");
		currentItemBurnTime = getBurnTimeForFuel(stackFuel());
		decayTime = tagCompound.getShort("DecayTime");

		if (tagCompound.hasKey("CustomName", 8))
		{
			campfireName = tagCompound.getString("CustomName");
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);

		tagCompound.setShort("BurnTime", (short)burnTime);
		tagCompound.setShort("CookTime", (short)cookTime);
		tagCompound.setShort("DecayTIme", (short)decayTime);
		NBTTagList tagList = new NBTTagList();

		for (int i = 0; i < stacks.length; ++i)
		{
			if (stacks[i] != null)
			{
				NBTTagCompound itemTag = new NBTTagCompound();
				stacks[i].writeToNBT(itemTag);
				itemTag.setByte("Slot", (byte)i);
				tagList.appendTag(itemTag);
			}
		}

		tagCompound.setTag("Items", tagList);
		if (hasCustomInventoryName())
		{
			tagCompound.setString("CustomName", campfireName);
		}
	}

	@Override
	public void updateEntity()
	{
		boolean burning = burnTime > 0;
		boolean dirty = false;

		if (burning)
		{
			burnTime--;
			decayTime = maxDecayTime;
		}
		else
		{
			decayTime = Math.max(0, decayTime - 1);
		}

		if (!worldObj.isRemote)
		{
			if (burnTime == 0 && canCook() && isDecaying()) //only start fuel if lit (w/ F&S or Fire Bow)
			{
				burnTime = getBurnTimeForFuel(stackFuel());
				currentItemBurnTime = burnTime;
				if (burnTime > 0)
				{
					decayTime = maxDecayTime;
					burning = true;
				}

				if (burning)
				{
					dirty = true;
					if (stackFuel() != null)
					{
						stackFuel().stackSize--;

						if (stackFuel().stackSize == 0)
						{
							stacks[SLOT_FUEL] = stackFuel().getItem().getContainerItem(stackFuel());
						}
					}
				}
			}

			if (isDecaying() && canCook())
			{
				cookTime++;
				if (cookTime == maxCookTime)
				{
					cookTime = 0;
					cookItem();     // Tadaaa!
					dirty = true;
				}
			}
			else
			{
				cookTime = 0;
			}
		}

		if (burning != decayTime > 0)
		{
			dirty = true;
			BlockCampfire.updateBlockState(decayTime > 0, worldObj, xCoord, yCoord, zCoord);
		}

		if (dirty)
		{
			markDirty();
		}
	}

	public boolean canCook()
	{
		if (stackInput() == null)
		{
			return false;
		}

		ItemStack potentialResult = CampfirePanRecipes.smelting().getSmeltingResult(stackInput());
		if (potentialResult == null || stackPan() == null)
		{
			potentialResult = CampfireRecipes.smelting().getSmeltingResult(stackInput());
		}

		if (potentialResult == null)
		{
			return false; // instant no if there's no recipe
		}

		if (stackOutput() == null)
		{
			return true; // instant yes if output is open
		}
		if (!stackOutput().isItemEqual(potentialResult))
		{
			return false; // instant no if output doesn't match recipe
		}

		int resultSize = stackOutput().stackSize + potentialResult.stackSize;
		boolean canFit = resultSize <= getInventoryStackLimit();
		boolean canFitWithItem = resultSize <= stackOutput().getMaxStackSize();
		return canFit && canFitWithItem;
	}

	public boolean isDecaying()
	{
		return decayTime > 0;
	}

	private void cookItem()
	{
		if (canCook())
		{
			ItemStack potentialResult = CampfirePanRecipes.smelting().getSmeltingResult(stackInput());
			if (potentialResult == null || stackPan() == null)
			{
				potentialResult = CampfireRecipes.smelting().getSmeltingResult(stackInput());
			}

			if (stackOutput() == null)
			{
				stacks[SLOT_OUTPUT] = potentialResult.copy();
			}
			else if (stackOutput().isItemEqual(potentialResult))
			{
				stackOutput().stackSize += potentialResult.stackSize;
			}

			stackInput().stackSize--;

			if (stackInput().stackSize <= 0)
			{
				stacks[SLOT_INPUT] = null;
			}

			if (stacks[SLOT_PAN] != null)
			{
				if (stacks[SLOT_PAN].getItem() == RegisterItems.pan)
				{
					int damage = stacks[SLOT_PAN].getItemDamage();
					stacks[SLOT_PAN].setItemDamage(damage + 1);

					if (stacks[SLOT_PAN].getItemDamage() >= stacks[SLOT_PAN].getMaxDamage())
					{
						stacks[SLOT_PAN] = null;
					}
				}
			}
		}
	}

	public ItemStack stackInput()
	{
		return stacks[SLOT_INPUT];
	}

	public ItemStack stackPan()
	{
		return stacks[SLOT_PAN];
	}

	public ItemStack stackOutput()
	{
		return stacks[SLOT_OUTPUT];
	}

	@Override
	public int getSizeInventory()
	{
		return stacks.length;
	}

	public static int getBurnTimeForFuel(ItemStack fuel)
	{
		if (fuel == null)
		{
			return 0;
		}

		Block block = Block.getBlockFromItem(fuel.getItem());
		Item item = fuel.getItem();

		if (block != null)
		{
			if (block.getMaterial() == Material.wood)
			{
				return 600;
			}
			if (block == Blocks.coal_block)
			{
				return 16000;
			}
			if (block == Blocks.wooden_slab)
			{
				return 300;
			}
			if (block == Blocks.sapling)
			{
				return 200;
			}
		}

		if (item != null)
		{
			if (item instanceof ItemTool)
			{
				if (((ItemTool)item).getToolMaterialName().equals("WOOD") ||
						((ItemTool)item).getToolMaterialName().equals("noobwood"))
				{
					return 400;
				}
			}
			if (item instanceof ItemSword)
			{
				if (((ItemSword)item).getToolMaterialName().equals("WOOD") ||
						((ItemSword)item).getToolMaterialName().equals("noobwood"))
				{
					return 400;
				}
			}
			if (item instanceof ItemHoe)
			{
				if (((ItemHoe)item).getToolMaterialName().equals("WOOD") ||
						((ItemHoe)item).getToolMaterialName().equals("noobwood"))
				{
					return 400;
				}
			}
			if (item == Items.stick)
			{
				return 200;
			}
			if (item == Items.coal)
			{
				return 1600;
			}
		}

		return 0;
	}

	public ItemStack stackFuel()
	{
		return stacks[SLOT_FUEL];
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return stacks[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount)
	{
		if (stacks[slot] != null)
		{
			ItemStack stack;
			if (stacks[slot].stackSize <= amount)
			{
				stack = stacks[slot];
				stacks[slot] = null;
				return stack;
			}
			else
			{
				stack = stacks[slot].splitStack(amount);

				if (stacks[slot].stackSize == 0)
				{
					stacks[slot] = null;
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
		if (stacks[slot] == null)
		{
			return null;
		}

		ItemStack stack = stacks[slot].copy();
		stacks[slot] = null;
		return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		stacks[slot] = stack;
	}

	@Override
	public String getInventoryName()
	{
		return hasCustomInventoryName() ? campfireName : "container.campfire";
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return campfireName != null && campfireName.length() > 0;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 16; //Not 64
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		if (worldObj.getTileEntity(xCoord, yCoord, zCoord) != this)
		{
			return false;
		}
		else
		{
			return player.getDistanceSq(xCoord + 0.5d, yCoord + 0.5d, zCoord + 0.5d) <= 64.0d;
		}
	}

	@Override
	public void openInventory()
	{
	}

	@Override
	public void closeInventory()
	{
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack)
	{
		return slot == SLOT_INPUT || slot == SLOT_PAN && isPan(stack) || slot == SLOT_FUEL && isItemFuel(stack);
	}

	private static boolean isPan(ItemStack stack)
	{
		if (stack == null)
		{
			return false;
		}

		return stack.getItem() instanceof ItemPan;
	}

	public static boolean isItemFuel(ItemStack itemstack1)
	{
		return getBurnTimeForFuel(itemstack1) > 0;
	}

	public void LightFuel()
	{
		int maxBurn = getBurnTimeForFuel(stackFuel());
		if (maxBurn > 0)
		{
			if (canCook())
			{
				burnTime = maxBurn;
				decayTime = maxDecayTime;
			}
		}
	}

	public boolean isBurning()
	{
		return burnTime > 0;
	}

	@SideOnly(Side.CLIENT)
	public int getCookProgressScaled(int i)
	{
		return cookTime * i / maxCookTime;
	}

	@SideOnly(Side.CLIENT)
	public int getBurnTimeRemainingScaled(int i)
	{
		if (currentItemBurnTime <= 0)
		{
			return 0;
		}

		return burnTime * i / currentItemBurnTime;
	}

	public int getDecayTimeRemainingScaled(int i)
	{
		return decayTime * i / maxDecayTime;
	}
}
