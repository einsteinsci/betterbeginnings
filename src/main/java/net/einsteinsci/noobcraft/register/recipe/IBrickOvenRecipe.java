package net.einsteinsci.noobcraft.register.recipe;

import net.einsteinsci.noobcraft.tileentity.TileEntityBrickOven;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IBrickOvenRecipe
{
	/**
	 * Used to check if a recipe matches current crafting inventory
	 */
	boolean matches(TileEntityBrickOven inv, World world);
	
	/**
	 * Returns an Item that is the result of this recipe
	 */
	ItemStack getCraftingResult(TileEntityBrickOven inv);
	
	/**
	 * Returns the size of the recipe area
	 */
	int getRecipeSize();
	
	ItemStack getRecipeOutput();
}
