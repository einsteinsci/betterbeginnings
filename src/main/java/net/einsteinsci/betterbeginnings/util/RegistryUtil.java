package net.einsteinsci.betterbeginnings.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RegistryUtil
{
	// fullID must be in the format "modid:unlocalizedName"
	public static Block getBlockFromRegistry(String fullID)
	{
		int colonAt = fullID.indexOf(":");
		if (colonAt == -1)
		{
			return null;
		}

		String modid = fullID.substring(0, colonAt);
		String name = fullID.substring(colonAt + 1);

		return GameRegistry.findBlock(modid, name);
	}

	public static Item getItemFromRegistry(String fullID)
	{
		int colonAt = fullID.indexOf(":");
		if (colonAt == -1)
		{
			return null;
		}

		String modid = fullID.substring(0, colonAt);
		String name = fullID.substring(colonAt + 1);

		return GameRegistry.findItem(modid, name);
	}

	public static String getForgeName(Item item)
	{
		return GameRegistry.findUniqueIdentifierFor(item).toString();
	}

	public static String getForgeName(Block block)
	{
		return GameRegistry.findUniqueIdentifierFor(block).toString();
	}

	public static String getForgeName(ItemStack stack)
	{
		if (stack == null)
		{
			return null;
		}

		if (stack.getItem() instanceof ItemBlock)
		{
			Block b = Block.getBlockFromItem(stack.getItem());
			return getForgeName(b);
		}

		return getForgeName(stack.getItem());
	}
}
