package therealfarfetchd.qcommon.architect.client;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import therealfarfetchd.qcommon.architect.Architect;
import therealfarfetchd.qcommon.architect.loader.ModelLoader;
import therealfarfetchd.qcommon.architect.model.Model;

public abstract class ModelLoaderBase implements ICustomModelLoader {

    private Map<ResourceLocation, Model> models = new HashMap<>();

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        if (!(modelLocation instanceof ModelResourceLocation)) return false;

        if (!preFilterModel((ModelResourceLocation) modelLocation)) return false;

        ResourceLocation model = getModelPath(modelLocation);
        if (models.containsKey(model)) return true;

        boolean fileExists = false;
        try (InputStream ignored = Architect.proxy.openResource(model, true)) {
            if (ignored != null) fileExists = true;
        } catch (IOException ignored) { }

        return fileExists;
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) {
        ResourceLocation model = getModelPath(modelLocation);

        Model m = models.computeIfAbsent(model, ModelLoader.INSTANCE::load);

        if (m == null) {
            // TODO return error model
            throw new IllegalStateException("Failed to parse model");
        }

        return createModel((ModelResourceLocation) modelLocation, m);
    }

    protected abstract IModel createModel(ModelResourceLocation modelLocation, Model m);

    protected abstract boolean preFilterModel(ModelResourceLocation model);

    protected abstract ResourceLocation getModelPath(ResourceLocation object);

    @Override
    public void onResourceManagerReload(@Nonnull IResourceManager resourceManager) {
        models.clear();
    }

}
