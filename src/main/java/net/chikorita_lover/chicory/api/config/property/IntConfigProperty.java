package net.chikorita_lover.chicory.api.config.property;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public class IntConfigProperty extends NumberConfigProperty<Integer> {
    public IntConfigProperty(String name, int defaultValue) {
        this(name, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public IntConfigProperty(String name, int defaultValue, int min, int max) {
        super(name, defaultValue, min, max);
    }

    @Override
    public Integer parse(String value) {
        return Integer.parseInt(value);
    }

    @Override
    public Codec<Integer> codec() {
        return Codec.INT;
    }

    @Override
    public PacketCodec<ByteBuf, Integer> packetCodec() {
        return PacketCodecs.INTEGER;
    }
}
