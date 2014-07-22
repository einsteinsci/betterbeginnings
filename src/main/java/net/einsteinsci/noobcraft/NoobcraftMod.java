package net.einsteinsci.noobcraft;

import net.einsteinsci.noobcraft.config.NoobcraftConfig;
import net.einsteinsci.noobcraft.event.NoobcraftEventHandler;
import net.einsteinsci.noobcraft.register.RegisterBlocks;
import net.einsteinsci.noobcraft.register.RegisterItems;
import net.einsteinsci.noobcraft.register.RegisterRecipes;
import net.einsteinsci.noobcraft.register.RegisterTileEntities;
import net.einsteinsci.noobcraft.register.RemoveRecipes;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = NoobcraftMod.MODID,
version = NoobcraftMod.VERSION,
guiFactory = "net.einsteinsci.noobcraft.config.NoobcraftConfigGuiFactory")
public class NoobcraftMod
{
	public static final String MODID = "noobcraft";
	public static final String VERSION = "0.0.1.2";
	
	public static Configuration configFile;
	
	public NoobcraftEventHandler eventHandler = new NoobcraftEventHandler();
	//public NoobcraftConfig config = new NoobcraftConfig();
	
	@Instance(NoobcraftMod.MODID)
	public static NoobcraftMod modInstance;
	
	@SidedProxy(clientSide = "net.einsteinsci.noobcraft.ClientProxy", serverSide = "net.einsteinsci.noobcraft.ServerProxy")
	public static ServerProxy proxy;
	
	public static final CreativeTabs tabNoobCraft = new CreativeTabs("tabNoobCraft")
	{
		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem()
		{
			return RegisterItems.flintKnife;
		}
	};
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{
		System.out.println("[NoobCraft] Ready to be a noob?");
		
		configFile = new Configuration(e.getSuggestedConfigurationFile());
		configFile.load();
		NoobcraftConfig.syncConfig(configFile);
		
		proxy.registerNetworkStuff();
		proxy.registerRenderThings();
		
		FMLCommonHandler.instance().bus().register(eventHandler);
		MinecraftForge.EVENT_BUS.register(eventHandler);
		
		RegisterItems.register();
		RegisterBlocks.register();
		RegisterTileEntities.register();
		
		RemoveRecipes.remove();
		RegisterRecipes.addShapelessRecipes();
		RegisterRecipes.addShapedRecipes();
		RegisterRecipes.addFurnaceRecipes();
		//GameRegistry.register
	}
	
	public void init(FMLInitializationEvent e)
	{
		
	}
}
