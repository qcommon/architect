package therealfarfetchd.qcommon.architect.client;

import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;

import therealfarfetchd.qcommon.architect.model.Model;
import therealfarfetchd.qcommon.architect.model.value.VariantStateProvider;

public class BlockModelLoader extends ModelLoaderBase {

    public static final BlockModelLoader INSTANCE = new BlockModelLoader();

    @Override
    protected UnbakedModel createModel(ModelIdentifier modelLocation, Model m) {
        VariantStateProvider vsp = new VariantStateProvider(modelLocation.getVariant());

        return new BlockModel(vsp, m);
    }

    @Override
    protected boolean preFilterModel(ModelIdentifier model) {
        return !model.getVariant().equals("inventory"); // no items
    }

    @Override
    protected Identifier getModelPath(Identifier object) {
        return new Identifier(object.getNamespace(), String.format("render/block/%s.json", object.getPath()));
    }

}
