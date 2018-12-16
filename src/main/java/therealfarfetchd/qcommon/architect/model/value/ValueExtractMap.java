package therealfarfetchd.qcommon.architect.model.value;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

class ValueExtractMap<K, T> implements Value<Map<K, T>> {

    private final Map<K, Value<T>> wrapped;

    private Collection<Map<K, T>> values;

    public ValueExtractMap(Map<K, Value<T>> wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public Map<K, T> get(StateProvider state) {
        return wrapped.entrySet().parallelStream()
            .map(entry -> Pair.of(entry.getKey(), entry.getValue().get(state)))
            .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    @Override
    public Collection<Map<K, T>> getPossibleValues() {
        if (values == null) {
            Map<K, List<T>> v = wrapped.entrySet().stream()
                .map(entry -> Pair.of(entry.getKey(), new ArrayList<>(entry.getValue().getPossibleValues())))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));

            values = getPermutations(v);
        }

        return values;
    }

    static <K, V> List<Map<K, V>> getPermutations(Map<K, List<V>> list) {
        ArrayList<Map<K, V>> result = new ArrayList<>();

        List<Map.Entry<K, List<V>>> prepared = new ArrayList<>(list.entrySet());

        int[] indices = new int[prepared.size()];

        final int last = prepared.size() - 1;
        final int lastSize = prepared.get(last).getValue().size();

        while (indices[last] < lastSize) {
            HashMap<K, V> permutation = new HashMap<>();

            for (int i = 0; i < prepared.size(); i++) {
                Map.Entry<K, List<V>> kv = prepared.get(i);
                permutation.put(kv.getKey(), kv.getValue().get(indices[i]));
            }

            indices[0]++;
            for (int i = 0; i < last; i++) {
                if (indices[i] == prepared.get(i).getValue().size()) {
                    indices[i] = 0;
                    indices[i + 1]++;
                }
            }

            result.add(permutation);
        }

        return result;
    }

}
