package net.einsteinsci.noobcraft.gui;


/*
 * @SideOnly(Side.CLIENT) public class GuiKilnStacked extends GuiContainer { private static final ResourceLocation
 * kilnGuiTextures = new ResourceLocation(ModMain.MODID + ":textures/gui/container/kilnStacked.png"); private
 * TileEntityKiln tileKilnTop; private TileEntityKiln tileKilnBottom;
 * 
 * public GuiKilnStacked(InventoryPlayer invPlayer, TileEntityKiln kilnTop, TileEntityKiln kilnBottom) { super(new
 * ContainerKilnStacked(invPlayer, kilnTop, kilnBottom)); tileKilnTop = kilnTop; tileKilnBottom = kilnBottom; }
 * 
 * @Override protected void drawGuiContainerForegroundLayer(int par1, int par2) { String string = "Double Kiln"; //
 * tileKilnTop.hasCustomInventoryName() ? tileKilnTop.getInventoryName() : I18n.format( //
 * tileKilnTop.getInventoryName(), new Object[0]); fontRendererObj.drawString(string, xSize / 2 -
 * fontRendererObj.getStringWidth(string), 6, 4210752); fontRendererObj.drawString(I18n.format("container.inventory",
 * new Object[0]), 8, ySize - 94, 4210752); }
 * 
 * @Override protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) { GL11.glColor4f(1.0f, 1.0f,
 * 1.0f, 1.0f); mc.getTextureManager().bindTexture(kilnGuiTextures); int k = (width - xSize) / 2; int l = (height -
 * ySize) / 2;
 * 
 * drawTexturedModalRect(k, l, 0, 0, xSize, ySize); int i1;
 * 
 * if (tileKilnBottom.isBurning()) { i1 = tileKilnBottom.getBurnTimeRemainingScaled(12); drawTexturedModalRect(k + 56, l
 * + 36 + 12 - i1, 176, 12 - i1, 14, i1 + 2); }
 * 
 * i1 = tileKilnTop.getCookProgressScaled(24); drawTexturedModalRect(k + 79, l + 29, 176, 14, i1 + 1, 16);
 * 
 * int i2 = tileKilnBottom.getCookProgressScaled(24); drawTexturedModalRect(k + 79, l + 49, 176, 14, i2 + 1, 16); }
 * 
 * }
 */
