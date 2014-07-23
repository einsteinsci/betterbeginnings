package net.einsteinsci.noobcraft.gui;

import net.einsteinsci.noobcraft.ModMain;
import net.einsteinsci.noobcraft.inventory.ContainerDoubleWorkbench;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

public class GuiDoubleWorkbench extends GuiContainer
{
	private static final ResourceLocation workbenchGuiTextures = new ResourceLocation(ModMain.MODID
		+ ":textures/gui/container/doubleWorkbench.png");
	
	private final ContainerDoubleWorkbench container;
	
	public GuiDoubleWorkbench(InventoryPlayer invPlayer, World world, int x, int y, int z)
	{
		super(new ContainerDoubleWorkbench(invPlayer, world, x, y, z));
		
		container = (ContainerDoubleWorkbench) inventorySlots;
	}
	
	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of
	 * the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		// I'm guessing the really big number at the end is the z layer.
		fontRendererObj.drawString(I18n.format("container.craftingdouble", new Object[0]), 33, 6, 4210752);
		// fontRendererObj.drawString(I18n.format("container.inventory", new
		// Object[0]), 33, ySize - 96 + 2, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(workbenchGuiTextures);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
	}
}
