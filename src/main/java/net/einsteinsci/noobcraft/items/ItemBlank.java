package net.einsteinsci.noobcraft.items;

import net.einsteinsci.noobcraft.ModMain;
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
