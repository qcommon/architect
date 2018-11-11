package therealfarfetchd.qcommon.architect.model.value;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nullable;

public class BlockStateProvider implements StateProvider {

    public final IBlockState state;

    private Map<String, String> cache = new HashMap<>();

    public BlockStateProvider(IBlockState state) {
        this.state = state;
    }

    @Nullable
    @Override
    public String getState(String key) {
        return cache.computeIfAbsent(key, k ->
            state.getPropertyKeys()
                .parallelStream()
                .filter($ -> $.getName().equals(k))
                .findAny()
                .map(prop -> getPropValueAsString(state, prop))
                .orElse(null)
        );
    }

    // only needed because java's generics are garbage
    private <T extends Comparable<T>> String getPropValueAsString(IBlockState st, IProperty<T> prop) {
        return prop.getName(state.getValue(prop));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockStateProvider that = (BlockStateProvider) o;
        return Objects.equals(state, that.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state);
    }

}
