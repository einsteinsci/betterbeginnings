package net.einsteinsci.betterbeginnings.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.inventory.ContainerDoubleWorkbench;
import net.einsteinsci.betterbeginnings.register.recipe.AdvancedCraftingHandler;
import net.einsteinsci.betterbeginnings.register.recipe.AdvancedRecipe;
import net.einsteinsci.betterbeginnings.renderer.RenderItemPartialTransparency;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class GuiDoubleWorkbench extends GuiContainer
{
	private static final ResourceLocation workbenchGuiTextures = new ResourceLocation(ModMain.MODID +
																							  ":textures/gui/container/doubleWorkbench.png");

	private final ContainerDoubleWorkbench container;

	private final RenderItemPartialTransparency partialTransItemRenderer = new RenderItemPartialTransparency();

	public GuiDoubleWorkbench(InventoryPlayer invPlayer, World world, int x, int y, int z)
	{
		super(new ContainerDoubleWorkbench(invPlayer, world, x, y, z));

		container = (ContainerDoubleWorkbench)inventorySlots;
	}

	@Override
	public void drawScreen(int xMouse, int yMouse, float par3)
	{
		super.drawScreen(xMouse, yMouse, par3);

		renderTransparentItems();
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
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

		// renderTransparentItems();
	}

	public void renderTransparentItems()
	{
		if (AdvancedCraftingHandler.crafting().hasRecipe(container.craftMatrix, container.worldObj))
		{
			for (AdvancedRecipe recipe : AdvancedCraftingHandler.crafting().recipes)
			{
				if (recipe.matchesMostly(container.craftMatrix, container.worldObj))
				{
					for (int i = 0; i < recipe.getNeededMaterials().length; ++i)
					{
						ItemStack needed = recipe.getNeededMaterials()[i];

						if (needed.getItemDamage() == OreDictionary.WILDCARD_VALUE)
						{
							needed.setItemDamage(0);
						}

						Slot slot = container.matSlots[i];
						if (container.addedMats.getStackInSlot(i) == null)
						{
							drawItemStack(needed, (width - xSize) / 2 + slot.xDisplayPosition, (height - ySize) / 2 +
									slot.yDisplayPosition, "" + needed.stackSize);
						}
					}
				}
			}
		}
	}

	private void drawItemStack(ItemStack stack, int xPos, int yPos, String note)
	{
		GL11.glTranslatef(0.0F, 0.0F, 32.0F);
		zLevel = 200.0F;
		partialTransItemRenderer.zLevel = 200.0F;
		FontRenderer font = null;
		if (stack != null)
		{
			font = stack.getItem().getFontRenderer(stack);
		}
		if (font == null)
		{
			font = fontRendererObj;
		}

		RenderHelper.enableGUIStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glEnable(GL11.GL_LIGHTING);
		partialTransItemRenderer.renderItemAndEffectIntoGUI(font, mc.getTextureManager(), stack, xPos, yPos);
		partialTransItemRenderer.renderItemOverlayIntoGUI(font, mc.getTextureManager(), stack, xPos, yPos - 0, note);
		zLevel = 0.0F;
		partialTransItemRenderer.zLevel = 0.0F;
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		RenderHelper.enableStandardItemLighting();
	}
}


// Buffer ;)
