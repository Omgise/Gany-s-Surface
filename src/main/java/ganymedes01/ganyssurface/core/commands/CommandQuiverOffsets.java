package ganymedes01.ganyssurface.core.commands;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ganymedes01.ganyssurface.client.renderer.entity.QuiverRenderConfig;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

/**
 * Gany's Surface
 *
 * Client command that nudges where the quiver sits on the player's back and remembers it, so the
 * sprite can be lined up against a player model without rebuilding the mod.
 *
 * @author ganymedes01
 *
 */

@SideOnly(Side.CLIENT)
public class CommandQuiverOffsets extends CommandBase {

	@Override
	public String getCommandName() {
		return "quiveroffsets";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/quiveroffsets [scale x y z | reset]";
	}

	@Override
	@SuppressWarnings("rawtypes")
	public List getCommandAliases() {
		return java.util.Arrays.asList("qoffsets");
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (args.length == 0) {
			sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GRAY + "Quiver back sprite: " + EnumChatFormatting.WHITE + QuiverRenderConfig.describe()));
			sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GRAY + "Usage: " + EnumChatFormatting.WHITE + getCommandUsage(sender)));
			return;
		}

		if ("reset".equalsIgnoreCase(args[0])) {
			QuiverRenderConfig.reset();
			QuiverRenderConfig.save();
			sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Quiver back sprite reset: " + QuiverRenderConfig.describe()));
			return;
		}

		if (args.length != 4) {
			sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Usage: " + getCommandUsage(sender)));
			return;
		}

		try {
			QuiverRenderConfig.scale = Float.parseFloat(args[0]);
			QuiverRenderConfig.x = Float.parseFloat(args[1]);
			QuiverRenderConfig.y = Float.parseFloat(args[2]);
			QuiverRenderConfig.z = Float.parseFloat(args[3]);
		} catch (NumberFormatException e) {
			sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Numbers only: " + getCommandUsage(sender)));
			return;
		}

		QuiverRenderConfig.save();
		sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Quiver back sprite: " + QuiverRenderConfig.describe()));
	}
}
