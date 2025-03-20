package net.chikorita_lover.chicory.api.config.property;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public class FloatConfigProperty extends NumberConfigProperty<Float> {
    public FloatConfigProperty(String name, float defaultValue) {
        this(name, defaultValue, -Float.MAX_VALUE, Float.MAX_VALUE);
    }

    public FloatConfigProperty(String name, float defaultValue, float min, float max) {
        super(name, defaultValue, min, max);
    }

    @Override
    public Float parse(String value) {
        return Float.parseFloat(value);
    }

    @Override
    public Codec<Float> codec() {
        return Codec.FLOAT;
    }

    @Override
    public PacketCodec<ByteBuf, Float> packetCodec() {
        return PacketCodecs.FLOAT;
    }
}
