package therealfarfetchd.qcommon.architect.proxy;

import net.minecraft.resource.ResourceReloadListener;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nullable;

import therealfarfetchd.qcommon.architect.Architect;

public class CommonProxy {

    @Nullable
    public InputStream openResource(Identifier id, boolean respectResourcePack) {
        return Architect.class.getClassLoader().getResourceAsStream(String.format("assets/%s/%s", id.getNamespace(), id.getPath()));
    }

    public boolean resourceExists(Identifier id, boolean respectResourcePack) {
        boolean fileExists = false;

        try (InputStream ignored = Architect.proxy.openResource(id, respectResourcePack)) {
            if (ignored != null) fileExists = true;
        } catch (IOException ignored) { }

        return fileExists;
    }

    @Nullable
    public InputStream openResource(Identifier id) {
        return openResource(id, true);
    }

    public void registerModelLoader() {}

    public void registerReloadListener(ResourceReloadListener listener) {}

}
