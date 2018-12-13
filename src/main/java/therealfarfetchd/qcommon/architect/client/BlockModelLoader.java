package therealfarfetchd.qcommon.architect.client;

import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import therealfarfetchd.qcommon.architect.Architect;
import therealfarfetchd.qcommon.architect.loader.ModelLoader;
import therealfarfetchd.qcommon.architect.model.Model;
import therealfarfetchd.qcommon.architect.model.value.VariantStateProvider;

public class BlockModelLoader implements CustomModelLoader {

    public static final BlockModelLoader INSTANCE = new BlockModelLoader();

    private Map<Identifier, Model> models = new HashMap<>();

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
            throw new IllegalStateException("Failed to parse model");
        }

        return new BlockModel(vsp, m);
    }

    @Override
    public void onResourceReload(ResourceManager var1) {
        models.clear();
    }

}
