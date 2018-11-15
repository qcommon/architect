package therealfarfetchd.qcommon.architect.model.value;

import java.util.Collection;

public class ValuePull<T> implements Value<T>, KeyInfo<T> {

    private final Value<T> wrapped;

    private final Collection<T> values;

    public ValuePull(Value<T> wrapped) {
        this.wrapped = wrapped;
        KeyInfo keyInfo = wrapped.getKeyInfo();
        if (keyInfo == null) throw new IllegalArgumentException(String.format("wrapped value %s does not support key info!", wrapped));

        values = wrapped.getPossibleValues();
    }

    @Override
    public T get(StateProvider state) {
        return wrapped.get(state); // TODO
    }

    @Override
    public Collection<T> getPossibleValues() {
        return values;
    }

    @Override
    public Value<T> pull() {
        return this;
    }

    @Override
    public Value<T> asCached() {
        return this;
    }

}
