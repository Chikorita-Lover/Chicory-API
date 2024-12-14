package net.chikorita_lover.chicory.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public final class SplashTextRegistry {
    public static final List<String> SPLASH_TEXTS = new ArrayList<>();
    public static final List<Identifier> RESOURCE_IDS = new ArrayList<>();
    private static final String DEFAULT_PATH = "texts/splashes.txt";

    /**
     * Adds an individual string to potentially display at random as a splash text.
     *
     * @param text the splash text to potentially display
     */
    public static void add(String text) {
        SPLASH_TEXTS.add(text);
    }

    /**
     * Loads splash texts to display on the title screen from a resource file.
     *
     * @param id the resource location containing the text file
     */
    public static void addFile(Identifier id) {
        RESOURCE_IDS.add(id);
    }

    /**
     * Loads splash texts to display on the title screen from the default location in a resource pack.
     *
     * @param namespace the namespace of the resource pack containing the text file
     */
    public static void addPack(String namespace) {
        addFile(Identifier.of(namespace, DEFAULT_PATH));
    }
}
