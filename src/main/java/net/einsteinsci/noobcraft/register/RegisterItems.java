package net.einsteinsci.noobcraft.register;

import net.einsteinsci.noobcraft.items.*;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemFood;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.registry.GameRegistry;

public class RegisterItems
{
	public static final ToolMaterial noobWood = EnumHelper.addToolMaterial("noobwood", 0, 60, 2.0f, -4, 35);
	
	public static final Item flintKnife = new ItemKnifeFlint();
	public static final Item boneKnife = new ItemKnifeBone();
	public static final Item ironKnife = new ItemKnifeIron();
	public static final Item diamondKnife = new ItemKnifeDiamond();
	
	public static final Item flintHatchet = new ItemFlintHatchet();
	public static final Item bonePickaxe = new ItemBonePickaxe();
	
	public static final Item boneShard = new ItemBoneShard();
	public static final Item testItem = new ItemTestItem();
	public static final Item blankItem = new ItemBlank();
	public static final Item leatherStrip = new ItemLeatherStrip();
	public static final Item ironNugget = new ItemIronNugget();
	public static final Item charredMeat = new ItemCharredMeat();
	
	public static final Item noobWoodSword = new NoobWoodSword(noobWood);
	
	public static void register()
	{
		RegisterHelper.registerItem(flintKnife);
		RegisterHelper.registerItem(boneKnife);
		RegisterHelper.registerItem(ironKnife);
		RegisterHelper.registerItem(diamondKnife);
		
		RegisterHelper.registerItem(flintHatchet);
		RegisterHelper.registerItem(bonePickaxe);
		
		RegisterHelper.registerItem(boneShard);
		RegisterHelper.registerItem(testItem);
		RegisterHelper.registerItem(blankItem);
		RegisterHelper.registerItem(leatherStrip);
		RegisterHelper.registerItem(ironNugget);
		RegisterHelper.registerItem(charredMeat);
		
		RegisterHelper.registerItem(noobWoodSword);
		
		oreDictRegistry();
	}
	
	public static void tweakVanilla()
	{
		((ItemFood)GameRegistry.findItem("minecraft", "beef")).setPotionEffect(17, 20, 0, 20);
		((ItemFood)GameRegistry.findItem("minecraft", "porkchop")).setPotionEffect(17, 25, 0, 25);
		((ItemFood)GameRegistry.findItem("minecraft", "fish")).setPotionEffect(17, 30, 1, 60); // Both fish types here
	}
	
	public static void oreDictRegistry()
	{
		OreDictionary.registerOre("nuggetIron", ironNugget);
	}
}
