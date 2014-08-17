package net.einsteinsci.betterbeginnings.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.inventory.ContainerBrickOven;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityBrickOven;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiBrickOven extends GuiContainer
{
	private static final ResourceLocation ovenGuiTextures = new ResourceLocation(ModMain.MODID +
		":textures/gui/container/brickOven.png");
	private TileEntityBrickOven tileBrickOven;
	
	public GuiBrickOven(InventoryPlayer inventory, TileEntityBrickOven tile)
	{
		super(new ContainerBrickOven(inventory, tile));
		tileBrickOven = tile;
	}
	
	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		String string =
			tileBrickOven.hasCustomInventoryName() ? tileBrickOven.getInventoryName() : I18n.format(
				tileBrickOven.getInventoryName(), new Object[0]);
		fontRendererObj.drawString(string, xSize / 2 - fontRendererObj.getStringWidth(string), 6, 4210752);
		fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, ySize - 94, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		mc.getTextureManager().bindTexture(ovenGuiTextures);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
		int i1;
		
		if (tileBrickOven.isBurning())
		{
			i1 = tileBrickOven.getBurnTimeRemainingScaled(12);
			drawTexturedModalRect(k + 92, l + 41 + 12 - i1, 176, 12 - i1, 14, i1 + 2);
		}
		
		i1 = tileBrickOven.getCookProgressScaled(24);
		drawTexturedModalRect(k + 89, l + 20, 176, 14, i1 + 1, 16);
	}
	
	
}
