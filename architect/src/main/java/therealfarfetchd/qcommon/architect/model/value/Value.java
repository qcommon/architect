package therealfarfetchd.qcommon.architect.model.value;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

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

    default Value<T> asCached() {
        return this;
    }

    static <T> Value<T> wrap(T t) {
        return new ConstantValue<>(t);
    }

    static <T> Value<List<T>> extract(Collection<Value<T>> c) {
        return new ValueExtractList<>(c);
    }

    static <K, T> Value<Map<K, T>> extract(Map<K, Value<T>> c) {
        return new ValueExtractMap<>(c);
    }

}
