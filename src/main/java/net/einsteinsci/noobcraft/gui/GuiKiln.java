package net.einsteinsci.noobcraft.gui;

import net.einsteinsci.noobcraft.NoobcraftMod;
import net.einsteinsci.noobcraft.inventory.ContainerKiln;
import net.einsteinsci.noobcraft.tileentity.TileEntityKiln;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiKiln extends GuiContainer
{
	private static final ResourceLocation kilnGuiTextures = new ResourceLocation(NoobcraftMod.MODID + ":textures/gui/container/kiln.png");
	private TileEntityKiln tileKiln;

	public GuiKiln(InventoryPlayer invPlayer, TileEntityKiln tile) 
	{
		super(new ContainerKiln(invPlayer, tile));
		this.tileKiln = tile;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		String string = this.tileKiln.hasCustomInventoryName() ? 
				tileKiln.getInventoryName() : 
				I18n.format(this.tileKiln.getInventoryName(), new Object[0]);
		this.fontRendererObj.drawString(string, xSize / 2 - fontRendererObj.getStringWidth(string), 6, 4210752);
		this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 94, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) 
	{
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.getTextureManager().bindTexture(kilnGuiTextures);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
		int i1;
		
		if (this.tileKiln.isBurning())
		{
			i1 = this.tileKiln.getBurnTimeRemainingScaled(12);
			this.drawTexturedModalRect(k + 56, l + 36 + 12 - i1, 176, 12 - i1, 14, i1 + 2);
		}
		
		i1 = this.tileKiln.getCookProgressScaled(24);
		this.drawTexturedModalRect(k + 79, l + 34, 176, 14, i1 + 1, 16);
	}
	
}
