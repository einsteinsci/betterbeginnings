package net.einsteinsci.betterbeginnings.register;

import net.einsteinsci.betterbeginnings.ModMain;
import net.einsteinsci.betterbeginnings.blocks.*;
import net.minecraft.block.Block;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Level;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class RegisterBlocks
{
	public static final BlockKiln kiln = new BlockKiln(false);
	public static final BlockKiln kilnLit = new BlockKiln(true);
	public static final BlockDoubleWorkbench doubleWorkbench = new BlockDoubleWorkbench();
	public static final BlockBrickOven brickOven = new BlockBrickOven(false);
	public static final BlockBrickOven brickOvenLit = new BlockBrickOven(true);
	public static final BlockSmelter smelter = new BlockSmelter(false);
	public static final BlockSmelter smelterLit = new BlockSmelter(true);
	public static final BlockInfusionRepairStation infusionRepairStation = new BlockInfusionRepairStation();
	public static final BlockCampfire campfire = new BlockCampfire(false);
	public static final BlockCampfire campfireLit = new BlockCampfire(true);
	public static final BlockObsidianKiln obsidianKiln = new BlockObsidianKiln(false);
	public static final BlockObsidianKiln obsidianKilnLit = new BlockObsidianKiln(true);
	public static final BlockNetherBrickOven netherBrickOven = new BlockNetherBrickOven(false);
	public static final BlockNetherBrickOven netherBrickOvenLit = new BlockNetherBrickOven(true);
	public static final BlockEnderSmelter enderSmelter = new BlockEnderSmelter(false);
	public static final BlockEnderSmelter enderSmelterLit = new BlockEnderSmelter(true);

	public static final List<Block> allBlocks = new ArrayList<>();

	public static void register()
	{
		RegisterHelper.registerBlock(kiln);
		RegisterHelper.registerBlock(kilnLit);

		RegisterHelper.registerBlock(doubleWorkbench);

		RegisterHelper.registerBlock(brickOven);
		RegisterHelper.registerBlock(brickOvenLit);

		RegisterHelper.registerBlock(smelter);
		RegisterHelper.registerBlock(smelterLit);

		RegisterHelper.registerBlock(infusionRepairStation);

		RegisterHelper.registerBlock(campfire);
		RegisterHelper.registerBlock(campfireLit);

		RegisterHelper.registerBlock(obsidianKiln);
		RegisterHelper.registerBlock(obsidianKilnLit);

		RegisterHelper.registerBlock(netherBrickOven);
		RegisterHelper.registerBlock(netherBrickOvenLit);

		RegisterHelper.registerBlock(enderSmelter);
		RegisterHelper.registerBlock(enderSmelterLit);

		oreDictionary();
		loadAllBlocks();
	}

	public static void oreDictionary()
	{
		OreDictionary.registerOre("craftingTableWood", doubleWorkbench);
	}

	private static void loadAllBlocks()
	{
		List<Field> fields = new ArrayList<>();
		Field[] allFields = RegisterBlocks.class.getFields();
		for (Field f : allFields)
		{
			if (Block.class.isAssignableFrom(f.getType()) &&
					IBBName.class.isAssignableFrom(f.getType()))
			{
				fields.add(f);
			}
		}

		for (Field f : fields)
		{
			try
			{
				allBlocks.add((Block)f.get(null));
			}
			catch (Exception e)
			{
				ModMain.Log(Level.ERROR, "Field " + f.getName() + " does not inherit from Block.");
			}
		}
	}
}
