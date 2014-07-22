package net.einsteinsci.noobcraft.config;

import net.minecraftforge.common.config.Configuration;

public class NoobcraftConfig
{
	public static boolean greetUser;
	
	public NoobcraftConfig()
	{
		greetUser = false;
	}
	
	public static void syncConfig(Configuration config)
	{
		greetUser = config.getBoolean("greetUser", Configuration.CATEGORY_GENERAL, false, "Greet user upon login");
		
		if (config.hasChanged())
		{
			config.save();
		}
	}
}
