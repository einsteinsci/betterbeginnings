package net.einsteinsci.noobcraft.items;

import net.einsteinsci.noobcraft.NoobcraftMod;

public class ItemKnifeIron extends ItemKnife 
{
	public ItemKnifeIron() 
	{
		super(ToolMaterial.IRON);
		setUnlocalizedName("ironKnife");
		setTextureName(NoobcraftMod.MODID + ":" + getUnlocalizedName().substring(5));
		setCreativeTab(NoobcraftMod.tabNoobCraft);
	}
	
}
