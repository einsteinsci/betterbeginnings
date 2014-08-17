package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;
import net.minecraft.item.ItemAxe;

public class ItemFlintHatchet extends ItemAxe
{
	public ItemFlintHatchet()
	{
		super(ToolMaterial.WOOD);
		setUnlocalizedName("flintHatchet");
		setTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));
		setCreativeTab(ModMain.tabBetterBeginnings);
	}
}
