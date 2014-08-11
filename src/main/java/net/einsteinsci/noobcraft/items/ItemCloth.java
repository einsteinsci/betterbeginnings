package net.einsteinsci.noobcraft.items;

import net.einsteinsci.noobcraft.ModMain;
import net.minecraft.item.Item;

/**
 * Created by einsteinsci on 8/10/2014.
 */
public class ItemCloth extends Item
{
	public ItemCloth()
	{
		super();

		setUnlocalizedName("cloth");
		setTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));

		setCreativeTab(ModMain.tabNoobCraft);
	}
}
