package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;
import net.minecraft.item.ItemFood;

/**
 * Created by einsteinsci on 8/11/2014.
 */
public class ItemMarshmallow extends ItemFood
{
	public ItemMarshmallow()
	{
		super(1, 2.0f, false);
		setUnlocalizedName("marshmallow");
		setTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));
		setCreativeTab(ModMain.tabBetterBeginnings);
	}
}
