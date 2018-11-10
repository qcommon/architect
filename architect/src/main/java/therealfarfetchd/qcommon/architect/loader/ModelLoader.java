package therealfarfetchd.qcommon.architect.loader;

import com.google.gson.JsonObject;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

import therealfarfetchd.qcommon.architect.factories.FactoryRegistry;
import therealfarfetchd.qcommon.architect.factories.ModelFactory;
import therealfarfetchd.qcommon.architect.model.EmptyModel;
import therealfarfetchd.qcommon.architect.model.Model;

public class ModelLoader extends GenLoaderJSON<Model> {

    public static final ModelLoader INSTANCE = new ModelLoader();

    @Override
    public Model load(ParseContext ctx, SourceFileInfo info, JsonObject json) {
        ResourceLocation rl = new ResourceLocation("minecraft", "default");
        if (json.has("type")) {
            rl = JsonParserUtils.parseGenStringStatic(ctx, json, "type", "a model type", s -> true, ResourceLocation::new, rl);
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

    @Nullable
    @Override
    public JsonObject loadSource(ParseContext ctx, ResourceLocation rl) {
        return super.loadSource(ctx, rl);
    }

}
