package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;

public class ItemKnifeDiamond extends ItemKnife
{
	public ItemKnifeDiamond()
	{
		//
		super(ToolMaterial.EMERALD);
		setUnlocalizedName("diamondKnife");
		setTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));
		setCreativeTab(ModMain.tabBetterBeginnings);
	}
}
