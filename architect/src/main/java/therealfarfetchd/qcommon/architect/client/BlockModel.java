package therealfarfetchd.qcommon.architect.client;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import therealfarfetchd.qcommon.architect.Architect;
import therealfarfetchd.qcommon.architect.model.Model;
import therealfarfetchd.qcommon.architect.model.Quad;

public class BlockModel implements IModel {

    private final Model model;

    public BlockModel(Model model) {
        this.model = model;
    }

    @Override
    public Collection<ResourceLocation> getTextures() {
        return Collections.singletonList(new ResourceLocation(Architect.MODID, "pablo")); // TODO
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        List<Quad> quadList = model.getParts().stream()
            .flatMap($ -> $.getFaces().stream())
            .flatMap($ -> $.toQuads().stream())
            .collect(Collectors.toList());

        return null;
    }



}
