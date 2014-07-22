package net.einsteinsci.noobcraft.items;

import net.einsteinsci.noobcraft.NoobcraftMod;
import net.minecraft.item.Item;

public class ItemLeatherStrip extends Item
{
	public ItemLeatherStrip()
	{
		super();
		setUnlocalizedName("leatherStrip");
		setTextureName(NoobcraftMod.MODID + ":" + getUnlocalizedName().substring(5));
		setCreativeTab(NoobcraftMod.tabNoobCraft);
	}
}
