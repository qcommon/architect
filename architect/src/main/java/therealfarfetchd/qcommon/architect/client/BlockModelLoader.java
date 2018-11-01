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

import therealfarfetchd.qcommon.architect.Architect;
import therealfarfetchd.qcommon.architect.loader.ModelLoader;
import therealfarfetchd.qcommon.architect.model.Model;

public class BlockModelLoader implements ICustomModelLoader {

    public static final BlockModelLoader INSTANCE = new BlockModelLoader();

    private Map<ResourceLocation, Model> models = new HashMap<>();

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        if (!(modelLocation instanceof ModelResourceLocation)) return false;

        if (((ModelResourceLocation) modelLocation).getVariant().equals("inventory")) return false; // no items

        ResourceLocation model = new ResourceLocation(modelLocation.getNamespace(), String.format("render/block/%s.json", modelLocation.getPath()));
        if (models.containsKey(model)) return true;

        boolean fileExists = false;
        try (InputStream ignored = Architect.proxy.openResource(model, true)) {
            fileExists = true;
        } catch (IOException ignored) { }

        return fileExists;
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) {
        ResourceLocation model = new ResourceLocation(modelLocation.getNamespace(), String.format("render/block/%s.json", modelLocation.getPath()));

        Model m = models.computeIfAbsent(model, ModelLoader.INSTANCE::load);

        return new BlockModel(m);
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        models.clear();
    }

}
