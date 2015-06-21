package net.einsteinsci.betterbeginnings.inventory;

import net.einsteinsci.betterbeginnings.register.InfusionRepairUtil;
import net.einsteinsci.betterbeginnings.register.RegisterBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class ContainerInfusionRepair extends Container
{
	public Slot[] circleSlots = new Slot[9];
	public Slot resultSlot;
	public InventoryInfusionRepair inputs = new InventoryInfusionRepair(this, 9);
	public IInventory output = new InventoryCraftResult();
	public World worldObj;
	EntityPlayer player;

	private BlockPos pos;

	public ContainerInfusionRepair(InventoryPlayer invPlayer, World world, BlockPos loc)
	{
		worldObj = world;
		pos = loc;

		player = invPlayer.player;

		circleSlots[0] = new Slot(inputs, 0, 44, 35);

		circleSlots[1] = new Slot(inputs, 1, 44, 6);
		circleSlots[2] = new Slot(inputs, 2, 67, 12);
		circleSlots[3] = new Slot(inputs, 3, 73, 35);
		circleSlots[4] = new Slot(inputs, 4, 67, 58);
		circleSlots[5] = new Slot(inputs, 5, 44, 63);
		circleSlots[6] = new Slot(inputs, 6, 21, 58);
		circleSlots[7] = new Slot(inputs, 7, 15, 35);
		circleSlots[8] = new Slot(inputs, 8, 21, 12);

		for (Slot slot : circleSlots)
		{
			addSlotToContainer(slot);
		}

		resultSlot = new SlotInfusionRepairResult(invPlayer.player, inputs, output, 9, 136, 35);
		addSlotToContainer(resultSlot);

		for (int i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int i = 0; i < 9; ++i)
		{
			addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 142));
		}

		onCraftMatrixChanged(inputs);
	}

	public void detectAndSendChanges()
	{
		if (InfusionRepairUtil.canRepair(inputs, player))
		{
			ItemStack result = inputs.getStackInSlot(0).copy();
			result.setItemDamage(0);
			output.setInventorySlotContents(0, result);
			//resultSlot.setBackgroundIcon(result.getItem().getIconFromDamage(0)); // TODO: Fix bg icons
		}
		else
		{
			output.setInventorySlotContents(0, null);
			//resultSlot.setBackgroundIcon(null);
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotId)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot)inventorySlots.get(slotId);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (slotId < 10)
			{
				if (!mergeItemStack(itemstack1, 10, 46, false))
				{
					return null;
				}
			}
			else if (slotId >= 10 && slotId <= 45)
			{
				if (itemstack != null)
				{
					if (itemstack.getItem().isDamageable())
					{
						if (!mergeItemStack(itemstack1, 0, 1, false))
						{
							return null;
						}
					}
					else if (!mergeItemStack(itemstack1, 1, 9, false))
					{
						return null;
					}
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
		return slot.inventory != output && super.canMergeSlot(stack, slot);
	}

	@Override
	public void onContainerClosed(EntityPlayer player)
	{
		//super.onContainerClosed(player);

		if (!worldObj.isRemote)
		{
			for (int i = 0; i < 9; ++i)
			{
				ItemStack itemstack = inputs.getStackInSlotOnClosing(i);

				if (itemstack != null)
				{
					player.dropPlayerItemWithRandomChoice(itemstack, false);
				}
			}
		}
	}

	@Override
	public void onCraftMatrixChanged(IInventory craftInv)
	{
		detectAndSendChanges();
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return worldObj.getBlockState(pos).getBlock() == RegisterBlocks.infusionRepairStation
				&& player.getDistanceSq((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D,
				                        (double)pos.getZ() + 0.5D) <= 64.0D;
	}
}
