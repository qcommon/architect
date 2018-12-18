package therealfarfetchd.qcommon.architect.proxy;

import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloadListener;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nullable;

import therealfarfetchd.qcommon.architect.client.CustomModelLoader;
import therealfarfetchd.qcommon.architect.client.dynrender.DynModelLoader;

public class ClientProxy extends CommonProxy {

    @Nullable
    @Override
    public InputStream openResource(Identifier id, boolean respectResourcePack) {
        if (!respectResourcePack) return super.openResource(id, respectResourcePack);

        try {
            return MinecraftClient.getInstance().getResourceManager().getResource(id).getInputStream();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void registerModelLoader() {
        for (CustomModelLoader loader : CustomModelLoader.LOADERS) {
            registerReloadListener(loader);
        }

        registerReloadListener(DynModelLoader.INSTANCE);
    }

    @Override
    public void registerReloadListener(ResourceReloadListener listener) {
        super.registerReloadListener(listener);

        final ResourceManager rm = MinecraftClient.getInstance().getResourceManager();

        if (rm instanceof ReloadableResourceManager) {
            final ReloadableResourceManager rrm = (ReloadableResourceManager) rm;

            rrm.addListener(listener);
        }
    }

}
