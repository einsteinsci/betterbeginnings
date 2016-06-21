package net.einsteinsci.betterbeginnings.register;

import net.einsteinsci.betterbeginnings.config.BBConfig;
import net.einsteinsci.betterbeginnings.config.json.*;
import net.einsteinsci.betterbeginnings.items.ItemCharredMeat;
import net.einsteinsci.betterbeginnings.register.recipe.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.*;

public class RegisterRecipes
{
	public static void addShapelessRecipes()
	{
		// Sticks from Saplings & Knife
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.stick), "treeSapling", "itemKnife"));

		// Bone Shard
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegisterItems.boneShard, 2),
			Items.bone, "itemKnife"));

		// Leather Strip
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegisterItems.leatherStrip, 3),
			Items.leather, "itemKnife"));
		
		// Leather Strip (shortcut from rabbit hide)
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegisterItems.leatherStrip),
			Items.rabbit_hide, "itemKnife"));


		// Leather Strip (shortcut from rabbit hide)
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegisterItems.leatherStrip),
			Items.rabbit_hide, "itemKnife"));

		// Bonemeal from Bone Shard (a bit more rewarding)
		GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, 2, 15), RegisterItems.boneShard);

		// Iron Nugget
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegisterItems.ironNugget, 9), "ingotIron"));

		// String
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.string, 4),
			new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE),
			"itemKnife"));

		// Twine
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegisterItems.twine, 2), Blocks.vine, "itemKnife"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegisterItems.twine),
			new ItemStack(Blocks.tallgrass, 1, 1), "itemKnife"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegisterItems.twine),
			new ItemStack(Blocks.tallgrass, 1, 2), "itemKnife"));

		// Roasting Stick
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegisterItems.roastingStick),
			"stickWood", "stickWood", "itemKnife"));

		if (BBConfig.moduleCampfire)
		{
			// Campfire
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegisterBlocks.campfire),
				"stickWood", "stickWood", "stickWood", "itemKindling"));
		}
	}

	public static void addFurnaceRecipes()
	{
		addKilnRecipes();
		addBrickOvenRecipes();
		addSmelterRecipes();
		addCampfireRecipes();
	}

	private static void addKilnRecipes()
	{
		KilnConfig.addRecipe(Items.clay_ball, new ItemStack(Items.brick), 0.35f);
		KilnConfig.addRecipe(Blocks.clay, new ItemStack(Blocks.hardened_clay), 0.1f);
		KilnConfig.addRecipe("cobblestone", new ItemStack(Blocks.stone), 0.1f);
		KilnConfig.addRecipe(new ItemStack(Blocks.stonebrick, 1, 0), new ItemStack(Blocks.stonebrick, 1, 2), 0.1f);
		KilnConfig.addRecipe(Blocks.cactus, new ItemStack(Items.dye, 1, 2), 0.1f);
		KilnConfig.addRecipe("logWood", new ItemStack(Items.coal, 1, 1), 0.15f);
		KilnConfig.addRecipe(Blocks.sand, new ItemStack(Blocks.glass), 0.1f);
		KilnConfig.addRecipe(new ItemStack(Blocks.sponge, 1, 1), new ItemStack(Blocks.sponge, 1, 0), 0.1f);
		KilnConfig.addRecipe(Blocks.netherrack, new ItemStack(Items.netherbrick), 0.1f);

		KilnConfig.addRecipe(Items.beef, new ItemStack(RegisterItems.charredMeat), 0.1f);
		KilnConfig.addRecipe(Items.porkchop, new ItemStack(RegisterItems.charredMeat), 0.1f);
		KilnConfig.addRecipe(Items.chicken, new ItemStack(RegisterItems.charredMeat, 1,
			ItemCharredMeat.META_CHICKEN), 0.1f);
		KilnConfig.addRecipe(new ItemStack(Items.fish, 1, 0), new ItemStack(RegisterItems.charredMeat, 1,
			ItemCharredMeat.META_FISH), 0.1f);
		KilnConfig.addRecipe(new ItemStack(Items.fish, 1, 1), new ItemStack(RegisterItems.charredMeat, 1,
			ItemCharredMeat.META_FISH), 0.1f);
		KilnConfig.addRecipe(Items.rabbit, new ItemStack(RegisterItems.charredMeat, 1,
			ItemCharredMeat.META_RABBIT), 0.1f);
		KilnConfig.addRecipe(Items.mutton, new ItemStack(RegisterItems.charredMeat, 1,
			ItemCharredMeat.META_MUTTON), 0.1f);
	}

	private static void addBrickOvenRecipes()
	{
		BrickOvenConfig.addShapedRecipe(new ItemStack(Items.golden_apple), "GGG", "GAG", "GGG", 'G',
		                                       Items.gold_ingot, 'A', Items.apple);
		BrickOvenConfig.addShapedRecipe(new ItemStack(Items.golden_apple, 1, 1), "###", "#A#", "###", '#',
		                                       Blocks.gold_block, 'A', Items.apple);
		BrickOvenConfig.addShapedRecipe(new ItemStack(Items.cake), "MMM", "SES", "WWW", 'M', Items.milk_bucket,
		                                       'S', Items.sugar, 'E', Items.egg, 'W', "cropWheat");
		BrickOvenConfig.addShapedRecipe(new ItemStack(Items.bread, 2), "WWW", 'W', "cropWheat");
		BrickOvenConfig.addShapedRecipe(new ItemStack(Items.cookie, 8), "WCW", 'W', "cropWheat", 'C',
		                                       new ItemStack(Items.dye, 1, 3)); // Cocoa bean
		BrickOvenConfig.addShapedRecipe(new ItemStack(Items.rabbit_stew), " R ", "CPM", " B ",
			'R', Items.cooked_rabbit, 'C', "cropCarrot", 'P', "cropPotato",
			'M', Blocks.brown_mushroom, 'B', Items.bowl);
		BrickOvenConfig.addShapedRecipe(new ItemStack(Items.rabbit_stew), " R ", "CPM", " B ",
			'R', Items.cooked_rabbit, 'C', "cropCarrot", 'P', "cropPotato",
			'M', Blocks.red_mushroom, 'B', Items.bowl);
		BrickOvenConfig.addShapedRecipe(new ItemStack(RegisterItems.marshmallow, 3), " S ", "SSS", " S ",
			'S', Items.sugar);

		BrickOvenConfig.addShapelessRecipe(new ItemStack(Items.mushroom_stew), Blocks.brown_mushroom,
			Blocks.red_mushroom, Items.bowl);
		BrickOvenConfig.addShapelessRecipe(new ItemStack(Items.cooked_beef), Items.beef);
		BrickOvenConfig.addShapelessRecipe(new ItemStack(Items.cooked_porkchop), Items.porkchop);
		BrickOvenConfig.addShapelessRecipe(new ItemStack(Items.cooked_chicken), Items.chicken);
		BrickOvenConfig.addShapelessRecipe(new ItemStack(Items.cooked_fish), Items.fish);
		BrickOvenConfig.addShapelessRecipe(new ItemStack(Items.cooked_fish, 1, 1),
			new ItemStack(Items.fish, 1, 1));
		BrickOvenConfig.addShapelessRecipe(new ItemStack(Items.cooked_rabbit), Items.rabbit);
		BrickOvenConfig.addShapelessRecipe(new ItemStack(Items.cooked_mutton), Items.mutton);
		BrickOvenConfig.addShapelessRecipe(new ItemStack(Items.baked_potato), "cropPotato");
		BrickOvenConfig.addShapelessRecipe(new ItemStack(Items.pumpkin_pie), Items.egg, Items.sugar,
		                                          Blocks.pumpkin);
		BrickOvenConfig.addShapelessRecipe(new ItemStack(Items.fermented_spider_eye), Items.spider_eye,
												  Items.sugar, Blocks.brown_mushroom);
		BrickOvenConfig.addShapelessRecipe(new ItemStack(Items.magma_cream),
			Items.slime_ball, Items.blaze_powder);
	}

	private static void addSmelterRecipes()
	{
		// Vanilla Ore Recipes (keep the result vanilla to prevent weirdness)
		SmelterConfig.addRecipe("oreIron", new ItemStack(Items.iron_ingot), 0.7f, 1, 1);
		SmelterConfig.addRecipe("oreGold", new ItemStack(Items.gold_ingot), 1.0f, 2, 1);

		// Modded Ore Recipes
		RegisterHelper.registerSmelterConfigOreRecipe("oreCopper", "ingotCopper", 0.6f, 1, 1);
		RegisterHelper.registerSmelterConfigOreRecipe("oreTin", "ingotTin", 0.6f, 1, 1);
		RegisterHelper.registerSmelterConfigOreRecipe("oreAluminum", "ingotAluminum", 0.8f, 1, 1);
		RegisterHelper.registerSmelterConfigOreRecipe("oreSilver", "ingotSilver", 1.0f, 1, 1);
		RegisterHelper.registerSmelterConfigOreRecipe("oreLead", "ingotLead", 0.6f, 1, 1);
		RegisterHelper.registerSmelterConfigOreRecipe("orePlatinum", "ingotPlatinum", 1.0f, 2, 1);
		RegisterHelper.registerSmelterConfigOreRecipe("oreNickel", "ingotNickel", 0.8f, 1, 1);

		// Recipes that might be better suited in Kiln only
		if (BBConfig.canSmelterDoKilnStuff)
		{
			SmelterConfig.addRecipe(new ItemStack(Blocks.sand, 1, 0),
				new ItemStack(Blocks.glass), 0.1f, 1, 0);
			SmelterConfig.addRecipe(new ItemStack(Blocks.sand, 1, 1),
				new ItemStack(Blocks.stained_glass, 1, 1), 0.1f, 1, 0); // Red sand makes orange stained glass.
			SmelterConfig.addRecipe(Blocks.netherrack, new ItemStack(Items.netherbrick), 0.25f, 1, 1);
			SmelterConfig.addRecipe(Blocks.stonebrick, new ItemStack(Blocks.stonebrick, 1, 2), 0.1f, 1, 0);

			SmelterConfig.addRecipe("cobblestone", new ItemStack(Blocks.stone), 0.1f, 0, 0);
			SmelterConfig.addRecipe(Items.clay_ball, new ItemStack(Items.brick), 0.3f, 0, 0);
			SmelterConfig.addRecipe(Blocks.clay, new ItemStack(Blocks.hardened_clay), 0.35f, 0, 0);
			SmelterConfig.addRecipe(new ItemStack(Blocks.sponge, 1, 1),
				new ItemStack(Blocks.sponge, 1, 0), 0.1f, 0, 0);
			SmelterConfig.addRecipe(new ItemStack(Blocks.stonebrick, 1, 0),
				new ItemStack(Blocks.stonebrick, 1, 2), 0.1f, 0, 0);
		}

		// Silk touch recipes
		SmelterConfig.addRecipe("oreCoal", new ItemStack(Items.coal, 1), 0.25f, 2, 1);
		SmelterConfig.addRecipe("oreQuartz", new ItemStack(Items.quartz, 2), 0.4f, 2, 2);
		SmelterConfig.addRecipe("oreLapis", new ItemStack(Items.dye, 8, 4), 0.5f, 2, 3);
		SmelterConfig.addRecipe("oreRedstone", new ItemStack(Items.redstone, 4), 0.8f, 2, 2);
		SmelterConfig.addRecipe("oreDiamond", new ItemStack(Items.diamond, 1), 1.0f, 3, 1);
		SmelterConfig.addRecipe("oreEmerald", new ItemStack(Items.emerald, 1), 1.0f, 3, 1);

		// Silk touch recipes (modded)
		RegisterHelper.registerSmelterConfigOreRecipe("oreRuby", "gemRuby", 0.8f, 2, 1);
		RegisterHelper.registerSmelterConfigOreRecipe("oreSapphire", "gemSapphire", 0.8f, 2, 1);
		RegisterHelper.registerSmelterConfigOreRecipe("oreOlivine", "gemOlivine", 0.8f, 2, 1);
	}

	private static void addCampfireRecipes()
	{
		CampfireConfig.addRecipe("logWood", new ItemStack(Items.coal, 1, 1), 0.15f);
		CampfireConfig.addRecipe(new ItemStack(Blocks.sponge, 1, 1), new ItemStack(Blocks.sponge, 1, 0), 0.1f);

		CampfireConfig.addRecipe(RegisterItems.roastingStickRawMallow,
			new ItemStack(RegisterItems.roastingStickCookedMallow), 0.5f);

		CampfireConfig.addRecipe(Items.beef, new ItemStack(RegisterItems.charredMeat), 0.1f);
		CampfireConfig.addRecipe(Items.porkchop, new ItemStack(RegisterItems.charredMeat), 0.1f);
		CampfireConfig.addRecipe(Items.chicken, new ItemStack(RegisterItems.charredMeat, 1,
			ItemCharredMeat.META_CHICKEN), 0.1f);
		CampfireConfig.addRecipe(new ItemStack(Items.fish, 1, 0), new ItemStack(RegisterItems.charredMeat, 1,
			ItemCharredMeat.META_FISH), 0.1f);
		CampfireConfig.addRecipe(new ItemStack(Items.fish, 1, 1), new ItemStack(RegisterItems.charredMeat, 1,
			ItemCharredMeat.META_FISH), 0.1f);
		CampfireConfig.addRecipe(Items.rabbit, new ItemStack(RegisterItems.charredMeat, 1,
			ItemCharredMeat.META_RABBIT), 0.1f);
		CampfireConfig.addRecipe(Items.mutton, new ItemStack(RegisterItems.charredMeat, 1,
			ItemCharredMeat.META_MUTTON), 0.1f);

		if (BBConfig.canCampfireDoAllKilnStuff)
		{
			CampfireConfig.addRecipe(Items.clay_ball, new ItemStack(Items.brick), 0.35f);
			CampfireConfig.addRecipe(Blocks.clay, new ItemStack(Blocks.hardened_clay), 0.1f);
			CampfireConfig.addRecipe("cobblestone", new ItemStack(Blocks.stone), 0.1f);
			CampfireConfig.addRecipe(new ItemStack(Blocks.stonebrick, 1, 0),
				new ItemStack(Blocks.stonebrick, 1, 2), 0.1f);
			CampfireConfig.addRecipe(Blocks.cactus, new ItemStack(Items.dye, 1, 2), 0.1f);
			CampfireConfig.addRecipe(Blocks.sand, new ItemStack(Blocks.glass), 0.1f);
			CampfireConfig.addRecipe(Blocks.netherrack, new ItemStack(Items.netherbrick), 0.1f);
		}

		CampfireConfig.addPanRecipe(Items.beef, new ItemStack(Items.cooked_beef), 0.1f);
		CampfireConfig.addPanRecipe(Items.porkchop, new ItemStack(Items.cooked_porkchop), 0.1f);
		CampfireConfig.addPanRecipe(Items.chicken, new ItemStack(Items.cooked_chicken), 0.1f);
		CampfireConfig.addPanRecipe(new ItemStack(Items.fish, 1, 0), new ItemStack(Items.cooked_fish, 1, 0), 0.1f);
		CampfireConfig.addPanRecipe(new ItemStack(Items.fish, 1, 1), new ItemStack(Items.cooked_fish, 1, 1), 0.1f);
		CampfireConfig.addPanRecipe(Items.rabbit, new ItemStack(Items.cooked_rabbit), 0.1f);
		CampfireConfig.addPanRecipe(Items.mutton, new ItemStack(Items.cooked_mutton), 0.1f);
	}

	public static void addAdvancedRecipes()
	{
		// region advancedCraftingForLotsOfThings
		if (BBConfig.advancedCraftingForLotsOfThings)
		{
			// Wooden Doors
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.oak_door, 3),
				new Object[] {new ItemStack(RegisterItems.leatherStrip, 2)},
				"##", "##", "##",
				'#', new ItemStack(Blocks.planks, 1, 0));
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.spruce_door, 3),
				new Object[] {new ItemStack(RegisterItems.leatherStrip, 2)},
				"##", "##", "##",
				'#', new ItemStack(Blocks.planks, 1, 1));
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.birch_door, 3),
				new Object[] {new ItemStack(RegisterItems.leatherStrip, 2)},
				"##", "##", "##",
				'#', new ItemStack(Blocks.planks, 1, 2));
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.jungle_door, 3),
				new Object[] {new ItemStack(RegisterItems.leatherStrip, 2)},
				"##", "##", "##",
				'#', new ItemStack(Blocks.planks, 1, 3));
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.acacia_door, 3),
				new Object[] {new ItemStack(RegisterItems.leatherStrip, 2)},
				"##", "##", "##",
				'#', new ItemStack(Blocks.planks, 1, 4));
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.dark_oak_door, 3),
				new Object[] {new ItemStack(RegisterItems.leatherStrip, 2)},
				"##", "##", "##",
				'#', new ItemStack(Blocks.planks, 1, 5));

			// Iron Door
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.iron_door),
			                                          new Object[] {"nuggetIron", 2},
			                                          "II", "II", "II",
			                                          'I', "ingotIron");
			// Fence Gates
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Blocks.oak_fence_gate),
				new Object[] {"itemString", 4, new ItemStack(RegisterItems.leatherStrip, 2)},
				"/#/",
				"/#/",
				'/', "stickWood",
				'#', new ItemStack(Blocks.planks, 1, 0));
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Blocks.spruce_fence_gate),
				new Object[] {"itemString", 4, new ItemStack(RegisterItems.leatherStrip, 2)},
				"/#/",
				"/#/",
				'/', "stickWood",
				'#', new ItemStack(Blocks.planks, 1, 1));
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Blocks.birch_fence_gate),
				new Object[] {"itemString", 4, new ItemStack(RegisterItems.leatherStrip, 2)},
				"/#/",
				"/#/",
				'/', "stickWood",
				'#', new ItemStack(Blocks.planks, 1, 2));
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Blocks.jungle_fence_gate),
				new Object[] {"itemString", 4, new ItemStack(RegisterItems.leatherStrip, 2)},
				"/#/",
				"/#/",
				'/', "stickWood",
				'#', new ItemStack(Blocks.planks, 1, 3));
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Blocks.acacia_fence_gate),
				new Object[] {"itemString", 4, new ItemStack(RegisterItems.leatherStrip, 2)},
				"/#/",
				"/#/",
				'/', "stickWood",
				'#', new ItemStack(Blocks.planks, 1, 4));
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Blocks.dark_oak_fence_gate),
				new Object[] {"itemString", 4, new ItemStack(RegisterItems.leatherStrip, 2)},
				"/#/",
				"/#/",
				'/', "stickWood",
				'#', new ItemStack(Blocks.planks, 1, 5));

			// Trapdoor
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Blocks.trapdoor),
			                                          new Object[] {new ItemStack(RegisterItems.leatherStrip, 2)},
			                                          "###", "###",
			                                          '#', "plankWood");
			// Chest. Yep, you need iron before you can make a chest. If you absolutely must store stuff before you have
			// iron, use your kiln (provided it isn't kiln-able ;D).
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Blocks.chest),
				new Object[] {"nuggetIron", 3, new ItemStack(RegisterItems.leatherStrip, 1)},
													  "###",
													  "# #",
													  "###",
													  '#', "plankWood");

			if (BBConfig.anyStringForTraps)
			{
				// Trapped Chest
				AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Blocks.trapped_chest),
					new Object[]{"itemString", 2, "dustRedstone", 2},
					"C", "H",
					'C', Blocks.chest,
					'H', Blocks.tripwire_hook);
				// Tripwire Hook
				AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Blocks.tripwire_hook),
					new Object[]{"itemString", 1, "dustRedstone", 1},
					"I", "/", "#",
					'I', "ingotIron",
					'/', "stickWood",
					'#', "plankWood");
			}
			else
			{
				// Trapped Chest
				AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Blocks.trapped_chest),
					new Object[]{new ItemStack(RegisterItems.thread, 2), "dustRedstone", 2},
					"C", "H",
					'C', Blocks.chest,
					'H', Blocks.tripwire_hook);
				// Tripwire Hook
				AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Blocks.tripwire_hook),
					new Object[]{new ItemStack(RegisterItems.thread, 1), "dustRedstone", 1},
					"I", "/", "#",
					'I', "ingotIron",
					'/', "stickWood",
					'#', "plankWood");
			}
			// Piston
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Blocks.piston),
			                                          new Object[] {"nuggetIron", 2},
			                                          "###", "CIC", "CRC",
			                                          '#', "plankWood",
			                                          'I', "ingotIron",
			                                          'C', "cobblestone",
			                                          'R', "dustRedstone");
			// Dispenser
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Blocks.dispenser),
			                                          new Object[] {"nuggetIron", 2},
			                                          "###", "#B#", "#R#",
			                                          '#', "cobblestone",
			                                          'B', new ItemStack(Items.bow, 1, 0),
			                                          'R', "dustRedstone");
			// Note Block
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Blocks.noteblock),
			                                          new Object[] {"itemString", 2},
			                                          "###", "#R#", "###",
			                                          '#', "plankWood",
			                                          'R', "dustRedstone");
			// Gold Rail
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Blocks.golden_rail, 6),
			                                          new Object[] {"nuggetGold", 2},
			                                          "G G", "G/G", "GRG",
			                                          'G', "ingotGold",
			                                          '/', "stickWood",
			                                          'R', "dustRedstone");
			// Detector Rail
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Blocks.detector_rail, 6),
			                                          new Object[] {"nuggetIron", 2, "stickWood", 4},
			                                          "I I", "I_I", "IRI",
			                                          'I', "ingotIron",
			                                          '_', Blocks.stone_pressure_plate,
			                                          'R', "dustRedstone");
			// TNT
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Blocks.tnt, 2),
			                                          new Object[] {"itemString", 3},
			                                          "G#G", "#G#", "G#G",
			                                          'G', Items.gunpowder,
			                                          '#', Blocks.sand);
			// Bookshelf
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Blocks.bookshelf),
			                                          new Object[] {"nuggetIron", 1},
			                                          "###", "BBB", "###",
			                                          '#', "plankWood",
			                                          'B', Items.book);
			// Ladder
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Blocks.ladder, 4),
			                                          new Object[] {"itemString", 1},
			                                          "/ /", "///", "/ /",
			                                          '/', "stickWood");
			// Rail
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Blocks.rail, 16),
			                                          new Object[] {"nuggetIron", 1},
			                                          "I I", "I/I", "I I",
			                                          'I', "ingotIron",
			                                          '/', "stickWood");
			// Enchanting Table
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Blocks.enchanting_table),
				new Object[] {new ItemStack(Items.leather), "dyeRed", 2, "gemLapis", 4},
			                                          " B ", "D#D", "###",
			                                          'B', Items.book,
			                                          'D', "gemDiamond",
			                                          '#', Blocks.obsidian);
			// Beacon
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Blocks.beacon),
			                                          new Object[] {"blockDiamond", 1,
					                                          new ItemStack(Items.blaze_rod, 4),
					                                          new ItemStack(Items.potionitem, 1, 16)}, //Awkward potion
			                                          "+++", "+S+", "###",
			                                          '+', "blockGlassColorless",
			                                          'S', Items.nether_star,
			                                          '#', Blocks.obsidian);
			// Anvil
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Blocks.anvil), new Object[] {"nuggetIron", 4},
			                                          "###", " I ", "III",
			                                          '#', "blockIron",
			                                          'I', "ingotIron");
			// Hopper
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Blocks.hopper, 4), new Object[] {"nuggetIron", 2,
					                                          new ItemStack(Blocks.stone_pressure_plate, 1)},
			                                          "I I", "I#I", " I ",
			                                          'I', "ingotIron",
			                                          '#', Blocks.chest);
			// Activator Rail
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Blocks.activator_rail, 6),
			                                          new Object[] {"nuggetIron", 4},
			                                          "I/I", "IiI", "I/I",
			                                          'I', "ingotIron",
			                                          '/', "stickWood",
			                                          'i', Blocks.redstone_torch);
			// Dropper
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Blocks.dropper),
			                                          new Object[] {"nuggetIron", 2},
			                                          "###", "# #", "#R#",
			                                          '#', "cobblestone",
			                                          'R', "dustRedstone");
			// Minecart
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.minecart),
			                                          new Object[] {"nuggetIron", 2,
					                                          new ItemStack(RegisterItems.leatherStrip, 1)},
			                                          "I I", "III",
			                                          'I', "ingotIron");
			// Compass
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.compass),
			                                          new Object[] {"nuggetIron", 3,
					                                          new ItemStack(Items.potionitem, 1, 0)}, //Water Bottle
			                                          " I ", "IRI", " I ",
			                                          'I', "ingotIron",
			                                          'R', "dustRedstone");
			// Clock
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.clock),
			                                          new Object[] {"nuggetGold", 3, "dyeBlack", 1},
			                                          " G ", "GRG", " G ",
			                                          'G', "ingotGold",
			                                          'R', "dustRedstone");
			// Bed
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.bed), new Object[] {"nuggetIron", 2},
			                                          "***", "###",
			                                          '*', new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE),
			                                          '#', "plankWood");
			// Brewing Stand
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.brewing_stand),
			                                          new Object[] {"nuggetGold", 1, "nuggetIron", 3},
			                                          " / ", "###",
			                                          '/', Items.blaze_rod,
			                                          '#', "cobblestone");
			// Cauldron
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.cauldron), new Object[] {"nuggetIron", 3},
			                                          "I I", "I I", "III",
			                                          'I', "ingotIron");

			// Jukebox
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Blocks.jukebox),
				new Object[] {"nuggetGold", 1, "itemString", 2},
				"###", "#D#", "###",
				'#', "plankWood",
				'D', "gemDiamond");

			// Redstone Lamp
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Blocks.redstone_lamp),
				new Object[] {"dustRedstone", 2},
				" R ", "R#R", " R ",
				'R', "dustRedstone",
				'#', "glowstone");

			// Ender Chest
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Blocks.ender_chest),
				new Object[] {new ItemStack(Items.blaze_powder, 4)},
				"###", "#E#", "###",
				'#', Blocks.obsidian,
				'E', Items.ender_eye);

			// Weighted Pressure Plate (Iron)
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Blocks.heavy_weighted_pressure_plate),
				new Object[] {"dustRedstone", 1}, "II", 'I', "ingotIron");

			// Weighted Pressure Plate (Gold)
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Blocks.light_weighted_pressure_plate),
				new Object[] {"dustRedstone", 1}, "GG", 'G', "ingotGold");

			// Daylight Sensor
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Blocks.daylight_detector),
				new Object[] {"dustRedstone", 2},
				"###", "QQQ", "---",
				'#', "blockGlassColorless",
				'Q', "gemQuartz",
				'-', Blocks.wooden_slab);

			// Iron Trapdoor
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Blocks.iron_trapdoor),
				new Object[] {"nuggetIron", 2},
				"II", "II",
				'I', "ingotIron");

			// Item Frame
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.item_frame),
				new Object[] {"itemString", 1},
				"///", "/L/", "///",
				'/', "stickWood",
				'L', Items.leather);

			// Comparator
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.comparator),
				new Object[] {"dustRedstone", 1},
				" i ", "iQi", "###",
				'i', Blocks.redstone_torch,
				'Q', Items.quartz,
				'#', "stone");

			// Armor Stand
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.armor_stand),
				new Object[] {"nuggetIron", 2},
				"///", " / ", "/-/",
				'/', "stickWood",
				'-', new ItemStack(Blocks.stone_slab, 1, 0));
		}
		// endregion

		// Bow
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.bow),
		                                          new Object[] {new ItemStack(RegisterItems.leatherStrip, 3)},
		                                          " /s", "/ s", " /s",
		                                          's', "itemString",
		                                          '/', "stickWood");
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.bow), true,
		                                          new Object[] {new ItemStack(RegisterItems.leatherStrip, 3)},
		                                          "s/ ", "s /", "s/ ",
		                                          's', "itemString",
		                                          '/', "stickWood");

		// Fishing rod
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.fishing_rod),
		                                          new Object[] {new ItemStack(RegisterItems.ironNugget)},
		                                          "  /", " /s", "/ s",
		                                          '/', "stickWood",
		                                          's', "itemString");

		// Shears
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.shears),
			new Object[] {"nuggetIron", 1},
			" I", "I ",
			'I', "ingotIron");

		// Bone Pickaxe
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(RegisterItems.bonePickaxe),
		                                          new Object[] {new ItemStack(RegisterItems.leatherStrip, 2)},
		                                          "sBs", " / ", " / ",
		                                          's', RegisterItems.boneShard,
		                                          'B', Items.bone,
		                                          '/', "stickWood");

		// region Tools & Armor

		// Leather armor
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.leather_helmet),
		                                          new Object[] {"itemString", 3,
				                                          new ItemStack(Blocks.wool, 2, OreDictionary.WILDCARD_VALUE)},
												  "LLL",
												  "L L",
												  'L', Items.leather);
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.leather_chestplate),
		                                          new Object[] {"itemString", 2,
				                                          new ItemStack(Blocks.wool, 4, OreDictionary.WILDCARD_VALUE)},
												  "L L",
												  "LLL",
												  "LLL",
												  'L', Items.leather);
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.leather_leggings),
		                                          new Object[] {"itemString", 4,
				                                          new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE)},
												  "LLL",
												  "L L",
												  "L L",
												  'L', Items.leather);
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.leather_boots),
		                                          new Object[] {"itemString", 4,
				                                          new ItemStack(Blocks.wool, 3, OreDictionary.WILDCARD_VALUE)},
		                                          "L L", "L L",
		                                          'L', Items.leather);

		// Stone Tools/Weapons
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.stone_pickaxe),
		                                          new Object[] {new ItemStack(RegisterItems.leatherStrip, 2)},
		                                          "###", " / ", " / ",
		                                          '#', "stone",
		                                          '/', "stickWood");
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.stone_sword),
		                                          new Object[] {new ItemStack(RegisterItems.leatherStrip, 2)},
		                                          "#", "#", "/",
		                                          '#', "stone",
		                                          '/', "stickWood");
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.stone_shovel),
		                                          new Object[] {new ItemStack(RegisterItems.leatherStrip, 1)},
		                                          "#", "/", "/",
		                                          '#', "stone",
		                                          '/', "stickWood");
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.stone_axe),
		                                          new Object[] {new ItemStack(RegisterItems.leatherStrip, 2)},
		                                          "##", "#/", " /",
		                                          '#', "stone",
		                                          '/', "stickWood");
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.stone_axe), true,
		                                          new Object[] {new ItemStack(RegisterItems.leatherStrip, 2)},
		                                          "##", "/#", "/ ",
		                                          '#', "stone",
		                                          '/', "stickWood");
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.stone_hoe),
		                                          new Object[] {new ItemStack(RegisterItems.leatherStrip, 1)},
		                                          "##", " /", " /",
		                                          '#', "stone",
		                                          '/', "stickWood");
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.stone_hoe), true,
		                                          new Object[] {new ItemStack(RegisterItems.leatherStrip, 1)},
		                                          "##", "/ ", "/ ",
		                                          '#', "stone",
		                                          '/', "stickWood");

		if (BBConfig.allowStringAsToolBinding)
		{
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.stone_pickaxe),
				new Object[] {"itemStringTough", 4},
				"###", " / ", " / ",
				'#', "stone",
				'/', "stickWood");
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.stone_sword),
				new Object[] {"itemStringTough", 4},
				"#", "#", "/",
				'#', "stone",
				'/', "stickWood");
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.stone_shovel),
				new Object[] {"itemStringTough", 2},
				"#", "/", "/",
				'#', "stone",
				'/', "stickWood");
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.stone_axe),
				new Object[] {"itemStringTough", 4},
				"##", "#/", " /",
				'#', "stone",
				'/', "stickWood");
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.stone_axe), true,
				new Object[] {"itemStringTough", 4},
				"##", "/#", "/ ",
				'#', "stone",
				'/', "stickWood");
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.stone_hoe),
				new Object[] {"itemStringTough", 2},
				"##", " /", " /",
				'#', "stone",
				'/', "stickWood");
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.stone_hoe), true,
				new Object[] {"itemStringTough", 2},
				"##", "/ ", "/ ",
				'#', "stone",
				'/', "stickWood");
		}

		// Iron armor
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.iron_helmet),
		                                          new Object[] {"nuggetIron", 2,
				                                          new ItemStack(RegisterItems.leatherStrip, 3),
				                                          new ItemStack(Blocks.wool, 2, OreDictionary.WILDCARD_VALUE)},
		                                          "III", "I I",
		                                          'I', "ingotIron");
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.iron_chestplate),
		                                          new Object[] {"nuggetIron", 6,
				                                          new ItemStack(RegisterItems.leatherStrip, 2),
														  new ItemStack(Blocks.wool, 4, OreDictionary.WILDCARD_VALUE)},
		                                          "I I", "III", "III",
		                                          'I', "ingotIron");
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.iron_leggings),
		                                          new Object[] {"nuggetIron", 4,
				                                          new ItemStack(RegisterItems.leatherStrip, 4),
														  new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE)},
		                                          "III", "I I", "I I",
		                                          'I', "ingotIron");
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.iron_boots),
		                                          new Object[] {"nuggetIron", 3,
				                                          new ItemStack(RegisterItems.leatherStrip, 2),
				                                          new ItemStack(Blocks.wool, 3, OreDictionary.WILDCARD_VALUE)},
		                                          "I I", "I I",
		                                          'I', "ingotIron");

		// Iron Tools/Weapons
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.iron_pickaxe),
		                                          new Object[] {new ItemStack(RegisterItems.leatherStrip, 3)},
		                                          "III", " / ", " / ",
		                                          'I', "ingotIron",
		                                          '/', "stickWood");
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.iron_sword),
		                                          new Object[] {new ItemStack(RegisterItems.leatherStrip, 2)},
		                                          "I", "I", "/",
		                                          'I', "ingotIron",
		                                          '/', "stickWood");
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.iron_shovel),
		                                          new Object[] {new ItemStack(RegisterItems.leatherStrip, 2)},
		                                          "I", "/", "/",
		                                          'I', "ingotIron",
		                                          '/', "stickWood");
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.iron_axe),
		                                          new Object[] {new ItemStack(RegisterItems.leatherStrip, 2)},
		                                          "II", "I/", " /",
		                                          'I', "ingotIron",
		                                          '/', "stickWood");
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.iron_axe), true,
		                                          new Object[] {new ItemStack(RegisterItems.leatherStrip, 2)},
		                                          "II", "/I", "/ ",
		                                          'I', "ingotIron",
		                                          '/', "stickWood");
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.iron_hoe),
		                                          new Object[] {new ItemStack(RegisterItems.leatherStrip, 1)},
		                                          "II", " /", " /",
		                                          'I', "ingotIron",
		                                          '/', "stickWood");
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.iron_hoe), true,
		                                          new Object[] {new ItemStack(RegisterItems.leatherStrip, 1)},
		                                          "II", "/ ", "/ ",
		                                          'I', "ingotIron",
		                                          '/', "stickWood");
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(RegisterItems.ironKnife), true,
		                                          new Object[] {new ItemStack(RegisterItems.leatherStrip, 2)},
		                                          " I", "/ ",
		                                          'I', "ingotIron",
		                                          '/', "stickWood");
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(RegisterItems.ironKnife),
		                                          new Object[] {new ItemStack(RegisterItems.leatherStrip, 2)},
		                                          "I ", " /",
		                                          'I', "ingotIron",
		                                          '/', "stickWood");

		if (BBConfig.allowStringAsToolBinding)
		{
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.iron_pickaxe),
				new Object[] {"itemStringTough", 4},
				"III", " / ", " / ",
				'I', "ingotIron",
				'/', "stickWood");
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.iron_sword),
				new Object[] {"itemStringTough", 4},
				"I", "I", "/",
				'I', "ingotIron",
				'/', "stickWood");
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.iron_shovel),
				new Object[] {"itemStringTough", 2},
				"I", "/", "/",
				'I', "ingotIron",
				'/', "stickWood");
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.iron_axe),
				new Object[] {"itemStringTough", 4},
				"II", "I/", " /",
				'I', "ingotIron",
				'/', "stickWood");
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.iron_axe), true,
				new Object[] {"itemStringTough", 4},
				"II", "/I", "/ ",
				'I', "ingotIron",
				'/', "stickWood");
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.iron_hoe),
				new Object[] {"itemStringTough", 2},
				"II", " /", " /",
				'I', "ingotIron",
				'/', "stickWood");
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.iron_hoe), true,
				new Object[] {"itemStringTough", 2},
				"II", "/ ", "/ ",
				'I', "ingotIron",
				'/', "stickWood");
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(RegisterItems.ironKnife), true,
				new Object[] {"itemStringTough", 4},
				" I", "/ ",
				'I', "ingotIron",
				'/', "stickWood");
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(RegisterItems.ironKnife),
				new Object[] {"itemStringTough", 4},
				"I ", " /",
				'I', "ingotIron",
				'/', "stickWood");
		}

		//Gold armor
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.golden_helmet),
		                                          new Object[] {new ItemStack(RegisterItems.leatherStrip, 3),
				                                          "nuggetGold", 2,
				                                          new ItemStack(Blocks.wool, 2, OreDictionary.WILDCARD_VALUE)},
		                                          "III", "I I",
		                                          'I', "ingotGold");
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.golden_chestplate),
		                                          new Object[] {new ItemStack(RegisterItems.leatherStrip, 2),
				                                          "nuggetGold", 6,
				                                          new ItemStack(Blocks.wool, 4, OreDictionary.WILDCARD_VALUE)},
		                                          "I I",
		                                          "III",
		                                          "III",
		                                          'I', "ingotGold");
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.golden_leggings),
		                                          new Object[] {new ItemStack(RegisterItems.leatherStrip, 4),
				                                          "nuggetGold", 4,
				                                          new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE)},
		                                          "III",
		                                          "I I",
		                                          "I I",
		                                          'I', "ingotGold");
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.golden_boots),
		                                          new Object[] {new ItemStack(RegisterItems.leatherStrip, 2),
				                                          "nuggetGold", 3,
				                                          new ItemStack(Blocks.wool, 3, OreDictionary.WILDCARD_VALUE)},
		                                          "I I", "I I",
		                                          'I', "ingotGold");


		//Gold Weapons/Tools
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.golden_pickaxe),
		                                          new Object[] {new ItemStack(RegisterItems.leatherStrip, 3)},
		                                          "III", " / ", " / ",
		                                          'I', "ingotGold",
		                                          '/', "stickWood");
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.golden_sword),
		                                          new Object[] {new ItemStack(RegisterItems.leatherStrip, 2)},
		                                          "I", "I", "/",
		                                          'I', "ingotGold",
		                                          '/', "stickWood");
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.golden_shovel),
		                                          new Object[] {new ItemStack(RegisterItems.leatherStrip, 2)},
		                                          "I", "/", "/",
		                                          'I', "ingotGold",
		                                          '/', "stickWood");
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.golden_axe),
		                                          new Object[] {new ItemStack(RegisterItems.leatherStrip, 2)},
		                                          "II", "I/", " /",
		                                          'I', "ingotGold",
		                                          '/', "stickWood");
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.golden_axe), true,
		                                          new Object[] {new ItemStack(RegisterItems.leatherStrip, 2)},
		                                          "II", "/I", "/ ",
		                                          'I', "ingotGold",
		                                          '/', "stickWood");
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.golden_hoe),
		                                          new Object[] {new ItemStack(RegisterItems.leatherStrip, 1)},
		                                          "II", " /", " /",
		                                          'I', Items.gold_ingot,
		                                          '/', Items.stick);
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.golden_hoe), true,
		                                          new Object[] {new ItemStack(RegisterItems.leatherStrip, 1)},
		                                          "II", "/ ", "/ ",
		                                          'I', "ingotGold",
		                                          '/', "stickWood");
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(RegisterItems.goldKnife), true,
		                                          new Object[] {new ItemStack(RegisterItems.leatherStrip, 2)},
		                                          " I", "/ ",
		                                          'I', "ingotGold",
		                                          '/', "stickWood");
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(RegisterItems.goldKnife),
		                                          new Object[] {new ItemStack(RegisterItems.leatherStrip, 2)},
		                                          "I ", " /",
		                                          'I', "ingotGold",
		                                          '/', "stickWood");

		if (BBConfig.allowStringAsToolBinding)
		{
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.golden_pickaxe),
				new Object[] {"itemStringTough", 6},
				"III", " / ", " / ",
				'I', "ingotGold",
				'/', "stickWood");
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.golden_sword),
				new Object[] {"itemStringTough", 4},
				"I", "I", "/",
				'I', "ingotGold",
				'/', "stickWood");
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.golden_shovel),
				new Object[] {"itemStringTough", 4},
				"I", "/", "/",
				'I', "ingotGold",
				'/', "stickWood");
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.golden_axe),
				new Object[] {"itemStringTough", 4},
				"II", "I/", " /",
				'I', "ingotGold",
				'/', "stickWood");
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.golden_axe), true,
				new Object[] {"itemStringTough", 4},
				"II", "/I", "/ ",
				'I', "ingotGold",
				'/', "stickWood");
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.golden_hoe),
				new Object[] {"itemStringTough", 2},
				"II", " /", " /",
				'I', "ingotGold",
				'/', "stickWood");
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.golden_hoe), true,
				new Object[] {"itemStringTough", 2},
				"II", "/ ", "/ ",
				'I', "ingotGold",
				'/', "stickWood");
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(RegisterItems.goldKnife), true,
				new Object[] {"itemStringTough", 4},
				" I", "/ ",
				'I', "ingotGold",
				'/', "stickWood");
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(RegisterItems.goldKnife),
				new Object[] {"itemStringTough", 4},
				"I ", " /",
				'I', "ingotGold",
				'/', "stickWood");
		}
		
		// Diamond armor
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.diamond_helmet),
		                                          new Object[] {"dustRedstone", 3,
				                                          new ItemStack(Items.blaze_powder, 2),
														  new ItemStack(RegisterItems.leatherStrip, 3),
														  new ItemStack(Blocks.wool, 2, OreDictionary.WILDCARD_VALUE)},
		                                          "DDD", "D D",
		                                          'D', "gemDiamond");
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.diamond_chestplate),
		                                          new Object[] {"dustRedstone", 4,
				                                          new ItemStack(Items.blaze_powder, 6),
														  new ItemStack(RegisterItems.leatherStrip, 2),
														  new ItemStack(Blocks.wool, 4, OreDictionary.WILDCARD_VALUE)},
		                                          "D D", "DDD", "DDD",
		                                          'D', "gemDiamond");
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.diamond_leggings),
		                                          new Object[] {"dustRedstone", 4,
				                                          new ItemStack(Items.blaze_powder, 3),
														  new ItemStack(RegisterItems.leatherStrip, 4),
														  new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE)},
		                                          "DDD", "D D", "D D",
		                                          'D', "gemDiamond");
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.diamond_boots),
		                                          new Object[] {"dustRedstone", 3,
				                                          new ItemStack(Items.blaze_powder, 3),
														  new ItemStack(RegisterItems.leatherStrip, 2),
														  new ItemStack(Blocks.wool, 3, OreDictionary.WILDCARD_VALUE)},
		                                          "D D", "D D",
		                                          'D', "gemDiamond");

		// Diamond Tools/Weapons
		if (BBConfig.requireBlazePowderForDiamondPick)
		{
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.diamond_pickaxe),
				new Object[] {"dustRedstone", 5, new ItemStack(Items.blaze_powder, 3),
					new ItemStack(RegisterItems.leatherStrip, 3)},
				"DDD", " / ", " / ",
				'D', "gemDiamond",
				'/', "stickWood");
		}
		else
		{
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.diamond_pickaxe),
				new Object[] {"dustRedstone", 5,
					new ItemStack(RegisterItems.leatherStrip, 3)},
				"DDD", " / ", " / ",
				'D', "gemDiamond",
				'/', "stickWood");
		}
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.diamond_sword),
		    new Object[] {"dustRedstone", 4,
			    new ItemStack(Items.blaze_powder, 5),
			    new ItemStack(RegisterItems.leatherStrip, 2)},
		    "D", "D", "/",
		    'D', "gemDiamond",
		    '/', "stickWood");
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.diamond_shovel),
			new Object[] {"dustRedstone", 2,
			    new ItemStack(Items.blaze_powder, 1),
			    new ItemStack(RegisterItems.leatherStrip, 2)},
			"D", "/", "/",
			'D', "gemDiamond",
			'/', "stickWood");
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.diamond_axe),
			new Object[] {"dustRedstone", 3,
			    new ItemStack(Items.blaze_powder, 2),
			    new ItemStack(RegisterItems.leatherStrip, 2)},
			"DD", "D/", " /",
			'D', "gemDiamond",
			'/', "stickWood");
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.diamond_axe), true,
			new Object[] {"dustRedstone", 3,
			    new ItemStack(Items.blaze_powder, 2),
			    new ItemStack(RegisterItems.leatherStrip, 2)},
			"DD", "/D", "/ ",
			'D', "gemDiamond",
			'/', "stickWood");
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.diamond_hoe),
			new Object[] {"dustRedstone", 1,
			    new ItemStack(Items.blaze_powder, 1),
			    new ItemStack(RegisterItems.leatherStrip, 1)},
			"DD", " /", " /",
			'D', "gemDiamond",
			'/', "stickWood");
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.diamond_hoe), true,
			new Object[] {"dustRedstone", 1,
			    new ItemStack(Items.blaze_powder, 1),
			    new ItemStack(RegisterItems.leatherStrip, 1)},
			"DD", "/ ", "/ ",
			'D', "gemDiamond",
			'/', "stickWood");
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(RegisterItems.diamondKnife), true,
		    new Object[] {"dustRedstone", 3,
			    new ItemStack(Items.blaze_powder, 3),
			    new ItemStack(RegisterItems.leatherStrip, 2)},
		    " D", "/ ",
		    'D', "gemDiamond",
		    '/', "stickWood");
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(RegisterItems.diamondKnife),
			new Object[] {"dustRedstone", 3,
			        new ItemStack(Items.blaze_powder, 3),
			        new ItemStack(RegisterItems.leatherStrip, 2)},
			"D ", " /",
			'D', "gemDiamond",
			'/', "stickWood");

		if (BBConfig.allowStringAsToolBinding)
		{
			if (BBConfig.requireBlazePowderForDiamondPick)
			{
				AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.diamond_pickaxe),
					new Object[] {"dustRedstone", 5, new ItemStack(Items.blaze_powder, 3),
						"itemStringTough", 4},
					"DDD", " / ", " / ",
					'D', "gemDiamond",
					'/', "stickWood");
			}
			else
			{
				AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.diamond_pickaxe),
					new Object[] {"dustRedstone", 5, "itemStringTough", 4},
					"DDD", " / ", " / ",
					'D', "gemDiamond",
					'/', "stickWood");
			}
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.diamond_sword),
				new Object[] {"dustRedstone", 4,
					new ItemStack(Items.blaze_powder, 5),
					"itemStringTough", 4},
				"D", "D", "/",
				'D', "gemDiamond",
				'/', "stickWood");
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.diamond_shovel),
				new Object[] {"dustRedstone", 2,
					new ItemStack(Items.blaze_powder, 1),
					"itemStringTough", 4},
				"D", "/", "/",
				'D', "gemDiamond",
				'/', "stickWood");
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.diamond_axe),
				new Object[] {"dustRedstone", 3,
					new ItemStack(Items.blaze_powder, 2),
					"itemStringTough", 4},
				"DD", "D/", " /",
				'D', "gemDiamond",
				'/', "stickWood");
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.diamond_axe), true,
				new Object[] {"dustRedstone", 3,
					new ItemStack(Items.blaze_powder, 2),
					"itemStringTough", 4},
				"DD", "/D", "/ ",
				'D', "gemDiamond",
				'/', "stickWood");
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.diamond_hoe),
				new Object[] {"dustRedstone", 1,
					new ItemStack(Items.blaze_powder, 1),
					"itemStringTough", 2},
				"DD", " /", " /",
				'D', "gemDiamond",
				'/', "stickWood");
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.diamond_hoe), true,
				new Object[] {"dustRedstone", 1,
					new ItemStack(Items.blaze_powder, 1),
					"itemStringTough", 2},
				"DD", "/ ", "/ ",
				'D', "gemDiamond",
				'/', "stickWood");
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(RegisterItems.diamondKnife), true,
				new Object[] {"dustRedstone", 3,
					new ItemStack(Items.blaze_powder, 3),
					"itemStringTough", 4},
				" D", "/ ",
				'D', "gemDiamond",
				'/', "stickWood");
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(RegisterItems.diamondKnife),
				new Object[] {"dustRedstone", 3,
					new ItemStack(Items.blaze_powder, 3),
					"itemStringTough", 4},
				"D ", " /",
				'D', "gemDiamond",
				'/', "stickWood");
		}

		// endregion Tools & Armor

		if (BBConfig.moduleInfusionRepair)
		{
			// Repair Infusion Station
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(RegisterBlocks.infusionRepairStation),
				new Object[]{
					"dustRedstone", 32, "gemLapis", 32,
					new ItemStack(Items.blaze_powder, 16)
				},
				"DID", "OSO", "OOO",
				'D', "gemDiamond",
				'I', "blockIron",
				'S', Blocks.bookshelf,
				'O', Blocks.obsidian);

			// Infusion Scroll
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(RegisterItems.infusionScroll),
				new Object[] { "gemDiamond", 2, "stickWood", 2 },
				"PPP", "RPR", "PPP",
				'P', Items.paper,
				'R', "dustRedstone");
		}

		if (BBConfig.moduleFurnaces)
		{
			// Obsidian Kiln
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(RegisterBlocks.obsidianKiln),
				new Object[]{"dustRedstone", 16},
				"ROR", "OKO", "ROR",
				'R', "dustRedstone",
				'O', Blocks.obsidian,
				'K', RegisterBlocks.kiln);

			// Nether Brick Oven
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(RegisterBlocks.netherBrickOven),
				new Object[]{
					new ItemStack(Items.blaze_powder, 4),
					new ItemStack(Blocks.obsidian, 1)
				},
				"NNN", "NBN", "NGN",
				'N', Blocks.nether_brick,
				'G', "blockGlassColorless",
				'B', RegisterBlocks.brickOven);

			// Ender Smelter
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(RegisterBlocks.enderSmelter),
				new Object[]{new ItemStack(Items.ender_pearl, 4), "nuggetIron", 4},
				"#E#", "#S#", "#E#",
				'#', Blocks.end_stone,
				'S', RegisterBlocks.smelter,
				'E', Items.ender_eye);

			if (Loader.isModLoaded("enderio") || Loader.isModLoaded("buildcraft") ||
				Loader.isModLoaded("cofhcore") || Loader.isModLoaded("immersiveengineering"))
			{
				AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(RegisterBlocks.redstoneKiln),
					new Object[]{ "dustRedstone", 32, "nuggetGold", 16, new ItemStack(Items.blaze_powder, 10) },
					"DED", "/K/", "DED",
					'K', RegisterBlocks.obsidianKiln,
					'/', Items.blaze_rod,
					'E', Items.ender_eye,
					'D', Items.diamond);
			}
		}

		// Rock Hammer
		AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(RegisterItems.rockHammer),
			new Object[] {new ItemStack(RegisterItems.leatherStrip, 2), "nuggetIron", 2},
		    "I#I", " / ", " / ",
		    'I', "ingotIron",
		    '#', "stone",
		    '/', "stickWood");

		if (BBConfig.moduleCampfire)
		{
			// Pan
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(RegisterItems.pan),
				new Object[]{"nuggetIron", 2,},
				"/  ", " II",
				'/', "stickWood",
				'I', "ingotIron");
		}

		if (BBConfig.netherlessBlazePowderRecipe)
		{
			AdvancedCraftingConfig.addAdvancedRecipe(new ItemStack(Items.blaze_powder, 4),
				new Object[]{new ItemStack(Items.gunpowder, 8), new ItemStack(Items.flint, 8)},
				"CRC", "RfR", "CRC",
				'C', "blockCoal",
				'R', "blockRedstone",
				'f', Items.flint_and_steel);
		}
	}

	public static void addShapedRecipes()
	{
		// Rotisserie
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegisterItems.rotisserie), "/s", "s/",
			'/', RegisterItems.roastingStick, 's', "itemStringTough"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegisterItems.rotisserie), "s/", "/s",
			'/', RegisterItems.roastingStick, 's', "itemStringTough"));

		// Knife
		GameRegistry.addShapedRecipe(new ItemStack(RegisterItems.flintKnife), "F ", " F", 'F', Items.flint);
		GameRegistry.addShapedRecipe(new ItemStack(RegisterItems.flintKnife), " F", "F ", 'F', Items.flint);

		GameRegistry.addShapedRecipe(new ItemStack(RegisterItems.boneKnife), " S", "/ ", 'S', RegisterItems.boneShard,
			'/', Items.bone);
		GameRegistry.addShapedRecipe(new ItemStack(RegisterItems.boneKnife), "S ", " /", 'S', RegisterItems.boneShard,
			'/', Items.bone);

		// Thread
		GameRegistry.addShapedRecipe(new ItemStack(RegisterItems.thread), "ss", "ss", 's', RegisterItems.silk);

		// Cloth
		GameRegistry.addShapedRecipe(new ItemStack(RegisterItems.cloth, 4), "ttt", "ttt", 't', RegisterItems.thread);

		// Lead (ore dictionary)
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.lead), "ss ", "s* ", "  s",
			's', "itemString", '*', Items.slime_ball));

		// Flint Hatchet
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegisterItems.flintHatchet), "FL", " /",
			'F', Items.flint, 'L', RegisterItems.leatherStrip, '/', "stickWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegisterItems.flintHatchet), "LF", "/ ",
			'F', Items.flint, 'L', RegisterItems.leatherStrip, '/', "stickWood"));

		// Noob Wood Sword
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegisterItems.noobWoodSword), "#", "#", "/",
		                                           '#', "plankWood", '/', "stickWood"));

		if (BBConfig.moduleFurnaces)
		{
			// Kiln
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegisterBlocks.kiln),
				"###", "# #", "###", '#', "cobblestone"));

			// Brick Oven
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegisterBlocks.brickOven), "BBB", "# #", "###",
				'#', Blocks.brick_block, 'B', "ingotBrick"));

			// Smelter
			GameRegistry.addShapedRecipe(new ItemStack(RegisterBlocks.smelter), "###", "#C#", "###",
				'#', new ItemStack(Blocks.stonebrick, 1, 0), 'C', new ItemStack(Items.coal, 1, 1));

			// Vanilla Furnace
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.furnace), "###", "# #", "---",
				'#', "cobblestone", '-', new ItemStack(Blocks.stone_slab, 1, 3))); // cobblestone slab
		}

		// Gravel->Flint
		GameRegistry.addShapedRecipe(new ItemStack(Items.flint), "##", "##", '#', Blocks.gravel);

		// Iron Nugget->Ingot
		GameRegistry.addShapedRecipe(new ItemStack(Items.iron_ingot), "***", "***", "***", '*',
													 RegisterItems.ironNugget);

		// Workbench
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegisterBlocks.doubleWorkbench), "##", "##", '#',
									 "plankWood"));

		if (BBConfig.canMakeChainArmor)
		{
			// Chain Armor
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.chainmail_helmet), "***", "* *", '*',
										 "nuggetIron"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.chainmail_chestplate), "* *", "***", "***", '*',
										 "nuggetIron"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.chainmail_leggings), "***", "* *", "* *", '*',
										 "nuggetIron"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.chainmail_boots), "* *", "* *", '*',
										 "nuggetIron"));
		}

		// Craft vanilla benches into BB workbenches
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegisterBlocks.doubleWorkbench, 2), "##",
			'#', "craftingTableWood"));

		// Craft BB workbenches into vanilla ones
		GameRegistry.addShapedRecipe(new ItemStack(Blocks.crafting_table), "#", '#', RegisterBlocks.doubleWorkbench);

		if (BBConfig.moduleCampfire) // campfire recipe is shapeless now
		{
			// Fire Bow
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegisterItems.fireBow),
				"//", "/s",
				'/', "stickWood",
				's', "itemString"));
		}
	}
}