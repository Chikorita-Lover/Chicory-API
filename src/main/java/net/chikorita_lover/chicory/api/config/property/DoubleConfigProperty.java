package net.chikorita_lover.chicory.api.config.property;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public class DoubleConfigProperty extends NumberConfigProperty<Double> {
    public DoubleConfigProperty(String name, double defaultValue) {
        this(name, defaultValue, -Double.MAX_VALUE, Double.MAX_VALUE);
    }

    public DoubleConfigProperty(String name, double defaultValue, double min, double max) {
        super(name, defaultValue, min, max);
    }

    @Override
    public Double parse(String value) {
        return Double.parseDouble(value);
    }

    @Override
    public Codec<Double> codec() {
        return Codec.DOUBLE;
    }

    @Override
    public PacketCodec<ByteBuf, Double> packetCodec() {
        return PacketCodecs.DOUBLE;
    }
}
