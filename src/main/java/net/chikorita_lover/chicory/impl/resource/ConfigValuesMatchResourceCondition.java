package net.chikorita_lover.chicory.impl.resource;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import net.chikorita_lover.chicory.ChicoryApi;
import net.chikorita_lover.chicory.api.config.Config;
import net.chikorita_lover.chicory.api.config.property.ConfigProperty;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

public record ConfigValuesMatchResourceCondition(Config config,
                                                 List<ConfigProperty.Value> values) implements ResourceCondition {
    public static final MapCodec<ConfigValuesMatchResourceCondition> CODEC = new MapCodec<>() {
        @Override
        public <T> Stream<T> keys(DynamicOps<T> ops) {
            return Stream.of("name", "values").map(ops::createString);
        }

        @Override
        public <T> DataResult<ConfigValuesMatchResourceCondition> decode(DynamicOps<T> ops, MapLike<T> input) {
            final Config config = Config.getConfig(ops.getStringValue(input.get("name")).getOrThrow());
            List<ConfigProperty.Value> values = ops.getMap(input.get("values")).getOrThrow().entries().map(pair -> {
                ConfigProperty property = ops.getStringValue(pair.getFirst()).map(config::getProperty).getOrThrow();
                return property.createValue(((Pair<T, T>) property.codec().decode(ops, pair.getSecond()).getOrThrow()).getFirst());
            }).toList();
            return DataResult.success(new ConfigValuesMatchResourceCondition(config, values));
        }

        @Override
        public <T> RecordBuilder<T> encode(ConfigValuesMatchResourceCondition input, final DynamicOps<T> ops, RecordBuilder<T> prefix) {
            prefix.add("name", ops.createString(input.config.getName()));
            prefix.add("values", ops.createMap(input.values.stream().map(value -> {
                ConfigProperty property = value.property();
                return Pair.of(ops.createString(property.getName()), (T) property.codec().encodeStart(ops, value.value()).getOrThrow());
            })));
            return prefix;
        }
    };
    public static final ResourceConditionType<ConfigValuesMatchResourceCondition> TYPE = ResourceConditionType.create(ChicoryApi.of("config_values_match"), CODEC);

    @Override
    public ResourceConditionType<?> getType() {
        return TYPE;
    }

    @Override
    public boolean test(@Nullable RegistryWrapper.WrapperLookup registries) {
        return this.values.stream().allMatch(value -> this.config.get(value.property()) == value.value());
    }
}
