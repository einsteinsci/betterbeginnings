package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.register.IBBName;
import net.minecraft.item.Item;

public class ItemLeatherStrip extends Item implements IBBName
{
	public ItemLeatherStrip()
	{
		super();
		setUnlocalizedName("leatherStrip");
		setCreativeTab(ModMain.tabBetterBeginnings);
	}

	@Override
	public String getName()
	{
		return "leatherStrip";
	}
}
