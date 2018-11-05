package therealfarfetchd.qcommon.architect.model.value;

import javax.annotation.Nullable;

public interface StateProvider {

    @Nullable
    String getState(String key);

}
