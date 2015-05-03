package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;

public class ItemKnifeIron extends ItemKnife
{
	public ItemKnifeIron()
	{
		super(ToolMaterial.IRON);
		setUnlocalizedName("ironKnife");
		setCreativeTab(ModMain.tabBetterBeginnings);
	}

	@Override
	public String getName()
	{
		return "ironKnife";
	}
}
