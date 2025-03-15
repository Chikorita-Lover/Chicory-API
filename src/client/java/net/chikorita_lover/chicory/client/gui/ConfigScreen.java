package net.chikorita_lover.chicory.client.gui;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.chikorita_lover.chicory.api.config.Config;
import net.chikorita_lover.chicory.api.config.ConfigCategory;
import net.chikorita_lover.chicory.api.config.property.BooleanConfigProperty;
import net.chikorita_lover.chicory.api.config.property.ConfigProperty;
import net.chikorita_lover.chicory.api.config.property.NumberConfigProperty;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ApiStatus.Internal
@Environment(EnvType.CLIENT)
public class ConfigScreen extends Screen {
    private final ThreePartsLayoutWidget layout = new ThreePartsLayoutWidget(this);
    private final Screen parent;
    private final Config config;
    private final Set<AbstractPropertyWidget> invalidPropertyWidgets = new HashSet<>();
    @Nullable
    private PropertyListWidget propertyListWidget;
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
        this.propertyListWidget = this.layout.addBody(new PropertyListWidget());
        DirectionalLayoutWidget layoutWidget = this.layout.addFooter(DirectionalLayoutWidget.horizontal().spacing(8));
        this.doneButton = layoutWidget.add(ButtonWidget.builder(ScreenTexts.DONE, button -> this.close()).build());
        this.layout.forEachChild(this::addDrawableChild);
        this.initTabNavigation();
    }

    @Override
    protected void initTabNavigation() {
        this.layout.refreshPositions();
        if (this.propertyListWidget != null) {
            this.propertyListWidget.position(this.width, this.layout);
        }
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
        if (this.client.world == null) {
            this.config.saveValues();
        }
    }

    private void markInvalid(AbstractPropertyWidget widget) {
        this.invalidPropertyWidgets.add(widget);
        this.updateDoneButton();
    }

    private void markValid(AbstractPropertyWidget widget) {
        this.invalidPropertyWidgets.remove(widget);
        this.updateDoneButton();
    }

    private void updateDoneButton() {
        if (this.doneButton != null) {
            this.doneButton.active = this.invalidPropertyWidgets.isEmpty();
        }
    }

    public abstract static class AbstractPropertyWidget extends ElementListWidget.Entry<AbstractPropertyWidget> {
        @Nullable
        protected final List<OrderedText> description;

        public AbstractPropertyWidget(@Nullable List<OrderedText> description) {
            this.description = description;
        }
    }

    public class PropertyListWidget extends ElementListWidget<AbstractPropertyWidget> {
        public PropertyListWidget() {
            super(MinecraftClient.getInstance(), ConfigScreen.this.width, ConfigScreen.this.layout.getContentHeight(), ConfigScreen.this.layout.getHeaderHeight(), 24);
            Map<ConfigCategory, List<ConfigProperty<?>>> propertiesByCategory = ConfigScreen.this.config.getPropertiesByCategory();
            propertiesByCategory.forEach((category, properties) -> {
                this.addEntry(new PropertyCategoryWidget(Text.translatable("config.category." + ConfigScreen.this.config.getName() + "." + category.name()).formatted(Formatting.BOLD, Formatting.YELLOW)));
                properties.forEach(property -> {
                    Text name = Text.translatable("config." + ConfigScreen.this.config.getName() + "." + property.getName());
                    if (property instanceof NumberConfigProperty<?> numberProperty) {
                        this.addEntry(new NumberPropertyWidget(name, this.createDescription(property), numberProperty));
                    }
                    if (property instanceof BooleanConfigProperty booleanProperty) {
                        this.addEntry(new BooleanPropertyWidget(name, this.createDescription(property), booleanProperty));
                    }
                });
            });
        }

        private List<OrderedText> createDescription(ConfigProperty<?> property) {
            ImmutableCollection<OrderedText> list;
            MutableText name = Text.literal(property.getName()).formatted(Formatting.YELLOW);
            String defaultValue = property.getDefaultValue().toString();
            MutableText defaultText = Text.translatable("editGamerule.default", Text.literal(defaultValue)).formatted(Formatting.GRAY);
            String descriptionKey = "config." + ConfigScreen.this.config.getName() + "." + property.getName() + ".description";
            if (I18n.hasTranslation(descriptionKey)) {
                ImmutableCollection.Builder<OrderedText> builder = ImmutableList.builder();
                builder.add(name.asOrderedText());
                MutableText description = Text.translatable(descriptionKey);
                ConfigScreen.this.textRenderer.wrapLines(description, 150).forEach(builder::add);
                list = builder.add(defaultText.asOrderedText()).build();
            } else {
                list = ImmutableList.of(name.asOrderedText(), defaultText.asOrderedText());
            }
            return list.asList();
        }

        @Override
        public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
            super.renderWidget(context, mouseX, mouseY, delta);
            AbstractPropertyWidget widget = this.getHoveredEntry();
            if (widget != null && widget.description != null) {
                ConfigScreen.this.setTooltip(widget.description);
            }
        }
    }

    public class NumberPropertyWidget extends AbstractNamedPropertyWidget {
        private final TextFieldWidget valueWidget;

        public <T extends Number> NumberPropertyWidget(Text name, List<OrderedText> description, final NumberConfigProperty<T> property) {
            super(description, name);
            this.valueWidget = new TextFieldWidget(ConfigScreen.this.client.textRenderer, 10, 5, 44, 20, name.copy().append("\n").append(property.getName()).append("\n"));
            this.valueWidget.setText(ConfigScreen.this.config.get(property).toString());
            this.valueWidget.setChangedListener(value -> {
                if (property.isValid(value)) {
                    this.valueWidget.setEditableColor(0xE0E0E0);
                    ConfigScreen.this.config.set(property, property.createValue(value).value());
                    ConfigScreen.this.markValid(this);
                } else {
                    this.valueWidget.setEditableColor(Colors.RED);
                    ConfigScreen.this.markInvalid(this);
                }
            });
            this.valueWidget.active = ConfigScreen.this.client.world == null;
            this.children.add(this.valueWidget);
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.drawName(context, x, y);
            this.valueWidget.setX(x + entryWidth - 45);
            this.valueWidget.setY(y);
            this.valueWidget.render(context, mouseX, mouseY, tickDelta);
        }
    }

    public class BooleanPropertyWidget extends AbstractNamedPropertyWidget {
        private final CyclingButtonWidget<Boolean> toggleButton;

        public BooleanPropertyWidget(Text name, List<OrderedText> description, final BooleanConfigProperty property) {
            super(description, name);
            this.toggleButton = CyclingButtonWidget.onOffBuilder(ConfigScreen.this.config.get(property)).omitKeyText().narration(button -> button.getGenericNarrationMessage().append("\n").append(property.getName())).build(10, 5, 44, 20, name, (button, value) -> ConfigScreen.this.config.set(property, value));
            this.toggleButton.active = ConfigScreen.this.client.world == null;
            this.children.add(this.toggleButton);
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.drawName(context, x, y);
            this.toggleButton.setX(x + entryWidth - 45);
            this.toggleButton.setY(y);
            this.toggleButton.render(context, mouseX, mouseY, tickDelta);
        }
    }

    public abstract class AbstractNamedPropertyWidget extends AbstractPropertyWidget {
        protected final List<ClickableWidget> children = Lists.newArrayList();
        private final List<OrderedText> name;

        public AbstractNamedPropertyWidget(@Nullable final List<OrderedText> description, final Text name) {
            super(description);
            this.name = ConfigScreen.this.client.textRenderer.wrapLines(name, 175);
        }

        protected void drawName(DrawContext context, int x, int y) {
            int top = -this.name.size() * 5 + y + 10;
            for (int i = 0; i < this.name.size(); ++i) {
                context.drawText(ConfigScreen.this.client.textRenderer, this.name.get(i), x, i * 10 + top, Colors.WHITE, false);
            }
        }

        @Override
        public List<? extends Element> children() {
            return this.children;
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return this.children;
        }
    }

    public class PropertyCategoryWidget extends AbstractPropertyWidget {
        private final Text name;

        public PropertyCategoryWidget(Text text) {
            super(null);
            this.name = text;
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            context.drawCenteredTextWithShadow(ConfigScreen.this.client.textRenderer, this.name, x + entryWidth / 2, y + 5, Colors.WHITE);
        }

        @Override
        public List<? extends Element> children() {
            return ImmutableList.of();
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return ImmutableList.of(new Selectable() {
                public Selectable.SelectionType getType() {
                    return Selectable.SelectionType.HOVERED;
                }

                public void appendNarrations(NarrationMessageBuilder builder) {
                    builder.put(NarrationPart.TITLE, PropertyCategoryWidget.this.name);
                }
            });
        }
    }
}
