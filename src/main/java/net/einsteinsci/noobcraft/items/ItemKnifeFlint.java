package net.einsteinsci.noobcraft.items;

import net.einsteinsci.noobcraft.ModMain;
import net.minecraft.item.Item.ToolMaterial;

public class ItemKnifeFlint extends ItemKnife
{
	public ItemKnifeFlint() 
	{
		super(ToolMaterial.WOOD);
		setUnlocalizedName("flintKnife");
		setTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));
		setCreativeTab(ModMain.tabNoobCraft);
	}
}
