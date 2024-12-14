package net.chikorita_lover.chicory.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import net.minecraft.client.session.Session;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Environment(EnvType.CLIENT)
public class SplashTextRenderEvents {
    /**
     * This event can be used to set the splash text to render after the splash text is chosen by the vanilla method.
     * A splash text can be set by modifying the provided atomic reference.
     * If a splash text is successfully set, it will override the splash determined by the vanilla method.
     */
    public static final Event<Replace> REPLACE = EventFactory.createArrayBacked(Replace.class, listeners -> (builder, splashTexts, calendar, session, random) -> {
        for (Replace listener : listeners) {
            listener.replaceSplashText(builder, splashTexts, calendar, session, random);
        }
    });

    @FunctionalInterface
    public interface Replace {
        /**
         * Called after the vanilla method has selected a splash text renderer to be displayed.
         *
         * @param builder     a mutable reference to a splash text renderer
         * @param splashTexts a list of splash texts read from resource files
         * @param calendar    a calendar with the time set to the current date
         * @param session     the current client session
         * @param random      the random used by the splash text supplier
         */
        void replaceSplashText(AtomicReference<SplashTextRenderer> builder, List<String> splashTexts, Calendar calendar, Session session, Random random);
    }
}
