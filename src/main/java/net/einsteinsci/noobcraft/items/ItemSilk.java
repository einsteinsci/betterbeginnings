package net.einsteinsci.noobcraft.items;

import net.einsteinsci.noobcraft.ModMain;
import net.minecraft.item.Item;

public class ItemSilk extends Item
{
	public ItemSilk()
	{
		super();

		setUnlocalizedName("silk");
		setTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));

		setCreativeTab(ModMain.tabNoobCraft);
	}
}
