package net.kunmc.lab.kmp.event.common;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;

public class ResponseEvent extends Event {
    private final ResourceLocation location;
    private final int id;
    private final String message;
    private final CompoundNBT data;

    public ResponseEvent(ResourceLocation location, int id, String message, CompoundNBT data) {
        this.location = location;
        this.id = id;
        this.message = message;
        this.data = data;
    }

    public CompoundNBT getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }

    public ResourceLocation getLocation() {
        return location;
    }

    //クライアントからの応答
    public static class Client extends ResponseEvent {
        private final ServerPlayerEntity player;

        public Client(ServerPlayerEntity player, ResourceLocation location, int id, String message, CompoundNBT data) {
            super(location, id, message, data);
            this.player = player;
        }

        public ServerPlayerEntity getPlayer() {
            return player;
        }
    }

    //サーバーからの応答
    public static class Server extends ResponseEvent {
        public Server(ResourceLocation location, int id, String message, CompoundNBT data) {
            super(location, id, message, data);
        }
    }
}