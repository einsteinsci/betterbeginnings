package net.einsteinsci.noobcraft.event;

import net.einsteinsci.noobcraft.register.RegisterItems;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

/**
 * Created by einsteinsci on 8/11/2014.
 */
public class Worldgen
{
	public static void addWorldgen()
	{
		ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(
				RegisterItems.marshmallow), 1, 5, 100));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST)
				.addItem(new WeightedRandomChestContent(new ItemStack(RegisterItems.marshmallow), 1, 10, 200));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(
				RegisterItems.marshmallow), 1, 10, 200));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING)
				.addItem(new WeightedRandomChestContent(new ItemStack(RegisterItems.marshmallow), 1, 12, 150));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR)
				.addItem(new WeightedRandomChestContent(new ItemStack(RegisterItems.marshmallow), 1, 12, 150));
	}
}
