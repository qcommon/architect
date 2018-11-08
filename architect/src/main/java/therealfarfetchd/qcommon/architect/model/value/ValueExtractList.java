package therealfarfetchd.qcommon.architect.model.value;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

class ValueExtractList<T> implements Value<List<T>> {

    private final Collection<Value<T>> wrapped;

    private Collection<List<T>> values;

    public ValueExtractList(Collection<Value<T>> wrapped) {
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

    static <T> List<List<T>> getPermutations(List<List<T>> list) {
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
