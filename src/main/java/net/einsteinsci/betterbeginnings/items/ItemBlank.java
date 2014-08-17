package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;
import net.minecraft.item.Item;

public class ItemBlank extends Item
{
	public ItemBlank()
	{
		super();
		setUnlocalizedName("blank");
		setTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));
	}
}
