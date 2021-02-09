package net.kunmc.lab.kmp.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;

public class KMPCommands {
    public static void registerCommand(CommandDispatcher<CommandSource> d) {
        MusicCommand.register(d);
    }
}
