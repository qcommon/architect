package therealfarfetchd.qcommon.architect.client;

import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;

import therealfarfetchd.qcommon.architect.model.Model;
import therealfarfetchd.qcommon.architect.model.value.NullStateProvider;
import therealfarfetchd.qcommon.architect.model.value.StateProvider;

public class ItemModelLoader extends ModelLoaderBase {

    public static final ItemModelLoader INSTANCE = new ItemModelLoader();

    @Override
    protected UnbakedModel createModel(ModelIdentifier modelLocation, Model m) {
        StateProvider sp = NullStateProvider.INSTANCE;

        return new ItemModel(sp, m);
    }

    @Override
    protected boolean preFilterModel(ModelIdentifier model) {
        return model.getVariant().equals("inventory"); // items only
    }

    @Override
    protected Identifier getModelPath(Identifier object) {
        return new Identifier(object.getNamespace(), String.format("render/item/%s.json", object.getPath()));
    }

}
