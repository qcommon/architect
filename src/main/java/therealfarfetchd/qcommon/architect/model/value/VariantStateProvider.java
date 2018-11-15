package therealfarfetchd.qcommon.architect.model.value;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nullable;

public class VariantStateProvider implements StateProvider {

    public final Map<String, String> values;

    public VariantStateProvider(String variant) {
        this.values = parseVariants(variant);
    }

    @Nullable
    @Override
    public String getState(String key) {
        return values.get(key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VariantStateProvider that = (VariantStateProvider) o;
        return Objects.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }

    private static Map<String, String> parseVariants(String s) {
        Map<String, String> map = new HashMap<>();
        String[] strings = s.split(",");
        for (String entry : strings) {
            int i = entry.indexOf("=");
            String k, v;

            if (i < 1) {
                continue;
            }

            k = entry.substring(0, i);
            v = entry.substring(i + 1);
            map.put(k, v);
        }
        return map;
    }

}
