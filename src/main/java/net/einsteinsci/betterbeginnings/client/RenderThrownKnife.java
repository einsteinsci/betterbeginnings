package net.einsteinsci.betterbeginnings.client;

import net.einsteinsci.betterbeginnings.entity.projectile.EntityThrownKnife;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class RenderThrownKnife extends RenderSnowball 
{	
	public RenderThrownKnife(Minecraft minecraft) 
	{
		super(minecraft.getRenderManager(), Items.stick, minecraft.getRenderItem());
	}
	
	@Override
	public ItemStack func_177082_d(Entity entityIn) 
	{
		return ((EntityThrownKnife) entityIn).getKnife();
	}
}
