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
	public static boolean moreBonesPeacefulOnly;
	public static boolean spawnMarshmallows;
	public static boolean canSmelterDoKilnStuff;

	public BBConfig()
	{
		greetUser = true;
		advancedCraftingForLotsOfThings = true;
		canMakeVanillaWorkbench = false;
		canMakeVanillaFurnace = false;
		canMakeChainArmor = true;
		flamingAnimalsDropCharredMeat = true;
		moreBones = true;
		moreBonesPeacefulOnly = true;
		spawnMarshmallows = true;
		canSmelterDoKilnStuff = false;
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
		moreBonesPeacefulOnly = config.getBoolean("moreBonesPeacefulOnly", GENERAL, true,
		                                          "Non-skeleton mobs only drop bones on peaceful. Requires 'moreBones' to be true in order to do anything.");
		spawnMarshmallows = config.getBoolean("spawnMarshmallows",
		                                      GENERAL,
		                                      true,
		                                      "Marshmallows spawn in dungeons, mineshafts, desert temples, and jungle temples.");
		canSmelterDoKilnStuff = config.getBoolean("canSmelterDoKilnStuff",
		                                          CRAFTING,
		                                          false,
		                                          "Allow smelter to make glass, bricks, smooth stone, etc.");

		if (config.hasChanged())
		{
			config.save();
		}
	}
}
