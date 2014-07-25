package net.einsteinsci.noobcraft.gui;

import net.einsteinsci.noobcraft.ModMain;
import net.einsteinsci.noobcraft.inventory.ContainerBrickOven;
import net.einsteinsci.noobcraft.tileentity.TileEntityBrickOven;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiBrickOven extends GuiContainer
{
	private static final ResourceLocation ovenGuiTextures = new ResourceLocation(ModMain.MODID
		+ ":textures/gui/container/brickOven.png");
	private TileEntityBrickOven tileBrickOven;
	
	public GuiBrickOven(InventoryPlayer inventory, TileEntityBrickOven tile)
	{
		super(new ContainerBrickOven(inventory, tile));
		tileBrickOven = tile;
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
