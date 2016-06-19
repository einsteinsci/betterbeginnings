package net.einsteinsci.betterbeginnings.commands;

import net.einsteinsci.betterbeginnings.config.BBConfigFolderLoader;
import net.einsteinsci.betterbeginnings.config.json.BrickOvenConfig;
import net.einsteinsci.betterbeginnings.config.json.SmelterConfig;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;

import java.util.ArrayList;
import java.util.List;

public class JsonGenerateCommand extends CommandBase
{
	public static final String SMELTER = "smelter";
	public static final String BRICKOVEN = "brickoven";
	public static final String KILN = "kiln";

	@Override
	public String getCommandName()
	{
		return "jsongen";
	}

	@Override
	public String getCommandUsage(ICommandSender sender)
	{
		// Do not translate this, as "smelter", "brickoven", and "kiln" are hardcoded.
		return "jsongen <smelter | brickoven | kiln>";
	}

	public int getRequiredPermissionLevel()
	{
		return 2;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException
	{
		if (args.length == 0 || args.length > 1)
		{
			throw new WrongUsageException(getCommandUsage(sender));
		}

		String code = args[0];
		if (code.equalsIgnoreCase(SMELTER))
		{
			SmelterConfig.INSTANCE.generateAutoConfig();
			BBConfigFolderLoader.saveAutoJson(SmelterConfig.INSTANCE);
		}
		else if (code.equalsIgnoreCase(BRICKOVEN))
		{
			BrickOvenConfig.INSTANCE.generateAutoConfig();
			BBConfigFolderLoader.saveAutoJson(BrickOvenConfig.INSTANCE);
		}
		else if (code.equalsIgnoreCase(KILN))
		{
			SmelterConfig.INSTANCE.generateAffectedInputs();
			BrickOvenConfig.INSTANCE.generateAffectedOutputs();
		}
		else
		{
			throw new WrongUsageException(getCommandUsage(sender));
		}

		sender.addChatMessage(new ChatComponentTranslation("command.jsongen.complete"));
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
	{
		List<String> res = new ArrayList<>();
		if (args == null || args.length == 0)
		{
			res.add(SMELTER);
			res.add(BRICKOVEN);
			res.add(KILN);
		}

		return res;
	}
}
