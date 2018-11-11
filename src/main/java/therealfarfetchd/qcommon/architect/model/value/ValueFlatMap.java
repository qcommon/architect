package therealfarfetchd.qcommon.architect.model.value;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

class ValueFlatMap<S, T> implements Value<T> {

    private final Value<S> wrapped;
    private final Function<S, Value<T>> mapper;

    private Collection<T> values;

    ValueFlatMap(Value<S> wrapped, Function<S, Value<T>> mapper) {
        this.wrapped = wrapped;
        this.mapper = mapper;
    }

    @Override
    public T get(StateProvider state) {
        return mapper.apply(wrapped.get(state)).get(state);
    }

    @Override
    public Collection<T> getPossibleValues() {
        if (values == null) {
            values = wrapped.getPossibleValues().stream()
                .map(mapper)
                .flatMap(v -> v.getPossibleValues().stream())
                .collect(Collectors.toList());
        }

        return values;
    }

}
