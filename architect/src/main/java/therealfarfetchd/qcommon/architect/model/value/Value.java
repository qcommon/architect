package therealfarfetchd.qcommon.architect.model.value;

import java.util.Collection;

public interface Value<T, K> {

    T get(K i);

    Collection<T> getValidValues();

}
