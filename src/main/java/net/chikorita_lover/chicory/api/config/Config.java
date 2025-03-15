package net.chikorita_lover.chicory.api.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.chikorita_lover.chicory.ChicoryApi;
import net.chikorita_lover.chicory.api.config.property.ConfigProperty;
import net.chikorita_lover.chicory.network.SyncConfigS2CPacket;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.ApiStatus;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Supplier;

public class Config {
    private static final Map<String, Config> CONFIGS = new HashMap<>();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static boolean frozen;
    private final String name;
    private final Map<String, ConfigProperty<?>> properties = new LinkedHashMap<>();
    private final Map<ConfigProperty<?>, ConfigCategory> propertyToCategory = new HashMap<>();
    private final Map<ConfigCategory, List<ConfigProperty<?>>> propertiesByCategory = new LinkedHashMap<>();
    private final Path filePath;
    private ConfigValues values;

    public Config(String name) {
        assertNotFrozen();
        this.name = name;
        this.filePath = FabricLoader.getInstance().getConfigDir().resolve(name + ".json");
        this.values = new ConfigValues(this);
        CONFIGS.put(name, this);
    }

    @ApiStatus.Internal
    public static Config getConfig(String name) {
        return CONFIGS.get(name);
    }

    @ApiStatus.Internal
    public static void reloadConfigs() {
        CONFIGS.values().forEach(Config::loadValues);
    }

    @ApiStatus.Internal
    public static void freezeConfigs() {
        if (frozen) {
            return;
        }
        frozen = true;
        ServerLifecycleEvents.SERVER_STARTING.register(server -> Config.reloadConfigs());
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> CONFIGS.values().forEach(config -> {
            final ConfigValues commonValues = new ConfigValues(config);
            config.properties.values().stream().filter(property -> !config.isClient(property)).forEach(property -> commonValues.put(config.values.get(property)));
            ServerPlayNetworking.send(handler.player, new SyncConfigS2CPacket(commonValues));
        }));
        ChicoryApi.LOGGER.info("Initialized {} configs", CONFIGS.size());
    }

    @ApiStatus.Internal
    private static void assertNotFrozen() {
        if (frozen) {
            throw new IllegalStateException("Configs are already frozen");
        }
    }

    /**
     * Registers and assigns {@code property} for use with this config.
     * Registered config properties will be written to and read from file.
     *
     * @param property the property to register
     * @param category the category to assign the property to
     * @return a supplier that returns the property's assigned value
     */
    public <T> Supplier<T> register(final ConfigProperty<T> property, ConfigCategory category) {
        assertNotFrozen();
        this.properties.put(property.getName(), property);
        this.propertyToCategory.put(property, category);
        this.propertiesByCategory.computeIfAbsent(category, ignored -> new ArrayList<>()).add(property);
        this.values.put(property.createValue());
        return () -> this.get(property);
    }

    /**
     * {@return the current value of the config property}
     *
     * @param property the config property to retrieve
     */
    public <T> T get(ConfigProperty<T> property) {
        return this.values.get(property).value();
    }

    public ConfigProperty getProperty(String name) {
        return this.properties.get(name);
    }

    public <T> void set(ConfigProperty<T> property, T value) {
        this.values.put(property.createValue(value));
    }

    @ApiStatus.Internal
    public String getName() {
        return this.name;
    }

    @ApiStatus.Internal
    public ConfigValues getValues() {
        return this.values;
    }

    @ApiStatus.Internal
    public void setValues(ConfigValues values) {
        this.values = values;
    }

    @ApiStatus.Internal
    public ConfigCategory getCategory(ConfigProperty<?> property) {
        return this.propertyToCategory.get(property);
    }

    @ApiStatus.Internal
    public Map<ConfigCategory, List<ConfigProperty<?>>> getPropertiesByCategory() {
        return this.propertiesByCategory;
    }

    @ApiStatus.Internal
    public boolean contains(ConfigProperty<?> property) {
        return this.properties.containsValue(property);
    }

    @ApiStatus.Internal
    public boolean isClient(ConfigProperty<?> property) {
        return this.propertyToCategory.get(property).isClient();
    }

    @ApiStatus.Internal
    public void loadValues() {
        if (Files.exists(this.filePath)) {
            try (Reader reader = Files.newBufferedReader(this.filePath)) {
                JsonObject json = GSON.fromJson(reader, JsonObject.class);
                for (String key : json.keySet()) {
                    if (this.properties.containsKey(key)) {
                        this.values.put(this.properties.get(key).deserialize(json));
                    }
                }
            } catch (Exception e) {
                ChicoryApi.LOGGER.error("Could not read config values for mod {}: {}", this.name, e.getLocalizedMessage());
            }
        } else {
            this.saveValues();
        }
    }

    @SuppressWarnings("unchecked")
    @ApiStatus.Internal
    public void saveValues() {
        try (Writer writer = Files.newBufferedWriter(this.filePath)) {
            final JsonObject json = new JsonObject();
            this.values.forEach(value -> json.add(value.property().getName(), value.property().serialize(value)));
            GSON.toJson(json, writer);
        } catch (Exception e) {
            ChicoryApi.LOGGER.error("Could not write config values for mod {}: {}", this.name, e.getLocalizedMessage());
        }
    }
}
