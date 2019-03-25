package therealfarfetchd.qcommon.architect;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.loader.api.FabricLoader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import therealfarfetchd.qcommon.architect.client.BlockModelLoader;
import therealfarfetchd.qcommon.architect.client.ItemModelLoader;
import therealfarfetchd.qcommon.architect.factories.FactoryRegistry;
import therealfarfetchd.qcommon.architect.proxy.ClientProxy;
import therealfarfetchd.qcommon.architect.proxy.CommonProxy;

public class Architect implements ModInitializer {

    public static final String MODID = "qcommon-architect";

    public static Architect INSTANCE;

    public static CommonProxy proxy;

    public Logger logger = LogManager.getLogger(MODID);

    @Override
    public void onInitialize() {
//        ShaderBuilder.test();
        INSTANCE = this;
        switch (FabricLoader.getInstance().getEnvironmentType()) {
            case CLIENT:
                proxy = new ClientProxy();
                break;
            case SERVER:
                proxy = new CommonProxy();
                break;
        }

        ModelLoadingRegistry.INSTANCE.registerVariantProvider(unused -> BlockModelLoader.INSTANCE);
        ModelLoadingRegistry.INSTANCE.registerVariantProvider(unused -> ItemModelLoader.INSTANCE);
    }

    public void onGameInit() {
        FactoryRegistry.INSTANCE.readFactoryDefinitions();

        proxy.registerModelLoader();
    }

}
