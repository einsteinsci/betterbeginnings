package net.einsteinsci.betterbeginnings.register.recipe;

import net.minecraft.item.ItemStack;

public class SmelterRecipe
{
	private ItemStack outputStack;
	private ItemStack inputStack;
	private float experienceGiven;
	private int boostersNeeded;
	private int bonusPerBoost;
	@Deprecated
	private float bonusChance;

	public SmelterRecipe(ItemStack output, ItemStack input, float experience, int boosters, int bonus)
	{
		outputStack = output;
		inputStack = input;
		experienceGiven = experience;
		boostersNeeded = boosters;
		bonusPerBoost = bonus;
		bonusChance = 0;
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

	public int getBoosters()
	{
		return boostersNeeded;
	}

	public int getBonusPerBoost()
	{
		return bonusPerBoost;
	}

	@Deprecated
	public float getBonusChance()
	{
		return bonusChance;
	}
}
