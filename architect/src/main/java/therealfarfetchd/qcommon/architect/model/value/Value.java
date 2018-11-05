package therealfarfetchd.qcommon.architect.model.value;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface Value<T> {

    T get(StateProvider state);

    default <R> Value<R> map(Function<T, R> op) {
        return new ValueMap<>(this, op);
    }

    default <R> Value<R> flatMap(Function<T, Value<R>> op) {
        return new ValueFlatMap<>(this, op);
    }

    Collection<T> getPossibleValues();

    default boolean isConstant() {
        return getPossibleValues().size() == 1;
    }

    static <T> Value<T> wrap(T t) {
        return new ConstantValue<>(t);
    }

    static <T> Value<List<T>> extract(Collection<Value<T>> c) {
        return new ValueExtract<>(c);
    }

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

    }

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

    class ValueExtract<T> implements Value<List<T>> {

        private final Collection<Value<T>> wrapped;

        private Collection<List<T>> values;

        public ValueExtract(Collection<Value<T>> wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public List<T> get(StateProvider state) {
            return wrapped.stream().map($ -> $.get(state)).collect(Collectors.toList());
        }

        @Override
        public Collection<List<T>> getPossibleValues() {
            if (values == null) {
                List<List<T>> v = wrapped.stream()
                    .map(Value::getPossibleValues)
                    .map(ArrayList::new)
                    .collect(Collectors.toList());

                values = getPermutations(v);
            }

            return values;
        }

        private static <T> List<List<T>> getPermutations(List<List<T>> list) {
            ArrayList<List<T>> result = new ArrayList<>();

            int[] indices = new int[list.size()];

            final int last = list.size() - 1;
            final int lastSize = list.get(last).size();

            while (indices[last] < lastSize) {
                ArrayList<T> permutation = new ArrayList<>();

                for (int i = 0; i < list.size(); i++) {
                    permutation.add(list.get(i).get(indices[i]));
                }

                indices[0]++;
                for (int i = 0; i < last; i++) {
                    if (indices[i] == list.get(i).size()) {
                        indices[i] = 0;
                        indices[i + 1]++;
                    }
                }

                result.add(permutation);
            }

            return result;
        }

    }

}
