package net.einsteinsci.betterbeginnings.register.recipe;

import net.minecraft.item.ItemStack;

public class SmelterRecipe
{
	private ItemStack outputStack;
	private ItemStack inputStack;
	private float experienceGiven;
	private int gravelNeeded;

	private int bonusIfEnder;
	private float bonusChance;

	public SmelterRecipe(ItemStack output, ItemStack input, float experience, int gravel, int bonus, float chance)
	{
		outputStack = output;
		inputStack = input;
		experienceGiven = experience;
		gravelNeeded = gravel;
		bonusIfEnder = bonus;
		bonusChance = chance;
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

	public int getBonus()
	{
		return bonusIfEnder;
	}

	public float getBonusChance()
	{
		return bonusChance;
	}
}
