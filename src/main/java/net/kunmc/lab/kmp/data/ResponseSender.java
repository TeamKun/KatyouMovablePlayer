package net.kunmc.lab.kmp.data;

import net.kunmc.lab.kmp.packet.ClientToResponseMessage;
import net.kunmc.lab.kmp.packet.PacketHandler;
import net.kunmc.lab.kmp.packet.ServerToResponseMessage;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.UUID;

public class ResponseSender {
    //クライアントからサーバーへ応答を送る
    public static void sendToServer(ResourceLocation location, int id, String message, CompoundNBT data) {
        PacketHandler.INSTANCE.sendToServer(new ClientToResponseMessage(location, id, message, data));
    }

    //サーバーからクライアントへ応答を送る
    public static void sendToClient(PacketDistributor.PacketTarget target, ResourceLocation location, int id, String message, CompoundNBT data) {
        PacketHandler.INSTANCE.send(target, new ServerToResponseMessage(location, id, message, data));
    }

    //サーバーからクライアントへ応答を送る(プレイヤー)
    public static void sendToClient(ServerPlayerEntity player, ResourceLocation location, int id, String message, CompoundNBT data) {
        sendToClient(PacketDistributor.PLAYER.with(() -> player), location, id, message, data);
    }

    //サーバーからクライアントへ応答を送る(プレイヤーのUUID)
    public static void sendToClient(String playerUUID, MinecraftServer ms, ResourceLocation location, int id, String message, CompoundNBT data) {
        sendToClient(ms.getPlayerList().getPlayerByUUID(UUID.fromString(playerUUID)), location, id, message, data);
    }
}