package therealfarfetchd.qcommon.architect.model.part;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class ValueVariance<T> {

    private ValueVariance() {}

    public abstract Type getType();

    public static final class Single<T> extends ValueVariance<T> {

        private final T value;

        private Single(T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }

        @Override
        public Type getType() {
            return Type.SINGLE;
        }

        public static <T> Single<T> of(T value) {
            return new Single<>(value);
        }

    }

    public static final class Variants<T> extends ValueVariance<T> {

        private final Set<T> values;

        private Variants(Collection<T> values) {
            this.values = Collections.unmodifiableSet(new HashSet<>(values));
        }

        public Set<T> getValues() {
            return values;
        }

        @Override
        public Type getType() {
            return Type.VARIANTS;
        }

        public static <T> Variants<T> of(Collection<T> values) {
            return new Variants<>(values);
        }

    }

    public static final class Dynamic<T> extends ValueVariance<T> {

        public static Dynamic INSTANCE = new Dynamic();

        private Dynamic() {}

        @Override
        public Type getType() {
            return Type.DYNAMIC;
        }

        @SuppressWarnings("unchecked")
        public static <T> Dynamic<T> of() {
            return INSTANCE;
        }

    }

    public enum Type {SINGLE, VARIANTS, DYNAMIC}

}
