package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.register.IBBName;
import net.minecraft.item.ItemFood;

public class ItemJerkyBeef extends ItemFood implements IBBName
{
	public ItemJerkyBeef()
	{
		super(4, 8.0f, true);
		setUnlocalizedName("jerkyBeef");
		setCreativeTab(ModMain.tabBetterBeginnings);
	}

	@Override
	public String getName()
	{
		return "jerkyBeef";
	}
}
