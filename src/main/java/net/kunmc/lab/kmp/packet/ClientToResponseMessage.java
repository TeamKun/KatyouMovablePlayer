package net.kunmc.lab.kmp.packet;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

public class ClientToResponseMessage {
    public ResourceLocation location;
    public int id;
    public String message;
    public CompoundNBT data;

    public ClientToResponseMessage(ResourceLocation location, int id, String message, CompoundNBT data) {
        this.location = location;
        this.id = id;
        this.message = message;
        this.data = data;
    }

    public static ClientToResponseMessage decodeMessege(PacketBuffer buffer) {
        return new ClientToResponseMessage(new ResourceLocation(buffer.readString(32767)), buffer.readInt(), buffer.readString(32767), buffer.readCompoundTag());
    }

    public static void encodeMessege(ClientToResponseMessage messegeIn, PacketBuffer buffer) {
        buffer.writeString(messegeIn.location.toString());
        buffer.writeInt(messegeIn.id);
        buffer.writeString(messegeIn.message);
        buffer.writeCompoundTag(messegeIn.data);
    }
}