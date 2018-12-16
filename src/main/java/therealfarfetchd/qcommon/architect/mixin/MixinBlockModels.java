package therealfarfetchd.qcommon.architect.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import therealfarfetchd.qcommon.architect.client.BlockModelLoader;

@Mixin(BlockModels.class)
public abstract class MixinBlockModels {

    @Inject(
        method = "getModelId(Lnet/minecraft/block/BlockState;)Lnet/minecraft/client/util/ModelIdentifier;",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void getModelId(BlockState state, CallbackInfoReturnable<ModelIdentifier> cir) {
        final Identifier blockId = Registry.BLOCK.getId(state.getBlock());
        Identifier id = new ModelIdentifier(new Identifier(blockId.getNamespace(), String.format("block/%s", blockId.getPath())), "invalid");
        if (BlockModelLoader.INSTANCE.accepts(id)) {
            cir.setReturnValue(getModelId(id, state));
            cir.cancel();
        }
    }

    @Shadow
    public static ModelIdentifier getModelId(Identifier var0, BlockState var1) {
        return null;
    }


}
