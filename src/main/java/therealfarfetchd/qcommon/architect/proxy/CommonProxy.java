package therealfarfetchd.qcommon.architect.proxy;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.InputStream;

import javax.annotation.Nullable;

import therealfarfetchd.qcommon.architect.Architect;
import therealfarfetchd.qcommon.architect.factories.FactoryRegistry;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(this);
        FactoryRegistry.INSTANCE.readFactoryDefinitions();
    }

    public void postInit(FMLPostInitializationEvent e) { }

    @Nullable
    public InputStream openResource(ResourceLocation rl, boolean respectResourcePack) {
        return Architect.class.getClassLoader().getResourceAsStream(String.format("assets/%s/%s", rl.getNamespace(), rl.getPath()));
    }

    @Nullable
    public InputStream openResource(ResourceLocation rl) {
        return openResource(rl, true);
    }

}
