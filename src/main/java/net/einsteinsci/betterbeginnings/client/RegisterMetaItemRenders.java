package net.einsteinsci.betterbeginnings.client;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.items.ItemCharredMeat;
import net.einsteinsci.betterbeginnings.register.RegisterItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

public class RegisterMetaItemRenders
{
	public static void init()
	{
		// region charred meats
		for (int i = 0; i < 6; i++)
		{
			registerItemIconWithMeta(RegisterItems.charredMeat, i, ItemCharredMeat.getSimpleUnlocalizedName(i));
		}

		String[] charredMeatNames = new String[6];
		for (int i = 0; i < 6; i++)
		{
			charredMeatNames[i] = ModMain.MODID + ":" + ItemCharredMeat.getSimpleUnlocalizedName(i);
		}
		ModelBakery.addVariantName(RegisterItems.charredMeat, charredMeatNames);
		// endregion charred meats
	}

	public static void registerItemIcon(Item item)
	{
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0,
			new ModelResourceLocation(ModMain.MODID + ":" + item.getUnlocalizedName().substring(5), "inventory"));
	}

	public static void registerItemIconWithMeta(Item item, int meta, String filename)
	{
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta,
			new ModelResourceLocation(ModMain.MODID + ":" + filename, "inventory"));
	}
}
