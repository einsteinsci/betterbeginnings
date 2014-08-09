package net.einsteinsci.noobcraft;

import net.einsteinsci.noobcraft.config.NoobcraftConfig;
import net.einsteinsci.noobcraft.event.NoobcraftEventHandler;
import net.einsteinsci.noobcraft.network.RepairTableRepairPacket;
import net.einsteinsci.noobcraft.register.*;
import net.einsteinsci.noobcraft.register.achievement.RegisterAchievements;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = ModMain.MODID, version = ModMain.VERSION,
guiFactory = "net.einsteinsci.noobcraft.config.NoobcraftConfigGuiFactory")
public class ModMain
{
	public static final String MODID = "noobcraft";
	public static final String VERSION = "0.0.3.1";
	
	public static Configuration configFile;
	
	public NoobcraftEventHandler eventHandler = new NoobcraftEventHandler();
	// public NoobcraftConfig config = new NoobcraftConfig();
	
	public static SimpleNetworkWrapper network;
	public static final int PACKET_REPAIR_TABLE_REPAIR = 0;
	
	@Instance(ModMain.MODID)
	public static ModMain modInstance;
	
	@SidedProxy(clientSide = "net.einsteinsci.noobcraft.ClientProxy",
		serverSide = "net.einsteinsci.noobcraft.ServerProxy")
	public static ServerProxy proxy;
	
	public static final CreativeTabs tabNoobCraft = new CreativeTabs("tabNoobCraft") {
		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem()
		{
			return RegisterItems.flintKnife;
		}
	};
	
	public static final AchievementPage pageNoobCraft = new AchievementPage("Noobcraft",
		RegisterAchievements.getAchievements());
	
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
		
		RegisterItems.register();
		RegisterBlocks.register();
		RegisterTileEntities.register();
		
		
		RemoveRecipes.remove();
		RegisterRecipes.addShapelessRecipes();
		RegisterRecipes.addShapedRecipes();
		RegisterRecipes.addAdvancedRecipes();
		RegisterRecipes.addFurnaceRecipes();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e)
	{
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
