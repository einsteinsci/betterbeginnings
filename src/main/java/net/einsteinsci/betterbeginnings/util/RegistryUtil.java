package net.einsteinsci.betterbeginnings.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
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
}
