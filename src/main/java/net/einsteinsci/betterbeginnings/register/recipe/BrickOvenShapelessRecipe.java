package net.einsteinsci.betterbeginnings.register.recipe;

import net.einsteinsci.betterbeginnings.tileentity.TileEntityBrickOven;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityBrickOvenBase;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityNetherBrickOven;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

public class BrickOvenShapelessRecipe implements IBrickOvenRecipe
{
	/**
	 * Is a List of ItemStack that composes the recipe.
	 */
	public final List<OreRecipeElement> recipeItems;
	/**
	 * Is the ItemStack that you get when craft the recipe.
	 */
	private final ItemStack recipeOutput;

	public BrickOvenShapelessRecipe(ItemStack output, List<OreRecipeElement> input)
	{
		recipeOutput = output;
		recipeItems = input;
	}

	/**
	 * Used to check if a recipe matches current crafting inventory
	 */
	@Override
	public boolean matches(TileEntityBrickOvenBase oven)
	{
		ArrayList<OreRecipeElement> arraylist = new ArrayList<>(recipeItems);

		for (int col = 0; col < 3; ++col)
		{
			for (int row = 0; row < 3; ++row)
			{
				ItemStack itemstack = oven.getStackInRowAndColumn(row, col);

				if (itemstack != null)
				{
					boolean flag = false;
					Iterator iterator = arraylist.iterator();

					while (iterator.hasNext())
					{
						OreRecipeElement itemstack1 = (OreRecipeElement)iterator.next();

						if (itemstack1 != null && itemstack1.matches(itemstack))
						{
							flag = true;
							arraylist.remove(itemstack1);
							break;
						}
					}

					if (!flag)
					{
						return false;
					}
				}
			}
		}

		return arraylist.isEmpty();
	}

	//@Override
	//public boolean matches(TileEntityNetherBrickOven oven)
	//{
	//	ArrayList<OreRecipeElement> arraylist = new ArrayList<>(recipeItems);
	//
	//	for (int col = 0; col < 3; ++col)
	//	{
	//		for (int row = 0; row < 3; ++row)
	//		{
	//			ItemStack itemstack = oven.getStackInRowAndColumn(row, col);
	//
	//			if (itemstack != null)
	//			{
	//				boolean flag = false;
	//				Iterator iterator = arraylist.iterator();
	//
	//				while (iterator.hasNext())
	//				{
	//					ItemStack itemstack1 = (ItemStack)iterator.next();
	//
	//					if (itemstack.getItem() == itemstack1.getItem() &&
	//							(itemstack1.getItemDamage() == OreDictionary.WILDCARD_VALUE || itemstack
	//									.getItemDamage() == itemstack1
	//									.getItemDamage()))
	//					{
	//						flag = true;
	//						arraylist.remove(itemstack1);
	//						break;
	//					}
	//				}
	//
	//				if (!flag)
	//				{
	//					return false;
	//				}
	//			}
	//		}
	//	}
	//
	//	return arraylist.isEmpty();
	//}

	/**
	 * Returns an Item that is the result of this recipe
	 */
	@Override
	public ItemStack getCraftingResult(TileEntityBrickOvenBase oven)
	{
		return recipeOutput.copy();
	}

	/**
	 * Returns the size of the recipe area
	 */
	@Override
	public int getRecipeSize()
	{
		return recipeItems.size();
	}

	@Override
	public boolean contains(ItemStack stack)
	{
		for (OreRecipeElement ore : recipeItems)
		{
			if (ore.matches(stack))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return recipeOutput;
	}

	@Override
	public OreRecipeElement[] getInputs()
	{
		OreRecipeElement[] res = new OreRecipeElement[9];

		int i = 0;
		for (OreRecipeElement ore : recipeItems)
		{
			if (ore != null)
			{
				res[i] = ore;
			}

			i++;
		}

		return res;
	}
}
