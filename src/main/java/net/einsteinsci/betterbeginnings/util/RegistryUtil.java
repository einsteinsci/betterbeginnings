package net.einsteinsci.betterbeginnings.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Level;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

	public static String getModOwner(Item item)
	{
		String forgeName = getForgeName(item);

		String res = "";
		for (char c : forgeName.toCharArray())
		{
			if (c == ':')
			{
				break;
			}

			res += c;
		}

		return res;
	}

	public static String getCommonOreDictName(List<ItemStack> items)
	{
		List<String> commonNames = null;

		for (ItemStack is : items)
		{
			if (commonNames == null)
			{
				commonNames = getOreNames(is);
			}
			else
			{
				List<String> compared = getOreNames(is);

				Iterator<String> iter = commonNames.iterator();
				while (iter.hasNext())
				{
					String s = iter.next();

					if (!compared.contains(s))
					{
						iter.remove();
					}
				}

				if (commonNames.isEmpty())
				{
					return null;
				}
			}
		}

		if (commonNames == null || commonNames.isEmpty())
		{
			return null;
		}

		return commonNames.get(0);
	}

	public static List<String> getOreNames(ItemStack item)
	{
		List<String> res = new ArrayList<>();
		for (String ore : OreDictionary.getOreNames())
		{
			if (OreDictionary.getOres(ore).contains(item))
			{
				res.add(ore);
			}
		}

		return res;
	}

	// Kinda sketchy
	public static Tuple getRecipeCharacter(Map<Object, Character> map, Object obj, Character current)
	{
		boolean res = false;
		Character token = '0';

		if (obj instanceof ItemStack)
		{
			ItemStack stack = (ItemStack)obj;

			boolean found = false;
			for (Map.Entry<Object, Character> kvp : map.entrySet())
			{
				if (kvp.getKey() instanceof ItemStack)
				{
					ItemStack k = (ItemStack)kvp.getKey();
					if (Util.areItemStacksEqualIgnoreSize(k, stack))
					{
						token = kvp.getValue();
						found = true;
						break;
					}
				}
			}

			if (!found)
			{
				map.put(stack, token);
				res = true;
			}
		}
		else if (obj instanceof List)
		{
			String ore = null;
			try
			{
				ore = RegistryUtil.getCommonOreDictName((List<ItemStack>)obj);
			}
			catch (ClassCastException ex)
			{
				LogUtil.log(Level.ERROR, "Failed to cast list in ore dictionary conversion: " + ex.toString());
			}

			if (ore != null)
			{
				if (map.containsKey(ore))
				{
					token = map.get(ore);
				}
				else
				{
					map.put(ore, current);
					res = true;
				}
			}
		}

		return new Tuple(res, token);
	}
}
