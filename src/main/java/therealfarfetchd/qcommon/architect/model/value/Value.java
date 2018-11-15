package therealfarfetchd.qcommon.architect.model.value;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Nullable;

public interface Value<T> {

    /**
     * Get the actual value based on the state.
     */
    T get(StateProvider state);

    /**
     * Maps all possible values based on the passed function.
     */
    default <R> Value<R> map(Function<T, R> op) {
        return new ValueMap<>(this, op);
    }

    /**
     * Flattens the value based on the passed function.
     */
    default <R> Value<R> flatMap(Function<T, Value<R>> op) {
        return new ValueFlatMap<>(this, op);
    }

    /**
     * Returns a collection of all values that can be returned by get().
     */
    Collection<T> getPossibleValues();

    /**
     * @return true if only one value can be returned by get().
     */
    default boolean isConstant() {
        return getPossibleValues().size() == 1;
    }

    /**
     * Adds a cache to this Value, if possible.
     * The returned Value will cache the result of get() calls.
     */
    default Value<T> asCached() {
        return new ValueCached<>(this);
    }

    /**
     * Compute the pending operations in this pipeline now, if possible.
     * Without this, get() calls are computed lazily, which means it calls get() for every previous element of the pipeline,
     * which might cause a performance impact if get() is called often.
     */
    default Value<T> pull() {
        if (getKeyInfo() == null) return this;
        return new ValuePull<>(this);
    }

    @Nullable
    default KeyInfo<T> getKeyInfo() {
        //noinspection unchecked
        return this instanceof KeyInfo ? (KeyInfo<T>) this : null;
    }

    /**
     * Wrap a constant object into a Value object.
     */
    static <T> Value<T> wrap(T t) {
        return new ConstantValue<>(t);
    }

    /**
     * Extract a Value from a list of Values.
     */
    static <T> Value<List<T>> extract(Collection<Value<T>> c) {
        return new ValueExtractList<>(c);
    }

    /**
     * Extract a Value from the values of a Map.
     */
    static <K, T> Value<Map<K, T>> extract(Map<K, Value<T>> c) {
        return new ValueExtractMap<>(c);
    }

}
