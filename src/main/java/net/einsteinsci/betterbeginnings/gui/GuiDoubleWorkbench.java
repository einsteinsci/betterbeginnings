package net.einsteinsci.betterbeginnings.gui;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.inventory.ContainerDoubleWorkbench;
import net.einsteinsci.betterbeginnings.register.recipe.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class GuiDoubleWorkbench extends GuiContainer
{
	private static final ResourceLocation workbenchGuiTextures = new ResourceLocation(ModMain.MODID +
		":textures/gui/container/doubleWorkbench_MC18.png");

	private final ContainerDoubleWorkbench container;
	private boolean needsCatalystItems = false;

	//protected final int trueXSize = xSize + 20;

	public GuiDoubleWorkbench(InventoryPlayer invPlayer, World world, BlockPos pos)
	{
		super(new ContainerDoubleWorkbench(invPlayer, world, pos));
		xSize += 20;

		container = (ContainerDoubleWorkbench)inventorySlots;
	}

	@Override
	public void drawScreen(int xMouse, int yMouse, float par3)
	{
		super.drawScreen(xMouse, yMouse, par3);

		//renderTransparentItems();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		// I'm guessing the really big number at the end is the z layer.
		fontRendererObj.drawString(I18n.format("container.craftingdouble"), 33 + 20, 6, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(workbenchGuiTextures);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);

		if (needsCatalystItems)
		{
			drawTexturedModalRect(k + 144, l + 30, xSize, 0, 26, 26);
		}

		renderTransparentItems(k, l);
	}

	public void renderTransparentItems(int k, int l)
	{
		final int CATALYST_X_OFFSET = -20;

		needsCatalystItems = false;
		if (AdvancedCraftingHandler.crafting().hasRecipe(container.craftMatrix, container.worldObj))
		{
			for (AdvancedRecipe recipe : AdvancedCraftingHandler.crafting().recipes)
			{
				if (recipe.matchesMostly(container.craftMatrix, container.worldObj))
				{
					for (int i = 0; i < recipe.getNeededMaterials().length; ++i)
					{
						OreRecipeElement neededElement = recipe.getNeededMaterials()[i];
						ItemStack needed = neededElement.getFirst().copy();

						if (needed.getItemDamage() == OreDictionary.WILDCARD_VALUE)
						{
							needed.setItemDamage(0);
						}

						Slot slot = container.matSlots[i];
						if (container.addedMats.getStackInSlot(i) == null)
						{
							drawItemStack(needed, k + slot.xDisplayPosition + CATALYST_X_OFFSET,
								l + slot.yDisplayPosition, "" + needed.stackSize);
						}
					}

					ItemStack result = recipe.getRecipeOutput();
					Slot slot = container.resultSlot;
					if (container.craftResult.getStackInSlot(0) == null)
					{
						if (result != null)
						{
							// Draw red output box
							mc.getTextureManager().bindTexture(workbenchGuiTextures);
							needsCatalystItems = true;

							drawItemStack(result, k + slot.xDisplayPosition, l + slot.yDisplayPosition, "");
						}
					}
					break;
				}
			}
		}
	}

	private void drawItemStack(ItemStack stack, int xPos, int yPos, String note)
	{
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

		// GL11.glEnable(GL11.GL_BLEND);
		// GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		// GL11.glColor4f(1,1,1, 0.80f);

		renderItem.renderItemAndEffectIntoGUI(stack, xPos, yPos);

		FontRenderer font = null;
		if (stack != null)
		{
			font = stack.getItem().getFontRenderer(stack);
		}
		if (font == null)
		{
			font = fontRendererObj;
		}

		renderItem.renderItemOverlayIntoGUI(font, stack, xPos, yPos, note);

		// GL11.glDisable(GL11.GL_BLEND);
		// GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		// GL11.glBlendFunc(GL11.GL_SRC_ALPHA, 0);
	}
}