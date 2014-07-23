package net.einsteinsci.noobcraft.items;

import net.einsteinsci.noobcraft.ModMain;

public class ItemKnifeIron extends ItemKnife 
{
	public ItemKnifeIron() 
	{
		super(ToolMaterial.IRON);
		setUnlocalizedName("ironKnife");
		setTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));
		setCreativeTab(ModMain.tabNoobCraft);
	}
	
}
