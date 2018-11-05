package therealfarfetchd.qcommon.architect.model.value;

import javax.annotation.Nullable;

public class NullStateProvider implements StateProvider {

    public static final NullStateProvider INSTANCE = new NullStateProvider();

    private NullStateProvider() {}

    @Nullable
    @Override
    public String getState(String key) {
        return null;
    }

}
