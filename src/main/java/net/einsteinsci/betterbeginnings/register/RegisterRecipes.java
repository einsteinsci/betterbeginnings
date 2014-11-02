package net.einsteinsci.betterbeginnings.register;

import cpw.mods.fml.common.registry.GameRegistry;
import net.einsteinsci.betterbeginnings.config.BBConfig;
import net.einsteinsci.betterbeginnings.register.recipe.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.*;

public class RegisterRecipes
{
	public static void addShapelessRecipes()
	{
		// Sticks from Saplings & Knife
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.stick), "treeSapling", "itemKnife"));

		// Bone Shard
		GameRegistry.addRecipe(
				new ShapelessOreRecipe(new ItemStack(RegisterItems.boneShard, 2), Items.bone, "itemKnife"));

		// Leather Strip
		GameRegistry.addRecipe(
				new ShapelessOreRecipe(new ItemStack(RegisterItems.leatherStrip, 3), Items.leather, "itemKnife"));

		// Bonemeal from Bone Shard (a bit more rewarding)
		GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, 2, 15), RegisterItems.boneShard);

		// Iron Nugget
		GameRegistry.addShapelessRecipe(new ItemStack(RegisterItems.ironNugget, 9), Items.iron_ingot);

		// String
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.string, 4),
		                                              new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE),
		                                              "itemKnife"));

		// Twine
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(RegisterItems.twine, 2), Blocks.vine, "itemKnife"));
	}

	public static void addFurnaceRecipes()
	{
		addKilnRecipes();
		addBrickOvenRecipes();
		addSmelterRecipes();
	}

	private static void addKilnRecipes()
	{
		KilnRecipes.addRecipe(Items.clay_ball, new ItemStack(Items.brick), 0.35f);
		KilnRecipes.addRecipe(Blocks.clay, new ItemStack(Blocks.hardened_clay), 0.1f);
		KilnRecipes.addRecipe(Blocks.cobblestone, new ItemStack(Blocks.stone), 0.1f);
		KilnRecipes.addRecipe(Blocks.log, new ItemStack(Items.coal, 1, 1), 0.15f);
		KilnRecipes.addRecipe(Blocks.log2, new ItemStack(Items.coal, 1, 1), 0.15f);
		KilnRecipes.addRecipe(Blocks.sand, new ItemStack(Blocks.glass), 0.1f);
		KilnRecipes.addRecipe(Items.beef, new ItemStack(RegisterItems.charredMeat), 0.1f);
		KilnRecipes.addRecipe(Items.porkchop, new ItemStack(RegisterItems.charredMeat), 0.1f);
		KilnRecipes.addRecipe(Items.chicken, new ItemStack(RegisterItems.charredMeat), 0.1f);
		KilnRecipes.addRecipe(new ItemStack(Items.fish, 1, 0), new ItemStack(RegisterItems.charredMeat), 0.1f);
		KilnRecipes.addRecipe(new ItemStack(Items.fish, 1, 1), new ItemStack(RegisterItems.charredMeat), 0.1f);
	}

	private static void addBrickOvenRecipes()
	{
		BrickOvenRecipeHandler.addShapedRecipe(new ItemStack(Items.golden_apple), "GGG", "GAG", "GGG", 'G',
											   Items.gold_ingot, 'A', Items.apple);
		BrickOvenRecipeHandler.addShapedRecipe(new ItemStack(Items.golden_apple, 1, 1), "###", "#A#", "###", '#',
											   Blocks.gold_block, 'A', Items.apple);
		BrickOvenRecipeHandler.addShapedRecipe(new ItemStack(Items.golden_carrot), "***", "*C*", "***", '*',
											   Items.gold_nugget, 'C', Items.carrot);
		BrickOvenRecipeHandler.addShapedRecipe(new ItemStack(Items.cake), "MMM", "SES", "WWW", 'M', Items.milk_bucket,
											   'S', Items.sugar, 'E', Items.egg, 'W', Items.wheat);
		BrickOvenRecipeHandler.addShapedRecipe(new ItemStack(Items.bread, 2), "WWW", 'W', Items.wheat);
		BrickOvenRecipeHandler.addShapedRecipe(new ItemStack(Items.cookie, 8), "WCW", 'W', Items.wheat, 'C',
											   new ItemStack(Items.dye, 1, 3));

		BrickOvenRecipeHandler.addShapelessRecipe(new ItemStack(Items.mushroom_stew), Blocks.brown_mushroom,
												  Blocks.red_mushroom, Items.bowl);
		BrickOvenRecipeHandler.addShapelessRecipe(new ItemStack(Items.cooked_beef), Items.beef);
		BrickOvenRecipeHandler.addShapelessRecipe(new ItemStack(Items.cooked_porkchop), Items.porkchop);
		BrickOvenRecipeHandler.addShapelessRecipe(new ItemStack(Items.cooked_chicken), Items.chicken);
		BrickOvenRecipeHandler.addShapelessRecipe(new ItemStack(Items.cooked_fished), Items.fish);
		BrickOvenRecipeHandler.addShapelessRecipe(new ItemStack(Items.cooked_fished, 1, 1), new ItemStack(Items.fish,
																										  1, 1));
		BrickOvenRecipeHandler.addShapelessRecipe(new ItemStack(Items.baked_potato), Items.potato);
		BrickOvenRecipeHandler.addShapelessRecipe(new ItemStack(Items.pumpkin_pie), Items.egg, Items.sugar,
												  Blocks.pumpkin);
		BrickOvenRecipeHandler.addShapelessRecipe(new ItemStack(Items.fermented_spider_eye), Items.spider_eye,
												  Items.sugar, Blocks.brown_mushroom);
		BrickOvenRecipeHandler
				.addShapelessRecipe(new ItemStack(Items.magma_cream), Items.slime_ball, Items.blaze_powder);
		//BrickOvenRecipeHandler.addShapelessRecipe(new ItemStack(RegisterItems.roastingStickcookedMallow), RegisterItems.roastingStickrawMallow);
	}

	private static void addSmelterRecipes()
	{
		RegisterHelper.registerSmelterOreRecipe("oreIron", new ItemStack(Items.iron_ingot), 0.7f, 1, 1, 0.3f);
		RegisterHelper.registerSmelterOreRecipe("oreGold", new ItemStack(Items.gold_ingot), 1.0f, 2, 1, 0.2f);

		// Recipes that might be better suited in Kiln only
		if (BBConfig.canSmelterDoKilnStuff)
		{
			SmelterRecipeHandler
					.addRecipe(new ItemStack(Blocks.sand, 1, 0), new ItemStack(Blocks.glass), 0.1f, 1, 0, 0.0f);
			SmelterRecipeHandler
					.addRecipe(new ItemStack(Blocks.sand, 1, 1), new ItemStack(Blocks.stained_glass, 1, 1),
					           0.1f, 1, 0, 0.0f); // Red sand makes orange stained glass.
			SmelterRecipeHandler.addRecipe(Blocks.netherrack, new ItemStack(Items.netherbrick), 0.25f, 1, 1, 0.25f);
			SmelterRecipeHandler.addRecipe(Blocks.stonebrick, new ItemStack(Blocks.stonebrick, 1, 2), 0.1f, 1, 0, 0.0f);

			RegisterHelper.registerSmelterOreRecipe("cobblestone", new ItemStack(Blocks.stone), 0.1f, 0, 0, 0.0f);
			SmelterRecipeHandler.addRecipe(Items.clay_ball, new ItemStack(Items.brick), 0.3f, 0, 0, 0.0f);
			SmelterRecipeHandler.addRecipe(Blocks.clay, new ItemStack(Blocks.hardened_clay), 0.35f, 0, 0, 0.0f);
		}

		// Silk touch recipes
		RegisterHelper.registerSmelterOreRecipe("oreCoal", new ItemStack(Items.coal, 1), 0.25f, 2, 2, 0.8f);
		RegisterHelper.registerSmelterOreRecipe("oreQuartz", new ItemStack(Items.quartz, 2), 0.4f, 2, 4, 0.6f);
		RegisterHelper.registerSmelterOreRecipe("oreLapis", new ItemStack(Items.dye, 8, 4), 0.5f, 2, 8, 0.8f);
		RegisterHelper.registerSmelterOreRecipe("oreRedstone", new ItemStack(Items.redstone, 4), 0.8f, 2, 4, 0.6f);
		RegisterHelper.registerSmelterOreRecipe("oreDiamond", new ItemStack(Items.diamond, 1), 1.0f, 3, 2, 0.3f);
		RegisterHelper.registerSmelterOreRecipe("oreEmerald", new ItemStack(Items.emerald, 1), 1.0f, 3, 2, 0.5f);
	}

	public static void addAdvancedRecipes()
	{
		if (BBConfig.advancedCraftingForLotsOfThings)
		{
			// Wooden Door
			AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.wooden_door),
													  new ItemStack[] {new ItemStack(RegisterItems.leatherStrip, 2)},
													  "##",
													  "##",
													  "##",
													  '#',
													  new ItemStack(
															  Blocks.planks, 1, OreDictionary.WILDCARD_VALUE));
			// Iron Door
			AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.iron_door), new ItemStack[] {new ItemStack(
					RegisterItems.ironNugget, 3)}, "II", "II", "II", 'I', Items.iron_ingot);
			// Fence Gate
			AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Blocks.fence_gate),
													  new ItemStack[] {
															  new ItemStack(Items.string, 4), new ItemStack(
															  RegisterItems.leatherStrip,
															  4)},
													  "/#/",
													  "/#/",
													  '/',
													  Items.stick,
													  '#',
													  new ItemStack(Blocks.planks, 1, OreDictionary.WILDCARD_VALUE));
			// Trapdoor
			AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Blocks.trapdoor), new ItemStack[] {new ItemStack(
					RegisterItems.leatherStrip, 2)}, "###", "###", '#', new ItemStack(Blocks.planks, 1,
																					  OreDictionary.WILDCARD_VALUE));
			// Chest. Yep, you need iron before you can make a chest. If you absolutely must store stuff before you have
			// iron, use your kiln (provided it isn't kiln-able).
			AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Blocks.chest),
													  new ItemStack[] {
															  new ItemStack(RegisterItems.ironNugget, 3), new ItemStack(
															  RegisterItems.leatherStrip,
															  1)},
													  "###",
													  "# #",
													  "###",
													  '#',
													  new ItemStack(Blocks.planks, 1, OreDictionary.WILDCARD_VALUE));
			// Trapped Chest
			AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Blocks.trapped_chest),
													  new ItemStack[] {
															  new ItemStack(RegisterItems.thread,
																			2), new ItemStack(Items.redstone, 1)},
													  "C",
													  "H",
													  'C',
													  Blocks.chest,
													  'H',
													  Blocks.tripwire_hook);
			// Tripwire Hook
			AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Blocks.tripwire_hook),
													  new ItemStack[] {
															  new ItemStack(RegisterItems.thread,
																			1), new ItemStack(Items.redstone, 1)},
													  "I",
													  "/",
													  "#",
													  'I',
													  Items.iron_ingot,
													  '/',
													  Items.stick,
													  '#',
													  new ItemStack(Blocks.planks, 1, OreDictionary.WILDCARD_VALUE));
			// Piston
			AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Blocks.piston),
													  new ItemStack[] {new ItemStack(
															  RegisterItems.ironNugget, 2)},
													  "###",
													  "CIC",
													  "CRC",
													  '#',
													  new ItemStack(Blocks.planks, 1,
																	OreDictionary.WILDCARD_VALUE),
													  'I',
													  Items.iron_ingot,
													  'C',
													  Blocks.cobblestone,
													  'R',
													  Items.redstone);
			// Dispenser
			AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Blocks.dispenser),
			                                          new ItemStack[] {new ItemStack(RegisterItems.ironNugget, 2)},
			                                          "###", "#B#", "#R#",
			                                          '#', Blocks.cobblestone,
			                                          'B', new ItemStack(Items.bow, 1, 0),
			                                          'R', Items.redstone);
			// Note Block
			AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Blocks.noteblock),
			                                          new ItemStack[] {new ItemStack(Items.string, 2)},
			                                          "###", "#R#", "###",
			                                          '#',
			                                          new ItemStack(Blocks.planks, 1, OreDictionary.WILDCARD_VALUE),
			                                          'R', Items.redstone);
			// Gold Rail
			AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Blocks.golden_rail, 6),
			                                          new ItemStack[] {new ItemStack(Items.gold_nugget, 2)},
			                                          "G G", "G/G", "GRG",
			                                          'G', Items.gold_ingot,
			                                          '/', Items.stick,
			                                          'R', Items.redstone);
			// Detector Rail
			AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Blocks.detector_rail, 6),
			                                          new ItemStack[] {new ItemStack(RegisterItems.ironNugget, 2),
					                                          new ItemStack(Items.stick, 4)},
			                                          "I I", "I_I", "IRI",
			                                          'I', Items.iron_ingot,
			                                          '_', Blocks.stone_pressure_plate,
			                                          'R', Items.redstone);
			// TNT
			AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Blocks.tnt, 2),
			                                          new ItemStack[] {new ItemStack(Items.string, 3)},
			                                          "G#G", "#G#", "G#G",
			                                          'G', Items.gunpowder,
			                                          '#', Blocks.sand);
			// Bookshelf
			AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Blocks.bookshelf),
			                                          new ItemStack[] {new ItemStack(RegisterItems.ironNugget, 1)},
			                                          "###", "BBB", "###",
			                                          '#',
			                                          new ItemStack(Blocks.planks, 1, OreDictionary.WILDCARD_VALUE),
			                                          'B', Items.book);
			// Ladder
			AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Blocks.ladder, 4),
			                                          new ItemStack[] {new ItemStack(Items.string, 1)},
			                                          "/ /", "///", "/ /",
			                                          '/', Items.stick);
			// Rail
			AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Blocks.rail, 16),
			                                          new ItemStack[] {new ItemStack(RegisterItems.ironNugget, 1)},
			                                          "I I", "I/I", "I I",
			                                          'I', Items.iron_ingot,
			                                          '/', Items.stick);
			// Enchanting Table
			AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Blocks.enchanting_table),
			                                          new ItemStack[] {new ItemStack(Items.leather),
					                                          new ItemStack(Items.dye, 1, 1),  //Rose Red
					                                          new ItemStack(Items.dye, 1, 4)}, //Lapis
			                                          " B ", "D#D", "###",
			                                          'B', Items.book,
			                                          'D', Items.diamond,
			                                          '#', Blocks.obsidian);
			// Beacon
			AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Blocks.beacon),
			                                          new ItemStack[] {new ItemStack(Blocks.diamond_block, 1),
					                                          new ItemStack(Items.blaze_rod, 4),
					                                          new ItemStack(Items.potionitem, 1, 16)}, //Awkward potion
			                                          "+++", "+S+", "###",
			                                          '+', Blocks.glass,
			                                          'S', Items.nether_star,
			                                          '#', Blocks.obsidian);
			// Anvil
			AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Blocks.anvil),
			                                          new ItemStack[] {new ItemStack(RegisterItems.ironNugget, 4)},
			                                          "###", " I ", "III",
			                                          '#', Blocks.iron_block,
			                                          'I', Items.iron_ingot);
			// Hopper
			AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Blocks.hopper, 4),
			                                          new ItemStack[] {new ItemStack(RegisterItems.ironNugget, 2),
					                                          new ItemStack(Blocks.stone_pressure_plate, 1)},
			                                          "I I", "I#I", " I ",
			                                          'I', Items.iron_ingot,
			                                          '#', Blocks.chest);
			// Activator Rail
			AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Blocks.activator_rail, 6),
			                                          new ItemStack[] {new ItemStack(RegisterItems.ironNugget, 4)},
			                                          "I/I", "IiI", "I/I",
			                                          'I', Items.iron_ingot,
			                                          '/', Items.stick,
			                                          'i', Blocks.redstone_torch);
			// Dropper
			AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Blocks.dropper),
			                                          new ItemStack[] {new ItemStack(RegisterItems.ironNugget, 2)},
			                                          "###", "# #", "#R#",
			                                          '#', Blocks.cobblestone,
			                                          'R', Items.redstone);
			// Minecart
			AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.minecart),
			                                          new ItemStack[] {new ItemStack(RegisterItems.ironNugget, 2),
					                                          new ItemStack(RegisterItems.leatherStrip, 1)},
			                                          "I I", "III",
			                                          'I', Items.iron_ingot);
			// Compass
			AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.compass),
			                                          new ItemStack[] {new ItemStack(RegisterItems.ironNugget, 3),
					                                          new ItemStack(Items.potionitem, 1, 0)}, //Water Bottle
			                                          " I ", "IRI", " I ",
			                                          'I', Items.iron_ingot,
			                                          'R', Items.redstone);
			// Clock
			AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.clock),
			                                          new ItemStack[] {new ItemStack(Items.gold_nugget, 3),
					                                          new ItemStack(Items.dye, 1, 0)}, //Ink sac
			                                          " G ", "GRG", " G ",
			                                          'G', Items.gold_ingot,
			                                          'R', Items.redstone);
			// Bed
			AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.bed),
			                                          new ItemStack[] {new ItemStack(RegisterItems.ironNugget, 2)},
			                                          "***", "###",
			                                          '*', new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE),
			                                          '#',
			                                          new ItemStack(Blocks.planks, 1, OreDictionary.WILDCARD_VALUE));
			// Brewing Stand
			AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.brewing_stand),
			                                          new ItemStack[] {new ItemStack(Items.gold_nugget, 1),
					                                          new ItemStack(RegisterItems.ironNugget, 3)},
			                                          " / ", "###",
			                                          '/', Items.blaze_rod,
			                                          '#', Blocks.cobblestone);
			// Cauldron
			AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.cauldron),
			                                          new ItemStack[] {new ItemStack(RegisterItems.ironNugget, 3)},
			                                          "I I", "I I", "III",
			                                          'I', Items.iron_ingot);
		}

		// Bow
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.bow), new ItemStack[] {new ItemStack(
				RegisterItems.leatherStrip, 3)}, " /s", "/ s", " /s", 's', Items.string, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.bow), new ItemStack[] {new ItemStack(
				RegisterItems.leatherStrip, 3)}, "s/ ", "s /", "s/ ", 's', Items.string, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.bow), new ItemStack[] {new ItemStack(
				RegisterItems.leatherStrip, 3)}, " /s", "/ s", " /s", 's', RegisterItems.thread, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.bow), new ItemStack[] {new ItemStack(
				RegisterItems.leatherStrip, 3)}, "s/ ", "s /", "s/ ", 's', RegisterItems.thread, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.bow), new ItemStack[] {new ItemStack(
				RegisterItems.leatherStrip, 3)}, " /s", "/ s", " /s", 's', RegisterItems.twine, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.bow), new ItemStack[] {new ItemStack(
				RegisterItems.leatherStrip, 3)}, "s/ ", "s /", "s/ ", 's', RegisterItems.twine, '/', Items.stick);

		// Fishing rod
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.fishing_rod), new ItemStack[] {new ItemStack(
				RegisterItems.ironNugget)}, "  /", " /s", "/ s", '/', Items.stick, 's', Items.string);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.fishing_rod), new ItemStack[] {new ItemStack(
				RegisterItems.ironNugget)}, "  /", " /s", "/ s", '/', Items.stick, 's', RegisterItems.thread);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.fishing_rod), new ItemStack[] {new ItemStack(
				RegisterItems.ironNugget)}, "  /", " /s", "/ s", '/', Items.stick, 's', RegisterItems.twine);

		// Bone Pickaxe
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(RegisterItems.bonePickaxe),
												  new ItemStack[] {new ItemStack(RegisterItems.leatherStrip, 2)},
												  "sBs",
												  " / ",
												  " / ",
												  's',
												  RegisterItems.boneShard,
												  'B',
												  Items.bone,
												  '/',
												  Items.stick);

		// Leather armor
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.leather_helmet),
												  new ItemStack[] {
														  new ItemStack(Items.string, 3), new ItemStack(Blocks.wool,
																										2,
																										OreDictionary.WILDCARD_VALUE)},
												  "LLL",
												  "L L",
												  'L',
												  Items.leather);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.leather_chestplate),
												  new ItemStack[] {
														  new ItemStack(Items.string, 2), new ItemStack(Blocks.wool,
																										4,
																										OreDictionary.WILDCARD_VALUE)},
												  "L L",
												  "LLL",
												  "LLL",
												  'L',
												  Items.leather);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.leather_leggings),
												  new ItemStack[] {
														  new ItemStack(Items.string, 4), new ItemStack(Blocks.wool,
																										1,
																										OreDictionary.WILDCARD_VALUE)},
												  "LLL",
												  "L L",
												  "L L",
												  'L',
												  Items.leather);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.leather_boots),
												  new ItemStack[] {
														  new ItemStack(Items.string, 2), new ItemStack(Blocks.wool,
																										3,
																										OreDictionary.WILDCARD_VALUE)},
												  "L L",
												  "L L",
												  'L',
												  Items.leather);

		// Stone Tools/Weapons
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.stone_pickaxe), new ItemStack[] {new ItemStack(
				RegisterItems.leatherStrip, 2)}, "###", " / ", " / ", '#', Blocks.stone, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.stone_sword), new ItemStack[] {new ItemStack(
				RegisterItems.leatherStrip, 2)}, "#", "#", "/", '#', Blocks.stone, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.stone_shovel), new ItemStack[] {new ItemStack(
				RegisterItems.leatherStrip, 1)}, "#", "/", "/", '#', Blocks.stone, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.stone_axe), new ItemStack[] {new ItemStack(
				RegisterItems.leatherStrip, 1)}, "##", "#/", " /", '#', Blocks.stone, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.stone_axe), new ItemStack[] {new ItemStack(
				RegisterItems.leatherStrip, 1)}, "##", "/#", "/ ", '#', Blocks.stone, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.stone_hoe), new ItemStack[] {new ItemStack(
				RegisterItems.leatherStrip, 1)}, "##", " /", " /", '#', Blocks.stone, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.stone_hoe), new ItemStack[] {new ItemStack(
				RegisterItems.leatherStrip, 1)}, "##", "/ ", "/ ", '#', Blocks.stone, '/', Items.stick);

		// Iron armor
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.iron_helmet), new ItemStack[] {
				new ItemStack(RegisterItems.leatherStrip, 3), new ItemStack(RegisterItems.ironNugget, 2),
				new ItemStack(Blocks.wool, 2, OreDictionary.WILDCARD_VALUE)}, "III", "I I", 'I', Items.iron_ingot);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.iron_chestplate),
												  new ItemStack[] {
														  new ItemStack(RegisterItems.leatherStrip, 2), new ItemStack(
														  RegisterItems.ironNugget,
														  6),
														  new ItemStack(Blocks.wool, 4, OreDictionary.WILDCARD_VALUE)},
												  "I I",
												  "III",
												  "III",
												  'I',
												  Items.iron_ingot);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.iron_leggings),
												  new ItemStack[] {
														  new ItemStack(RegisterItems.leatherStrip, 4), new ItemStack(
														  RegisterItems.ironNugget,
														  4),
														  new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE)},
												  "III",
												  "I I",
												  "I I",
												  'I',
												  Items.iron_ingot);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.iron_boots), new ItemStack[] {
				new ItemStack(RegisterItems.leatherStrip, 2), new ItemStack(RegisterItems.ironNugget, 3),
				new ItemStack(Blocks.wool, 3, OreDictionary.WILDCARD_VALUE)}, "I I", "I I", 'I', Items.iron_ingot);

		// Iron Tools/Weapons
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.iron_pickaxe), new ItemStack[] {new ItemStack(
				RegisterItems.leatherStrip, 3)}, "III", " / ", " / ", 'I', Items.iron_ingot, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.iron_sword), new ItemStack[] {new ItemStack(
				RegisterItems.leatherStrip, 2)}, "I", "I", "/", 'I', Items.iron_ingot, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.iron_shovel), new ItemStack[] {new ItemStack(
				RegisterItems.leatherStrip, 2)}, "I", "/", "/", 'I', Items.iron_ingot, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.iron_axe), new ItemStack[] {new ItemStack(
				RegisterItems.leatherStrip, 2)}, "II", "I/", " /", 'I', Items.iron_ingot, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.iron_axe), new ItemStack[] {new ItemStack(
				RegisterItems.leatherStrip, 2)}, "II", "/I", "/ ", 'I', Items.iron_ingot, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.iron_hoe), new ItemStack[] {new ItemStack(
				RegisterItems.leatherStrip, 1)}, "II", " /", " /", 'I', Items.iron_ingot, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.iron_hoe), new ItemStack[] {new ItemStack(
				RegisterItems.leatherStrip, 1)}, "II", "/ ", "/ ", 'I', Items.iron_ingot, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(RegisterItems.ironKnife),
												  new ItemStack[] {new ItemStack(RegisterItems.leatherStrip, 2)},
												  " I",
												  "/ ",
												  'I',
												  Items.iron_ingot,
												  '/',
												  Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(RegisterItems.ironKnife),
												  new ItemStack[] {new ItemStack(RegisterItems.leatherStrip, 2)},
												  "I ",
												  " /",
												  'I',
												  Items.iron_ingot,
												  '/',
												  Items.stick);
		//Gold armor
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.golden_helmet), new ItemStack[] {
			new ItemStack(RegisterItems.leatherStrip, 3), new ItemStack(Items.gold_nugget, 2),
			new ItemStack(Blocks.wool, 2, OreDictionary.WILDCARD_VALUE)}, "III", "I I", 'I', Items.gold_ingot);
	AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.golden_chestplate),
											  new ItemStack[] {
													  new ItemStack(RegisterItems.leatherStrip, 2), new ItemStack(
													  Items.gold_nugget,
													  6),
													  new ItemStack(Blocks.wool, 4, OreDictionary.WILDCARD_VALUE)},
											  "I I",
											  "III",
											  "III",
											  'I',
											  Items.gold_ingot);
	AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.golden_leggings),
											  new ItemStack[] {
													  new ItemStack(RegisterItems.leatherStrip, 4), new ItemStack(
													  Items.gold_nugget,
													  4),
													  new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE)},
											  "III",
											  "I I",
											  "I I",
											  'I',
											  Items.gold_ingot);
	AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.golden_boots), new ItemStack[] {
			new ItemStack(RegisterItems.leatherStrip, 2), new ItemStack(Items.gold_nugget, 3),
			new ItemStack(Blocks.wool, 3, OreDictionary.WILDCARD_VALUE)}, "I I", "I I", 'I', Items.gold_ingot);


		//Gold Weapons/Tools
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.golden_pickaxe), new ItemStack[] {new ItemStack(
				RegisterItems.leatherStrip, 3)}, "III", " / ", " / ", 'I', Items.gold_ingot, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.golden_sword), new ItemStack[] {new ItemStack(
				RegisterItems.leatherStrip, 2)}, "I", "I", "/", 'I', Items.gold_ingot, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.golden_shovel), new ItemStack[] {new ItemStack(
				RegisterItems.leatherStrip, 2)}, "I", "/", "/", 'I', Items.gold_ingot, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.golden_axe), new ItemStack[] {new ItemStack(
				RegisterItems.leatherStrip, 2)}, "II", "I/", " /", 'I', Items.gold_ingot, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.golden_axe), new ItemStack[] {new ItemStack(
				RegisterItems.leatherStrip, 2)}, "II", "/I", "/ ", 'I', Items.gold_ingot, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.golden_hoe), new ItemStack[] {new ItemStack(
				RegisterItems.leatherStrip, 1)}, "II", " /", " /", 'I', Items.gold_ingot, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.golden_hoe), new ItemStack[] {new ItemStack(
				RegisterItems.leatherStrip, 1)}, "II", "/ ", "/ ", 'I', Items.gold_ingot, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(RegisterItems.goldKnife),
												  new ItemStack[] {new ItemStack(RegisterItems.leatherStrip, 2)},
												  " I",
												  "/ ",
												  'I',
												  Items.gold_ingot,
												  '/',
												  Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(RegisterItems.goldKnife),
												  new ItemStack[] {new ItemStack(RegisterItems.leatherStrip, 2)},
												  "I ",
												  " /",
												  'I',
												  Items.gold_ingot,
												  '/',
												  Items.stick);
		
		// Diamond armor
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.diamond_helmet),
												  new ItemStack[] {new ItemStack(Items.redstone,
																				 3), new ItemStack(Items.blaze_powder,
																								   2),
														  new ItemStack(RegisterItems.leatherStrip, 3),
														  new ItemStack(Blocks.wool, 2, OreDictionary.WILDCARD_VALUE)},
												  "DDD",
												  "D D",
												  'D',
												  Items.diamond);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.diamond_chestplate),
												  new ItemStack[] {new ItemStack(Items.redstone,
																				 4), new ItemStack(Items.blaze_powder,
																								   6),
														  new ItemStack(RegisterItems.leatherStrip, 2),
														  new ItemStack(Blocks.wool, 4, OreDictionary.WILDCARD_VALUE)},
												  "D D",
												  "DDD",
												  "DDD",
												  'D',
												  Items.diamond);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.diamond_leggings),
												  new ItemStack[] {new ItemStack(Items.redstone,
																				 4), new ItemStack(Items.blaze_powder,
																								   3),
														  new ItemStack(RegisterItems.leatherStrip, 4),
														  new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE)},
												  "DDD",
												  "D D",
												  "D D",
												  'D',
												  Items.diamond);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.diamond_boots),
												  new ItemStack[] {new ItemStack(Items.redstone,
																				 2), new ItemStack(Items.blaze_powder,
																								   3),
														  new ItemStack(RegisterItems.leatherStrip, 2),
														  new ItemStack(Blocks.wool, 3, OreDictionary.WILDCARD_VALUE)},
												  "D D",
												  "D D",
												  'D',
												  Items.diamond);

		// Diamond Tools/Weapons
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.diamond_pickaxe),
												  new ItemStack[] {
														  new ItemStack(Items.redstone, 5),
														  new ItemStack(RegisterItems.leatherStrip, 3)},
												  "DDD",
												  " / ",
												  " / ",
												  'D', Items.diamond,
												  '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.diamond_sword), new ItemStack[] {
				new ItemStack(Items.redstone, 4), new ItemStack(Items.blaze_powder, 5),
				new ItemStack(RegisterItems.leatherStrip, 2)}, "D", "D", "/", 'D', Items.diamond, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.diamond_shovel), new ItemStack[] {
				new ItemStack(Items.redstone, 2), new ItemStack(Items.blaze_powder, 1),
				new ItemStack(RegisterItems.leatherStrip, 2)}, "D", "/", "/", 'D', Items.diamond, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.diamond_axe), new ItemStack[] {
				new ItemStack(Items.redstone, 3), new ItemStack(Items.blaze_powder, 2),
				new ItemStack(RegisterItems.leatherStrip, 2)}, "DD", "D/", " /", 'D', Items.diamond, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.diamond_axe), new ItemStack[] {
				new ItemStack(Items.redstone, 3), new ItemStack(Items.blaze_powder, 2),
				new ItemStack(RegisterItems.leatherStrip, 2)}, "DD", "/D", "/ ", 'D', Items.diamond, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.diamond_hoe), new ItemStack[] {
				new ItemStack(Items.redstone, 1), new ItemStack(Items.blaze_powder, 1),
				new ItemStack(RegisterItems.leatherStrip, 1)}, "DD", " /", " /", 'D', Items.diamond, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.diamond_hoe), new ItemStack[] {
				new ItemStack(Items.redstone, 1), new ItemStack(Items.blaze_powder, 1),
				new ItemStack(RegisterItems.leatherStrip, 1)}, "DD", "/ ", "/ ", 'D', Items.diamond, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(RegisterItems.diamondKnife), new ItemStack[] {
				new ItemStack(Items.redstone, 3), new ItemStack(Items.blaze_powder, 3),
				new ItemStack(RegisterItems.leatherStrip, 1)}, " D", "/ ", 'D', Items.diamond, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(RegisterItems.diamondKnife), new ItemStack[] {
				new ItemStack(Items.redstone, 3), new ItemStack(Items.blaze_powder, 3),
				new ItemStack(RegisterItems.leatherStrip, 1)}, "D ", " /", 'D', Items.diamond, '/', Items.stick);

		// Repair Infusion Station
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(RegisterBlocks.infusionRepairStation),
		                                          new ItemStack[] {new ItemStack(Items.redstone, 32),
				                                          new ItemStack(Items.blaze_powder, 16),
														  new ItemStack(Items.dye, 32, 4)},
												  "DID",
												  "OSO",
												  "OOO",
												  'D', Items.diamond, 'I', Blocks.iron_block,
												  'S', Blocks.bookshelf, 'O', Blocks.obsidian);

		// Obsidian Kiln
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(RegisterBlocks.obsidianKiln),
		                                          new ItemStack[] {new ItemStack(Items.redstone, 16)},
		                                          "ROR",
		                                          "OKO",
		                                          "ROR",
		                                          'R', Items.redstone, 'O', Blocks.obsidian, 'K', RegisterBlocks.kiln);

		// Nether Brick Oven
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(RegisterBlocks.netherBrickOven),
		                                          new ItemStack[] {new ItemStack(Items.blaze_powder, 4), new ItemStack(
				                                          Blocks.obsidian, 1)},
		                                          "NNN",
		                                          "NBN",
		                                          "NGN",
		                                          'N', Blocks.nether_brick, 'G', Blocks.glass, 'B',
		                                          RegisterBlocks.brickOven);

		// Ender Smelter
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(RegisterBlocks.enderSmelter),
		                                          new ItemStack[] {new ItemStack(Items.ender_pearl, 4),
				                                          new ItemStack(RegisterItems.ironNugget, 4)},
		                                          "#E#",
		                                          "#S#",
		                                          "#E#",
		                                          '#',
		                                          Blocks.end_stone,
		                                          'S',
		                                          RegisterBlocks.smelter,
		                                          'E',
		                                          Items.ender_eye);
	}

	public static void addShapedRecipes()
	{
		//Roasting stick
		//GameRegistry.addShapedRecipe(new ItemStack(RegisterItems.roastingStick), " s", "s ", 's', Items.stick);
		GameRegistry.addRecipe(
				new ShapedOreRecipe(new ItemStack(RegisterItems.roastingStick), " s", "s ", 's', "stickWood"));
		
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

		// Flint Hatchet
		GameRegistry.addRecipe(
				new ShapedOreRecipe(new ItemStack(RegisterItems.flintHatchet), "FL", " /", 'F', Items.flint, 'L',
				                    RegisterItems.leatherStrip, '/', "stickWood"));
		//GameRegistry.addShapedRecipe(new ItemStack(RegisterItems.flintHatchet), "FS", " /", 'F', Items.flint, 'S',
		//							 RegisterItems.leatherStrip, '/', Items.stick);

		// Noob Wood Sword
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegisterItems.noobWoodSword), "#", "#", "/",
		                                           '#', "plankWood", '/', "stickWood"));
		//GameRegistry.addShapedRecipe(new ItemStack(RegisterItems.noobWoodSword), "#", "#", "/", '#', new ItemStack(
		//		Blocks.planks, 1, OreDictionary.WILDCARD_VALUE), '/', Items.stick);

		// Kiln
		GameRegistry.addRecipe(
				new ShapedOreRecipe(new ItemStack(RegisterBlocks.kiln), "###", "# #", "###", '#', "cobblestone"));
		//GameRegistry.addShapedRecipe(new ItemStack(RegisterBlocks.kiln), "###", "# #", "###", '#', Blocks.cobblestone);

		// Brick Oven
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegisterBlocks.brickOven), "BBB", "# #", "###",
		                                           '#', Blocks.brick_block, 'B', "ingotBrick"));
		//GameRegistry.addShapedRecipe(new ItemStack(RegisterBlocks.brickOven), "BBB", "# #", "###", '#',
		//							 Blocks.brick_block, 'B', Items.brick);

		// Smelter
		GameRegistry.addShapedRecipe(new ItemStack(RegisterBlocks.smelter), "###", "#C#", "###", '#', new ItemStack(
				Blocks.stonebrick, 1, 0), 'C', new ItemStack(Items.coal, 1, 1));

		// Vanilla Furnace
		GameRegistry.addShapedRecipe(new ItemStack(Blocks.furnace), "###", "# #", "---", '#', Blocks.cobblestone, '-',
									 Blocks.stone_slab);

		// Gravel->Flint
		GameRegistry.addShapedRecipe(new ItemStack(Items.flint), "##", "##", '#', Blocks.gravel);

		// Iron Nugget->Ingot
		GameRegistry.addShapedRecipe(new ItemStack(Items.iron_ingot), "***", "***", "***", '*',
									 RegisterItems.ironNugget);

		// Workbench
		GameRegistry.addShapedRecipe(new ItemStack(RegisterBlocks.doubleWorkbench), "##", "##", '#', new ItemStack(
				Blocks.planks, 1, OreDictionary.WILDCARD_VALUE));

		if (BBConfig.canMakeChainArmor)
		{
			// Chain Armor
			GameRegistry.addShapedRecipe(new ItemStack(Items.chainmail_helmet), "***", "* *", '*',
										 RegisterItems.ironNugget);
			GameRegistry.addShapedRecipe(new ItemStack(Items.chainmail_chestplate), "* *", "***", "***", '*',
										 RegisterItems.ironNugget);
			GameRegistry.addShapedRecipe(new ItemStack(Items.chainmail_leggings), "***", "* *", "* *", '*',
										 RegisterItems.ironNugget);
			GameRegistry.addShapedRecipe(new ItemStack(Items.chainmail_boots), "* *", "* *", '*',
										 RegisterItems.ironNugget);
		}

		// Craft vanilla benches into BB workbenches
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RegisterBlocks.doubleWorkbench, 2), "##", '#',
		                                           "craftingTableWood"));
		GameRegistry
				.addShapedRecipe(new ItemStack(RegisterBlocks.doubleWorkbench, 2), "##", '#', Blocks.crafting_table);

		// Craft BB workbenches into vanilla ones
		if (BBConfig.canMakeVanillaWorkbench)
		{
			GameRegistry.addShapedRecipe(
					new ItemStack(Blocks.crafting_table), "#", '#', RegisterBlocks.doubleWorkbench);
		}
	}
}
