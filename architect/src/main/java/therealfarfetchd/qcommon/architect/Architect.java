package therealfarfetchd.qcommon.architect;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import therealfarfetchd.qcommon.architect.loader.ModelLoader;
import therealfarfetchd.qcommon.architect.model.Model;
import therealfarfetchd.qcommon.architect.proxy.CommonProxy;

@Mod(modid = Architect.MODID, useMetadata = true)
public class Architect {

    public static final String MODID = "qcommon-architect";

    @Mod.Instance public static Architect INSTANCE;

    @SidedProxy(
        clientSide = "therealfarfetchd.qcommon.architect.proxy.ClientProxy",
        serverSide = "therealfarfetchd.qcommon.architect.proxy.CommonProxy"
    )
    public static CommonProxy proxy;

    public Logger logger = LogManager.getLogger(MODID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        proxy.preInit(e);
        Model m = ModelLoader.INSTANCE.load(new ResourceLocation("qcommon-architect", "render/block/testblock.json"));
    }

}
