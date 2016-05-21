package net.einsteinsci.betterbeginnings.config.json.recipe;

import net.einsteinsci.betterbeginnings.config.json.JsonLoadedItemStack;
import net.einsteinsci.betterbeginnings.util.InfusionRepairUtil;
import net.minecraft.item.ItemStack;

public class JsonRepairInfusionAssociation
{
	private int enchantmentID;
	private JsonLoadedItemStack associatedItem;

	public JsonRepairInfusionAssociation(int enchID, ItemStack stack)
	{
		enchantmentID = enchID;
		associatedItem = new JsonLoadedItemStack(stack);
	}

	public void register()
	{
		InfusionRepairUtil.registerEnchantment(enchantmentID, associatedItem.getFirstItemStackOrNull());
	}

	public int getEnchantmentID()
	{
		return enchantmentID;
	}

	public JsonLoadedItemStack getAssociatedItem()
	{
		return associatedItem;
	}
}
