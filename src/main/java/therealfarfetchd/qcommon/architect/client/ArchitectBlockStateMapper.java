package therealfarfetchd.qcommon.architect.client;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.util.ResourceLocation;

public class ArchitectBlockStateMapper extends StateMapperBase {

    public static final ArchitectBlockStateMapper INSTANCE = new ArchitectBlockStateMapper();

    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        final ResourceLocation rl = Block.REGISTRY.getNameForObject(state.getBlock());
        return new ModelResourceLocation(new ResourceLocation(rl.getNamespace(), String.format("block/%s", rl.getPath())), this.getPropertyString(state.getProperties()));
    }

}
