package therealfarfetchd.qcommon.architect.mixin;

import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.util.Identifier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import therealfarfetchd.qcommon.architect.client.CustomModelLoader;

@Mixin(ModelLoader.class)
public abstract class MixinModelLoader {

    @Inject(
        at = @At("HEAD"),
        method = "loadModel(Lnet/minecraft/util/Identifier;)V",
        cancellable = true
    )
    private void loadModel(Identifier id, CallbackInfo ci) {
        for (CustomModelLoader loader : CustomModelLoader.LOADERS) {
            if (loader.accepts(id)) {
                UnbakedModel model = loader.loadModel(id);
                putModel(id, model);
                ci.cancel();
                return;
            }
        }
    }

    @Shadow
    protected abstract void putModel(Identifier var1, UnbakedModel var2);

}
