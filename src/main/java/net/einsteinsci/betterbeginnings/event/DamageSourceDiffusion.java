package net.einsteinsci.betterbeginnings.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;

public class DamageSourceDiffusion extends DamageSource
{
	public DamageSourceDiffusion()
	{
		super("diffusion");
		setDamageBypassesArmor();
		setDifficultyScaled();
	}

	@Override
	public IChatComponent getDeathMessage(EntityLivingBase mob)
	{
		return new ChatComponentTranslation("death.diffusion", mob.getCommandSenderName());
	}
}
