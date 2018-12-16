package therealfarfetchd.qcommon.architect.loader;

import net.minecraft.util.Identifier;

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
            Identifier fallback = new Identifier("empty");
            Identifier id = JsonParserUtils.parseGenStringStatic(ctx, json, "parent", "a model location", s -> true, Identifier::new, fallback);
            if (id == fallback) return Model.EMPTY;
            Identifier fixed = new Identifier(id.getNamespace(), String.format("render/%s.json", id.getPath()));
            Model parent = load(fixed);
            return parent != null ? merge(ctx, info, json, parent) : Model.EMPTY;
        }

        Identifier id = new Identifier("minecraft", "default");
        if (json.has("type")) {
            id = JsonParserUtils.parseGenStringStatic(ctx, json, "type", "a model type", s -> true, Identifier::new, id);
        }

        ModelFactory mf = FactoryRegistry.INSTANCE.getModelFactory(id);

        if (mf == null) {
            ctx.error(String.format("Invalid model type '%s'", id));
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
    public JsonObject loadSource(ParseContext ctx, Identifier rl) {
        return super.loadSource(ctx, rl);
    }

}
