package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.register.IBBName;


public class ItemRockHammer extends ItemHammer implements IBBName
{
	public ItemRockHammer(ToolMaterial material)
	{
		super(material);
		setUnlocalizedName("rockHammer");
		setCreativeTab(ModMain.tabBetterBeginnings);
	}

	@Override
	public String getName()
	{
		return "rockHammer";
	}
}
