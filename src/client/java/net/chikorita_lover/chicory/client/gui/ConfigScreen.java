package net.chikorita_lover.chicory.client.gui;

import net.chikorita_lover.chicory.api.config.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

@Environment(EnvType.CLIENT)
public class ConfigScreen extends Screen {
    protected final ThreePartsLayoutWidget layout = new ThreePartsLayoutWidget(this);
    protected final Config config;
    private final Screen parent;
    private final Set<ConfigPropertyListWidget.AbstractPropertyWidget> invalidPropertyWidgets = new HashSet<>();
    @Nullable
    private ConfigPropertyListWidget listWidget;
    @Nullable
    private ButtonWidget doneButton;

    public ConfigScreen(Screen parent, Config config) {
        super(Text.translatable("config." + config.getName() + ".title"));
        this.parent = parent;
        this.config = config;
    }

    @Override
    protected void init() {
        this.layout.addHeader(this.title, this.textRenderer);
        this.listWidget = this.layout.addBody(new ConfigPropertyListWidget(this));
        DirectionalLayoutWidget layoutWidget = this.layout.addFooter(DirectionalLayoutWidget.horizontal().spacing(8));
        this.doneButton = layoutWidget.add(ButtonWidget.builder(ScreenTexts.DONE, button -> this.close()).build());
        this.layout.forEachChild(this::addDrawableChild);
        this.initTabNavigation();
    }

    @Override
    protected void initTabNavigation() {
        this.layout.refreshPositions();
        if (this.listWidget != null) {
            this.listWidget.position(this.width, this.layout);
        }
    }

    @Override
    public void close() {
        if (this.client.world == null) {
            this.config.saveValues();
        }
        this.client.setScreen(this.parent);
    }

    protected void markInvalid(ConfigPropertyListWidget.AbstractPropertyWidget widget) {
        this.invalidPropertyWidgets.add(widget);
        this.updateDoneButton();
    }

    protected void markValid(ConfigPropertyListWidget.AbstractPropertyWidget widget) {
        this.invalidPropertyWidgets.remove(widget);
        this.updateDoneButton();
    }

    protected void updateDoneButton() {
        if (this.doneButton != null) {
            this.doneButton.active = this.invalidPropertyWidgets.isEmpty();
        }
    }
}
