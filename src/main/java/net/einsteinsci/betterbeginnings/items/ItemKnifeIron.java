package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;

public class ItemKnifeIron extends ItemKnife
{
	public ItemKnifeIron()
	{
		super(ToolMaterial.IRON);
		setUnlocalizedName("ironKnife");
		setTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));
		setCreativeTab(ModMain.tabBetterBeginnings);
	}

}
