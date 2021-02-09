package net.kunmc.lab.kmp.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import net.kunmc.lab.kmp.config.ServerConfig;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

public class MusicCommand {
    public static void register(CommandDispatcher<CommandSource> d) {
        d.register(Commands.literal("kmp").requires((source) -> source.hasPermissionLevel(2)).then(Commands.literal("range").executes(src -> showRange(src.getSource())).then(Commands.argument("range", DoubleArgumentType.doubleArg(0d)).executes(src -> setRange(src.getSource(), DoubleArgumentType.getDouble(src, "range"))))));

    }

    public static int setRange(CommandSource source, double range) {
        ServerConfig.MusicRange.set(range);
        source.sendFeedback(new TranslationTextComponent("commands.kmp.setrange", range), true);
        return 1;
    }

    public static int showRange(CommandSource source) {
        source.sendFeedback(new TranslationTextComponent("commands.kmp.showrange", ServerConfig.MusicRange.get()), true);
        return 1;
    }

}
