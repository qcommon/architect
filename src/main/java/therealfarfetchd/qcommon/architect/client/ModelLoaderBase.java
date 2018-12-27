package therealfarfetchd.qcommon.architect.client;

import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonParser;

import therealfarfetchd.qcommon.architect.Architect;
import therealfarfetchd.qcommon.architect.factories.impl.model.DefaultModel;
import therealfarfetchd.qcommon.architect.factories.impl.part.FactoryBox;
import therealfarfetchd.qcommon.architect.loader.ModelLoader;
import therealfarfetchd.qcommon.architect.loader.ParseContext;
import therealfarfetchd.qcommon.architect.model.Model;
import therealfarfetchd.qcommon.architect.model.ModelTransformMap;
import therealfarfetchd.qcommon.architect.model.part.Part;

public abstract class ModelLoaderBase implements CustomModelLoader {

    private Map<Identifier, Model> models = new HashMap<>();
    private Model errorModel;

    @Override
    public boolean accepts(Identifier modelLocation) {
        if (!(modelLocation instanceof ModelIdentifier)) return false;

        if (!preFilterModel((ModelIdentifier) modelLocation)) return false;

        Identifier model = getModelPath(modelLocation);
        if (models.containsKey(model)) return true;

        return Architect.proxy.resourceExists(model, true);
    }

    @Override
    public UnbakedModel loadModel(Identifier modelLocation) {
        long start = System.nanoTime();
        Identifier model = getModelPath(modelLocation);

        Model m = models.computeIfAbsent(model, ModelLoader.INSTANCE::load);

        if (m == null) {
            return createModel((ModelIdentifier) modelLocation, getErrorModel());
        }

        final UnbakedModel model1 = createModel((ModelIdentifier) modelLocation, m);

        long duration = System.nanoTime() - start;
        System.out.printf("Loading model '%s' took %dms.\n", modelLocation, duration / 1000000);

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
