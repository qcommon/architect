package therealfarfetchd.qcommon.architect.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.SimpleBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import therealfarfetchd.qcommon.architect.model.Model;
import therealfarfetchd.qcommon.architect.model.texref.TextureRef;
import therealfarfetchd.qcommon.architect.model.value.StateProvider;

public class ItemModel extends BaseModel {

    public ItemModel(StateProvider sp, Model model) {
        super(sp, model);
    }

    @Override
    public Collection<ResourceLocation> getTextures() {
        Set<ResourceLocation> requiredTextures = new HashSet<>();
        addModelTextures(requiredTextures);
        return requiredTextures;
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        Function<TextureRef, TextureAtlasSprite> mapper = tr -> bakedTextureGetter.apply(tr.getTexture(tm));
        TextureAtlasSprite particle = Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();

        Map<EnumFacing, List<BakedQuad>> quadsMap = getQuads(format, mapper);

        return new SimpleBakedModel(quadsMap.get(null), quadsMap, true, true, particle, ItemCameraTransforms.DEFAULT, ItemOverrideList.NONE);
    }

}
