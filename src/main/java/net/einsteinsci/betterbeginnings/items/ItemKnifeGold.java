package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;

public class ItemKnifeGold extends ItemKnife
{
	public ItemKnifeGold()
	{
		super(ToolMaterial.GOLD);
		setUnlocalizedName("goldKnife");
		setTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));
		setCreativeTab(ModMain.tabBetterBeginnings);
	}
}
