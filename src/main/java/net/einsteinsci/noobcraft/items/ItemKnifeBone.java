package net.einsteinsci.noobcraft.items;

import net.einsteinsci.noobcraft.ModMain;
import net.minecraft.item.Item.ToolMaterial;

public class ItemKnifeBone extends ItemKnife
{
	public ItemKnifeBone() 
	{
		super(ToolMaterial.STONE);
		setUnlocalizedName("boneKnife");
		setTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));
		setCreativeTab(ModMain.tabNoobCraft);
	}
}
