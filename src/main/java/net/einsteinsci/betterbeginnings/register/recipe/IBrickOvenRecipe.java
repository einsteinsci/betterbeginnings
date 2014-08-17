package net.einsteinsci.betterbeginnings.register.recipe;

import net.einsteinsci.betterbeginnings.tileentity.TileEntityBrickOven;
import net.minecraft.item.ItemStack;

public interface IBrickOvenRecipe
{
	/**
	 * Used to check if a recipe matches current crafting inventory
	 */
	boolean matches(TileEntityBrickOven inv);

	/**
	 * Returns an Item that is the result of this recipe
	 */
	ItemStack getCraftingResult(TileEntityBrickOven inv);

	/**
	 * Returns the size of the recipe area
	 */
	int getRecipeSize();

	boolean contains(ItemStack stack);

	ItemStack getRecipeOutput();
}
