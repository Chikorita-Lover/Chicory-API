package net.chikorita_lover.chicory.api.config.property;

import java.util.Comparator;

public abstract class NumberConfigProperty<T extends Number> extends ConfigProperty<T> {
    private final Comparator<T> comparator = Comparator.comparing(Number::doubleValue);
    private final T min;
    private final T max;

    public NumberConfigProperty(String name, T defaultValue, T min, T max) {
        super(name, defaultValue);
        if (this.comparator.compare(defaultValue, min) < 0) {
            throw new IllegalArgumentException("Default value of " + name + " must be greater than or equal to min (" + min + ")");
        }
        if (this.comparator.compare(defaultValue, max) > 0) {
            throw new IllegalArgumentException("Default value of " + name + " must be less than or equal to max (" + max + ")");
        }
        if (this.comparator.compare(max, min) <= 0) {
            throw new IllegalArgumentException("Max value of " + name + " must be greater than min (" + min + ")");
        }
        this.min = min;
        this.max = max;
    }

    public Value<T> createValue(String value) {
        return this.createValue(this.parse(value));
    }

    public boolean isValid(String value) {
        try {
            return this.isValid(this.parse(value));
        } catch (NumberFormatException e) {
            return false;
        }
    }

    protected abstract T parse(String value);

    @Override
    public boolean isValid(T value) {
        return this.comparator.compare(value, this.min) >= 0 && this.comparator.compare(value, this.max) <= 0;
    }
}
