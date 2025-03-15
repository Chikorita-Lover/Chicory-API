package net.chikorita_lover.chicory.api.config;

public record ConfigCategory(String name, boolean isClient) {
    /**
     * Creates a new {@link ConfigCategory} that is synced from server to client.
     * Common categories often include properties that modify a mod's server-side functions, such as a block's behavior.
     *
     * @param name the name of the category
     * @return the new category
     */
    public static ConfigCategory common(String name) {
        return new ConfigCategory(name, false);
    }

    /**
     * Creates a new {@link ConfigCategory} that is unmodified by the server.
     * Client categories often include properties that modify a mod's client-side functions, such as the appearance of interfaces.
     *
     * @param name the name of the category
     * @return the new category
     */
    public static ConfigCategory client(String name) {
        return new ConfigCategory(name, true);
    }
}
