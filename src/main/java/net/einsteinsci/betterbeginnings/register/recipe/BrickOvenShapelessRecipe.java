package net.einsteinsci.betterbeginnings.register.recipe;

import net.einsteinsci.betterbeginnings.tileentity.TileEntityBrickOven;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

public class BrickOvenShapelessRecipe implements IBrickOvenRecipe
{
	/**
	 * Is a List of ItemStack that composes the recipe.
	 */
	public final List<ItemStack> recipeItems;
	/**
	 * Is the ItemStack that you get when craft the recipe.
	 */
	private final ItemStack recipeOutput;

	public BrickOvenShapelessRecipe(ItemStack output, List<ItemStack> input)
	{
		recipeOutput = output;
		recipeItems = input;
	}

	/**
	 * Used to check if a recipe matches current crafting inventory
	 */
	@Override
	public boolean matches(TileEntityBrickOven oven)
	{
		ArrayList<ItemStack> arraylist = new ArrayList<ItemStack>(recipeItems);

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
						ItemStack itemstack1 = (ItemStack)iterator.next();

						if (itemstack.getItem() == itemstack1.getItem() &&
								(itemstack1.getItemDamage() == OreDictionary.WILDCARD_VALUE || itemstack
										.getItemDamage() == itemstack1
										.getItemDamage()))
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

	/**
	 * Returns an Item that is the result of this recipe
	 */
	@Override
	public ItemStack getCraftingResult(TileEntityBrickOven oven)
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
		for (ItemStack s : recipeItems)
		{
			if (s.getItem() == stack.getItem())
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
}
