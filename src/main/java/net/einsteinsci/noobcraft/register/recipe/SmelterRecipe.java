package net.einsteinsci.noobcraft.register.recipe;

import net.minecraft.item.ItemStack;

public class SmelterRecipe
{
	private ItemStack outputStack;
	private ItemStack inputStack;
	private float experienceGiven;
	private int gravelNeeded;
	
	public SmelterRecipe(ItemStack output, ItemStack input, float experience, int gravel)
	{
		outputStack = output;
		inputStack = input;
		experienceGiven = experience;
		gravelNeeded = gravel;
	}
	
	public ItemStack getOutput()
	{
		return outputStack;
	}
	
	public ItemStack getInput()
	{
		return inputStack;
	}
	
	public float getExperience()
	{
		return experienceGiven;
	}
	
	public int getGravel()
	{
		return gravelNeeded;
	}
}
