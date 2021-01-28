package net.kunmc.lab.kmp.handler;

import net.kunmc.lab.kmp.event.common.ResponseEvent;
import net.kunmc.lab.kmp.packet.ClientToResponseMessage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientToResponseMessageHandler {
    public static void reversiveMessage(ClientToResponseMessage message, Supplier<NetworkEvent.Context> ctx) {
        MinecraftForge.EVENT_BUS.post(new ResponseEvent.Client(ctx.get().getSender(), message.location, message.id, message.message, message.data));
    }
}
