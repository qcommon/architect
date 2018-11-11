package therealfarfetchd.qcommon.architect.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nullable;

import therealfarfetchd.qcommon.architect.client.BlockModelLoader;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);

        ModelLoaderRegistry.registerLoader(BlockModelLoader.INSTANCE);
    }

    @Nullable
    @Override
    public InputStream openResource(ResourceLocation rl, boolean respectResourcePack) {
        if (!respectResourcePack) return super.openResource(rl, respectResourcePack);

        try {
            return Minecraft.getMinecraft().getResourceManager().getResource(rl).getInputStream();
        } catch (IOException e) {
            return null;
        }
    }

}
