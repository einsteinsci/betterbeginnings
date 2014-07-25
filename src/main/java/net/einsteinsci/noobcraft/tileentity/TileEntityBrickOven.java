package net.einsteinsci.noobcraft.tileentity;

import net.einsteinsci.noobcraft.blocks.BlockBrickOven;
import net.einsteinsci.noobcraft.register.recipe.BrickOvenRecipeHandler;
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
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityBrickOven extends TileEntity implements IInventory
{
	public static final int FUEL = 0;
	public static final int OUTPUT = 1;
	public static final int INPUTSTART = 2;
	// private static final int[] slotsTop = new int[] { 0 };
	// private static final int[] slotsBottom = new int[] { 2, 1 };
	// private static final int[] slotsSides = new int[] { 1 };
	
	public static final int COOKTIME = 100;
	
	private ItemStack[] ovenStacks = new ItemStack[11];
	
	public int ovenBurnTime;
	public int currentItemBurnLength;
	
	public int ovenCookTime;
	
	// public ContainerBrickOven container;
	
	private String ovenName;
	
	public TileEntityBrickOven()
	{
		super();
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
		if (hasCustomInventoryName())
		{
			tagCompound.setString("CustomName", ovenName);
		}
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
	
	// public InventoryBrickOvenMatrix getCraftMatrix()
	// {
	// InventoryBrickOvenMatrix matrix = new InventoryBrickOvenMatrix(c, 3, 3);
	//
	// for (int i = 0; i < 3; ++i)
	// {
	// for (int j = 0; j < 3; ++j)
	// {
	// int slot = j + i * 3;
	// matrix.setInventorySlotContents(slot, ovenStacks[slot]);
	// }
	// }
	//
	// return matrix;
	// }
	
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
	
	@Override
	public void updateEntity()
	{
		boolean flag = ovenBurnTime > 0;
		boolean flag1 = false;
		
		if (ovenBurnTime > 0)
		{
			--ovenBurnTime;
		}
		
		if (!worldObj.isRemote)
		{
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
		}
		
		if (flag != ovenBurnTime > 0)
		{
			flag1 = true;
			BlockBrickOven.updateBlockState(ovenBurnTime > 0, worldObj, xCoord, yCoord, zCoord);
		}
		
		if (flag1)
		{
			markDirty();
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
			ItemStack stack = BrickOvenRecipeHandler.instance().findMatchingRecipe(this, worldObj);
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
	
	public void smeltItem()
	{
		if (canSmelt())
		{
			ItemStack itemStack = BrickOvenRecipeHandler.instance().findMatchingRecipe(this, worldObj);
			
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
					--ovenStacks[i].stackSize;
					
					if (ovenStacks[i].stackSize <= 0)
					{
						ovenStacks[i] = null;
					}
				}
			}
			
			// --ovenStacks[0].stackSize;
			
			// if (ovenStacks[0].stackSize <= 0)
			// {
			// ovenStacks[0] = null;
			// }
		}
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
					return 300;
				}
				
				if (block.getMaterial() == Material.wood)
				{
					return 600;
				}
				
				if (block == Blocks.coal_block)
				{
					return 32000;
				}
				
				// VERY LARGE AMOUNT OF POWER!!!
				if (block == Blocks.bedrock)
				{
					return Short.MAX_VALUE;
				}
			}
			
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
				return 3200;
			}
			if (item == Item.getItemFromBlock(Blocks.sapling))
			{
				return 200;
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
	
	public static boolean isItemFuel(ItemStack itemStack)
	{
		return getItemBurnTime(itemStack) > 0;
	}
	
	public boolean isBurning()
	{
		return ovenBurnTime > 0;
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
	public int getInventoryStackLimit()
	{
		return 64;
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
	public boolean hasCustomInventoryName()
	{
		return ovenName != null && ovenName.length() > 0;
	}
	
	@Override
	public String getInventoryName()
	{
		return hasCustomInventoryName() ? ovenName : "container.brickoven";
	}
	
	public void furnaceName(String displayName)
	{
		ovenName = displayName;
	}
	
	@Override
	public int getSizeInventory()
	{
		return ovenStacks.length;
	}
	
	@Override
	public ItemStack getStackInSlot(int i)
	{
		return ovenStacks[i];
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
	public boolean isItemValidForSlot(int slot, ItemStack stack)
	{
		return slot == OUTPUT ? false : slot == FUEL ? isItemFuel(stack) : true;
	}
	
	// @Override
	// public int[] getAccessibleSlotsFromSide(int side)
	// {
	// return side == 0 ? slotsBottom : side == 1 ? slotsTop : slotsSides;
	// }
	
	// @Override
	public boolean canInsertItem(int par1, ItemStack stack, int par3)
	{
		return isItemValidForSlot(par1, stack);
	}
	
	// @Override
	public boolean canExtractItem(int par1, ItemStack stack, int par3)
	{
		return par3 != 0 || par1 != 1 || stack.getItem() == Items.bucket;
	}
	
	@Override
	public void openInventory()
	{
		
	}
	
	@Override
	public void closeInventory()
	{
		
	}
	
	public ItemStack getStackInRowAndColumn(int row, int column)
	{
		return getStackInSlot(INPUTSTART + row + column * 3);
	}
}



















// Buffer
