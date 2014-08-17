package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;
import net.minecraft.item.ItemFood;

public class ItemJerkyRaw extends ItemFood
{

	public ItemJerkyRaw()
	{
		super(0, 8.0f, true);
		setUnlocalizedName("jerkyRaw");
		setTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));
		setCreativeTab(ModMain.tabBetterBeginnings);
		this.setPotionEffect(17, 30, 1, 60);
	}

}
