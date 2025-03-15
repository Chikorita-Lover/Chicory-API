package net.chikorita_lover.chicory.network;

import net.chikorita_lover.chicory.ChicoryApi;
import net.chikorita_lover.chicory.api.config.ConfigValues;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public record SyncConfigS2CPacket(ConfigValues values) implements CustomPayload {
    public static final Id<SyncConfigS2CPacket> PACKET_ID = new Id<>(ChicoryApi.of("sync_config"));
    public static final PacketCodec<RegistryByteBuf, SyncConfigS2CPacket> PACKET_CODEC = PacketCodec.of(SyncConfigS2CPacket::write, SyncConfigS2CPacket::new);

    public SyncConfigS2CPacket(RegistryByteBuf buf) {
        this(ConfigValues.PACKET_CODEC.decode(buf));
    }

    public void write(RegistryByteBuf buf) {
        ConfigValues.PACKET_CODEC.encode(buf, this.values);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
