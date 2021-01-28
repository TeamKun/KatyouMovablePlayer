package net.kunmc.lab.kmp.packet;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.Vec3d;

import java.util.UUID;

public class MusicRingUpdateMessage {
    public UUID uuid;
    public Vec3d musicPos;
    public float musicVolume;

    public MusicRingUpdateMessage(UUID uuid, Vec3d musicPos, float musicVolume) {
        this.uuid = uuid;
        this.musicPos = musicPos;
        this.musicVolume = musicVolume;
    }

    public static MusicRingUpdateMessage decodeMessege(PacketBuffer buffer) {

        return new MusicRingUpdateMessage(UUID.fromString(buffer.readString(32767)), new Vec3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble()), buffer.readFloat());
    }

    public static void encodeMessege(MusicRingUpdateMessage messegeIn, PacketBuffer buffer) {
        buffer.writeString(messegeIn.uuid.toString());
        buffer.writeDouble(messegeIn.musicPos.getX());
        buffer.writeDouble(messegeIn.musicPos.getY());
        buffer.writeDouble(messegeIn.musicPos.getZ());
        buffer.writeFloat(messegeIn.musicVolume);
    }
}