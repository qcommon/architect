package therealfarfetchd.qcommon.architect.model.value;

import java.util.Collection;
import java.util.Collections;

public class ConstantValue<T, K> implements Value<T, K> {

    private final T value;

    public ConstantValue(T value) {
        this.value = value;
    }

    @Override
    public T get(K i) {
        return value;
    }

    @Override
    public Collection<T> getValidValues() {
        return Collections.singleton(value);
    }

}
