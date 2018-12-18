package therealfarfetchd.qcommon.architect.client.dynrender;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloadListener;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import therealfarfetchd.qcommon.architect.Architect;
import therealfarfetchd.qcommon.architect.loader.ModelLoader;
import therealfarfetchd.qcommon.architect.model.Model;

public class DynModelLoader implements ResourceReloadListener {

    public static final DynModelLoader INSTANCE = new DynModelLoader();

    private Map<Block, DynRender> models = new HashMap<>();
    private Map<Block, Model> rawModels = new HashMap<>();

    @Override
    public void onResourceReload(ResourceManager rm) {
        models.values().forEach(DynRender::destroy);

        models.clear();
        rawModels.clear();

        for (Block block : Registry.BLOCK) {
            tryLoadModel(block);
        }
    }

    private void tryLoadModel(Block block) {
        Identifier id = Registry.BLOCK.getId(block);
        Identifier modelId = new Identifier(id.getNamespace(), String.format("render/block/%s.json", id.getPath()));

        if (Architect.proxy.resourceExists(modelId, true)) {
            rawModels.put(block, ModelLoader.INSTANCE.load(modelId));
        }
    }

    public boolean hasDynModel(Block block) {
        return rawModels.containsKey(block);
    }

    public boolean hasDynModel(BlockState state) {
        return hasDynModel(state.getBlock());
    }

    @Nullable
    public DynRender getDynModel(Block block) {
        return models.get(block);
    }

    @Nullable
    public DynRender getDynModel(BlockState state) {
        return getDynModel(state.getBlock());
    }

}
