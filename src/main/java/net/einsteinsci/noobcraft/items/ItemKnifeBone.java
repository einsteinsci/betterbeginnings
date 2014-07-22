package net.einsteinsci.noobcraft.items;

import net.einsteinsci.noobcraft.NoobcraftMod;
import net.minecraft.item.Item.ToolMaterial;

public class ItemKnifeBone extends ItemKnife
{
	public ItemKnifeBone() 
	{
		super(ToolMaterial.STONE);
		setUnlocalizedName("boneKnife");
		setTextureName(NoobcraftMod.MODID + ":" + getUnlocalizedName().substring(5));
		setCreativeTab(NoobcraftMod.tabNoobCraft);
	}
}
