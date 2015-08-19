package net.einsteinsci.betterbeginnings.items;


import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.register.IBBName;
import net.einsteinsci.betterbeginnings.register.RegisterItems;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemRoastingStick extends Item implements IBBName
{

	public ItemRoastingStick()
	{
		setUnlocalizedName(getName());
		setCreativeTab(ModMain.tabBetterBeginnings);
		setMaxStackSize(10);
	}

	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		if (player.inventory.hasItemStack(new ItemStack(RegisterItems.marshmallow)))
		{
			player.inventory.consumeInventoryItem(RegisterItems.marshmallow);
			ItemStack mallowStick = new ItemStack(RegisterItems.roastingStickRawMallow);
			if (!player.inventory.addItemStackToInventory(mallowStick))
			{
				EntityItem drop = new EntityItem(world, player.posX, player.posY, player.posZ, mallowStick);

				world.spawnEntityInWorld(drop);
			}

			stack.stackSize--;
			if (stack.stackSize <= 0)
			{
				player.inventory.setItemStack(null);
			}
		}
		player.inventoryContainer.detectAndSendChanges();
		return stack;
	}

	@Override
	public String getName()
	{
		return "roastingStick";
	}
}
