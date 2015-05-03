package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.register.IBBName;
import net.minecraft.item.ItemSword;

//Replaces Vanilla Wood Sword
public class ItemNoobWoodSword extends ItemSword implements IBBName
{
	public ItemNoobWoodSword(ToolMaterial material)
	{
		super(material);
		setUnlocalizedName("noobWoodSword");
		//setTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));
		//setMaxDamage(60);
		//setCreativeTab(CreativeTabs.tabCombat);
	}

	@Override
	public String getName()
	{
		return "noobWoodSword";
	}
}
