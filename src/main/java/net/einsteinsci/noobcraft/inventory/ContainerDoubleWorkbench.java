package net.einsteinsci.noobcraft.inventory;

import net.einsteinsci.noobcraft.register.RegisterBlocks;
import net.einsteinsci.noobcraft.register.recipe.AdvancedCraftingHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;

public class ContainerDoubleWorkbench extends Container
{
	/** The crafting matrix inventory (3x3). */
	public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
	public InventoryWorkbenchAdditionalMaterials addedMats = new InventoryWorkbenchAdditionalMaterials(this,
		4);
	public IInventory craftResult = new InventoryCraftResult();
	private World worldObj;
	private int posX;
	private int posY;
	private int posZ;
	
	public ContainerDoubleWorkbench(InventoryPlayer invPlayer, World world, int x, int y, int z)
	{
		worldObj = world;
		posX = x;
		posY = y;
		posZ = z;
		addSlotToContainer(new SlotAdvancedCrafting(invPlayer.player, craftMatrix, craftResult, addedMats, 0,
			129, 35));
		int l;
		int i1;
		
		// Matrix
		for (l = 0; l < 3; ++l)
		{
			for (i1 = 0; i1 < 3; ++i1)
			{
				addSlotToContainer(new Slot(craftMatrix, i1 + l * 3, 35 + i1 * 18, 17 + l * 18));
			}
		}
		
		// Inventory
		for (l = 0; l < 3; ++l)
		{
			for (i1 = 0; i1 < 9; ++i1)
			{
				addSlotToContainer(new Slot(invPlayer, i1 + l * 9 + 9, 8 + i1 * 18, 84 + l * 18));
			}
		}
		
		// Hotbar
		for (l = 0; l < 9; ++l)
		{
			addSlotToContainer(new Slot(invPlayer, l, 8 + l * 18, 142));
		}
		
		// Additional materials
		for (l = 0; l < 4; ++l)
		{
			addSlotToContainer(new Slot(addedMats, l, 8, 7 + l * 18));
			
			onCraftMatrixChanged(craftMatrix);
		}
	}
	
	/**
	 * Callback for when the crafting matrix is changed. Detects if recipe is
	 * valid.
	 */
	@Override
	public void onCraftMatrixChanged(IInventory inventory)
	{
		boolean hasAddedMats = false;
		ItemStack result = AdvancedCraftingHandler.crafting().findMatchingRecipe(craftMatrix, addedMats,
			worldObj);
		
		if (result == null)
		{
			result = CraftingManager.getInstance().findMatchingRecipe(craftMatrix, worldObj);
		}
		
		if (result != null)
		{
			hasAddedMats = true;
			
			if (result.getItem() == Items.diamond_hoe)
			{
				int wtf = 0;
			}
		}
		
		if (hasAddedMats)
		{
			craftResult.setInventorySlotContents(0, result);
		}
		else
		{
			craftResult.setInventorySlotContents(0, null);
		}
	}
	
	/**
	 * Called when the container is closed.
	 */
	@Override
	public void onContainerClosed(EntityPlayer player)
	{
		super.onContainerClosed(player);
		
		if (!worldObj.isRemote)
		{
			for (int i = 0; i < 9; ++i)
			{
				ItemStack itemstack = craftMatrix.getStackInSlotOnClosing(i);
				
				if (itemstack != null)
				{
					player.dropPlayerItemWithRandomChoice(itemstack, false);
				}
			}
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		// return worldObj.getBlock(posX, posY, posZ) !=
		// RegisterBlocks.blockDoubleWorkbench ? false : player.getDistanceSq(
		// posX + 0.5D, posY + 0.5D, posZ + 0.5D) <= 64.0D;
		
		return worldObj.getBlock(posX, posY, posZ) == RegisterBlocks.blockDoubleWorkbench
			&& worldObj.getBlockMetadata(posX, posY, posZ) != 0
			&& player.getDistanceSq(posX + 0.5D, posY + 0.5D, posZ + 0.5D) <= 64.0D;
	}
	
	/**
	 * Called when a player shift-clicks on a slot. You must override this or
	 * you will crash when someone does that. I am not touching this code.
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotId)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot) inventorySlots.get(slotId);
		
		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if (slotId == 0)
			{
				if (!mergeItemStack(itemstack1, 10, 46, true))
				{
					return null;
				}
				
				slot.onSlotChange(itemstack1, itemstack);
			}
			else if (slotId >= 10 && slotId < 37)
			{
				if (!mergeItemStack(itemstack1, 37, 46, false))
				{
					return null;
				}
			}
			else if (slotId >= 37 && slotId < 46)
			{
				if (!mergeItemStack(itemstack1, 10, 37, false))
				{
					return null;
				}
			}
			else if (!mergeItemStack(itemstack1, 10, 46, false))
			{
				return null;
			}
			
			if (itemstack1.stackSize == 0)
			{
				slot.putStack((ItemStack) null);
			}
			else
			{
				slot.onSlotChanged();
			}
			
			if (itemstack1.stackSize == itemstack.stackSize)
			{
				return null;
			}
			
			slot.onPickupFromSlot(player, itemstack1);
		}
		
		return itemstack;
	}
	
	// Can Stack Enter? (I think)
	@Override
	public boolean func_94530_a(ItemStack stack, Slot slot)
	{
		return slot.inventory != craftResult && super.func_94530_a(stack, slot);
	}
}
