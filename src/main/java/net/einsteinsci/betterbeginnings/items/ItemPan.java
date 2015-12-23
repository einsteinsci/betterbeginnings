package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.register.IBBName;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemPan extends Item implements ICampfireUtensil
{
	public ItemPan()
	{
		super();
		setUnlocalizedName(getName());
		setMaxDamage(250);
		setMaxStackSize(1);
		setCreativeTab(ModMain.tabBetterBeginnings);
	}

	@Override
	public float getCampfireSpeedModifier(ItemStack stack)
	{
		return 2.0f;
	}

	@Override
	public boolean doCookingDamage(ItemStack stack)
	{
		int damage = stack.getItemDamage();
		stack.setItemDamage(damage + 1);

		return stack.getItemDamage() >= stack.getMaxDamage();
	}

	@Override
	public String getName()
	{
		return "pan";
	}
}
