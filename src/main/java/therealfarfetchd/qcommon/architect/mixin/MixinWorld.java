package therealfarfetchd.qcommon.architect.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import therealfarfetchd.qcommon.architect.client.dynrender.DynModelLoader;
import therealfarfetchd.qcommon.architect.client.dynrender.DynRender;
import therealfarfetchd.qcommon.architect.client.dynrender.DynRenderProvider;

@Mixin(World.class)
public abstract class MixinWorld implements DynRenderProvider {

    private final Multimap<DynRender, BlockPos> dynRenderBlocks = HashMultimap.create(20, 50);

    public void recategorize() {
        List<BlockPos> blocks = new ArrayList<>(dynRenderBlocks.values());
        dynRenderBlocks.clear();

        for (BlockPos pos : blocks) {
            BlockState bs = getBlockState(pos);
            Block b = bs.getBlock();

            final DynRender dynModel = DynModelLoader.INSTANCE.getDynModel(b);
            if (dynModel != null) {
                dynRenderBlocks.put(dynModel, pos);
            }
        }
    }

    @Override
    public Map<DynRender, Collection<BlockPos>> getRenders() {
        return dynRenderBlocks.asMap();
    }

    @Shadow
    public abstract BlockState getBlockState(BlockPos var1);

}
