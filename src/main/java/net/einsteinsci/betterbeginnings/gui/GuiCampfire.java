package net.einsteinsci.betterbeginnings.gui;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.inventory.ContainerCampfire;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityCampfire;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiCampfire extends GuiContainer
{

	private static final ResourceLocation campfireGuiTextures = new ResourceLocation(ModMain.MODID +
																							 ":textures/gui/container/campfire.png");
	private TileEntityCampfire tileCampfire;

	public GuiCampfire(InventoryPlayer invPlayer, TileEntityCampfire tile)
	{
		super(new ContainerCampfire(invPlayer, tile));
		tileCampfire = tile;
		this.xSize = 176;
		this.ySize = 166;
	}

	public void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 4, 4210752);
	}

	public void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		mc.getTextureManager().bindTexture(campfireGuiTextures);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;

		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
		int i1;

		if (tileCampfire.isBurning())
		{
			System.out.println("Burning");
			i1 = tileCampfire.getBurnTimeRemainingScaled(12);
			drawTexturedModalRect(k + 56, l + 36 + 12 - i1, 176, 12 - i1, 14, i1 + 2);
		}
		i1 = tileCampfire.getCookProgressScaled(24);
		drawTexturedModalRect(k + 79, l + 34, 176, 14, i1 + 1, 16);
	}

}
