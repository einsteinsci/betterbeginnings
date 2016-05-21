package net.einsteinsci.betterbeginnings.register.recipe;

import net.einsteinsci.betterbeginnings.tileentity.TileEntityBrickOven;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

public class BrickOvenRecipeHandler
{
	private static final BrickOvenRecipeHandler INSTANCE = new BrickOvenRecipeHandler();
	protected List<IBrickOvenRecipe> recipes = new ArrayList<IBrickOvenRecipe>();

	public BrickOvenRecipeHandler()
	{ }

	public static void addShapedRecipe(ItemStack output, Object... args)
	{
		instance().putShapedRecipe(output, args);
	}

	public BrickOvenShapedRecipe putShapedRecipe(ItemStack result, Object... args)
	{
		String s = "";
		int i = 0;
		int j = 0;
		int k = 0;

		if (args[i] instanceof String[])
		{
			String[] astring = (String[])args[i++];

			for (int l = 0; l < astring.length; ++l)
			{
				String s1 = astring[l];
				++k;
				j = s1.length();
				s = s + s1;
			}
		}
		else
		{
			while (args[i] instanceof String)
			{
				String s2 = (String)args[i++];
				++k;
				j = s2.length();
				s = s + s2;
			}
		}

		HashMap hashmap;

		for (hashmap = new HashMap(); i < args.length; i += 2)
		{
			Character character = (Character)args[i];
			ItemStack itemstack1 = null;

			if (args[i + 1] instanceof Item)
			{
				itemstack1 = new ItemStack((Item)args[i + 1]);
			}
			else if (args[i + 1] instanceof Block)
			{
				itemstack1 = new ItemStack((Block)args[i + 1], 1, OreDictionary.WILDCARD_VALUE);
			}
			else if (args[i + 1] instanceof ItemStack)
			{
				itemstack1 = (ItemStack)args[i + 1];
			}

			hashmap.put(character, itemstack1);
		}

		ItemStack[] aitemstack = new ItemStack[j * k];

		for (int i1 = 0; i1 < j * k; ++i1)
		{
			char c0 = s.charAt(i1);

			if (hashmap.containsKey(Character.valueOf(c0)))
			{
				aitemstack[i1] = ((ItemStack)hashmap.get(Character.valueOf(c0))).copy();
			}
			else
			{
				aitemstack[i1] = null;
			}
		}

		BrickOvenShapedRecipe ovenrecipe = new BrickOvenShapedRecipe(j, k, aitemstack, result);
		recipes.add(ovenrecipe);
		return ovenrecipe;
	}

	public static BrickOvenRecipeHandler instance()
	{
		return INSTANCE;
	}

	public static void addShapelessRecipe(ItemStack output, Object... args)
	{
		instance().putShapelessRecipe(output, args);
	}

	public BrickOvenShapelessRecipe putShapelessRecipe(ItemStack output, Object... args)
	{
		ArrayList arraylist = new ArrayList();
		Object[] aobject = args;
		int i = args.length;

		for (int j = 0; j < i; ++j)
		{
			Object object1 = aobject[j];

			if (object1 instanceof ItemStack)
			{
				arraylist.add(((ItemStack)object1).copy());
			}
			else if (object1 instanceof Item)
			{
				arraylist.add(new ItemStack((Item)object1));
			}
			else
			{
				if (!(object1 instanceof Block))
				{
					throw new RuntimeException("Invalid shapeless recipe!");
				}

				arraylist.add(new ItemStack((Block)object1));
			}
		}

		BrickOvenShapelessRecipe recipe = new BrickOvenShapelessRecipe(output, arraylist);
		recipes.add(recipe);

		return recipe;
	}

	public ItemStack findMatchingRecipe(TileEntityBrickOven oven)
	{
		int i = 0;
		ItemStack itemstack = null;
		ItemStack itemstack1 = null;
		int j;

		for (j = TileEntityBrickOven.INPUTSTART; j < oven.getSizeInventory(); ++j)
		{
			ItemStack itemstack2 = oven.getStackInSlot(j);
			// System.out.println("\nBrickOvenRecipeHandler.findMatchingRecipeResult(): j = " + j);

			if (itemstack2 != null)
			{
				if (i == 0)
				{
					itemstack = itemstack2;
				}

				if (i == 1)
				{
					itemstack1 = itemstack2;
				}

				++i;
			}
		}

		for (IBrickOvenRecipe recipe : recipes)
		{
			// IBrickOvenRecipe recipe = (IBrickOvenRecipe)recipes.get(j);

			if (recipe.matches(oven))
			{
				return recipe.getCraftingResult(oven);
			}
		}

		return null;
	}

	public boolean isInRecipe(ItemStack stack)
	{
		for (IBrickOvenRecipe recipe : recipes)
		{
			if (recipe.contains(stack))
			{
				return true;
			}
		}
		return false;
	}

	public static List<IBrickOvenRecipe> getRecipeList()
	{
		return instance().recipes;
	}
}
