package therealfarfetchd.qcommon.architect.model.value;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

public class ConstantValue<T> implements Value<T>, KeyInfo {

    private final T value;

    public ConstantValue(T value) {
        this.value = value;
    }

    @Override
    public <R> Value<R> map(Function<T, R> op) {
        return new ConstantValue<>(op.apply(value));
    }

    @Override
    public <R> Value<R> flatMap(Function<T, Value<R>> op) {
        return op.apply(value);
    }

    @Override
    public T get(StateProvider state) {
        return value;
    }

    @Override
    public Collection<T> getPossibleValues() {
        return Collections.singleton(value);
    }

    @Override
    public Value<T> asCached() {
        return this;
    }

    @Override
    public Value<T> pull() {
        return this;
    }

}
