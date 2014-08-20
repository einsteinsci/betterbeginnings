package net.einsteinsci.betterbeginnings.inventory;

import cpw.mods.fml.common.FMLCommonHandler;
import net.einsteinsci.betterbeginnings.register.InfusionRepairUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;

/**
 * Created by einsteinsci on 8/20/2014.
 */
public class SlotInfusionRepairResult extends SlotCrafting
{
	IInventory inputSlots;
	EntityPlayer entPlayer;

	public SlotInfusionRepairResult(EntityPlayer player, IInventory inputs, IInventory output, int slotId, int xPos, int yPos)
	{
		super(player, inputs, output, slotId, xPos, yPos);

		inputSlots = inputs;
		entPlayer = player;
	}

	public void onPickupFromSlot(EntityPlayer player, ItemStack stack)
	{
		FMLCommonHandler.instance().firePlayerCraftingEvent(player, stack, inputSlots);
		onCrafting(stack);

		InventoryRepairTable inputs = (InventoryRepairTable)inputSlots;

		ArrayList<ItemStack> required = InfusionRepairUtil.getRequiredStacks(inputs);

		for (ItemStack requiredStack : required)
		{
			for (int i = 0; i < inputSlots.getSizeInventory(); ++i)
			{
				if (requiredStack.getItem() == inputSlots.getStackInSlot(i).getItem() &&
						(requiredStack.getItemDamage() == OreDictionary.WILDCARD_VALUE ||
								requiredStack.getItemDamage() == inputSlots.getStackInSlot(i).getItemDamage()))
				{
					inputSlots.decrStackSize(i, requiredStack.stackSize);

					ItemStack itemstack1 = inputSlots.getStackInSlot(i);

					if (itemstack1.getItem().hasContainerItem(itemstack1))
					{
						ItemStack containerStack = itemstack1.getItem().getContainerItem(itemstack1);

						if (containerStack != null && containerStack.isItemStackDamageable() && containerStack
								.getItemDamage() > containerStack.getMaxDamage())
						{
							MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(entPlayer, containerStack));
							continue;
						}

						if (!itemstack1.getItem()
								.doesContainerItemLeaveCraftingGrid(itemstack1) || !entPlayer.inventory
								.addItemStackToInventory(containerStack))
						{
							if (inputSlots.getStackInSlot(i) == null)
							{
								inputSlots.setInventorySlotContents(i, containerStack);
							}
							else
							{
								entPlayer.dropPlayerItemWithRandomChoice(containerStack, false);
							}
						}
					}

					if (!entPlayer.capabilities.isCreativeMode)
					{
						entPlayer.addExperienceLevel(-InfusionRepairUtil.getTakenLevels(inputs));
					}

					break;
				}
			}
		}

		inputSlots.setInventorySlotContents(0, null);
	}
}
