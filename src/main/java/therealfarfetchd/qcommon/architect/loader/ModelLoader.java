package therealfarfetchd.qcommon.architect.loader;

import net.minecraft.util.Identifier;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import therealfarfetchd.qcommon.architect.factories.FactoryRegistry;
import therealfarfetchd.qcommon.architect.factories.ModelFactory;
import therealfarfetchd.qcommon.architect.model.EmptyModel;
import therealfarfetchd.qcommon.architect.model.Model;

public class ModelLoader extends GenLoaderJSON<Model> {

    public static final ModelLoader INSTANCE = new ModelLoader();

    @Override
    public Model load(ParseContext ctx, SourceFileInfo info, JsonObject json) {
        Identifier id = new Identifier("minecraft", "default");
        if (json.has("type")) {
            id = JsonParserUtils.parseGenStringStatic(ctx, json, "type", "a model type", s -> true, Identifier::new, id);
        }

        ModelFactory mf = FactoryRegistry.INSTANCE.getModelFactory(id);

        if (mf == null) {
            ctx.error(String.format("Invalid model type '%s'", id));
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
    public JsonObject loadSource(ParseContext ctx, Identifier rl) {
        return super.loadSource(ctx, rl);
    }

}
