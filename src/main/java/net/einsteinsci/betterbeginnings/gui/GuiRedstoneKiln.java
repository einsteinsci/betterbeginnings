package net.einsteinsci.betterbeginnings.gui;

import net.einsteinsci.betterbeginnings.inventory.ContainerRedstoneKiln;
import net.einsteinsci.betterbeginnings.tileentity.TileEntityRedstoneKiln;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiRedstoneKiln extends GuiContainer
{
	private static final ResourceLocation kilnGuiTextures = new ResourceLocation(
		"betterbeginnings:textures/gui/container/redstoneKiln.png");
	private TileEntityRedstoneKiln tileKiln;

	public GuiRedstoneKiln(InventoryPlayer invPlayer, TileEntityRedstoneKiln tile)
	{
		super(new ContainerRedstoneKiln(invPlayer, tile));
		tileKiln = tile;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		String string = tileKiln.hasCustomName() ? tileKiln.getCommandSenderName() :
			I18n.format(tileKiln.getCommandSenderName());
		fontRendererObj.drawString(string, 38, 6, 4210752);
		fontRendererObj.drawString(I18n.format("container.inventory"), 40, ySize - 94, 4210752);

		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;

		// In fuel zone
		if (mouseX >= k + 17 && mouseX <= k + 32 && mouseY >= l + 10 && mouseY <= l + 57)
		{
			//_drawPowerTooltip(mouseX - k, mouseY - l);
		}
	}

	private void _drawPowerTooltip(int x, int y)
	{
		List<String> lines = new ArrayList<>();

		lines.add("" + tileKiln.getBattery().getEnergyStored() + " RF");

		drawHoveringText(lines, x, y); //Draw tooltip
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

		if (tileKiln.getBattery().getEnergyStored() > 0)
		{
			i1 = tileKiln.getEnergyScaled(48);
			drawTexturedModalRect(k + 17, l + 10 + 48 - i1, 176, 31 + 48 - i1, 16, i1 + 2);
		}

		if (tileKiln.isBurning())
		{
			drawTexturedModalRect(k + 56, l + 46, 176, 0, 14, 14);
		}

		i1 = tileKiln.getCookProgressScaled(24);
		drawTexturedModalRect(k + 79, l + 34, 176, 14, i1 + 1, 16);
	}
}
