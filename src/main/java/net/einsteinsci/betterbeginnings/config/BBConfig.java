package net.einsteinsci.betterbeginnings.config;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.util.LogUtil;
import net.einsteinsci.betterbeginnings.util.RegistryUtil;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Level;

import java.util.*;

public class BBConfig
{
	public static final String GENERAL = Configuration.CATEGORY_GENERAL;
	public static final String CRAFTING = "otherCrafting";
	public static final String SMELTING = "smelting";
	public static final String INFUSION = "infusion";
	public static final String MOBDROPS = "mobdrops";
	public static final String WORLDGEN = "worldgen";
	public static final String TWEAKS = "tweaks";

	public static boolean greetUser;
	public static boolean debugLogging;
	public static boolean moduleBlockBreaking;
	public static boolean moduleAdvancedCrafting;
	public static boolean moduleInfusionRepair;
	public static boolean moduleCampfire;
	public static boolean moduleFurnaces;

	public static boolean advancedCraftingForLotsOfThings;
	public static boolean removeCraftedFoodRecipes;
	public static boolean canMakeChainArmor;
	public static boolean removeWoodToolRecipes;
	public static boolean anyStringForTraps;
	public static boolean allowStringAsToolBinding;
	public static boolean requireBlazePowderForDiamondPick;
	public static boolean netherlessBlazePowderRecipe;

	public static boolean flamingAnimalsDropCharredMeat;
	public static boolean spidersDropString;
	public static boolean moreBones;
	public static boolean moreBonesPeacefulOnly;

	public static boolean spawnMarshmallows;

	public static boolean canSmelterDoKilnStuff;
	public static boolean removeSmeltingRecipes;
	public static boolean removeOnlyVanillaSmeltingRecipes;
	public static boolean canCampfireDoAllKilnStuff;
	public static boolean smeltersCanUseCoal;

	public static boolean makeStuffStackable;
	public static boolean noDamageOnBadBreak;

	public static int diffusionHealthTaken;

	public static List<String> alwaysBreakableStrings;
	public static List<Block> alwaysBreakable;

	public static List<String> alsoPickaxesStrings;
	public static Map<Item, Integer> alsoPickaxes;

	public static List<String> alsoAxesStrings;
	public static Map<Item, Integer> alsoAxes;

	public static void initialize()
	{
		greetUser = true;
		debugLogging = false;
		moduleBlockBreaking = true;
		moduleAdvancedCrafting = true;
		moduleInfusionRepair = true;
		moduleCampfire = true;
		moduleFurnaces = true;

		advancedCraftingForLotsOfThings = true;
		removeCraftedFoodRecipes = true;
		canMakeChainArmor = true;
		removeWoodToolRecipes = true;
		anyStringForTraps = false;
		allowStringAsToolBinding = true;
		requireBlazePowderForDiamondPick = true;
		netherlessBlazePowderRecipe = true;

		flamingAnimalsDropCharredMeat = true;
		spidersDropString = false;
		moreBones = true;
		moreBonesPeacefulOnly = true;

		spawnMarshmallows = true;

		canSmelterDoKilnStuff = false;
		removeSmeltingRecipes = true;
		removeOnlyVanillaSmeltingRecipes = true;
		canCampfireDoAllKilnStuff = false;
		smeltersCanUseCoal = false;

		makeStuffStackable = true;
		noDamageOnBadBreak = false;

		diffusionHealthTaken = 16;

		alwaysBreakableStrings = new ArrayList<>();
		alsoPickaxesStrings = new ArrayList<>();
		alsoAxesStrings = new ArrayList<>();
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
		moduleFurnaces = config.getBoolean("Module - Furnaces", GENERAL, true,
			"Enable Furnaces recipes. Set to false to only allow vanilla furnace. \n" +
				"This will revert the vanilla furnace to the vanilla recipe.");

		// Mob Drops
		flamingAnimalsDropCharredMeat = config.getBoolean("Flaming animals drop charred meat", MOBDROPS, true,
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
			"Require Advanced Crafting for things like doors, pistons, chests, etc." +
			" Delete your config/betterbeginnings/advancedcrafting/main.json file afterward.");
		removeCraftedFoodRecipes = config.getBoolean("Remove crafted food recipes", CRAFTING, true,
			"Remove crafting recipes of vanilla food items, enforcing the use of the brick ovens.");
		canMakeChainArmor = config.getBoolean("Enable chain armor", CRAFTING, true,
			"Allow vanilla chain armor to be craftable from iron nuggets");
		removeWoodToolRecipes = config.getBoolean("Remove wooden tool recipes", CRAFTING, true,
			"Remove recipes for wooden pickaxe, axe, shovel, and hoe.");
		anyStringForTraps = config.getBoolean("Any string for traps", CRAFTING, false,
			"Allow any string to be used for tripwire hooks, trapped chests, etc. Delete your" +
			" config/betterbeginnings/advancedcrafting/main.json file afterward.");
		allowStringAsToolBinding = config.getBoolean("Allow string and twine as tool binding", CRAFTING, true,
			"Allow string and twine to be used in place of leather strips in tool bindings, at a higher cost." +
			" Delete your config/betterbeginnings/advancedcrafting/main.json file afterward.");
		requireBlazePowderForDiamondPick = config.getBoolean("Require blaze powder for diamond pick", CRAFTING, true,
			"Require blaze powder for a Diamond Pickaxe like all other diamond tools. This will require a trip to the" +
			" Nether unless 'Netherless blaze powder recipe' is set to true. Delete your" +
			" config/betterbeginnings/advancedcrafting/main.json file afterward.");
		netherlessBlazePowderRecipe = config.getBoolean("Netherless blaze powder recipe", CRAFTING, true,
			"Add an alternate, Netherless, but expensive recipe for blaze powder to help ease getting a diamond" +
			" pick. Still works even if 'Require blaze powder for diamond pick' is false." +
			" Delete your config/betterbeginnings/advancedcrafting/main.json file afterward.");

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
		smeltersCanUseCoal = config.getBoolean("Allow smelters to use coal as fuel", SMELTING, false,
			"Allow smelters to use regular coal as fuel in addition to charcoal.");

		//////////
		// ints //
		//////////
		diffusionHealthTaken = config.getInt("Health taken in diffusion", INFUSION, 16, 0, 100,
			"Amount of total health taken during cloth diffusion (in half-hearts).");

		////////////
		// Arrays //
		////////////

		// ALWAYS BREAKABLE
		String[] _alwaysBreakable = config.getStringList("Always breakable", TWEAKS, new String[] {},
		    "List of blocks to always be breakable. Use this format: 'modid:blockName'.");
		alwaysBreakableStrings.clear();
		Collections.addAll(alwaysBreakableStrings, _alwaysBreakable);

		// ALSO PICKAXES
		String[] _alsoPickaxes = config.getStringList("Also pickaxes", TWEAKS, new String[] {},
			"List of tools that should be treated as pickaxes. Use this format: 'modid:itemName=toolTier'");
		alsoPickaxesStrings.clear();
		Collections.addAll(alsoPickaxesStrings, _alsoPickaxes);

		// ALSO AXES
		String[] _alsoAxes = config.getStringList("Also axes", TWEAKS, new String[] {},
			"List of tools that should be treated as axes. Use this format: 'modid:itemName=toolTier'");
		alsoAxesStrings.clear();
		Collections.addAll(alsoAxesStrings, _alsoAxes);

		//////////
		// Save //
		//////////

		if (config.hasChanged())
		{
			config.save();
		}
	}

	public static void fillAlwaysBreakable()
	{
		alwaysBreakable = new ArrayList<>();
		for (String s : alwaysBreakableStrings)
		{
			Block b = RegistryUtil.getBlockFromRegistry(s);
			if (b == null)
			{
				LogUtil.log(Level.ERROR, "No block found matching '" + s + "'.");
			}
			else
			{
				alwaysBreakable.add(b);
			}
		}
	}

	public static void fillAlsoPickaxes()
	{
		alsoPickaxes = new HashMap<>();
		for (String entry : alsoPickaxesStrings)
		{
			int colonAt = entry.indexOf(":");
			int equalsAt = entry.indexOf("=");
			if (colonAt == -1 || equalsAt == -1)
			{
				LogUtil.log(Level.ERROR, "Invalid format: '" + entry + "'.");
				continue;
			}

			String modid = entry.substring(0, colonAt);
			String simpleName = entry.substring(colonAt + 1, equalsAt);

			Item item = GameRegistry.findItem(modid, simpleName);

			if (item == null)
			{
				LogUtil.log(Level.ERROR, "No item found within '" + entry + "'.");
				continue;
			}

			String levelStr = entry.substring(equalsAt + 1);
			try
			{
				int level = Integer.parseInt(levelStr);

				alsoPickaxes.put(item, level);
			}
			catch (NumberFormatException e)
			{
				LogUtil.log(Level.ERROR, "Invalid number: " + levelStr + " within " + entry);
			}
		}
	}

	public static void fillAlsoAxes()
	{
		alsoAxes = new HashMap<>();
		for (String entry : alsoAxesStrings)
		{
			int colonAt = entry.indexOf(":");
			int equalsAt = entry.indexOf("=");
			if (colonAt == -1 || equalsAt == -1)
			{
				LogUtil.log(Level.ERROR, "Invalid format: '" + entry + "'.");
				continue;
			}

			String modid = entry.substring(0, colonAt);
			String simpleName = entry.substring(colonAt + 1, equalsAt);

			Item item = GameRegistry.findItem(modid, simpleName);

			if (item == null)
			{
				LogUtil.log(Level.ERROR, "No item found within '" + entry + "'.");
				continue;
			}

			String levelStr = entry.substring(equalsAt + 1);
			try
			{
				int level = Integer.parseInt(levelStr);

				alsoAxes.put(item, level);
			}
			catch (NumberFormatException e)
			{
				LogUtil.log(Level.ERROR, "Invalid number: " + levelStr + " within " + entry);
			}
		}
	}
}