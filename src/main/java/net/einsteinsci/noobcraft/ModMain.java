package net.einsteinsci.noobcraft;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.einsteinsci.noobcraft.config.NoobcraftConfig;
import net.einsteinsci.noobcraft.event.NoobcraftEventHandler;
import net.einsteinsci.noobcraft.event.Worldgen;
import net.einsteinsci.noobcraft.network.RepairTableRepairPacket;
import net.einsteinsci.noobcraft.register.*;
import net.einsteinsci.noobcraft.register.achievement.RegisterAchievements;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;

@Mod(modid = ModMain.MODID, version = ModMain.VERSION, name = ModMain.NAME,
guiFactory = "net.einsteinsci.noobcraft.config.NoobcraftConfigGuiFactory")
public class ModMain
{
	public static final String MODID = "noobcraft";
	public static final String VERSION = "0.0.3.1";
	public static final String NAME = "NoobCraft";
	public static final int PACKET_REPAIR_TABLE_REPAIR = 0;
	public static final CreativeTabs tabNoobCraft = new CreativeTabs("tabNoobCraft")
	{
		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem()
		{
			return RegisterItems.flintKnife;
		}
	};
	public static final AchievementPage pageNoobCraft = new AchievementPage("Noobcraft",
																			RegisterAchievements.getAchievements());

	// public NoobcraftConfig config = new NoobcraftConfig();
	@Instance(ModMain.MODID)
	public static ModMain modInstance;
	public static Configuration configFile;
	public static SimpleNetworkWrapper network;
	@SidedProxy(clientSide = "net.einsteinsci.noobcraft.ClientProxy",
		serverSide = "net.einsteinsci.noobcraft.ServerProxy")
	public static ServerProxy proxy;
	public NoobcraftEventHandler eventHandler = new NoobcraftEventHandler();

	public static void Log(String text)
	{
		Log(Level.INFO, text);
	}

	public static void Log(Level level, String text)
	{
		FMLLog.log("NoobCraft", level, text);
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{
		// FMLLog.info("[NoobCraft] Ready to be a noob?");
		Log("Starting pre-initialization...");

		configFile = new Configuration(e.getSuggestedConfigurationFile());
		configFile.load();
		NoobcraftConfig.syncConfig(configFile);

		proxy.registerNetworkStuff();
		proxy.registerRenderThings();

		FMLCommonHandler.instance().bus().register(eventHandler);
		MinecraftForge.EVENT_BUS.register(eventHandler);

		network = NetworkRegistry.INSTANCE.newSimpleChannel("noobcraft");
		network.registerMessage(RepairTableRepairPacket.Handler.class, RepairTableRepairPacket.class,
								PACKET_REPAIR_TABLE_REPAIR, Side.SERVER);

		RegisterItems.registerItems();
		RegisterBlocks.register();
		RegisterTileEntities.register();

		// armorMaterialCloth.customCraftingMaterial = RegisterItems.cloth;
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e)
	{
		RemoveRecipes.remove();
		RegisterRecipes.addShapelessRecipes();
		RegisterRecipes.addShapedRecipes();
		RegisterRecipes.addAdvancedRecipes();
		RegisterRecipes.addFurnaceRecipes();

		Worldgen.addWorldgen();

		AchievementPage.registerAchievementPage(pageNoobCraft);
		RemoveRecipes.removeFurnaceRecipes();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent e)
	{
		// RemoveRecipes.removeFurnaceRecipes();
		RegisterItems.tweakVanilla();
	}
}
