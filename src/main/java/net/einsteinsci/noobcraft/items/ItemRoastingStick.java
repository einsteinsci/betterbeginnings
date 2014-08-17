package net.einsteinsci.noobcraft.items;


import net.einsteinsci.noobcraft.ModMain;
import net.einsteinsci.noobcraft.register.RegisterItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemRoastingStick extends Item {

	public ItemRoastingStick(){
		setUnlocalizedName("roastingStick");
		this.shouldRotateAroundWhenRendering();
		setTextureName(ModMain.MODID + ":" + getUnlocalizedName().substring(5));
		this.setCreativeTab(ModMain.tabNoobCraft);
		this.setMaxStackSize(1);
	}
	
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player){
		if(player.inventory.hasItemStack(new ItemStack(RegisterItems.marshmallow)))
		{
			player.inventory.consumeInventoryItem(RegisterItems.marshmallow);
			stack = new ItemStack(RegisterItems.roastingStickrawMallow);
		}
		player.inventoryContainer.detectAndSendChanges();
		return stack;
	}
}
