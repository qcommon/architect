package therealfarfetchd.qcommon.architect.model.value;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

class ValueMap<S, T> implements Value<T> {

    private final Value<S> wrapped;
    private final Function<S, T> mapper;

    private Collection<T> values;

    ValueMap(Value<S> wrapped, Function<S, T> mapper) {
        this.wrapped = wrapped;
        this.mapper = mapper;
    }

    @Override
    public T get(StateProvider state) {
        return mapper.apply(wrapped.get(state));
    }

    @Override
    public Collection<T> getPossibleValues() {
        if (values == null) {
            values = wrapped.getPossibleValues().stream()
                .map(mapper)
                .collect(Collectors.toList());
        }

        return values;
    }

    @Nullable
    @Override
    public KeyInfo<T> getKeyInfo() {
        return null; // TODO map KeyInfo<S> to KeyInfo<T>
    }

}
