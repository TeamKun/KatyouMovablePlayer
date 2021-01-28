package net.kunmc.lab.kmp.packet;

import net.kunmc.lab.kmp.KatyouMovablePlayer;
import net.kunmc.lab.kmp.client.handler.MusicRingMessageHandler;
import net.kunmc.lab.kmp.client.handler.MusicRingUpdateMessageHandler;
import net.kunmc.lab.kmp.client.handler.ServerToResponseMessageHandler;
import net.kunmc.lab.kmp.handler.ClientToResponseMessageHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {
    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(KatyouMovablePlayer.MODID, KatyouMovablePlayer.MODID + "_channel")).clientAcceptedVersions(a -> true).serverAcceptedVersions(a -> true).networkProtocolVersion(() -> PROTOCOL_VERSION).simpleChannel();
    private static int integer = -1;

    private static int next() {
        integer++;
        return integer;
    }

    public static void init() {
        INSTANCE.registerMessage(next(), ClientToResponseMessage.class, ClientToResponseMessage::encodeMessege, ClientToResponseMessage::decodeMessege, ClientToResponseMessageHandler::reversiveMessage);
        INSTANCE.registerMessage(next(), ServerToResponseMessage.class, ServerToResponseMessage::encodeMessege, ServerToResponseMessage::decodeMessege, ServerToResponseMessageHandler::reversiveMessage);
        INSTANCE.registerMessage(next(), MusicRingMessage.class, MusicRingMessage::encodeMessege, MusicRingMessage::decodeMessege, MusicRingMessageHandler::reversiveMessage);
        INSTANCE.registerMessage(next(), MusicRingUpdateMessage.class, MusicRingUpdateMessage::encodeMessege, MusicRingUpdateMessage::decodeMessege, MusicRingUpdateMessageHandler::reversiveMessage);
    }
}
