package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;
import net.minecraft.item.ItemFood;

public class ItemJerkyBeef extends ItemFood
{

	public ItemJerkyBeef()
	{
		super(4, 8.0f, true);
		setUnlocalizedName("jerkyBeef");
		setTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));
		setCreativeTab(ModMain.tabBetterBeginnings);
	}


}
