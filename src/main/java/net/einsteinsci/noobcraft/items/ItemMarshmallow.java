package net.einsteinsci.noobcraft.items;

import net.einsteinsci.noobcraft.ModMain;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;

/**
 * Created by einsteinsci on 8/11/2014.
 */
public class ItemMarshmallow extends ItemFood
{
	public ItemMarshmallow()
	{
		super(2, 2.0f, false);
		setUnlocalizedName("marshmallow");
		setTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));
		setCreativeTab(ModMain.tabNoobCraft);
	}
}
