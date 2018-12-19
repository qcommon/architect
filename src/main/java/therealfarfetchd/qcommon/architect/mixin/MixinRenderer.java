package therealfarfetchd.qcommon.architect.mixin;

import net.minecraft.class_856;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.client.render.Renderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

import java.util.Collection;
import java.util.Map.Entry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import therealfarfetchd.qcommon.architect.client.dynrender.DynRender;
import therealfarfetchd.qcommon.architect.client.dynrender.DynRender.InputProvider;
import therealfarfetchd.qcommon.architect.client.dynrender.DynRenderProvider;

@Mixin(Renderer.class)
public abstract class MixinRenderer {

    @Shadow
    private ClientWorld world;

    @Inject(method = "renderEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/class_856;F)V", at = @At("RETURN"))
    private void renderEntities(Entity var1, class_856 var2, float var3, CallbackInfo ci) {
        GuiLighting.enable();

        DynRenderProvider drp = DynRenderProvider.fromWorld(world);
        for (Entry<DynRender, Collection<BlockPos>> entry : drp.getRenders().entrySet()) {
            DynRender dr = entry.getKey();
            Collection<BlockPos> positions = entry.getValue();

            for (BlockPos pos : positions) {
                dr.draw(InputProvider.NULL, var1, var3, pos.getX() - var1.x, pos.getY() - var1.y, pos.getZ() - var1.z);
            }
        }
    }

}
