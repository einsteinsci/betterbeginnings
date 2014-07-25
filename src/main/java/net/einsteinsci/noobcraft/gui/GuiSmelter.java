package net.einsteinsci.noobcraft.gui;

import net.einsteinsci.noobcraft.ModMain;
import net.einsteinsci.noobcraft.inventory.ContainerSmelter;
import net.einsteinsci.noobcraft.tileentity.TileEntitySmelter;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiSmelter extends GuiContainer
{
	private static final ResourceLocation kilnGuiTextures = new ResourceLocation(ModMain.MODID +
		":textures/gui/container/smelter.png");
	private TileEntitySmelter tileSmelter;
	
	public GuiSmelter(InventoryPlayer invPlayer, TileEntitySmelter tile)
	{
		super(new ContainerSmelter(invPlayer, tile));
		tileSmelter = tile;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		String string =
			tileSmelter.hasCustomInventoryName() ? tileSmelter.getInventoryName() : I18n.format(
				tileSmelter.getInventoryName(), new Object[0]);
		fontRendererObj.drawString(string, xSize / 2 - fontRendererObj.getStringWidth(string), 6, 4210752);
		fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, ySize - 94, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		mc.getTextureManager().bindTexture(kilnGuiTextures);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
		int i1;
		
		if (tileSmelter.isBurning())
		{
			i1 = tileSmelter.getBurnTimeRemainingScaled(12);
			drawTexturedModalRect(k + 56, l + 36 + 12 - i1, 176, 12 - i1, 14, i1 + 2);
		}
		
		i1 = tileSmelter.getCookProgressScaled(24);
		drawTexturedModalRect(k + 79, l + 34, 176, 14, i1 + 1, 16);
	}
}
