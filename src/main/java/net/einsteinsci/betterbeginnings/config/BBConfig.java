package net.einsteinsci.betterbeginnings.config;

import net.minecraftforge.common.config.Configuration;

import java.util.*;

public class BBConfig
{
	public static final String GENERAL = Configuration.CATEGORY_GENERAL;
	public static final String CRAFTING = "otherCrafting";
	public static final String SMELTING = "smelting";
	public static final String MOBDROPS = "mobdrops";
	public static final String WORLDGEN = "worldgen";
	public static final String TWEAKS = "tweaks";

	public static boolean greetUser;
	public static boolean debugLogging;
	public static boolean moduleBlockBreaking;
	public static boolean moduleAdvancedCrafting;
	public static boolean moduleInfusionRepair;
	public static boolean moduleCampfire;

	public static boolean advancedCraftingForLotsOfThings;
	public static boolean removeCraftedFoodRecipes;
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
	public static boolean canCampfireDoAllKilnStuff;

	public static boolean makeStuffStackable;
	public static boolean noDamageOnBadBreak;

	public static List<String> alwaysBreakable;
	//public static List<String> alsoAxes;
	//public static List<String> alsoPickaxes;
	//public static List<String> alsoKnives;

	public static void initialize()
	{
		greetUser = true;
		debugLogging = false;
		moduleBlockBreaking = true;
		moduleAdvancedCrafting = true;
		moduleInfusionRepair = true;
		moduleCampfire = true;

		advancedCraftingForLotsOfThings = true;
		removeCraftedFoodRecipes = true;
		canMakeVanillaFurnace = true;
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
		canCampfireDoAllKilnStuff = false;

		makeStuffStackable = true;
		noDamageOnBadBreak = false;

		alwaysBreakable = new ArrayList<>();
		//alsoAxes = new ArrayList<>();
		//alsoPickaxes = new ArrayList<>();
		//alsoKnives = new ArrayList<>();
	}

	public static void syncConfig(Configuration config)
	{
		//////////////
		// Booleans //
		//////////////

		// General
		greetUser = config.getBoolean("Greet user", GENERAL, true, "Greet user upon login");
		debugLogging = config.getBoolean("Debug logging", GENERAL, false, "Log all kinds of stuff, for debug purposes");

		moduleBlockBreaking = config.getBoolean("Module - Block breaking", GENERAL, true,
			"Enable BetterBeginnings block breaking mechanics. Set to false for vanilla mechanics.");
		moduleAdvancedCrafting = config.getBoolean("Module - Force Advanced crafting", GENERAL, true,
			"Disable vanilla crafting for some recipes that require advanced crafting table.");
		moduleInfusionRepair = config.getBoolean("Module - Infusion Repair", GENERAL, true,
			"Enable Infusion Repair Table recipe. Set to false to force anvil repairs.");
		moduleCampfire = config.getBoolean("Module - Campfire", GENERAL, true,
			"Enable Campfire recipe. Set to false to make the beginning even harder.");

		// Mob Drops
		flamingAnimalsDropCharredMeat = config.getBoolean("Burn, Baby Burn", MOBDROPS, true,
			"Animals that die by flame drop charred meat instead of vanilla cooked meat");
		spidersDropString = config.getBoolean("Spiders drop string", MOBDROPS, false,
			"Spiders drop vanilla string as well as silk.");
		moreBones = config.getBoolean("All bones", MOBDROPS, true, "Almost all mobs drop bones, not just skeletons.");
		moreBonesPeacefulOnly = config.getBoolean("More bones only works on peaceful", MOBDROPS, true,
			"Non-skeleton mobs only drop bones on peaceful. Requires 'All bones' to be true in order to do anything.");

		// Worldgen
		spawnMarshmallows = config.getBoolean("Generate marshmallows", WORLDGEN, true,
			"[WIP] Marshmallows spawn in dungeons, mineshafts, desert temples, and jungle temples.");

		// Tweaks
		makeStuffStackable = config.getBoolean("Make stuff stackable", TWEAKS, true,
			"Makes items that should be stackable (minecarts, potions, etc.) stackable.");
		noDamageOnBadBreak = config.getBoolean("No damage on wrong block breaking", TWEAKS, false,
			"Set to true to disable damage when breaking certain blocks with your hands/face.");

		// Crafting
		advancedCraftingForLotsOfThings = config.getBoolean("Advanced crafting for lots of things", CRAFTING, true,
			"Require Advanced Crafting for things like doors, pistons, chests, etc.");
		removeCraftedFoodRecipes = config.getBoolean("Remove Crafted Food Recipes", CRAFTING, true,
			"Remove crafting recipes of vanilla food items, enforcing the use of the brick ovens.");
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
			SMELTING, true, "Only remove furnace recipes for vanilla items/blocks. Requires" +
				" 'Disable vanilla smelting'.");
		canCampfireDoAllKilnStuff = config.getBoolean("Allow campfire to use all kiln recipes", SMELTING, false,
			"Allow campfire to process all recipes kiln can, instead of just a few.");

		////////////
		// Arrays //
		////////////

		String[] _alwaysBreakable = config.getStringList("Always breakable", GENERAL, new String[] {},
		                                                 "List of blocks to always be breakable. Use this format: " +
				                                                 "'modid:blockName'.");
		alwaysBreakable.clear();
		Collections.addAll(alwaysBreakable, _alwaysBreakable);

		//////////
		// Save //
		//////////

		if (config.hasChanged())
		{
			config.save();
		}
	}
}
