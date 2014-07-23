package net.einsteinsci.noobcraft.items;

import net.einsteinsci.noobcraft.ModMain;
import net.minecraft.item.Item;

public class ItemLeatherStrip extends Item
{
	public ItemLeatherStrip()
	{
		super();
		setUnlocalizedName("leatherStrip");
		setTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));
		setCreativeTab(ModMain.tabNoobCraft);
	}
}
