package therealfarfetchd.qcommon.architect.proxy;

import net.minecraft.util.Identifier;

import java.io.InputStream;

import javax.annotation.Nullable;

import therealfarfetchd.qcommon.architect.Architect;

public class CommonProxy {

    @Nullable
    public InputStream openResource(Identifier id, boolean respectResourcePack) {
        return Architect.class.getClassLoader().getResourceAsStream(String.format("assets/%s/%s", id.getNamespace(), id.getPath()));
    }

    @Nullable
    public InputStream openResource(Identifier id) {
        return openResource(id, true);
    }

    public void registerModelLoader() {}

}
