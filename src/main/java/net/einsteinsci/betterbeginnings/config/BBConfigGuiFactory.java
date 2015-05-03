package net.einsteinsci.betterbeginnings.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

import java.util.Set;

public class BBConfigGuiFactory implements IModGuiFactory
{
	@Override
	public void initialize(Minecraft minecraftInstance)
	{
		// Nothin here
	}
	
	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass()
	{
		return BBConfigGui.class;
	}
	
	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
	{
		return null;
	}
	
	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element)
	{
		return null;
	}
	
}
