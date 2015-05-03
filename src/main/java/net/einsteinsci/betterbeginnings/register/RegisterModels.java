package net.einsteinsci.betterbeginnings.register;

import net.einsteinsci.betterbeginnings.ModMain;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

public class RegisterModels
{
	public static <BBBlock extends Block & IBBName, BBItem extends Item & IBBName> void register()
	{
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

		// blocks
		for (Block b : RegisterBlocks.allBlocks)
		{
			registerBlock((BBBlock)b);
		}

		// items
		for (Item i : RegisterItems.allItems)
		{
			registerItem((BBItem)i);
		}
	}

	public static <BBBlock extends Block & IBBName> void registerBlock(BBBlock block)
	{
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(
				Item.getItemFromBlock(block), 0, new ModelResourceLocation(
						ModMain.MODID + ":" + block.getName(), "inventory"));
	}

	public static <BBItem extends Item & IBBName> void registerItem(BBItem item)
	{
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(
				item, 0, new ModelResourceLocation(ModMain.MODID + ":" + item.getName(), "inventory"));
	}
}
