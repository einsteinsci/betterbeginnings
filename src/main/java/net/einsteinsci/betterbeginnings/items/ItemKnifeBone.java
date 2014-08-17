package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;

public class ItemKnifeBone extends ItemKnife
{
	public ItemKnifeBone()
	{
		super(ToolMaterial.STONE);
		setUnlocalizedName("boneKnife");
		setTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));
		setCreativeTab(ModMain.tabBetterBeginnings);
	}
}
