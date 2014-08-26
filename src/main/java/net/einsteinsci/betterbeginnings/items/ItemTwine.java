package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;
import net.minecraft.item.Item;

/**
 * Created by einsteinsci on 8/25/2014.
 */
public class ItemTwine extends Item
{
	public ItemTwine()
	{
		super();

		setUnlocalizedName("twine");
		setTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));
		setCreativeTab(ModMain.tabBetterBeginnings);
	}
}
