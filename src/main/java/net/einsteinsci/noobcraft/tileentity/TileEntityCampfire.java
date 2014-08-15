package net.einsteinsci.noobcraft.tileentity;

import net.einsteinsci.noobcraft.blocks.BlockCampfire;
import net.einsteinsci.noobcraft.config.NoobcraftConfig;
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
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.registry.GameRegistry;

public class TileEntityCampfire extends TileEntity implements ISidedInventory
{

	public String localizedName;
	public int burnTime;
	public int kindleTime;
	/** Campfire Speed */
	private int campfireSpeed = NoobcraftConfig.campfireCookTime;
	
	private static final int[] slots_top = new int[]{0};
	private static final int[] slots_bottom = new int[]{3};
	private static final int[] slots_sides = new int[]{2, 1};
	
	public ItemStack[] slots = new ItemStack[4];
	
	/** The start time for this item */
	public int currentItemBurnTime;
	
	/** How long time left before cooked */
	public int cookTime;
	
	public int getSizeInventory() {
		return this.slots.length;
	}

	
	public ItemStack getStackInSlot(int i) {
		return this.slots[i];
	}

	
	public ItemStack decrStackSize(int i, int j) {
		if(this.slots[i] != null){
			ItemStack itemstack;
			if(this.slots[i].stackSize <= j){
				itemstack = this.slots[i];
				this.slots[i] = null;
				return itemstack;
			}
			else{
				itemstack = this.slots[i].splitStack(j);
				if(this.slots[i].stackSize == 0){
					this.slots[i] = null;
				}
				return itemstack;
			}
		}
		
		return null;
	}

	
	public ItemStack getStackInSlotOnClosing(int i) {
		if(this.slots[i] != null){
			ItemStack itemstack = this.slots[i];
			this.slots[i] = null;
			return itemstack;
		}
		return null;
	}

	
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		this.slots[i] = itemstack;
		if(itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()){
			itemstack.stackSize = this.getInventoryStackLimit();
		}
	}

	
	public String getInventoryName() {
		return null;
	}

	
	public boolean hasCustomInventoryName() {
		return false;
	}

	
	public int getInventoryStackLimit() {
		return 0;
	}

	public boolean isUseableByPlayer(EntityPlayer player) {
		if (worldObj.getTileEntity(xCoord, yCoord, zCoord) != this)
		{
			return false;
		}
		else
		{
			return (player.getDistanceSq(xCoord + 0.5d, yCoord + 0.5d, zCoord + 0.5d) <= 64.0d);
		}
	}

	
	public void openInventory() {
		
	}

	
	public void closeInventory() {	
		
	}
	
	public void updateEntity(){
		boolean flag = this.burnTime > 0;
		boolean flag1 = false;
		
		if(burnTime > 0){
			this.burnTime--;
		}
		if(!this.worldObj.isRemote){
			if(this.burnTime == 0 && this.canCook()){
				this.currentItemBurnTime = this.burnTime = getItemBurnTime(this.slots[2]);
				
				if(this.burnTime > 0){
					flag1 = true;
					
					if(this.slots[2] != null){
						this.slots[2].stackSize--;
						
						if(this.slots[2].stackSize == 0){
							this.slots[2] = this.slots[2].getItem().getContainerItem(this.slots[2]);
						}
					}
				}
			if(this.isBurning() && this.canCook()){
				this.cookTime++;
				
				if(this.cookTime == this.campfireSpeed){
					this.cookTime = 0;
					this.cookItem();
					flag1 = true;
				}
			}else{
				this.cookTime = 0;
			}
			
			}
			
			if(flag != this.burnTime > 0){
				flag1 = true;
				BlockCampfire.updateCampfireState(this.burnTime > 0, this.kindleTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
			}
		}
		
		
		if(flag1){
			markDirty();
		}
		
	}

	
	private void cookItem() {
		if(this.canCook()){
			ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.slots[0]);
			
			if(this.slots[3] == null){
				this.slots[3] = itemstack.copy();
			}else if(this.slots[3].equals(itemstack)){
				this.slots[3].stackSize += itemstack.stackSize;
			}
			
			this.slots[0].stackSize--;
			if(this.slots[0].stackSize <= 0){
				this.slots[0] = null;
			}
		}
		
	}


	private boolean canCook() {
		if(this.slots[0] == null || this.slots[1] == null){
			return false;
		}else{
			ItemStack itemStack = FurnaceRecipes.smelting().getSmeltingResult(this.slots[0]);
			
			if(itemStack == null) return false;
			if(this.slots[3] == null) return true;
			if(!this.slots[3].isItemEqual(itemStack)) return false;
			
			int result = this.slots[3].stackSize + itemStack.stackSize;
			
			return (result <= getInventoryStackLimit() && result <= itemStack.getMaxStackSize());
		}
		
	}


	public boolean isItemValidForSlot(int i, ItemStack itemStack) {	
		return i == 2 ? true : (i == 1 ? isItemFuel(itemStack) : true) ;
	}

	
	public static boolean isItemFuel(ItemStack itemStack) {
		return getItemBurnTime(itemStack) > 0;
	}


	private static int getItemBurnTime(ItemStack itemStack)
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
					return 0;
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
				return 0;
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


	public int[] getAccessibleSlotsFromSide(int var1) {	
		return var1 == 0 ? slots_bottom : (var1 == 1 ? slots_top : slots_sides);
	}

	
	public boolean canInsertItem(int i, ItemStack itemStack, int j) {	
		return this.isItemValidForSlot(i, itemStack);
	}

	
	public boolean canExtractItem(int i, ItemStack itemStack, int j) {
		return i == 4;
	}

	public boolean isBurning() {
		return this.burnTime > 0;
	}
	
	public boolean isKindling() {
		return this.kindleTime > 0;
	}

	public boolean isInvNameLocalized() {
		return true;
	}




	public int getCookProgressScaled(int i) {
		// TODO Auto-generated method stub
		return 0;
	}


	public int getBurnTimeRemainingScaled(int i) {
		// TODO Auto-generated method stub
		return 0;
	}


}



















// Buffer
