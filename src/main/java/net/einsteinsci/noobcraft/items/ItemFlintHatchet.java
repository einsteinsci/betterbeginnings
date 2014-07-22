package net.einsteinsci.noobcraft.items;

import net.einsteinsci.noobcraft.NoobcraftMod;
import net.minecraft.item.ItemAxe;

public class ItemFlintHatchet extends ItemAxe
{
	public ItemFlintHatchet()
	{
		super(ToolMaterial.WOOD);
		setUnlocalizedName("flintHatchet");
		setTextureName(NoobcraftMod.MODID + ":" + getUnlocalizedName().substring(5));
		setCreativeTab(NoobcraftMod.tabNoobCraft);
	}
}
