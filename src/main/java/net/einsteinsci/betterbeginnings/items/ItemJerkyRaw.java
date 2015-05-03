package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.register.IBBName;
import net.minecraft.item.ItemFood;

public class ItemJerkyRaw extends ItemFood implements IBBName
{
	public ItemJerkyRaw()
	{
		super(0, 8.0f, true);
		setUnlocalizedName("jerkyRaw");
		setCreativeTab(ModMain.tabBetterBeginnings);
		setPotionEffect(17, 30, 1, 60);
	}

	@Override
	public String getName()
	{
		return "jerkyRaw";
	}
}
