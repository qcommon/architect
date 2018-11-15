package therealfarfetchd.qcommon.architect.model.value;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class Matcher<T> implements Value<T>, KeyInfo {

    private final Map<MatchTest, T> map;
    private final T _default;

    public Matcher(Map<MatchTest, T> map, T _default) {
        this.map = map;
        this._default = _default;
    }

    @Override
    public T get(StateProvider state) {
        return map.entrySet().stream()
            .filter($ -> $.getKey().matches(state))
            .map(Map.Entry::getValue)
            .findFirst()
            .orElse(_default);
    }

    @Override
    public Value<T> pull() {
        return this;
    }

    @Override
    public Collection<T> getPossibleValues() {
        return map.values();
    }

    public static class MatchTest {

        private final Map<String, String> constraints;

        public MatchTest(Map<String, String> constraints) {
            this.constraints = constraints;
        }

        public boolean matches(StateProvider stateProvider) {
            return constraints.entrySet().stream().allMatch(entry -> entry.getValue().equals(stateProvider.getState("" + entry.getKey())));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MatchTest matchTest = (MatchTest) o;
            return Objects.equals(constraints, matchTest.constraints);
        }

        @Override
        public int hashCode() {
            return Objects.hash(constraints);
        }

    }

}
