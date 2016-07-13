package net.einsteinsci.betterbeginnings.items;

import net.einsteinsci.betterbeginnings.entity.projectile.EntityThrownKnife;
import net.einsteinsci.betterbeginnings.register.IBBName;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

public abstract class ItemKnife extends ItemTool implements IBBName
{
	public static final float DAMAGE = 3.0f;
	public static final int DRAW_TIME = 32;

	public ItemKnife(ToolMaterial material)
	{
		super(DAMAGE, material, getBreakable());
	}

	public static Set getBreakable()
	{
		Set<Block> s = new HashSet<>();

		// s.add(Blocks.log);
		// s.add(Blocks.log2);
		// s.add(Blocks.planks);
		s.add(Blocks.pumpkin);
		s.add(Blocks.lit_pumpkin);
		s.add(Blocks.melon_block);
		s.add(Blocks.clay);
		s.add(Blocks.grass);
		s.add(Blocks.mycelium);
		s.add(Blocks.leaves);
		s.add(Blocks.leaves2);
		s.add(Blocks.brown_mushroom_block);
		s.add(Blocks.red_mushroom_block);
		s.add(Blocks.glass);
		s.add(Blocks.glass_pane);
		s.add(Blocks.soul_sand);
		s.add(Blocks.stained_glass);
		s.add(Blocks.stained_glass_pane);
		s.add(Blocks.cactus);

		return s;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn,
			EntityPlayer playerIn) 
	{
		playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
		return super.onItemRightClick(itemStackIn, worldIn, playerIn);
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn,
			EntityPlayer playerIn, int timeLeft) 
	{
		if(!worldIn.isRemote)
		{
			EntityThrownKnife knife = new EntityThrownKnife(worldIn, playerIn, stack);
			knife.setForce((float) Math.min((this.getMaxItemUseDuration(stack) - timeLeft), ItemKnife.DRAW_TIME) / ItemKnife.DRAW_TIME);
			System.out.println("SPAWN KNIFE");
			worldIn.spawnEntityInWorld(knife);
			if(!playerIn.capabilities.isCreativeMode)
			{
				playerIn.destroyCurrentEquippedItem();
			}
		}
		
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack) 
	{
		return EnumAction.BOW;
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack) 
	{
		return 72000;
	}

	@Override
	public boolean shouldRotateAroundWhenRendering()
	{
		return true;
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

		res.add("knife");

		return res;
	}

	// ...which also requires this...
	@Override
	public ItemStack getContainerItem(ItemStack itemStack)
	{
		ItemStack result = itemStack.copy();
		result.setItemDamage(itemStack.getItemDamage() + 1);

		return result;
	}

	// Allows durability-based crafting.
	@Override
	public boolean hasContainerItem(ItemStack stack)
	{
		return true;
	}

	@Override
	public abstract String getName();
}
