package net.einsteinsci.betterbeginnings.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.einsteinsci.betterbeginnings.inventory.ContainerKiln;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityKiln;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiKiln extends GuiContainer
{
	private static final ResourceLocation kilnGuiTextures = new ResourceLocation(
			"minecraft:textures/gui/container/furnace.png");
	private TileEntityKiln tileKiln;

	public GuiKiln(InventoryPlayer invPlayer, TileEntityKiln tile)
	{
		super(new ContainerKiln(invPlayer, tile));
		tileKiln = tile;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		String string =
				tileKiln.hasCustomInventoryName()
						? tileKiln.getInventoryName()
						: I18n.format(tileKiln.getInventoryName(),
									  new Object[0]);
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

		if (tileKiln.isBurning())
		{
			i1 = tileKiln.getBurnTimeRemainingScaled(12);
			drawTexturedModalRect(k + 56, l + 36 + 12 - i1, 176, 12 - i1, 14, i1 + 2);
		}

		i1 = tileKiln.getCookProgressScaled(24);
		drawTexturedModalRect(k + 79, l + 34, 176, 14, i1 + 1, 16);
	}

}
