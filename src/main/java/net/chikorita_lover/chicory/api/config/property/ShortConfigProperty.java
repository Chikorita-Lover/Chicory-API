package net.chikorita_lover.chicory.api.config.property;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public class ShortConfigProperty extends NumberConfigProperty<Short> {
    public ShortConfigProperty(String name, short defaultValue) {
        this(name, defaultValue, Short.MIN_VALUE, Short.MAX_VALUE);
    }

    public ShortConfigProperty(String name, short defaultValue, short min, short max) {
        super(name, defaultValue, min, max);
    }

    @Override
    protected Short parse(String value) {
        return Short.parseShort(value);
    }

    @Override
    public Codec<Short> codec() {
        return Codec.SHORT;
    }

    @Override
    public PacketCodec<ByteBuf, Short> packetCodec() {
        return PacketCodecs.SHORT;
    }
}
