package net.einsteinsci.betterbeginnings;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.einsteinsci.betterbeginnings.config.BBConfig;
import net.einsteinsci.betterbeginnings.event.BBEventHandler;
import net.einsteinsci.betterbeginnings.event.Worldgen;
import net.einsteinsci.betterbeginnings.network.PacketNetherBrickOvenFuelLevel;
import net.einsteinsci.betterbeginnings.network.ServerProxy;
import net.einsteinsci.betterbeginnings.register.*;
import net.einsteinsci.betterbeginnings.register.achievement.RegisterAchievements;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;

@Mod(modid = ModMain.MODID, version = ModMain.VERSION, name = ModMain.NAME,
     guiFactory = "net.einsteinsci.betterbeginnings.config.BBConfigGuiFactory")
public class ModMain
{
	public static final String MODID = "betterbeginnings";
	public static final String VERSION = "0.0.6.0";
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
	public static SimpleNetworkWrapper network;
	@SidedProxy(clientSide = "net.einsteinsci.betterbeginnings.network.ClientProxy",
	            serverSide = "net.einsteinsci.betterbeginnings.network.ServerProxy")
	public static ServerProxy proxy;
	public BBEventHandler eventHandler = new BBEventHandler();

	public static void LogDebug(String text)
	{
		if (BBConfig.debugLogging)
		{
			Log(Level.DEBUG, text);
		}
	}

	public static void Log(Level level, String text)
	{
		FMLLog.log(NAME, level, text);
	}

	public static void LogDebug(Level level, String text)
	{
		if (BBConfig.debugLogging)
		{
			Log(level, text);
		}
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{
		Log("Starting pre-initialization...");

		configFile = new Configuration(e.getSuggestedConfigurationFile());
		configFile.load();
		BBConfig.syncConfig(configFile);

		proxy.registerNetworkStuff();
		proxy.registerRenderThings();


		FMLCommonHandler.instance().bus().register(eventHandler);
		MinecraftForge.EVENT_BUS.register(eventHandler);

		network = NetworkRegistry.INSTANCE.newSimpleChannel("bbchannel");
		network.registerMessage(PacketNetherBrickOvenFuelLevel.PacketHandler.class,
		                        PacketNetherBrickOvenFuelLevel.class, 0, Side.CLIENT);

		RegisterItems.register();
		RegisterBlocks.register();
		RegisterTileEntities.register();
	}

	public static void Log(String text)
	{
		Log(Level.INFO, text);
	}

	@EventHandler
	public void init(FMLInitializationEvent e)
	{
		RemoveRecipes.remove();
		RegisterRecipes.addShapelessRecipes();
		RegisterRecipes.addShapedRecipes();
		RegisterRecipes.addAdvancedRecipes();
		RegisterRecipes.addFurnaceRecipes();

		RemoveRecipes.removeFurnaceRecipes();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e)
	{

		RegisterItems.tweakVanilla();
		Worldgen.addWorldgen();
		AchievementPage.registerAchievementPage(new AchievementPage(NAME, RegisterAchievements.getAchievements()));
		Log("Finished post-initialization.");
	}
}
