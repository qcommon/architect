package therealfarfetchd.qcommon.architect.mixin;

import net.minecraft.client.MinecraftClient;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import therealfarfetchd.qcommon.architect.Architect;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {

    @Inject(
        method = "init()V",
        at = @At(value = "FIELD",
            target = "Lnet/minecraft/client/MinecraftClient;spriteAtlas:Lnet/minecraft/client/texture/SpriteAtlasTexture;",
            opcode = Opcodes.PUTFIELD,
            shift = Shift.BEFORE
        )
    )
    private void init(CallbackInfo ci) {
        Architect.INSTANCE.onGameInit();
    }

}
