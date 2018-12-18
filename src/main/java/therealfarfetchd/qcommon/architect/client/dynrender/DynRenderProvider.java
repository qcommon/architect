package therealfarfetchd.qcommon.architect.client.dynrender;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.Map;

public interface DynRenderProvider {

    Map<DynRender, Collection<BlockPos>> getRenders();

    static DynRenderProvider fromWorld(World world) {
        return (DynRenderProvider) world;
    }

}
