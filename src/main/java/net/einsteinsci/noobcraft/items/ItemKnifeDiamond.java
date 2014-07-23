package net.einsteinsci.noobcraft.items;

import net.einsteinsci.noobcraft.ModMain;

public class ItemKnifeDiamond extends ItemKnife
{
	public ItemKnifeDiamond()
	{
		//
		super(ToolMaterial.EMERALD);
		setUnlocalizedName("diamondKnife");
		setTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));
		setCreativeTab(ModMain.tabNoobCraft);
	}
}
