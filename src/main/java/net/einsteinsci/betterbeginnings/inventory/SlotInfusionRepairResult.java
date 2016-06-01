package net.einsteinsci.betterbeginnings.inventory;

import net.einsteinsci.betterbeginnings.register.recipe.OreRecipeElement;
import net.einsteinsci.betterbeginnings.util.InfusionRepairUtil;
import net.einsteinsci.betterbeginnings.register.RegisterItems;
import net.einsteinsci.betterbeginnings.register.achievement.RegisterAchievements;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;

public class SlotInfusionRepairResult extends Slot
{
	IInventory inputSlots;
	EntityPlayer player;
	IInventory outputSlot;

	public SlotInfusionRepairResult(EntityPlayer entityPlayer, InventoryInfusionRepair inputs, IInventory output,
	                                int slotId, int xPos, int yPos)
	{
		super(inputs, slotId, xPos, yPos);
		player = entityPlayer;
		outputSlot = output;

		inputSlots = inputs;
	}

	public void onPickupFromSlot(EntityPlayer entityPlayer, ItemStack stack)
	{
		onCrafting(stack);

		InventoryInfusionRepair inputs = (InventoryInfusionRepair)inputSlots;

		ArrayList<OreRecipeElement> required = InfusionRepairUtil.getRequiredStacks(inputs);

		for (OreRecipeElement requiredStack : required)
		{
			for (int i = 0; i < inputSlots.getSizeInventory(); ++i)
			{
				if (requiredStack != null && inputSlots.getStackInSlot(i) != null)
				{
					if (requiredStack.matches(inputSlots.getStackInSlot(i)))
					{
						inputSlots.decrStackSize(i, requiredStack.stackSize);

						ItemStack itemstack1 = inputSlots.getStackInSlot(i);

						if (itemstack1 != null)
						{
							if (itemstack1.getItem().hasContainerItem(itemstack1))
							{
								ItemStack containerStack = itemstack1.getItem().getContainerItem(itemstack1);

								if (containerStack != null && containerStack.isItemStackDamageable() && containerStack
										.getItemDamage() > containerStack.getMaxDamage())
								{
									MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(entityPlayer, containerStack));
									continue;
								}

								//if (!itemstack1.getItem().doesContainerItemLeaveCraftingGrid(itemstack1) ||
								//		!entityPlayer.inventory.addItemStackToInventory(containerStack))
								//{
								//	if (inputSlots.getStackInSlot(i) == null)
								//	{
								//		inputSlots.setInventorySlotContents(i, containerStack);
								//	}
								//	else
								//	{
								//		entityPlayer.dropPlayerItemWithRandomChoice(containerStack, false);
								//	}
								//}
							}
						}

						break;
					}
				}
			}
		}

		if (!entityPlayer.capabilities.isCreativeMode)
		{
			entityPlayer.addExperienceLevel(-InfusionRepairUtil.getTakenLevels(inputs));
		}

		inputSlots.setInventorySlotContents(0, null);

		if (stack != null)
		{
			if (stack.getItem() == RegisterItems.noobWoodSword)
			{
				RegisterAchievements.achievementGet(entityPlayer, "repairNoobSword");
			}

			FMLCommonHandler.instance().firePlayerCraftingEvent(entityPlayer, stack, inputSlots);
		}
	}
}
