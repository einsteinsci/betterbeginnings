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
	public static boolean makeStuffStackable;

	public static List<String> alwaysBreakable;
	public static List<String> alsoAxes;
	public static List<String> alsoPickaxes;
	public static List<String> alsoKnives;

	public static void initialize()
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
		makeStuffStackable = true;

		alwaysBreakable = new ArrayList<String>();
		alsoAxes = new ArrayList<String>();
		alsoPickaxes = new ArrayList<String>();
		alsoKnives = new ArrayList<String>();
	}

	public static void syncConfig(Configuration config)
	{
		//////////////
		// Booleans //
		//////////////

		// General
		greetUser = config.getBoolean("Greet user", GENERAL, true, "Greet user upon login");
		debugLogging = config.getBoolean("Debug logging", GENERAL, false, "Log all kinds of stuff, for debug purposes");
		flamingAnimalsDropCharredMeat = config.getBoolean("Burn, Baby, Burn", GENERAL, true,
		                                                  "Animals that die by flame drop charred meat instead of vanilla cooked meat");
		spidersDropString = config.getBoolean("Spiders drop string", GENERAL, false,
		                                      "Spiders drop vanilla string as well as silk.");
		moreBones = config.getBoolean("All bones", GENERAL, true,
		                              "Almost all mobs drop bones, not just skeletons");
		moreBonesPeacefulOnly = config.getBoolean("More bones only works on peaceful", GENERAL, true,
		                                          "Non-skeleton mobs only drop bones on peaceful. Requires" +
				                                          " 'All bones' to be true in order to do anything.");
		spawnMarshmallows = config.getBoolean("Generate marshmallows", GENERAL, true,
		                                      "Marshmallows spawn in dungeons, mineshafts, desert temples, " +
				                                      "and jungle temples.");
		makeStuffStackable = config.getBoolean("Make stuff stackable", GENERAL, true,
		                                       "Makes items that should be stackable (doors, " +
				                                       "minecarts, potions) stackable.");

		// Crafting
		advancedCraftingForLotsOfThings = config.getBoolean("Advanced crafting for lots of things", CRAFTING, true,
		                                                    "Require Advanced Crafting for things like doors, " +
				                                                    "pistons, chests, etc.");
		requireAdvancedCrafting = config.getBoolean("Require Advanced Crafting", CRAFTING, true,
		                                            "Disable vanilla crafting for recipes that require advanced " +
				                                            "crafting table. Setting this to false sort of makes " +
				                                            "this part of the mod pointless.");
		removeCraftedFoodRecipes = config.getBoolean("Remove Crafted Food Recipes", CRAFTING, true,
		                                             "Remove crafting recipes of vanilla food items, enforcing the " +
				                                             "use of the ovens.");
		canMakeVanillaWorkbench = config.getBoolean("Enable vanilla workbench recipe", CRAFTING, true,
		                                            "Provide alternative recipe for vanilla crafting table");
		canMakeVanillaFurnace = config.getBoolean("Enable vanilla furnace recipe", CRAFTING, true,
		                                          "Provide alternative recipe for vanilla furnace");
		canMakeChainArmor = config.getBoolean("Enable chain armor", CRAFTING, true,
		                                      "Allow vanilla chain armor to be craftable from iron nuggets");
		removeWoodToolRecipes = config.getBoolean("Remove wooden tool recipes", CRAFTING, true,
		                                          "Remove recipes for wooden pickaxe, axe, shovel, and hoe.");

		// Smelting
		canSmelterDoKilnStuff = config.getBoolean("Smelter can make kiln products", SMELTING, false,
		                                          "Allow smelter to make glass, bricks, smooth stone, etc.");

		removeSmeltingRecipes = config.getBoolean("Disable vanilla smelting", SMELTING, true,
		                                          "Remove recipes that normally use the vanilla furnace");
		removeOnlyVanillaSmeltingRecipes = config.getBoolean("Only remove smelting recipes for vanilla items",
		                                                     SMELTING, true,
		                                                     "Only remove furnace recipes for vanilla items/blocks. " +
				                                                     "Requires 'Disable vanilla smelting'.");

		////////////
		// Arrays //
		////////////

		String[] _alwaysBreakable = config.getStringList("Always breakable", GENERAL, new String[] {},
		                                                 "List of blocks always breakable. Use this format: " +
				                                                 "'modid:blockName'.");
		alwaysBreakable.clear();
		Collections.addAll(alwaysBreakable, _alwaysBreakable);

		String[] _alsoAxes = config.getStringList("Also axes", GENERAL, new String[] {},
		                                          "List of items that qualify as axes for breakable checks. " +
				                                          "Use this format: 'modid:itemName'.");
		alsoAxes.clear();
		Collections.addAll(alsoAxes, _alsoAxes);

		String[] _alsoPickaxes = config.getStringList("Also pickaxes", GENERAL, new String[] {},
		                                              "List of items that qualify as pickaxes for breakable checks. " +
				                                              "Use this format: 'modid:itemName'.");
		alsoPickaxes.clear();
		Collections.addAll(alsoPickaxes, _alsoPickaxes);

		String[] _alsoKnives = config.getStringList("Also knives", GENERAL, new String[] {},
		                                            "List of items that qualify as knives for breakable checks. " +
				                                            "Use this format: 'modid:itemName'.");
		alsoKnives.clear();
		Collections.addAll(alsoKnives, _alsoKnives);

		//////////
		// Save //
		//////////

		if (config.hasChanged())
		{
			config.save();
		}
	}
}
