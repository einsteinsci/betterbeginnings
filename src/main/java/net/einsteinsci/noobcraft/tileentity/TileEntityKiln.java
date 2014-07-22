package net.einsteinsci.noobcraft.tileentity;

import net.einsteinsci.noobcraft.blocks.BlockKiln;
import net.einsteinsci.noobcraft.register.KilnRecipes;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityKiln extends TileEntity implements ISidedInventory
{
	private static final int[] slotsTop = new int[]{0};
	private static final int[] slotsBottom = new int[]{2, 1};
	private static final int[] slotsSides = new int[]{1};
	
	public static final int smeltTime = 250;
	
	private ItemStack[] kilnStacks = new ItemStack[3];
	
	public int kilnBurnTime;
	public int currentBurnTime;
	
	public int kilnCookTime;
	
	private String kilnName;
	
	public TileEntityKiln()
	{
		super();
	}
	
	public void furnaceName(String string){
		this.kilnName = string;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
	   super.writeToNBT(tagCompound);

	   tagCompound.setShort("BurnTime", (short)kilnBurnTime);
	   tagCompound.setShort("CookTime", (short)kilnCookTime);
	   NBTTagList tagList = new NBTTagList();
	   
	   for (int i = 0; i < this.kilnStacks.length; ++i)
	   {
		   if (this.kilnStacks[i] != null)
		   {
			   NBTTagCompound itemTag = new NBTTagCompound();
			   this.kilnStacks[i].writeToNBT(itemTag);
			   itemTag.setByte("Slot", (byte)i);
			   tagList.appendTag(itemTag);
		   }
	   }
	   
	   tagCompound.setTag("Items", tagList);
	   if (this.hasCustomInventoryName())
	   {
		   tagCompound.setString("CustomName", this.kilnName);
	   }
	}
	
	@SideOnly(Side.CLIENT)
	public int getCookProgressScaled(int progress)
	{
		return this.kilnCookTime * progress / smeltTime;
	}
	
	@SideOnly(Side.CLIENT)
	public int getBurnTimeRemainingScaled(int time)
	{
		if (this.currentBurnTime == 0)
		{
			this.currentBurnTime = smeltTime;
		}
		
		return this.currentBurnTime * time / smeltTime;
	}
	
	public boolean isBurning()
	{
		return this.kilnBurnTime > 0;
	}
	
	@Override
	public void updateEntity()
	{
		boolean flag = this.kilnBurnTime > 0;
		boolean flag1 = false;
		
		if (this.kilnBurnTime > 0)
		{
			--this.kilnBurnTime;
		}
		
		if (!this.worldObj.isRemote)
		{
			if (this.kilnBurnTime == 0 && this.canSmelt())
			{
				this.currentBurnTime = this.kilnBurnTime = getItemBurnTime(this.kilnStacks[1]);
				
				if (this.kilnBurnTime > 0)
				{
					flag1 = true;
					if (this.kilnStacks[1] != null)
					{
						--this.kilnStacks[1].stackSize;
						
						if (this.kilnStacks[1].stackSize == 0)
						{
							kilnStacks[1] = kilnStacks[1].getItem().getContainerItem(kilnStacks[1]);
						}
					}
				}
			}
			
			if (this.isBurning() && this.canSmelt())
			{
				++this.kilnCookTime;
				if (this.kilnCookTime == smeltTime)
				{
					this.kilnCookTime = 0;
					this.smeltItem();
					flag1 = true;
				}
			}
			else
			{
				this.kilnCookTime = 0;
			}
		}
		
		if (flag != this.kilnBurnTime > 0)
		{
			flag1 = true;
			BlockKiln.updateBlockState(this.kilnBurnTime > 0, worldObj, this.xCoord, this.yCoord, this.zCoord);
		}
		
		if (flag1)
		{
			this.markDirty();
		}
	}
	
	private boolean canSmelt()
	{
		if (this.kilnStacks[0] == null)
		{
			return false;
		}
		else
		{
			ItemStack stack = KilnRecipes.smelting().getSmeltingResult(this.kilnStacks[0]);
			if (stack == null)
			{
				return false;
			}
			
			if (this.kilnStacks[2] == null)
			{
				return true;
			}
			if (!this.kilnStacks[2].isItemEqual(stack))
			{
				return false;
			}
			
			int result = kilnStacks[2].stackSize + stack.stackSize;
			return result <= getInventoryStackLimit() && result <= this.kilnStacks[2].getMaxStackSize();
		}
	}
	
	public void smeltItem()
	{
		if (this.canSmelt())
		{
			ItemStack itemStack = KilnRecipes.smelting().getSmeltingResult(this.kilnStacks[0]);
			
			if (this.kilnStacks[2] == null)
			{
				this.kilnStacks[2] = itemStack.copy();
			}
			else if (this.kilnStacks[2].getItem() == itemStack.getItem())
			{
				this.kilnStacks[2].stackSize += itemStack.stackSize;
			}
			
			--kilnStacks[0].stackSize;
			
			if (kilnStacks[0].stackSize <= 0)
			{
				kilnStacks[0] = null;
			}
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
				
				//Insert any additional block fuels here
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
                
                //INFINITE POWER!!!
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
            	if (((ItemHoe)item).getToolMaterialName().equals("WOOD") ||
						((ItemHoe)item).getToolMaterialName().equals("noobwood"))
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
			
			//Blaze Rods and Lava are invalid fuel sources for a kiln.
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
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
	   super.readFromNBT(tagCompound);
	   
	   //ItemStacks
	   NBTTagList tagList = tagCompound.getTagList("Items", 10);
	   this.kilnStacks = new ItemStack[this.getSizeInventory()];
	   
	   for (int i = 0; i < tagList.tagCount(); ++i)
	   {
		   NBTTagCompound itemTag = tagList.getCompoundTagAt(i);
		   byte slot = itemTag.getByte("Slot");
		   
		   if (slot >= 0 && slot < this.kilnStacks.length)
		   {
			   this.kilnStacks[slot] = ItemStack.loadItemStackFromNBT(itemTag);
		   }
	   }
	   
	   //Burn Time & Cook Time
	   this.kilnBurnTime = tagCompound.getShort("BurnTime");
	   this.kilnCookTime = tagCompound.getShort("CookTime");
	   this.currentBurnTime = getItemBurnTime(this.kilnStacks[1]);
	   
	   if (tagCompound.hasKey("CustomName", 8))
	   {
		   this.kilnName = tagCompound.getString("CustomName");
	   }
	}

	@Override
	public ItemStack getStackInSlot(int i) 
	{
		return this.kilnStacks[i];
	}

	@Override
	public int getSizeInventory() 
	{
		return this.kilnStacks.length;
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (this.kilnStacks[slot] != null)
		{
			ItemStack stack;
			if (this.kilnStacks[slot].stackSize <= amount)
			{
				stack = this.kilnStacks[slot];
				this.kilnStacks[slot] = null;
				return stack;
			}
			else
			{
				stack = this.kilnStacks[slot].splitStack(amount);
				
				if (this.kilnStacks[slot].stackSize == 0)
				{
					this.kilnStacks[slot] = null;
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
		if (this.kilnStacks[slot] != null)
		{
			ItemStack stack = this.kilnStacks[slot];
			this.kilnStacks[slot] = null;
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
		this.kilnStacks[slot] = stack;
		
		if (stack != null && stack.stackSize > this.getInventoryStackLimit())
		{
			stack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public String getInventoryName() 
	{
		return this.hasCustomInventoryName() ? this.kilnName : "Kiln";
	}

	@Override
	public boolean hasCustomInventoryName() 
	{
		return this.kilnName != null && this.kilnName.length() > 0;
	}

	@Override
	public int getInventoryStackLimit() 
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) 
	{
		if (this.worldObj.getTileEntity(xCoord, yCoord, zCoord) != this)
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
		return slot == 2 ? false : (slot == 1 ? isItemFuel(stack) : true);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) 
	{
		return side == 0 ? slotsBottom : (side == 1 ? slotsTop : slotsSides);
	}

	@Override
	public boolean canInsertItem(int par1, ItemStack stack,	int par3) 
	{
		return this.isItemValidForSlot(par1, stack);
	}

	@Override
	public boolean canExtractItem(int par1, ItemStack stack, int par3) 
	{
		return par3 != 0 || par1 != 1 || stack.getItem() == Items.bucket;
	}
}
