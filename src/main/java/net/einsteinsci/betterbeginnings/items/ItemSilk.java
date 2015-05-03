package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.register.IBBName;
import net.minecraft.item.Item;

public class ItemSilk extends Item implements IBBName
{
	public ItemSilk()
	{
		super();

		setUnlocalizedName("silk");

		setCreativeTab(ModMain.tabBetterBeginnings);
	}

	@Override
	public String getName()
	{
		return "silk";
	}
}
