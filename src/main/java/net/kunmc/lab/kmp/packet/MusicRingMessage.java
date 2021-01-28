package net.kunmc.lab.kmp.packet;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.Vec3d;

import java.util.UUID;

public class MusicRingMessage {
    public UUID uuid;
    public Vec3d musicPos;
    public String musicURL;
    public long startPos;

    public MusicRingMessage(UUID uuid, Vec3d musicPos, String musicURL, long startPos) {
        this.uuid = uuid;
        this.musicPos = musicPos;
        this.musicURL = musicURL;
        this.startPos = startPos;
    }

    public static MusicRingMessage decodeMessege(PacketBuffer buffer) {

        return new MusicRingMessage(UUID.fromString(buffer.readString(32767)), new Vec3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble()), buffer.readString(32767), buffer.readLong());
    }

    public static void encodeMessege(MusicRingMessage messegeIn, PacketBuffer buffer) {
        buffer.writeString(messegeIn.uuid.toString());
        buffer.writeDouble(messegeIn.musicPos.getX());
        buffer.writeDouble(messegeIn.musicPos.getY());
        buffer.writeDouble(messegeIn.musicPos.getZ());
        buffer.writeString(messegeIn.musicURL);
        buffer.writeLong(messegeIn.startPos);
    }
}