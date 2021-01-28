package net.kunmc.lab.kmp.client.handler;

import net.kunmc.lab.kmp.event.common.ResponseEvent;
import net.kunmc.lab.kmp.packet.ServerToResponseMessage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerToResponseMessageHandler {
    public static void reversiveMessage(ServerToResponseMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().setPacketHandled(true);
        MinecraftForge.EVENT_BUS.post(new ResponseEvent.Server(message.location, message.id, message.message, message.data));
    }
}
