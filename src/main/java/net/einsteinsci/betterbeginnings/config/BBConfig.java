package net.einsteinsci.betterbeginnings.config;

import net.minecraftforge.common.config.Configuration;

public class BBConfig
{
	public static final String GENERAL = Configuration.CATEGORY_GENERAL;
	public static final String CRAFTING = "crafting";

	public static boolean greetUser;
	public static boolean advancedCraftingForLotsOfThings;
	public static boolean canMakeVanillaWorkbench;
	public static boolean canMakeVanillaFurnace;
	public static boolean canMakeChainArmor;
	public static boolean flamingAnimalsDropCharredMeat;
	public static boolean moreBones;
	public static boolean spawnMarshmallows;
	
	public BBConfig()
	{
		greetUser = true;
		advancedCraftingForLotsOfThings = true;
		canMakeVanillaWorkbench = true;
		canMakeVanillaFurnace = true;
		canMakeChainArmor = true;
		flamingAnimalsDropCharredMeat = true;
		moreBones = true;
		spawnMarshmallows = true;
	}

	public static void syncConfig(Configuration config)
	{
		greetUser = config.getBoolean("greetUser", GENERAL, true, "Greet user upon login");
		advancedCraftingForLotsOfThings =
				config.getBoolean("advancedCraftingForLotsOfThings", CRAFTING, true,
								  "Require Advanced Crafting for things like doors, pistons, chests, etc.");
		canMakeVanillaWorkbench =
				config.getBoolean("canMakeVanillaWorkbench", CRAFTING, true,
								  "Provide alternative recipe for vanilla crafting table");
		canMakeVanillaFurnace =
				config.getBoolean("canMakeVanillaFurnace",
				                  CRAFTING,
								  true,
								  "Provide alternative recipe for vanilla furnace");
		canMakeChainArmor =
				config.getBoolean("canMakeChainArmor", CRAFTING, true,
								  "Allow vanilla chain armor to be craftable from iron nuggets");
		flamingAnimalsDropCharredMeat =
				config.getBoolean("flamingAnimalsDropCharredMeat", GENERAL, true,
								  "Animals that die by flame drop charred meat instead of vanilla cooked meat");
		moreBones = config.getBoolean("moreBones", GENERAL, true, "Almost all mobs drop bones, not just skeletons");
		spawnMarshmallows = config.getBoolean("spawnMarshmallows", GENERAL, true, "Marshmallows spawn in dungeons, mineshafts, desert temples, and jungle temples.");
		if (config.hasChanged())
		{
			config.save();
		}
	}
}
