package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;
import net.minecraft.item.Item;

/**
 * Created by einsteinsci on 11/22/2014.
 */
public class ItemPan extends Item
{
	public ItemPan()
	{
		super();
		setUnlocalizedName("pan");
		// setMaxDamage(250); will do later
		setTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));
		setCreativeTab(ModMain.tabBetterBeginnings);
	}
}
