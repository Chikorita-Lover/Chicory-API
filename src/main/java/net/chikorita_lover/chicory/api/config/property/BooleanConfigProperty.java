package net.chikorita_lover.chicory.api.config.property;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public class BooleanConfigProperty extends ConfigProperty<Boolean> {
    public BooleanConfigProperty(String name, boolean defaultValue) {
        super(name, defaultValue);
    }

    @Override
    public Codec<Boolean> codec() {
        return Codec.BOOL;
    }

    @Override
    public PacketCodec<ByteBuf, Boolean> packetCodec() {
        return PacketCodecs.BOOL;
    }
}
