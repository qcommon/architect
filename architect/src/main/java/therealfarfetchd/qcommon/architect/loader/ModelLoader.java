package therealfarfetchd.qcommon.architect.loader;

import com.google.gson.JsonObject;

import net.minecraft.util.ResourceLocation;

import therealfarfetchd.qcommon.architect.factories.FactoryRegistry;
import therealfarfetchd.qcommon.architect.factories.ModelFactory;
import therealfarfetchd.qcommon.architect.model.EmptyModel;
import therealfarfetchd.qcommon.architect.model.Model;

public class ModelLoader extends GenLoader<Model> {

    public static final ModelLoader INSTANCE = new ModelLoader();

    @Override
    public Model load(ParseContext ctx, String fileName, JsonObject json) {
        ResourceLocation rl = new ResourceLocation("minecraft", "default");
        if (json.has("type")) {
            rl = JsonParserUtils.parseGenString(ctx, json, "type", "a model type", s -> true, ResourceLocation::new, rl);
        }

        ModelFactory mf = FactoryRegistry.INSTANCE.getModelFactory(rl);

        if (mf == null) {
            ctx.error(String.format("Invalid model type '%s'", rl));
            return EmptyModel.INSTANCE;
        }

        return mf.parse(ctx, json);
    }

    @Override
    protected Model getError() {
        return EmptyModel.INSTANCE;
    }

    @Override
    protected String getTypeName() {
        return "model";
    }

}
