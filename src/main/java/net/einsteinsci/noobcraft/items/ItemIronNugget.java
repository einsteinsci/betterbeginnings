package net.einsteinsci.noobcraft.items;

import net.einsteinsci.noobcraft.ModMain;
import net.minecraft.item.Item;

public class ItemIronNugget extends Item
{
	public ItemIronNugget()
	{
		super();
		setUnlocalizedName("ironNugget");
		setTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));
		setCreativeTab(ModMain.tabNoobCraft);
	}
}
