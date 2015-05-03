package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.register.IBBName;
import net.minecraft.item.Item;

public class ItemCloth extends Item implements IBBName
{
	public ItemCloth()
	{
		super();

		setUnlocalizedName("cloth");

		setCreativeTab(ModMain.tabBetterBeginnings);
	}

	@Override
	public String getName()
	{
		return "cloth";
	}
}
