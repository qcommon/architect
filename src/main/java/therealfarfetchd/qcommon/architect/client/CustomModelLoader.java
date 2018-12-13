package therealfarfetchd.qcommon.architect.client;

import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.resource.ResourceReloadListener;
import net.minecraft.util.Identifier;

public interface CustomModelLoader extends ResourceReloadListener {

    CustomModelLoader[] LOADERS = {
        BlockModelLoader.INSTANCE
    };

    boolean accepts(Identifier id);

    UnbakedModel loadModel(Identifier id);

}
