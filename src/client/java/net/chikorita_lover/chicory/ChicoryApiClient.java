package net.chikorita_lover.chicory;

import net.chikorita_lover.chicory.api.config.Config;
import net.chikorita_lover.chicory.api.config.ConfigEvents;
import net.chikorita_lover.chicory.network.SyncConfigS2CPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ChicoryApiClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> Config.reloadConfigs());
        ClientPlayNetworking.registerGlobalReceiver(SyncConfigS2CPacket.PACKET_ID, (payload, context) -> {
            Config config = payload.values().getConfig();
            config.getValues().putAllIfPresent(payload.values());
            ConfigEvents.CONFIG_LOAD.invoker().onLoad(config);
        });
    }
}