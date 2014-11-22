package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;
import net.minecraft.item.ItemFood;

/**
 * Created by einsteinsci on 8/11/2014.
 */
public class ItemMarshmallowCooked extends ItemFood
{
	public ItemMarshmallowCooked()
	{
		super(5, 6.0f, false);
		setUnlocalizedName("marshmallowCooked");
		setTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));
		setCreativeTab(ModMain.tabBetterBeginnings);
		setPotionEffect(1, 15, 0, 0.8f);
	}
}
