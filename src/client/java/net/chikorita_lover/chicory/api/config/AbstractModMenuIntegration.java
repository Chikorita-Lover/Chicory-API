package net.chikorita_lover.chicory.api.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.chikorita_lover.chicory.client.gui.ConfigScreen;

/**
 * A class that can be extended to provide integration with Mod Menu without having to implement Mod Menu through Gradle.
 */
public abstract class AbstractModMenuIntegration implements ModMenuApi {
    /**
     * {@return the config for this mod}
     */
    public abstract Config getConfig();

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new ConfigScreen(parent, this.getConfig());
    }
}
