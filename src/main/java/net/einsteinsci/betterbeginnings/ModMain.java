package net.einsteinsci.betterbeginnings;

import net.einsteinsci.betterbeginnings.config.BBConfig;
import net.einsteinsci.betterbeginnings.config.BBConfigFolderLoader;
import net.einsteinsci.betterbeginnings.event.BBEventHandler;
import net.einsteinsci.betterbeginnings.event.Worldgen;
import net.einsteinsci.betterbeginnings.network.PacketCampfireState;
import net.einsteinsci.betterbeginnings.network.PacketNetherBrickOvenFuelLevel;
import net.einsteinsci.betterbeginnings.network.ServerProxy;
import net.einsteinsci.betterbeginnings.register.*;
import net.einsteinsci.betterbeginnings.register.achievement.RegisterAchievements;
import net.einsteinsci.betterbeginnings.util.LogUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;

@Mod(modid = ModMain.MODID, version = ModMain.VERSION, name = ModMain.NAME,
     guiFactory = "net.einsteinsci.betterbeginnings.config.BBConfigGuiFactory")
public class ModMain
{
	public static final String MODID = "betterbeginnings";
	public static final String VERSION = "0.9.6-R1a";
	public static final String NAME = "Better Beginnings";
	public static final CreativeTabs tabBetterBeginnings = new CreativeTabs("tabBetterBeginnings")
	{
		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem()
		{
			return RegisterItems.flintKnife;
		}
	};

	@Instance(ModMain.MODID)
	public static ModMain modInstance;
	public static Configuration configFile;
	public BBEventHandler eventHandler = new BBEventHandler();

	@SidedProxy(clientSide = "net.einsteinsci.betterbeginnings.network.ClientProxy",
	            serverSide = "net.einsteinsci.betterbeginnings.network.ServerProxy")
	public static ServerProxy proxy;
	public static SimpleNetworkWrapper network;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{
		LogUtil.logDebug("Starting pre-initialization...");

		configFile = BBConfigFolderLoader.getConfigFile(e);
		configFile.load();
		BBConfig.initialize();
		BBConfig.syncConfig(configFile);

		proxy.registerNetworkStuff();
		proxy.registerRenderThings();

		FMLCommonHandler.instance().bus().register(eventHandler);
		MinecraftForge.EVENT_BUS.register(eventHandler);

		network = NetworkRegistry.INSTANCE.newSimpleChannel("bbchannel");
		network.registerMessage(PacketNetherBrickOvenFuelLevel.PacketHandler.class,
			PacketNetherBrickOvenFuelLevel.class, 0, Side.CLIENT);
		network.registerMessage(PacketCampfireState.PacketHandler.class,
			PacketCampfireState.class, 1, Side.CLIENT);

		RegisterItems.register();
		RegisterBlocks.register();
		RegisterTileEntities.register();
	}

	@EventHandler
	public void init(FMLInitializationEvent e)
	{
		RemoveRecipes.remove();
		RegisterRecipes.addShapelessRecipes();
		RegisterRecipes.addShapedRecipes();
		RegisterRecipes.addAdvancedRecipes();
		RegisterRecipes.addFurnaceRecipes();

		if (BBConfig.moduleFurnaces)
		{
			RemoveRecipes.removeFurnaceRecipes();
		}

		if (e.getSide() == Side.CLIENT)
		{
			RegisterModels.register();
		}
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e)
	{
		BBConfig.fillAlwaysBreakable();
		BBConfig.fillAlsoPickaxes();
		BBConfig.fillAlsoAxes();

		RegisterItems.tweakVanilla();
		Worldgen.addWorldgen();
		AchievementPage.registerAchievementPage(new AchievementPage(NAME, RegisterAchievements.getAchievements()));
		LogUtil.logDebug("Finished post-initialization.");
	}
}
