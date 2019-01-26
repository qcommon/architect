package therealfarfetchd.qcommon.architect.mixin;

import net.minecraft.client.render.WorldRenderer;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {

//    @Shadow
//    private ClientWorld world;
//
//    @Inject(method = "renderEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/render/VisibleRegion;F)V", at = @At("RETURN"))
//    private void renderEntities(Entity var1, VisibleRegion var2, float var3, CallbackInfo ci) {
//        GuiLighting.enable();
//
//        DynRenderProvider drp = DynRenderProvider.fromWorld(world);
//        for (Entry<DynRender, Collection<BlockPos>> entry : drp.getRenders().entrySet()) {
//            DynRender dr = entry.getKey();
//            Collection<BlockPos> positions = entry.getValue();
//
//            for (BlockPos pos : positions) {
//                dr.draw(InputProvider.NULL, var1, var3, pos.getX() - var1.x, pos.getY() - var1.y, pos.getZ() - var1.z);
//            }
//        }
//    }

}
