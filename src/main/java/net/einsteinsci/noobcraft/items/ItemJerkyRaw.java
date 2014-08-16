package net.einsteinsci.noobcraft.items;

import net.einsteinsci.noobcraft.ModMain;
import net.minecraft.item.ItemFood;

public class ItemJerkyRaw extends ItemFood
{

	public ItemJerkyRaw()
	{
		super(0, 8.0f, true);
		setUnlocalizedName("jerkyRaw");
		setTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));
		setCreativeTab(ModMain.tabNoobCraft);
		this.setPotionEffect(17, 30, 1, 60);
	}

}
