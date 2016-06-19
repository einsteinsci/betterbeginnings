package net.einsteinsci.betterbeginnings.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.lang.reflect.Field;
import java.util.List;

// General-purpose Util class
public class Util
{
	public static boolean areItemStacksEqualIgnoreSize(ItemStack template, ItemStack tested)
	{
		if (template == null)
		{
			return tested == null;
		}
		else if (tested == null)
		{
			return false;
		}

		return template.getItem() == tested.getItem() && (template.getMetadata() == tested.getMetadata() ||
			template.getMetadata() == OreDictionary.WILDCARD_VALUE);
	}

	public static <TObject, TField> TField getPrivateVariable(TObject obj, String fieldName)
	{
		Field privateStringField = null;
		try
		{
			privateStringField = obj.getClass().getDeclaredField(fieldName);
		}
		catch (NoSuchFieldException e)
		{
			return null;
		}
		privateStringField.setAccessible(true);

		try
		{
			return (TField)privateStringField.get(obj);
		}
		catch (IllegalAccessException e)
		{
			return null;
		}
	}

	public static boolean listContainsItemStackIgnoreSize(List<ItemStack> list, ItemStack stack)
	{
		for (ItemStack s : list)
		{
			if (areItemStacksEqualIgnoreSize(s, stack))
			{
				return true;
			}
		}

		return false;
	}
}
