package net.einsteinsci.betterbeginnings.config;

import net.minecraftforge.common.config.Configuration;

import java.util.*;

public class BBConfig
{
	public static final String GENERAL = Configuration.CATEGORY_GENERAL;
	public static final String CRAFTING = "crafting";
	public static final String SMELTING = "smelting";

	public static boolean greetUser;
	public static boolean debugLogging;
	public static boolean advancedCraftingForLotsOfThings;
	public static boolean requireAdvancedCrafting;
	public static boolean removeCraftedFoodRecipes;
	public static boolean canMakeVanillaWorkbench;
	public static boolean canMakeVanillaFurnace;
	public static boolean canMakeChainArmor;
	public static boolean removeWoodToolRecipes;
	public static boolean flamingAnimalsDropCharredMeat;
	public static boolean spidersDropString;
	public static boolean moreBones;
	public static boolean moreBonesPeacefulOnly;
	public static boolean spawnMarshmallows;
	public static boolean canSmelterDoKilnStuff;
	public static boolean removeSmeltingRecipes;
	public static boolean removeOnlyVanillaSmeltingRecipes;

	public static List<String> alwaysBreakable;
	public static List<String> alsoAxes;
	public static List<String> alsoPickaxes;
	public static List<String> alsoKnives;

	public BBConfig()
	{
		greetUser = true;
		debugLogging = false;
		advancedCraftingForLotsOfThings = true;
		requireAdvancedCrafting = true;
		removeCraftedFoodRecipes = true;
		canMakeVanillaWorkbench = false;
		canMakeVanillaFurnace = false;
		canMakeChainArmor = true;
		removeWoodToolRecipes = true;
		flamingAnimalsDropCharredMeat = true;
		spidersDropString = false;
		moreBones = true;
		moreBonesPeacefulOnly = true;
		spawnMarshmallows = true;
		canSmelterDoKilnStuff = false;
		removeSmeltingRecipes = true;
		removeOnlyVanillaSmeltingRecipes = true;

		alwaysBreakable = new ArrayList<String>();
	}

	public static void syncConfig(Configuration config)
	{
		// Booleans

		greetUser = config.getBoolean("greetUser", GENERAL, true, "Greet user upon login");
		debugLogging = config.getBoolean("debugLogging", GENERAL, false, "Log all kinds of stuff, for debug purposes");
		advancedCraftingForLotsOfThings =
				config.getBoolean("advancedCraftingForLotsOfThings", CRAFTING, true,
								  "Require Advanced Crafting for things like doors, pistons, chests, etc.");
		requireAdvancedCrafting = config.getBoolean("requireAdvancedCrafting", CRAFTING, true,
		                                            "Disable vanilla crafting for recipes that require advanced crafting table." +
				                                            " Setting this to false sort of makes this part of the mod pointless.");
		removeCraftedFoodRecipes = config.getBoolean("removeCraftedFoodRecipes", CRAFTING, true,
		                                             "Remove crafting recipes of vanilla food items, enforcing the use of the ovens.");
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
		removeWoodToolRecipes = config.getBoolean("removeToolRecipes", CRAFTING, true,
		                                          "Remove recipes for wooden pickaxe, axe, shovel, and hoe.");

		flamingAnimalsDropCharredMeat =
				config.getBoolean("flamingAnimalsDropCharredMeat", GENERAL, true,
								  "Animals that die by flame drop charred meat instead of vanilla cooked meat");
		spidersDropString = config
				.getBoolean("spidersDropString", GENERAL, false, "Spiders drop vanilla string as well as silk.");
		moreBones = config.getBoolean("moreBones", GENERAL, true, "Almost all mobs drop bones, not just skeletons");
		moreBonesPeacefulOnly = config.getBoolean("moreBonesPeacefulOnly", GENERAL, true,
		                                          "Non-skeleton mobs only drop bones on peaceful. Requires 'moreBones' to be true in order to do anything.");
		spawnMarshmallows = config.getBoolean("spawnMarshmallows",
		                                      GENERAL,
		                                      true,
		                                      "Marshmallows spawn in dungeons, mineshafts, desert temples, and jungle temples.");
		canSmelterDoKilnStuff = config.getBoolean("canSmelterDoKilnStuff",
		                                          SMELTING,
		                                          false,
		                                          "Allow smelter to make glass, bricks, smooth stone, etc.");

		removeSmeltingRecipes = config.getBoolean("removeSmeltingRecipes", SMELTING, true,
		                                          "Remove recipes that normally use the vanilla furnace");
		removeOnlyVanillaSmeltingRecipes = config.getBoolean("removeOnlyVanillaSmeltingRecipes", SMELTING, true,
		                                                     "Only remove furnace recipes for vanilla items/blocks. Requires removeSmeltingRecipes.");

		// Arrays

		String[] _alwaysBreakable = config.getStringList("alwaysBreakable", GENERAL, new String[] {},
		                                                 "List of blocks always breakable. Use this format: 'modid:blockName'.");
		alwaysBreakable.clear();
		Collections.addAll(alwaysBreakable, _alwaysBreakable);

		String[] _alsoAxes = config.getStringList("alsoAxes", GENERAL, new String[] {},
		                                          "List of items that qualify as axes for breakable checks. Use this format: 'modid:itemName'.");
		alsoAxes.clear();
		Collections.addAll(alsoAxes, _alsoAxes);

		String[] _alsoPickaxes = config.getStringList("alsoPickaxes", GENERAL, new String[] {},
		                                              "List of items that qualify as pickaxes for breakable checks. Use this format: 'modid:itemName'.");
		alsoPickaxes.clear();
		Collections.addAll(alsoPickaxes, _alsoPickaxes);

		String[] _alsoKnives = config.getStringList("alsoKnives", GENERAL, new String[] {},
		                                            "List of items that qualify as knives for breakable checks. Use this format: 'modid:itemName'.");
		alsoKnives.clear();
		Collections.addAll(alsoKnives, _alsoKnives);

		// Save

		if (config.hasChanged())
		{
			config.save();
		}
	}
}
