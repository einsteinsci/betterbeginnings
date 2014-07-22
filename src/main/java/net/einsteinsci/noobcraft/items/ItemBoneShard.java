package net.einsteinsci.noobcraft.items;

import net.einsteinsci.noobcraft.NoobcraftMod;
import net.minecraft.item.Item;

public class ItemBoneShard extends Item 
{
	public ItemBoneShard()
	{
		super();
		setUnlocalizedName("boneShard");
		setTextureName(NoobcraftMod.MODID + ":" + getUnlocalizedName().substring(5));
		setCreativeTab(NoobcraftMod.tabNoobCraft);
	}
}
