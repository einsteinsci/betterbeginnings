package net.einsteinsci.betterbeginnings.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.register.RegisterBlocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

// NEI support wizardry
@Optional.Interface(iface = "codechicken.nei.api.IConfigureNEI", modid = "NotEnoughItems")
public class NEIConfig implements IConfigureNEI
{
	@Override
	public void loadConfig()
	{
		NEIKilnRecipeHandler kilnRecipeHandler = new NEIKilnRecipeHandler();
		API.registerRecipeHandler(kilnRecipeHandler);
		API.registerUsageHandler(kilnRecipeHandler);

		NEISmelterRecipeHandler smelterRecipeHandler = new NEISmelterRecipeHandler();
		API.registerRecipeHandler(smelterRecipeHandler);
		API.registerUsageHandler(smelterRecipeHandler);

		NEIBrickOvenRecipeHandler brickOvenRecipeHandler = new NEIBrickOvenRecipeHandler();
		API.registerRecipeHandler(brickOvenRecipeHandler);
		API.registerUsageHandler(brickOvenRecipeHandler);

		NEIAdvancedCraftingHandler advancedCraftingHandler = new NEIAdvancedCraftingHandler();
		API.registerRecipeHandler(advancedCraftingHandler);
		API.registerUsageHandler(advancedCraftingHandler);

		NEICampfireRecipeHandler campfireRecipeHandler = new NEICampfireRecipeHandler();
		API.registerRecipeHandler(campfireRecipeHandler);
		API.registerUsageHandler(campfireRecipeHandler);

		API.hideItem(new ItemStack(RegisterBlocks.kilnLit));
		API.hideItem(new ItemStack(RegisterBlocks.brickOvenLit));
		API.hideItem(new ItemStack(RegisterBlocks.smelterLit));
		API.hideItem(new ItemStack(RegisterBlocks.obsidianKilnLit));
		API.hideItem(new ItemStack(RegisterBlocks.netherBrickOvenLit));
		API.hideItem(new ItemStack(RegisterBlocks.enderSmelterLit));
		API.hideItem(new ItemStack(RegisterBlocks.redstoneKilnLit));

		API.hideItem(new ItemStack(RegisterBlocks.campfireLit));
	}

	@Override
	public String getName()
	{
		return ModMain.NAME;
	}

	@Override
	public String getVersion()
	{
		return ModMain.VERSION;
	}
}
