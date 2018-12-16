package therealfarfetchd.qcommon.architect.client;

import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;

import therealfarfetchd.qcommon.architect.model.Model;
import therealfarfetchd.qcommon.architect.model.value.VariantStateProvider;

public class ItemModelLoader extends ModelLoaderBase {

    public static final ItemModelLoader INSTANCE = new ItemModelLoader();

    @Override
    protected UnbakedModel createModel(ModelIdentifier modelLocation, Model m) {
        VariantStateProvider vsp = new VariantStateProvider(modelLocation.getVariant());

        return new ItemModel(vsp, m);
    }

    @Override
    protected boolean preFilterModel(ModelIdentifier model) {
        return model.getPath().matches("item/\\w+"); // no special prefix
    }

    @Override
    protected Identifier getModelPath(Identifier object) {
        return new Identifier(object.getNamespace(), String.format("render/%s.json", object.getPath()));
    }

}
