package net.einsteinsci.noobcraft.register;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.registry.GameRegistry;

public class RegisterRecipes
{
	public static void addShapelessRecipes()
	{
		// Sticks from Saplings & Knife
		GameRegistry.addShapelessRecipe(new ItemStack(Items.stick), new ItemStack(Blocks.sapling, 1,
			OreDictionary.WILDCARD_VALUE), new ItemStack(RegisterItems.flintKnife, 1,
			OreDictionary.WILDCARD_VALUE));
		GameRegistry.addShapelessRecipe(new ItemStack(Items.stick), new ItemStack(Blocks.sapling, 1,
			OreDictionary.WILDCARD_VALUE), new ItemStack(RegisterItems.boneKnife, 1,
			OreDictionary.WILDCARD_VALUE));
		GameRegistry.addShapelessRecipe(new ItemStack(Items.stick), new ItemStack(Blocks.sapling, 1,
			OreDictionary.WILDCARD_VALUE), new ItemStack(RegisterItems.ironKnife, 1,
			OreDictionary.WILDCARD_VALUE));
		GameRegistry.addShapelessRecipe(new ItemStack(Items.stick), new ItemStack(Blocks.sapling, 1,
			OreDictionary.WILDCARD_VALUE), new ItemStack(RegisterItems.diamondKnife, 1,
			OreDictionary.WILDCARD_VALUE));
		
		// Bone Shard
		GameRegistry.addShapelessRecipe(new ItemStack(RegisterItems.boneShard, 2), Items.bone, new ItemStack(
			RegisterItems.flintKnife, 1, OreDictionary.WILDCARD_VALUE));
		GameRegistry.addShapelessRecipe(new ItemStack(RegisterItems.boneShard, 2), Items.bone, new ItemStack(
			RegisterItems.boneKnife, 1, OreDictionary.WILDCARD_VALUE));
		GameRegistry.addShapelessRecipe(new ItemStack(RegisterItems.boneShard, 2), Items.bone, new ItemStack(
			RegisterItems.ironKnife, 1, OreDictionary.WILDCARD_VALUE));
		GameRegistry.addShapelessRecipe(new ItemStack(RegisterItems.boneShard, 2), Items.bone, new ItemStack(
			RegisterItems.diamondKnife, 1, OreDictionary.WILDCARD_VALUE));
		
		// Leather Strip
		GameRegistry.addShapelessRecipe(new ItemStack(RegisterItems.leatherStrip, 3), Items.leather,
			new ItemStack(RegisterItems.flintKnife, 1, OreDictionary.WILDCARD_VALUE));
		GameRegistry.addShapelessRecipe(new ItemStack(RegisterItems.leatherStrip, 3), Items.leather,
			new ItemStack(RegisterItems.boneKnife, 1, OreDictionary.WILDCARD_VALUE));
		GameRegistry.addShapelessRecipe(new ItemStack(RegisterItems.leatherStrip, 3), Items.leather,
			new ItemStack(RegisterItems.ironKnife, 1, OreDictionary.WILDCARD_VALUE));
		GameRegistry.addShapelessRecipe(new ItemStack(RegisterItems.leatherStrip, 3), Items.leather,
			new ItemStack(RegisterItems.diamondKnife, 1, OreDictionary.WILDCARD_VALUE));
		
		
		// Bonemeal from Bone Shard (a bit more rewarding)
		GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, 2, 15), RegisterItems.boneShard);
		
		// Iron Nugget
		GameRegistry.addShapelessRecipe(new ItemStack(RegisterItems.ironNugget, 9), Items.iron_ingot);
	}
	
	public static void addFurnaceRecipes()
	{
		addKilnRecipes();
		addBrickOvenRecipes();
	}
	
	private static void addKilnRecipes()
	{
		KilnRecipes.addRecipe(Items.clay_ball, new ItemStack(Items.brick), 0.35f);
		KilnRecipes.addRecipe(Blocks.cobblestone, new ItemStack(Blocks.stone), 0.1f);
		KilnRecipes.addRecipe(Blocks.log, new ItemStack(Items.coal, 1, 1), 0.15f);
		KilnRecipes.addRecipe(Blocks.log2, new ItemStack(Items.coal, 1, 1), 0.15f);
		KilnRecipes.addRecipe(Blocks.sand, new ItemStack(Blocks.glass), 0.1f);
		KilnRecipes.addRecipe(Items.beef, new ItemStack(RegisterItems.charredMeat), 0.1f);
		KilnRecipes.addRecipe(Items.porkchop, new ItemStack(RegisterItems.charredMeat), 0.1f);
		KilnRecipes.addRecipe(Items.chicken, new ItemStack(RegisterItems.charredMeat), 0.1f);
		KilnRecipes
			.addRecipe(new ItemStack(Items.fish, 1, 0), new ItemStack(RegisterItems.charredMeat), 0.1f);
		KilnRecipes
			.addRecipe(new ItemStack(Items.fish, 1, 1), new ItemStack(RegisterItems.charredMeat), 0.1f);
	}
	
	private static void addBrickOvenRecipes()
	{
		BrickOvenRecipeHandler.instance().putShapedRecipe(new ItemStack(Items.golden_apple), "GGG", "GAG",
			"GGG", 'G', Items.gold_ingot, 'A', Items.apple);
	}
	
	public static void addAdvancedRecipes()
	{
		// Bow
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.bow), new ItemStack[] { new ItemStack(
			RegisterItems.leatherStrip, 3) }, " /s", "/ s", " /s", 's', Items.string, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.bow), new ItemStack[] { new ItemStack(
			RegisterItems.leatherStrip, 3) }, "s/ ", "s /", "s/ ", 's', Items.string, '/', Items.stick);
		
		// Bone Pickaxe
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(RegisterItems.bonePickaxe),
			new ItemStack[] { new ItemStack(RegisterItems.leatherStrip, 2) }, "sBs", " / ", " / ", 's',
			RegisterItems.boneShard, 'B', Items.bone, '/', Items.stick);
		
		// Leather armor
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.leather_helmet), new ItemStack[] {
			new ItemStack(Items.string, 3), new ItemStack(Blocks.wool, 2, OreDictionary.WILDCARD_VALUE) },
			"LLL", "L L", 'L', Items.leather);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.leather_chestplate), new ItemStack[] {
			new ItemStack(Items.string, 2), new ItemStack(Blocks.wool, 4, OreDictionary.WILDCARD_VALUE) },
			"L L", "LLL", "LLL", 'L', Items.leather);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.leather_leggings), new ItemStack[] {
			new ItemStack(Items.string, 4), new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE) },
			"LLL", "L L", "L L", 'L', Items.leather);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.leather_boots), new ItemStack[] {
			new ItemStack(Items.string, 2), new ItemStack(Blocks.wool, 3, OreDictionary.WILDCARD_VALUE) },
			"L L", "L L", 'L', Items.leather);
		
		// Stone Tools/Weapons
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.stone_pickaxe),
			new ItemStack[] { new ItemStack(RegisterItems.leatherStrip, 2) }, "###", " / ", " / ", '#',
			Blocks.stone, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.stone_sword),
			new ItemStack[] { new ItemStack(RegisterItems.leatherStrip, 2) }, "#", "#", "/", '#',
			Blocks.stone, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.stone_shovel),
			new ItemStack[] { new ItemStack(RegisterItems.leatherStrip, 1) }, "#", "/", "/", '#',
			Blocks.stone, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.stone_axe),
			new ItemStack[] { new ItemStack(RegisterItems.leatherStrip, 1) }, "##", "#/", " /", '#',
			Blocks.stone, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.stone_axe),
			new ItemStack[] { new ItemStack(RegisterItems.leatherStrip, 1) }, "##", "/#", "/ ", '#',
			Blocks.stone, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.stone_hoe),
			new ItemStack[] { new ItemStack(RegisterItems.leatherStrip, 1) }, "##", " /", " /", '#',
			Blocks.stone, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.stone_hoe),
			new ItemStack[] { new ItemStack(RegisterItems.leatherStrip, 1) }, "##", "/ ", "/ ", '#',
			Blocks.stone, '/', Items.stick);
		
		// Iron armor
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.iron_helmet), new ItemStack[] {
			new ItemStack(RegisterItems.leatherStrip, 3),
			new ItemStack(Blocks.wool, 2, OreDictionary.WILDCARD_VALUE) }, "III", "I I", 'I',
			Items.iron_ingot);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.iron_chestplate), new ItemStack[] {
			new ItemStack(RegisterItems.leatherStrip, 2),
			new ItemStack(Blocks.wool, 4, OreDictionary.WILDCARD_VALUE) }, "I I", "III", "III", 'I',
			Items.iron_ingot);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.iron_leggings), new ItemStack[] {
			new ItemStack(RegisterItems.leatherStrip, 4),
			new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE) }, "III", "I I", "I I", 'I',
			Items.iron_ingot);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.iron_boots), new ItemStack[] {
			new ItemStack(RegisterItems.leatherStrip, 2),
			new ItemStack(Blocks.wool, 3, OreDictionary.WILDCARD_VALUE) }, "I I", "I I", 'I',
			Items.iron_ingot);
		
		// Iron Tools/Weapons
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.iron_pickaxe),
			new ItemStack[] { new ItemStack(RegisterItems.leatherStrip, 3) }, "III", " / ", " / ", 'I',
			Items.iron_ingot, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.iron_sword),
			new ItemStack[] { new ItemStack(RegisterItems.leatherStrip, 2) }, "I", "I", "/", 'I',
			Items.iron_ingot, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.iron_shovel),
			new ItemStack[] { new ItemStack(RegisterItems.leatherStrip, 2) }, "I", "/", "/", 'I',
			Items.iron_ingot, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.iron_axe),
			new ItemStack[] { new ItemStack(RegisterItems.leatherStrip, 2) }, "II", "I/", " /", 'I',
			Items.iron_ingot, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.iron_axe),
			new ItemStack[] { new ItemStack(RegisterItems.leatherStrip, 2) }, "II", "/I", "/ ", 'I',
			Items.iron_ingot, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.iron_hoe),
			new ItemStack[] { new ItemStack(RegisterItems.leatherStrip, 1) }, "II", " /", " /", 'I',
			Items.iron_ingot, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.iron_hoe),
			new ItemStack[] { new ItemStack(RegisterItems.leatherStrip, 1) }, "II", "/ ", "/ ", 'I',
			Items.iron_ingot, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(RegisterItems.ironKnife),
			new ItemStack[] { new ItemStack(RegisterItems.leatherStrip, 2) }, " I", "/ ", 'I',
			Items.iron_ingot, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(RegisterItems.ironKnife),
			new ItemStack[] { new ItemStack(RegisterItems.leatherStrip, 2) }, "I ", " /", 'I',
			Items.iron_ingot, '/', Items.stick);
		
		// Diamond armor
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.diamond_helmet), new ItemStack[] {
			new ItemStack(Items.redstone, 3), new ItemStack(Items.blaze_powder, 2),
			new ItemStack(RegisterItems.leatherStrip, 3),
			new ItemStack(Blocks.wool, 2, OreDictionary.WILDCARD_VALUE) }, "DDD", "D D", 'D', Items.diamond);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.diamond_chestplate), new ItemStack[] {
			new ItemStack(Items.redstone, 4), new ItemStack(Items.blaze_powder, 6),
			new ItemStack(RegisterItems.leatherStrip, 2),
			new ItemStack(Blocks.wool, 4, OreDictionary.WILDCARD_VALUE) }, "D D", "DDD", "DDD", 'D',
			Items.diamond);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.diamond_leggings), new ItemStack[] {
			new ItemStack(Items.redstone, 4), new ItemStack(Items.blaze_powder, 3),
			new ItemStack(RegisterItems.leatherStrip, 4),
			new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE) }, "DDD", "D D", "D D", 'D',
			Items.diamond);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.diamond_boots), new ItemStack[] {
			new ItemStack(Items.redstone, 2), new ItemStack(Items.blaze_powder, 3),
			new ItemStack(RegisterItems.leatherStrip, 2),
			new ItemStack(Blocks.wool, 3, OreDictionary.WILDCARD_VALUE) }, "D D", "D D", 'D', Items.diamond);
		
		// Diamond Tools/Weapons
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.diamond_pickaxe), new ItemStack[] {
			new ItemStack(Items.redstone, 5), new ItemStack(Items.blaze_powder, 3),
			new ItemStack(RegisterItems.leatherStrip, 3) }, "DDD", " / ", " / ", 'D', Items.diamond, '/',
			Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.diamond_sword), new ItemStack[] {
			new ItemStack(Items.redstone, 4), new ItemStack(Items.blaze_powder, 5),
			new ItemStack(RegisterItems.leatherStrip, 2) }, "D", "D", "/", 'D', Items.diamond, '/',
			Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.diamond_shovel), new ItemStack[] {
			new ItemStack(Items.redstone, 2), new ItemStack(Items.blaze_powder, 1),
			new ItemStack(RegisterItems.leatherStrip, 2) }, "D", "/", "/", 'D', Items.diamond, '/',
			Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.diamond_axe), new ItemStack[] {
			new ItemStack(Items.redstone, 3), new ItemStack(Items.blaze_powder, 2),
			new ItemStack(RegisterItems.leatherStrip, 2) }, "DD", "D/", " /", 'D', Items.diamond, '/',
			Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.diamond_axe), new ItemStack[] {
			new ItemStack(Items.redstone, 3), new ItemStack(Items.blaze_powder, 2),
			new ItemStack(RegisterItems.leatherStrip, 2) }, "DD", "/D", "/ ", 'D', Items.diamond, '/',
			Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.diamond_hoe), new ItemStack[] {
			new ItemStack(Items.redstone, 1), new ItemStack(Items.blaze_powder, 1),
			new ItemStack(RegisterItems.leatherStrip, 1) }, "DD", " /", " /", 'D', Items.diamond, '/',
			Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(Items.diamond_hoe), new ItemStack[] {
			new ItemStack(Items.redstone, 1), new ItemStack(Items.blaze_powder, 1),
			new ItemStack(RegisterItems.leatherStrip, 1) }, "DD", "/ ", "/ ", 'D', Items.diamond, '/',
			Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(RegisterItems.diamondKnife), new ItemStack[] {
			new ItemStack(Items.redstone, 3), new ItemStack(Items.blaze_powder, 3),
			new ItemStack(RegisterItems.leatherStrip, 1) }, " D", "/ ", 'D', Items.diamond, '/', Items.stick);
		AdvancedCraftingHandler.addAdvancedRecipe(new ItemStack(RegisterItems.diamondKnife), new ItemStack[] {
			new ItemStack(Items.redstone, 3), new ItemStack(Items.blaze_powder, 3),
			new ItemStack(RegisterItems.leatherStrip, 1) }, "D ", " /", 'D', Items.diamond, '/', Items.stick);
	}
	
	public static void addShapedRecipes()
	{
		// Knife
		GameRegistry.addShapedRecipe(new ItemStack(RegisterItems.flintKnife), "F ", " F", 'F', Items.flint);
		GameRegistry.addShapedRecipe(new ItemStack(RegisterItems.flintKnife), " F", "F ", 'F', Items.flint);
		GameRegistry.addShapedRecipe(new ItemStack(RegisterItems.boneKnife), " S", "/ ", 'S',
			RegisterItems.boneShard, '/', Items.bone);
		GameRegistry.addShapedRecipe(new ItemStack(RegisterItems.boneKnife), "S ", " /", 'S',
			RegisterItems.boneShard, '/', Items.bone);
		// GameRegistry.addShapedRecipe(new ItemStack(RegisterItems.ironKnife),
		// "/ ", " I", '/', Items.stick,
		// 'I', Items.iron_ingot);
		// GameRegistry.addShapedRecipe(new ItemStack(RegisterItems.ironKnife),
		// " /", "I ", '/', Items.stick,
		// 'I', Items.iron_ingot);
		
		// Flint Hatchet
		GameRegistry.addShapedRecipe(new ItemStack(RegisterItems.flintHatchet), "FS", " /", 'F', Items.flint,
			'S', RegisterItems.leatherStrip, '/', Items.stick);
		
		// Bone Pickaxe
		// GameRegistry.addShapedRecipe(new
		// ItemStack(RegisterItems.bonePickaxe), "BLB", " / ", " / ", 'B',
		// RegisterItems.boneShard, 'L', RegisterItems.leatherStrip, '/',
		// Items.stick);
		
		// Noob Wood Sword
		GameRegistry.addShapedRecipe(new ItemStack(RegisterItems.noobWoodSword), "#", "#", "/", '#',
			new ItemStack(Blocks.planks, 1, OreDictionary.WILDCARD_VALUE), '/', Items.stick);
		
		// Kiln
		GameRegistry.addShapedRecipe(new ItemStack(RegisterBlocks.blockKiln), "###", "# #", "###", '#',
			Blocks.cobblestone);
		
		// Vanilla Furnace
		GameRegistry.addShapedRecipe(new ItemStack(Blocks.furnace), "###", "# #", "---", '#',
			Blocks.cobblestone, '-', Blocks.stone_slab);
		
		// Gravel->Flint
		GameRegistry.addShapedRecipe(new ItemStack(Items.flint), "##", "##", '#', Blocks.gravel);
		
		// Iron Nugget->Ingot
		GameRegistry.addShapedRecipe(new ItemStack(Items.iron_ingot), "***", "***", "***", '*',
			RegisterItems.ironNugget);
		
		// Workbench
		GameRegistry.addShapedRecipe(new ItemStack(RegisterBlocks.blockDoubleWorkbench), "##", "##", '#',
			new ItemStack(Blocks.planks, 1, OreDictionary.WILDCARD_VALUE));
		
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
}



















// PADDING!
