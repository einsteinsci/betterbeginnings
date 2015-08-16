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
		KilnRecipeHandler kilnRecipeHandler = new KilnRecipeHandler();

		API.registerRecipeHandler(kilnRecipeHandler);

		API.hideItem(new ItemStack(RegisterBlocks.kilnLit));
		API.hideItem(new ItemStack(RegisterBlocks.brickOvenLit));
		API.hideItem(new ItemStack(RegisterBlocks.smelterLit));
		API.hideItem(new ItemStack(RegisterBlocks.obsidianKilnLit));
		API.hideItem(new ItemStack(RegisterBlocks.netherBrickOvenLit));
		API.hideItem(new ItemStack(RegisterBlocks.enderSmelterLit));

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
