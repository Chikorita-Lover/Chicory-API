package net.chikorita_lover.chicory.api.config.property;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import io.netty.buffer.ByteBuf;
import net.chikorita_lover.chicory.ChicoryApi;
import net.minecraft.network.codec.PacketCodec;
import org.jetbrains.annotations.ApiStatus;

public abstract class ConfigProperty<T> {
    protected final T defaultValue;
    private final String name;

    public ConfigProperty(String name, T defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public Value<T> createValue() {
        return this.createValue(this.defaultValue);
    }

    public Value<T> createValue(T value) {
        if (!this.isValid(value)) {
            ChicoryApi.LOGGER.error("Value {} is not valid for config property {}", value, this.getName());
            value = this.defaultValue;
        }
        return new Value<>(this, value);
    }

    public boolean isValid(T value) {
        return true;
    }

    public abstract Codec<T> codec();

    public abstract PacketCodec<ByteBuf, T> packetCodec();

    public String getName() {
        return this.name;
    }

    public T getDefaultValue() {
        return this.defaultValue;
    }

    public Value<T> deserialize(JsonObject json) {
        return this.createValue(this.codec().decode(JsonOps.INSTANCE, json.get(this.name)).getOrThrow().getFirst());
    }

    public JsonElement serialize(Value<T> value) {
        return this.codec().encodeStart(JsonOps.INSTANCE, value.value()).getOrThrow();
    }

    @ApiStatus.Internal
    public record Value<T>(ConfigProperty<T> property, T value) {
    }
}
