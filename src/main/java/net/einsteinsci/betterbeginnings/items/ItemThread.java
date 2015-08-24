package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.register.IBBName;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemReed;

public class ItemThread extends ItemReed implements IBBName
{
	public ItemThread()
	{
		super(Blocks.tripwire);
		setUnlocalizedName("thread");
		setCreativeTab(ModMain.tabBetterBeginnings);
	}

	@Override
	public String getName()
	{
		return "thread";
	}
}
