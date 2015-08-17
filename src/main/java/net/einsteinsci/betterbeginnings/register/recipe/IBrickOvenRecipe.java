package net.einsteinsci.betterbeginnings.register.recipe;

import net.einsteinsci.betterbeginnings.tileentity.TileEntityBrickOven;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityNetherBrickOven;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface IBrickOvenRecipe
{
	/**
	 * Used to check if a recipe matches current crafting inventory
	 */
	boolean matches(TileEntityBrickOven inv);

	boolean matches(TileEntityNetherBrickOven inv);

	/**
	 * Returns an Item that is the result of this recipe
	 */
	ItemStack getCraftingResult(TileEntityBrickOven inv);

	ItemStack getCraftingResult(TileEntityNetherBrickOven inv);

	/**
	 * Returns the size of the recipe area
	 */
	int getRecipeSize();

	boolean contains(ItemStack stack);

	ItemStack getRecipeOutput();

	ItemStack[] getInputs();
}
