package net.einsteinsci.betterbeginnings.register.recipe;

import net.einsteinsci.betterbeginnings.inventory.InventoryWorkbenchAdditionalMaterials;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

public class AdvancedCraftingHandler
{
	private static final AdvancedCraftingHandler CRAFTING = new AdvancedCraftingHandler();
	public List<AdvancedRecipe> recipes = new ArrayList<>();

	public AdvancedCraftingHandler()
	{

	}

	public static void addAdvancedRecipe(ItemStack result, Object[] additionalMaterials, Object... args)
	{
		crafting().addRecipe(result, additionalMaterials, args);
	}

	public AdvancedRecipe addRecipe(ItemStack result, Object[] additionalMaterials, Object... args)
	{
		String totalRecipe = "";
		int i = 0;
		int width = 0;
		int height = 0;

		List<OreRecipeElement> addedMatList = new ArrayList<OreRecipeElement>();
		String lastString = "";
		for (Object mat : additionalMaterials)
		{
			if (mat instanceof ItemStack)
			{
				addedMatList.add(new OreRecipeElement((ItemStack)mat));
			}
			else if (mat instanceof String)
			{
				lastString = (String)mat;
			}
			else if (mat instanceof Integer)
			{
				addedMatList.add(new OreRecipeElement(lastString, (Integer)mat));
			}
		}
		OreRecipeElement[] addedMats = addedMatList.toArray(new OreRecipeElement[0]);

		// First String(s) in args
		if (args[i] instanceof String[])
		{
			String[] astring = (String[])args[i];
			i++;

			for (String s1 : astring)
			{
				height++;
				width = s1.length();
				totalRecipe += s1;
			}
		}
		else
		{
			while (args[i] instanceof String)
			{
				String s2 = (String)args[i];
				i++;
				height++;
				width = s2.length();
				totalRecipe += s2;
			}
		}

		HashMap hashmap = new HashMap();

		for (; i < args.length; i += 2)
		{
			Character character = (Character)args[i];
			OreRecipeElement stackInRecipe = null;

			if (args[i + 1] instanceof Item)
			{
				stackInRecipe = new OreRecipeElement(new ItemStack((Item)args[i + 1], 1, OreDictionary.WILDCARD_VALUE));
			}
			else if (args[i + 1] instanceof Block)
			{
				stackInRecipe = new OreRecipeElement(
						new ItemStack((Block)args[i + 1], 1, OreDictionary.WILDCARD_VALUE));
			}
			else if (args[i + 1] instanceof ItemStack)
			{
				stackInRecipe = new OreRecipeElement((ItemStack)args[i + 1]);
			}
			else if (args[i + 1] instanceof String)
			{
				stackInRecipe = new OreRecipeElement((String)args[i + 1], 1);
			}

			hashmap.put(character, stackInRecipe);
		}

		OreRecipeElement[] neededItems = new OreRecipeElement[width * height];

		for (int j = 0; j < width * height; ++j)
		{
			char iterChar = totalRecipe.charAt(j);

			if (hashmap.containsKey(Character.valueOf(iterChar)))
			{
				neededItems[j] = ((OreRecipeElement)hashmap.get(Character.valueOf(iterChar))).copy();
			}
			else
			{
				neededItems[j] = null;
			}
		}

		AdvancedRecipe advancedrecipes = new AdvancedRecipe(width, height, neededItems, result, addedMats);
		recipes.add(advancedrecipes);
		return advancedrecipes;
	}

	public static AdvancedCraftingHandler crafting()
	{
		return CRAFTING;
	}

	public static AdvancedRecipe AdvancedRecipeByResult(ItemStack result)
	{
		for (Object obj : crafting().recipes)
		{
			if (obj instanceof AdvancedRecipe)
			{
				AdvancedRecipe recipe = (AdvancedRecipe)obj;

				if (recipe.getRecipeOutput().getItem() == result.getItem())
				{
					return recipe;
				}
			}
		}

		return null;
	}

	public boolean hasRecipe(InventoryCrafting crafting, World world)
	{
		int i = 0;
		ItemStack itemstack = null;
		ItemStack itemstack1 = null;
		int j;

		for (j = 0; j < crafting.getSizeInventory(); ++j)
		{
			ItemStack itemstack2 = crafting.getStackInSlot(j);

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

		for (j = 0; j < recipes.size(); ++j)
		{
			AdvancedRecipe advrecipe = recipes.get(j);

			if (advrecipe.matchesMostly(crafting, world))
			{
				return true;
			}
		}

		return false;
	}

	public ItemStack findMatchingRecipe(InventoryCrafting crafting, InventoryWorkbenchAdditionalMaterials materials,
										World world)
	{
		int i = 0;
		ItemStack itemstack = null;
		ItemStack itemstack1 = null;
		int j;

		for (j = 0; j < crafting.getSizeInventory(); ++j)
		{
			ItemStack itemstack2 = crafting.getStackInSlot(j);

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

		if (i == 2 && itemstack.getItem() == itemstack1.getItem() && itemstack.stackSize == 1 &&
				itemstack1.stackSize == 1 && itemstack.getItem().isRepairable())
		{
			Item item = itemstack.getItem();
			int j1 = item.getMaxDamage() - itemstack.getItemDamage();
			int k = item.getMaxDamage() - itemstack1.getItemDamage();
			int l = j1 + k + item.getMaxDamage() * 5 / 100;
			int i1 = item.getMaxDamage() - l;

			if (i1 < 0)
			{
				i1 = 0;
			}

			return new ItemStack(itemstack.getItem(), 1, i1);
		}
		else
		{
			for (j = 0; j < recipes.size(); ++j)
			{
				AdvancedRecipe advrecipe = recipes.get(j);

				if (advrecipe.matches(crafting, materials, world))
				{
					return advrecipe.getCraftingResult(crafting);
				}
			}

			return null;
		}
	}

	public boolean isAddedMaterial(ItemStack stack)
	{
		for (Object obj : recipes)
		{
			if (obj instanceof AdvancedRecipe)
			{
				AdvancedRecipe recipe = (AdvancedRecipe)obj;

				if (recipe.hasMaterial(stack))
				{
					return true;
				}
			}
		}

		return false;
	}
}
