package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;

public class ItemKnifeFlint extends ItemKnife
{
	public ItemKnifeFlint()
	{
		super(ToolMaterial.WOOD);
		setUnlocalizedName("flintKnife");
		setTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));
		setCreativeTab(ModMain.tabBetterBeginnings);
	}
}
