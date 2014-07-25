package net.einsteinsci.noobcraft.items;

import net.einsteinsci.noobcraft.ModMain;
import net.minecraft.item.ItemFood;

// "That's disgusting!"
public class ItemCharredMeat extends ItemFood
{
	public ItemCharredMeat()
	{
		super(4, 8.0f, true);
		setUnlocalizedName("charredMeat");
		setTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));
		setCreativeTab(ModMain.tabNoobCraft);
	}
}
