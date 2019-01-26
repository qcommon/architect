package therealfarfetchd.qcommon.architect.client;

import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloadListener;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelVariantProvider;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.gson.JsonParser;

import therealfarfetchd.qcommon.architect.Architect;
import therealfarfetchd.qcommon.architect.factories.impl.model.DefaultModel;
import therealfarfetchd.qcommon.architect.factories.impl.part.FactoryBox;
import therealfarfetchd.qcommon.architect.loader.ModelLoader;
import therealfarfetchd.qcommon.architect.loader.ParseContext;
import therealfarfetchd.qcommon.architect.model.Model;
import therealfarfetchd.qcommon.architect.model.ModelTransformMap;
import therealfarfetchd.qcommon.architect.model.part.Part;

public abstract class ModelLoaderBase implements ModelVariantProvider, ResourceReloadListener {

    private Map<Identifier, Model> models = new HashMap<>();
    private Model errorModel;

    @Override
    @Nullable
    public UnbakedModel loadModelVariant(ModelIdentifier modelIdentifier, ModelProviderContext modelProviderContext) {
        if (!preFilterModel(modelIdentifier)) return null;

        Identifier model = getModelPath(modelIdentifier);

        if (!models.containsKey(model) && !Architect.proxy.resourceExists(model, true)) return null;

        long start = System.nanoTime();

        Model m = models.computeIfAbsent(model, ModelLoader.INSTANCE::load);

        if (m == null) {
            return createModel(modelIdentifier, getErrorModel());
        }

        final UnbakedModel model1 = createModel(modelIdentifier, m);

        long duration = System.nanoTime() - start;
        System.out.printf("Loading model '%s' took %dms.\n", modelIdentifier, duration / 1000000);

        return model1;
    }

    private Model getErrorModel() {
        if (errorModel == null) {
            final Identifier errorTex = new Identifier(Architect.MODID, "error");
            Part box = FactoryBox.INSTANCE.parse(ParseContext.create("temp"), new JsonParser().parse("{\"faces\":{\"all\":{\"texture\":\"#error\"}}}").getAsJsonObject());
            errorModel = new DefaultModel(Collections.singletonList(box), key -> errorTex, ModelTransformMap.IDENTITY);
        }
        return errorModel;
    }

    protected abstract UnbakedModel createModel(ModelIdentifier modelLocation, Model m);

    protected abstract boolean preFilterModel(ModelIdentifier model);

    protected abstract Identifier getModelPath(Identifier object);

    @Override
    public void onResourceReload(ResourceManager var1) {
        models.clear();
    }

}
