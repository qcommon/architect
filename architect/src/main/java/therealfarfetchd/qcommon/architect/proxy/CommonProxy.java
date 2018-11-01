package therealfarfetchd.qcommon.architect.proxy;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.InputStream;

import javax.annotation.Nullable;

import therealfarfetchd.qcommon.architect.Architect;
import therealfarfetchd.qcommon.architect.factories.FactoryRegistry;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent e) {
        FactoryRegistry.INSTANCE.readFactoryDefinitions();
    }

    @Nullable
    public InputStream openResource(ResourceLocation rl, boolean respectResourcePack) {
        return Architect.class.getClassLoader().getResourceAsStream(String.format("assets/%s/%s", rl.getNamespace(), rl.getPath()));
    }

    @Nullable
    public InputStream openResource(ResourceLocation rl) {
        return openResource(rl, true);
    }

}
