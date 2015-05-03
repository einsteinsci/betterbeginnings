package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.register.IBBName;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class ItemFlintHatchet extends ItemAxe implements IBBName
{
	public ItemFlintHatchet()
	{
		super(ToolMaterial.WOOD);
		setUnlocalizedName(getName());
		setCreativeTab(ModMain.tabBetterBeginnings);
	}

	@Override
	public String getName()
	{
		return "flintHatchet";
	}

	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass)
	{
		return toolMaterial.getHarvestLevel();
	}

	@Override
	public Set<String> getToolClasses(ItemStack stack)
	{
		Set<String> res = new HashSet<>();

		res.add("axe");

		return res;
	}
}
