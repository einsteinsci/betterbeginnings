package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.register.IBBName;
import net.minecraft.item.Item;

public class ItemBoneShard extends Item implements IBBName
{
	public ItemBoneShard()
	{
		super();
		setUnlocalizedName("boneShard");
		setCreativeTab(ModMain.tabBetterBeginnings);
	}

	@Override
	public String getName()
	{
		return "boneShard";
	}
}
