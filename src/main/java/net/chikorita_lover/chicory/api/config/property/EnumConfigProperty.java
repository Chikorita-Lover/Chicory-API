package net.chikorita_lover.chicory.api.config.property;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.encoding.StringEncoding;
import net.minecraft.text.Text;

import java.util.function.Function;

public class EnumConfigProperty<E extends Enum<E>> extends ConfigProperty<E> {
    private final Class<E> type;
    private final Function<E, String> translationKeyFunction;
    private final Codec<E> codec;
    private final PacketCodec<ByteBuf, E> packetCodec;

    public EnumConfigProperty(String name, Class<E> type, Function<E, String> translationKeyFunction) {
        this(name, type.getEnumConstants()[0], type, translationKeyFunction);
    }

    public EnumConfigProperty(String name, E defaultValue, final Class<E> type, Function<E, String> translationKeyFunction) {
        super(name, defaultValue);
        this.type = type;
        this.translationKeyFunction = translationKeyFunction;
        this.codec = new PrimitiveCodec<>() {
            @Override
            public <T> DataResult<E> read(DynamicOps<T> ops, T input) {
                return ops.getStringValue(input).map(name -> Enum.valueOf(type, name));
            }

            @Override
            public <T> T write(DynamicOps<T> ops, E value) {
                return ops.createString(value.name().toLowerCase());
            }
        };
        this.packetCodec = new PacketCodec<>() {
            @Override
            public void encode(ByteBuf buf, E value) {
                StringEncoding.encode(buf, value.name(), Short.MAX_VALUE);
            }

            @Override
            public E decode(ByteBuf buf) {
                return Enum.valueOf(type, StringEncoding.decode(buf, Short.MAX_VALUE));
            }
        };
    }

    public Text getText(E value) {
        return Text.translatable(this.translationKeyFunction.apply(value));
    }

    public Class<E> getType() {
        return this.type;
    }

    @Override
    public Codec<E> codec() {
        return this.codec;
    }

    @Override
    public PacketCodec<ByteBuf, E> packetCodec() {
        return this.packetCodec;
    }
}
