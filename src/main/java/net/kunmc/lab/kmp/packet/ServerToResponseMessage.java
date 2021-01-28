package net.kunmc.lab.kmp.packet;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

public class ServerToResponseMessage {
    public ResourceLocation location;
    public int id;
    public String message;
    public CompoundNBT data;

    public ServerToResponseMessage(ResourceLocation location, int id, String message, CompoundNBT data) {
        this.location = location;
        this.id = id;
        this.message = message;
        this.data = data;
    }

    public static ServerToResponseMessage decodeMessege(PacketBuffer buffer) {
        return new ServerToResponseMessage(new ResourceLocation(buffer.readString(32767)), buffer.readInt(), buffer.readString(32767), buffer.readCompoundTag());
    }

    public static void encodeMessege(ServerToResponseMessage messegeIn, PacketBuffer buffer) {
        buffer.writeString(messegeIn.location.toString());
        buffer.writeInt(messegeIn.id);
        buffer.writeString(messegeIn.message);
        buffer.writeCompoundTag(messegeIn.data);
    }
}
