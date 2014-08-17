package net.einsteinsci.betterbeginnings.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.inventory.ContainerSimpleWorkbench;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiSimpleWorkbench extends GuiContainer
{
	private static final ResourceLocation craftingTableGuiTextures = new ResourceLocation(ModMain.MODID
																								  + ":textures/gui/container/simpleWorkbench.png");
	private ContainerSimpleWorkbench container;

	public GuiSimpleWorkbench(InventoryPlayer invPlayer, World world, int x, int y, int z)
	{
		super(new ContainerSimpleWorkbench(invPlayer, world, x, y, z));

		container = (ContainerSimpleWorkbench)inventorySlots;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of
	 * the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		// I'm guessing the really big number at the end is the z layer.
		fontRendererObj.drawString(I18n.format("container.crafting", new Object[0]), 28, 6, 4210752);
		fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, ySize - 96 + 2,
								   4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(craftingTableGuiTextures);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);

		if (container.needsBigBoyBench())
		{
			drawTexturedModalRect(k + 87, l + 32, xSize, 0, 28, 21);
		}
	}
}
