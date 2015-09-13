package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;

public class ItemRotisserie extends ItemPan
{
	public ItemRotisserie()
	{
		super();
		setUnlocalizedName(getName());
		setMaxDamage(4);
		setMaxStackSize(1);
		setCreativeTab(ModMain.tabBetterBeginnings);
	}

	@Override
	public String getName()
	{
		return "rotisserie";
	}
}
