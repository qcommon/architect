package therealfarfetchd.qcommon.architect.model.value;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ValueCached<T> implements Value<T> {

    private final Value<T> wrapped;

    private Map<StateProvider, T> cache = new HashMap<>();

    public ValueCached(Value<T> wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public T get(StateProvider state) {
        return cache.computeIfAbsent(state, wrapped::get);
    }

    @Override
    public Collection<T> getPossibleValues() {
        return wrapped.getPossibleValues();
    }

    @Override
    public Value<T> asCached() {
        return this;
    }

    @Override
    public Value<T> pull() {
        return wrapped.pull();
    }
}
