package net.chikorita_lover.chicory;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChicoryApi implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("Chicory API");
    public static final String NAMESPACE = "chicory";

    public static Identifier of(String path) {
        return Identifier.of(NAMESPACE, path);
    }

    @Override
    public void onInitialize() {
    }
}