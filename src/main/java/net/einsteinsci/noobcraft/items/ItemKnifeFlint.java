package net.einsteinsci.noobcraft.items;

import net.einsteinsci.noobcraft.NoobcraftMod;
import net.minecraft.item.Item.ToolMaterial;

public class ItemKnifeFlint extends ItemKnife
{
	public ItemKnifeFlint() 
	{
		super(ToolMaterial.WOOD);
		setUnlocalizedName("flintKnife");
		setTextureName(NoobcraftMod.MODID + ":" + getUnlocalizedName().substring(5));
		setCreativeTab(NoobcraftMod.tabNoobCraft);
	}
}
