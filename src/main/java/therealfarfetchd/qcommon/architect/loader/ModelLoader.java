package therealfarfetchd.qcommon.architect.loader;

import net.minecraft.util.Identifier;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import therealfarfetchd.qcommon.architect.factories.FactoryRegistry;
import therealfarfetchd.qcommon.architect.factories.ModelFactory;
import therealfarfetchd.qcommon.architect.factories.impl.model.FactoryModelEmpty;
import therealfarfetchd.qcommon.architect.model.Model;

public class ModelLoader extends GenLoaderJSON<Model> {

    public static final ModelLoader INSTANCE = new ModelLoader();

    @Override
    public Model load(ParseMessageContainer log, SourceFileInfo info, JsonObject json) {
        ParseContext ctx = ParseContext.wrap(log);

        if (json.has("scale")) {
            ctx = ctx.withScale(ctx.dp.parseFloatStatic(ctx.log, json, "scale", ctx.posScale));
        }

        if (json.has("parent")) {
            Identifier fallback = new Identifier("empty");
            Identifier id = ctx.dp.parseGenStringStatic(log, json, "parent", "a model location", s -> true, Identifier::new, fallback);
            if (id == fallback) return Model.EMPTY;
            Identifier fixed = new Identifier(id.getNamespace(), String.format("render/%s.json", id.getPath()));
            JsonObject parentData = loadSource(ctx.log, fixed);
            Model parent = load(fixed);
            if (parentData == null || parent == null) return Model.EMPTY;
            return getFactory(ctx, parentData).merge(ctx, json, parent);
        }

        return getFactory(ctx, json).parse(ctx, json);
    }

    private ModelFactory getFactory(ParseContext ctx, JsonObject root) {
        Identifier id = new Identifier("minecraft", "default");
        if (root.has("type")) {
            id = ctx.dp.parseGenStringStatic(ctx.log, root, "type", "a model type", s -> true, Identifier::new, id);
        }

        ModelFactory mf = FactoryRegistry.INSTANCE.getModelFactory(id);

        if (mf == null) {
            ctx.log.error(String.format("Invalid model type '%s'", id));
            return new FactoryModelEmpty();
        }

        return mf;
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
    public JsonObject loadSource(ParseMessageContainer log, Identifier rl) {
        return super.loadSource(log, rl);
    }

}
