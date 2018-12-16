package therealfarfetchd.qcommon.architect.client;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;

import therealfarfetchd.qcommon.architect.model.Model;
import therealfarfetchd.qcommon.architect.model.value.VariantStateProvider;

public class ItemModelLoader extends ModelLoaderBase {

    public static final ItemModelLoader INSTANCE = new ItemModelLoader();

    @Override
    protected IModel createModel(ModelResourceLocation modelLocation, Model m) {
        VariantStateProvider vsp = new VariantStateProvider(modelLocation.getVariant());

        return new ItemModel(vsp, m);
    }

    @Override
    protected boolean preFilterModel(ModelResourceLocation model) {
        return model.getPath().matches("item/\\w+"); // items only
    }

    @Override
    protected ResourceLocation getModelPath(ResourceLocation object) {
        return new ResourceLocation(object.getNamespace(), String.format("render/item/%s.json", object.getPath().substring(5)));
    }

}
