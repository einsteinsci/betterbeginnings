package net.einsteinsci.betterbeginnings.inventory;

import net.einsteinsci.betterbeginnings.blocks.BlockDoubleWorkbench;
import net.einsteinsci.betterbeginnings.register.RegisterBlocks;
import net.einsteinsci.betterbeginnings.register.recipe.AdvancedCraftingHandler;
import net.einsteinsci.betterbeginnings.register.recipe.AdvancedRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ContainerDoubleWorkbench extends Container
{
	/**
	 * The crafting matrix inventory (3x3).
	 */
	public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
	public InventoryWorkbenchAdditionalMaterials addedMats = new InventoryWorkbenchAdditionalMaterials(this, 4);
	public IInventory craftResult = new InventoryCraftResult();
	public World worldObj;
	public Slot[] matSlots = new Slot[4];
	public Slot resultSlot;

	private AdvancedRecipe lastAdvancedRecipe;

	private EntityPlayer openingPlayer;

	private BlockPos pos;

	public ContainerDoubleWorkbench(InventoryPlayer invPlayer, World world, BlockPos loc)
	{
		worldObj = world;
		pos = loc;

		final int OFFSET = 20;

		resultSlot = new SlotAdvancedCrafting(invPlayer.player, this, craftMatrix, craftResult, addedMats, 0,
			129 + OFFSET, 35);
		addSlotToContainer(resultSlot);
		int i;
		int j;

		// Matrix
		for (i = 0; i < 3; ++i)
		{
			for (j = 0; j < 3; ++j)
			{
				addSlotToContainer(new Slot(craftMatrix, j + i * 3, 35 + OFFSET + j * 18, 17 + i * 18));
			}
		}

		// Inventory
		for (i = 0; i < 3; ++i)
		{
			for (j = 0; j < 9; ++j)
			{
				addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + OFFSET + j * 18, 84 + i * 18));
			}
		}

		// Hotbar
		for (i = 0; i < 9; ++i)
		{
			addSlotToContainer(new Slot(invPlayer, i, 8 + OFFSET + i * 18, 142));
		}

		// Additional materials
		for (i = 0; i < 4; ++i)
		{
			matSlots[i] = new Slot(addedMats, i, 8 + OFFSET, 7 + i * 18);
			addSlotToContainer(matSlots[i]);

			onCraftMatrixChanged(craftMatrix);
		}
	}

	@Override
	public void detectAndSendChanges()
	{
		for (int i = 0; i < inventorySlots.size(); ++i)
		{
			ItemStack itemstack = ((Slot)inventorySlots.get(i)).getStack();
			ItemStack itemstack1 = (ItemStack)inventoryItemStacks.get(i);

			if (!ItemStack.areItemStacksEqual(itemstack1, itemstack))
			{
				itemstack1 = itemstack == null ? null : itemstack.copy();
				inventoryItemStacks.set(i, itemstack1);

				for (Object listItem : crafters)
				{
					ICrafting crafter = (ICrafting)listItem;
					crafter.sendSlotContents(this, i, itemstack1);
				}
			}
		}
	}

	/**
	 * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that. I
	 * am not touching this code.
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotId)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot)inventorySlots.get(slotId);

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
				if (AdvancedCraftingHandler.crafting().isAddedMaterial(itemstack1))
				{
					if (!mergeItemStack(itemstack1, 46, 50, false))
					{
						return null;
					}
				}
				else if (!mergeItemStack(itemstack1, 37, 46, false))
				{
					return null;
				}
			}
			else if (slotId >= 37 && slotId < 46)
			{
				if (AdvancedCraftingHandler.crafting().isAddedMaterial(itemstack1))
				{
					if (!mergeItemStack(itemstack1, 46, 50, false))
					{
						return null;
					}
				}
				else if (!mergeItemStack(itemstack1, 10, 37, false))
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
				slot.putStack(null);
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
	public boolean canMergeSlot(ItemStack stack, Slot slot)
	{
		return slot.inventory != craftResult && super.canMergeSlot(stack, slot);
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
			for (int i = 0; i < craftMatrix.getSizeInventory(); ++i)
			{
				ItemStack itemstack = craftMatrix.getStackInSlotOnClosing(i);

				if (itemstack != null)
				{
					player.dropPlayerItemWithRandomChoice(itemstack, false);
				}
			}

			for (int i = 0; i < addedMats.getSizeInventory(); ++i)
			{
				ItemStack itemstack = addedMats.getStackInSlotOnClosing(i);

				if (itemstack != null)
				{
					player.dropPlayerItemWithRandomChoice(itemstack, false);
				}
			}
		}
	}

	/**
	 * Callback for when the crafting matrix is changed. Detects if recipe is valid.
	 */
	@Override
	public void onCraftMatrixChanged(IInventory inventory)
	{
		boolean hasAddedMats = false;
		ItemStack result = AdvancedCraftingHandler.crafting()
			.findMatchingRecipeResult(craftMatrix, addedMats, worldObj);
		if (result != null)
		{
			lastAdvancedRecipe = AdvancedCraftingHandler.crafting().findMatchingRecipe(craftMatrix,
				addedMats, worldObj);
		}

		if (result == null)
		{
			result = CraftingManager.getInstance().findMatchingRecipe(craftMatrix, worldObj);
		}

		if (result != null)
		{
			hasAddedMats = true;
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

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return worldObj.getBlockState(pos).getBlock() == RegisterBlocks.doubleWorkbench &&
			worldObj.getBlockState(pos).getValue(BlockDoubleWorkbench.CONNECTION) != EnumFacing.UP &&
			player.getDistanceSq((double)pos.getX() + 0.5d, (double)pos.getY() + 0.5d,
				(double)pos.getZ() + 0.5d) <= 64.0D;
	}

	public EntityPlayer getOpeningPlayer()
	{
		return openingPlayer;
	}

	public AdvancedRecipe getLastAdvancedRecipe()
	{
		return lastAdvancedRecipe;
	}
}
