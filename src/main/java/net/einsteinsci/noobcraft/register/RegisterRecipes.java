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
		//Sticks from Saplings & Knife
		GameRegistry.addShapelessRecipe(new ItemStack(Items.stick), new ItemStack(Blocks.sapling, 1, OreDictionary.WILDCARD_VALUE),
			new ItemStack(RegisterItems.flintKnife, 1, OreDictionary.WILDCARD_VALUE));
		GameRegistry.addShapelessRecipe(new ItemStack(Items.stick), new ItemStack(Blocks.sapling, 1, OreDictionary.WILDCARD_VALUE),
			new ItemStack(RegisterItems.boneKnife, 1, OreDictionary.WILDCARD_VALUE));
		GameRegistry.addShapelessRecipe(new ItemStack(Items.stick), new ItemStack(Blocks.sapling, 1, OreDictionary.WILDCARD_VALUE),
			new ItemStack(RegisterItems.ironKnife, 1, OreDictionary.WILDCARD_VALUE));
		
		//Bone Shard
		GameRegistry.addShapelessRecipe(new ItemStack(RegisterItems.boneShard, 2), Items.bone,
			new ItemStack(RegisterItems.flintKnife, 1, OreDictionary.WILDCARD_VALUE));
		GameRegistry.addShapelessRecipe(new ItemStack(RegisterItems.boneShard, 2), Items.bone,
			new ItemStack(RegisterItems.boneKnife, 1, OreDictionary.WILDCARD_VALUE));
		GameRegistry.addShapelessRecipe(new ItemStack(RegisterItems.boneShard, 2), Items.bone,
			new ItemStack(RegisterItems.ironKnife, 1, OreDictionary.WILDCARD_VALUE));
		
		//Leather Strip
		GameRegistry.addShapelessRecipe(new ItemStack(RegisterItems.leatherStrip, 3), Items.leather,
			new ItemStack(RegisterItems.flintKnife, 1, OreDictionary.WILDCARD_VALUE));
		GameRegistry.addShapelessRecipe(new ItemStack(RegisterItems.leatherStrip, 3), Items.leather,
			new ItemStack(RegisterItems.boneKnife, 1, OreDictionary.WILDCARD_VALUE));
		GameRegistry.addShapelessRecipe(new ItemStack(RegisterItems.leatherStrip, 3), Items.leather,
			new ItemStack(RegisterItems.ironKnife, 1, OreDictionary.WILDCARD_VALUE));
		
		
		//Bonemeal from Bone Shard (a bit more rewarding)
		GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, 2, 15), RegisterItems.boneShard);
	}
	
	public static void addFurnaceRecipes()
	{
		addKilnRecipes();
	}
	
	private static void addKilnRecipes()
	{
		KilnRecipes.addRecipe(Items.clay_ball, new ItemStack(Items.brick), 0.35f);
		KilnRecipes.addRecipe(Blocks.cobblestone, new ItemStack(Blocks.stone), 0.1f);
		KilnRecipes.addRecipe(Blocks.log, new ItemStack(Items.coal, 1, 1), 0.15f);
		KilnRecipes.addRecipe(Blocks.log2, new ItemStack(Items.coal, 1, 1), 0.15f);
		KilnRecipes.addRecipe(Blocks.sand, new ItemStack(Blocks.glass), 0.1f);
	}
	
	public static void addShapedRecipes()
	{
		//Knife
		GameRegistry.addShapedRecipe(new ItemStack(RegisterItems.flintKnife),
			"F ",
			" F",
			'F', Items.flint);
		GameRegistry.addShapedRecipe(new ItemStack(RegisterItems.flintKnife),
			" F",
			"F ",
			'F', Items.flint);
		GameRegistry.addShapedRecipe(new ItemStack(RegisterItems.boneKnife),
			" S",
			"/ ",
			'S', RegisterItems.boneShard,
			'/', Items.bone);
		GameRegistry.addShapedRecipe(new ItemStack(RegisterItems.boneKnife),
			"S ",
			" /",
			'S', RegisterItems.boneShard,
			'/', Items.bone);
		GameRegistry.addShapedRecipe(new ItemStack(RegisterItems.ironKnife),
			"/ ",
			" I",
			'/', Items.stick,
			'I', Items.iron_ingot);
		GameRegistry.addShapedRecipe(new ItemStack(RegisterItems.ironKnife),
			" /",
			"I ",
			'/', Items.stick,
			'I', Items.iron_ingot);
		
		//Flint Hatchet
		GameRegistry.addShapedRecipe(new ItemStack(RegisterItems.flintHatchet),
			"FS",
			" /",
			'F', Items.flint,
			'S', RegisterItems.leatherStrip,
			'/', Items.stick);
		
		//Bone Pickaxe
		GameRegistry.addShapedRecipe(new ItemStack(RegisterItems.bonePickaxe),
			"BLB",
			" / ",
			" / ",
			'B', RegisterItems.boneShard,
			'L', RegisterItems.leatherStrip,
			'/', Items.stick);
		
		//Noob Wood Sword
		GameRegistry.addShapedRecipe(new ItemStack(RegisterItems.noobWoodSword),
			"#",
			"#",
			"/",
			'#', new ItemStack(Blocks.planks, 1, OreDictionary.WILDCARD_VALUE),
			'/', Items.stick);
		
		//Kiln
		GameRegistry.addShapedRecipe(new ItemStack(RegisterBlocks.blockKiln),
			"###",
			"# #",
			"###",
			'#', Blocks.cobblestone);
		
		//Vanilla Furnace
		GameRegistry.addShapedRecipe(new ItemStack(Blocks.furnace),
			"###",
			"# #",
			"---",
			'#', Blocks.cobblestone,
			'-', Blocks.stone_slab);
		
		//Gravel->Flint
		GameRegistry.addShapedRecipe(new ItemStack(Items.flint),
			"##",
			"##",
			'#', Blocks.gravel);
	}
}



















// PADDING!
