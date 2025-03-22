package net.chikorita_lover.chicory.client.gui;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.chikorita_lover.chicory.api.config.property.BooleanConfigProperty;
import net.chikorita_lover.chicory.api.config.property.ConfigProperty;
import net.chikorita_lover.chicory.api.config.property.EnumConfigProperty;
import net.chikorita_lover.chicory.api.config.property.NumberConfigProperty;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;

@ApiStatus.Internal
public class ConfigPropertyListWidget extends ElementListWidget<ConfigPropertyListWidget.AbstractPropertyWidget> {
    private static final int ROW_WIDTH = 230;
    private static final int FIELD_WIDTH = 45;
    private final ConfigScreen parent;

    public ConfigPropertyListWidget(ConfigScreen parent) {
        super(MinecraftClient.getInstance(), parent.width, parent.layout.getContentHeight(), parent.layout.getHeaderHeight(), 24);
        this.parent = parent;
        parent.config.getPropertiesByCategory().forEach((category, properties) -> {
            this.addEntry(new PropertyCategoryWidget(Text.translatable("config.category." + this.parent.config.getName() + "." + category.name()).formatted(Formatting.YELLOW, Formatting.BOLD)));
            properties.stream().sorted(Comparator.comparing(ConfigProperty::getName)).forEach(this::addPropertyEntry);
        });
    }

    protected static List<OrderedText> createDescription(ConfigProperty<?> property) {
        ImmutableList.Builder<OrderedText> builder = ImmutableList.builder();
        builder.add(Text.literal(property.getName()).formatted(Formatting.YELLOW).asOrderedText());
        String description = property.getTranslationKey().concat(".description");
        if (I18n.hasTranslation(description)) {
            MinecraftClient.getInstance().textRenderer.wrapLines(Text.translatable(description), 175).forEach(builder::add);
        }
        builder.add(Text.translatable("editGamerule.default", property.getDefaultValue()).formatted(Formatting.GRAY).asOrderedText());
        return builder.build();
    }

    private void addPropertyEntry(ConfigProperty<?> property) {
        if (property instanceof NumberConfigProperty<?> numberProperty) {
            this.addEntry(new NumberPropertyWidget(numberProperty));
        }
        if (property instanceof BooleanConfigProperty booleanProperty) {
            this.addEntry(new BooleanPropertyWidget(booleanProperty));
        }
        if (property instanceof EnumConfigProperty<?> enumProperty) {
            this.addEntry(new EnumPropertyWidget<>(enumProperty));
        }
    }

    @Override
    public int getRowWidth() {
        return ROW_WIDTH;
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderWidget(context, mouseX, mouseY, delta);
        AbstractPropertyWidget widget = this.getHoveredEntry();
        if (widget != null && widget.description != null) {
            this.parent.setTooltip(widget.description);
        }
    }

    public abstract static class AbstractPropertyWidget extends Entry<AbstractPropertyWidget> {
        @Nullable
        protected final List<OrderedText> description;

        public AbstractPropertyWidget(@Nullable List<OrderedText> description) {
            this.description = description;
        }
    }

    public static class PropertyCategoryWidget extends AbstractPropertyWidget {
        private final Text name;

        public PropertyCategoryWidget(Text text) {
            super(null);
            this.name = text;
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, this.name, x + entryWidth / 2, y + 5, Colors.WHITE);
        }

        @Override
        public List<? extends Element> children() {
            return ImmutableList.of();
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return ImmutableList.of(new Selectable() {
                public SelectionType getType() {
                    return SelectionType.HOVERED;
                }

                public void appendNarrations(NarrationMessageBuilder builder) {
                    builder.put(NarrationPart.TITLE, PropertyCategoryWidget.this.name);
                }
            });
        }
    }

    public abstract static class AbstractNamedPropertyWidget extends AbstractPropertyWidget {
        protected final List<ClickableWidget> children = Lists.newArrayList();
        protected final List<OrderedText> name;

        public AbstractNamedPropertyWidget(@Nullable List<OrderedText> description, Text name) {
            super(description);
            this.name = MinecraftClient.getInstance().textRenderer.wrapLines(name, ROW_WIDTH - FIELD_WIDTH);
        }

        protected void draw(DrawContext context, int x, int y) {
            int top = y - this.name.size() * 5 + 10;
            for (int i = 0; i < this.name.size(); ++i) {
                context.drawText(MinecraftClient.getInstance().textRenderer, this.name.get(i), x, top + i * 10, Colors.WHITE, false);
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

    public class NumberPropertyWidget extends AbstractNamedPropertyWidget {
        private final TextFieldWidget valueWidget;

        public <T extends Number> NumberPropertyWidget(final NumberConfigProperty<T> property) {
            super(createDescription(property), Text.translatable(property.getTranslationKey()));
            this.valueWidget = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, 10, 5, FIELD_WIDTH - 1, 20, Text.translatable(property.getTranslationKey()).append("\n").append(property.getName()).append("\n"));
            this.valueWidget.setText(ConfigPropertyListWidget.this.parent.config.get(property).toString());
            this.valueWidget.setChangedListener(value -> {
                if (property.isValid(value)) {
                    this.valueWidget.setEditableColor(0xE0E0E0);
                    ConfigPropertyListWidget.this.parent.config.set(property, property.parse(value));
                    ConfigPropertyListWidget.this.parent.markValid(this);
                } else {
                    this.valueWidget.setEditableColor(Colors.RED);
                    ConfigPropertyListWidget.this.parent.markInvalid(this);
                }
            });
            this.valueWidget.active = MinecraftClient.getInstance().world == null;
            this.children.add(this.valueWidget);
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.draw(context, x, y);
            this.valueWidget.setPosition(x + entryWidth - FIELD_WIDTH, y);
            this.valueWidget.render(context, mouseX, mouseY, tickDelta);
        }
    }

    public class BooleanPropertyWidget extends AbstractNamedPropertyWidget {
        private final CyclingButtonWidget<Boolean> toggleButton;

        public BooleanPropertyWidget(final BooleanConfigProperty property) {
            super(createDescription(property), Text.translatable(property.getTranslationKey()));
            this.toggleButton = CyclingButtonWidget.onOffBuilder(ConfigPropertyListWidget.this.parent.config.get(property)).omitKeyText().narration(button -> button.getGenericNarrationMessage().append("\n").append(property.getName())).build(10, 5, FIELD_WIDTH - 1, 20, Text.translatable(property.getTranslationKey()), (button, value) -> ConfigPropertyListWidget.this.parent.config.set(property, value));
            this.toggleButton.active = MinecraftClient.getInstance().world == null;
            this.children.add(this.toggleButton);
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.draw(context, x, y);
            this.toggleButton.setPosition(x + entryWidth - FIELD_WIDTH, y);
            this.toggleButton.render(context, mouseX, mouseY, tickDelta);
        }
    }

    public class EnumPropertyWidget<E extends Enum<E>> extends AbstractNamedPropertyWidget {
        private final CyclingButtonWidget<E> toggleButton;

        public EnumPropertyWidget(final EnumConfigProperty<E> property) {
            super(createDescription(property), Text.translatable(property.getTranslationKey()));
            this.toggleButton = CyclingButtonWidget.builder(property::getText).values(property.getType().getEnumConstants()).initially(property.getDefaultValue()).omitKeyText().narration(button -> button.getGenericNarrationMessage().append("\n").append(property.getName())).build(10, 5, FIELD_WIDTH - 1, 20, Text.translatable(property.getTranslationKey()), (button, value) -> ConfigPropertyListWidget.this.parent.config.set(property, value));
            this.toggleButton.active = MinecraftClient.getInstance().world == null;
            this.children.add(this.toggleButton);
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.draw(context, x, y);
            this.toggleButton.setPosition(x + entryWidth - FIELD_WIDTH, y);
            this.toggleButton.render(context, mouseX, mouseY, tickDelta);
        }
    }
}
