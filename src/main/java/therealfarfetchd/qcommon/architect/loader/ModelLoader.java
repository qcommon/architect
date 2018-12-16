package therealfarfetchd.qcommon.architect.loader;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import therealfarfetchd.qcommon.architect.factories.FactoryRegistry;
import therealfarfetchd.qcommon.architect.factories.ModelFactory;
import therealfarfetchd.qcommon.architect.model.Model;

public class ModelLoader extends GenLoaderJSON<Model> {

    public static final ModelLoader INSTANCE = new ModelLoader();

    @Override
    public Model load(ParseContext ctx, SourceFileInfo info, JsonObject json) {
        if (json.has("parent")) {
            ResourceLocation fallback = new ResourceLocation("empty");
            ResourceLocation rl = JsonParserUtils.parseGenStringStatic(ctx, json, "parent", "a model location", s -> true, ResourceLocation::new, fallback);
            if (rl == fallback) return Model.EMPTY;
            ResourceLocation fixed = new ResourceLocation(rl.getNamespace(), String.format("render/%s.json", rl.getPath()));
            Model parent = load(fixed);
            return parent != null ? merge(ctx, info, json, parent) : Model.EMPTY;
        }

        ResourceLocation rl = new ResourceLocation("minecraft", "default");
        if (json.has("type")) {
            rl = JsonParserUtils.parseGenStringStatic(ctx, json, "type", "a model type", s -> true, ResourceLocation::new, rl);
        }

        ModelFactory mf = FactoryRegistry.INSTANCE.getModelFactory(rl);

        if (mf == null) {
            ctx.error(String.format("Invalid model type '%s'", rl));
            return Model.EMPTY;
        }

        return mf.parse(ctx, json);
    }

    public Model merge(ParseContext ctx, SourceFileInfo info, JsonObject json, Model parent) {
        // TODO
        return parent;
    }

    @Override
    protected Model getError() {
        return Model.EMPTY;
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
