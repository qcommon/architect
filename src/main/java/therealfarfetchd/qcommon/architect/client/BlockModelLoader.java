package therealfarfetchd.qcommon.architect.client;

import com.google.gson.JsonParser;

import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import therealfarfetchd.qcommon.architect.Architect;
import therealfarfetchd.qcommon.architect.factories.impl.model.DefaultModel;
import therealfarfetchd.qcommon.architect.factories.impl.part.FactoryBox;
import therealfarfetchd.qcommon.architect.loader.ModelLoader;
import therealfarfetchd.qcommon.architect.loader.ParseContext;
import therealfarfetchd.qcommon.architect.model.Model;
import therealfarfetchd.qcommon.architect.model.part.Part;
import therealfarfetchd.qcommon.architect.model.value.Value;
import therealfarfetchd.qcommon.architect.model.value.VariantStateProvider;

public class BlockModelLoader implements CustomModelLoader {

    public static final BlockModelLoader INSTANCE = new BlockModelLoader();

    private Map<Identifier, Model> models = new HashMap<>();

    @Nullable private Model errorModel;

    @Override
    public boolean accepts(Identifier id) {
        if (!(id instanceof ModelIdentifier)) return false;

        if (((ModelIdentifier) id).getVariant().equals("inventory")) return false; // no items

        Identifier model = new Identifier(id.getNamespace(), String.format("render/block/%s.json", id.getPath()));
        if (models.containsKey(model)) return true;

        boolean fileExists = false;
        try (InputStream ignored = Architect.proxy.openResource(model, true)) {
            if (ignored != null) fileExists = true;
        } catch (IOException ignored) { }

        return fileExists;
    }

    @Override
    public UnbakedModel loadModel(Identifier id) {
        Identifier model = new Identifier(id.getNamespace(), String.format("render/block/%s.json", id.getPath()));
        VariantStateProvider vsp = new VariantStateProvider(((ModelIdentifier) id).getVariant());

        Model m = models.computeIfAbsent(model, ModelLoader.INSTANCE::load);

        if (m == null) {
            // TODO return error model
            return new BlockModel(vsp, getErrorModel());
        }

        return new BlockModel(vsp, m);
    }

    private Model getErrorModel() {
        if (errorModel == null) {
            final Identifier errorTex = new Identifier(Architect.MODID, "error");
            Part box = new FactoryBox()
                .parse(new ParseContext("temp"), new JsonParser().parse("{\"faces\":{\"all\":{\"texture\":\"#error\"}}}").getAsJsonObject())
                .getPossibleValues()
                .iterator().next();
            errorModel = new DefaultModel(Value.wrap(Collections.singletonList(box)), Value.wrap(key -> errorTex));
        }
        return errorModel;
    }

    @Override
    public void onResourceReload(ResourceManager var1) {
        models.clear();
        errorModel = null;
    }

}
