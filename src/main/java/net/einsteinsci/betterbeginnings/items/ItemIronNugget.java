package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.register.IBBName;
import net.minecraft.item.Item;

public class ItemIronNugget extends Item implements IBBName
{
	public ItemIronNugget()
	{
		super();
		setUnlocalizedName("ironNugget");
		setCreativeTab(ModMain.tabBetterBeginnings);
	}

	@Override
	public String getName()
	{
		return "ironNugget";
	}
}
