package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;

public class ItemKnifeDiamond extends ItemKnife
{
	public ItemKnifeDiamond()
	{
		super(ToolMaterial.EMERALD);
		setUnlocalizedName("diamondKnife");
		setCreativeTab(ModMain.tabBetterBeginnings);
	}

	@Override
	public String getName()
	{
		return "diamondKnife";
	}
}
