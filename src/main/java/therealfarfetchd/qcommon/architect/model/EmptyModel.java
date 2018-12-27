package therealfarfetchd.qcommon.architect.model;

import java.util.Collections;
import java.util.List;

import therealfarfetchd.qcommon.architect.model.part.Part;
import therealfarfetchd.qcommon.architect.model.texref.TextureMapper;
import therealfarfetchd.qcommon.architect.model.texref.TextureRef;
import therealfarfetchd.qcommon.architect.model.value.StateProvider;

public class EmptyModel implements Model {

    public static final TextureMapper EMPTY_MAPPER = unused -> TextureRef.PLACEHOLDER.texture;

    @Override
    public List<Part> getParts(StateProvider sp) {
        return Collections.emptyList();
    }

    @Override
    public TextureMapper getTextureMapper() {
        return EMPTY_MAPPER;
    }

    @Override
    public ModelTransformMap getModelTransforms() {
        return ModelTransformMap.IDENTITY;
    }

    @Override
    public Model withModelTransforms(ModelTransformMap map) {
        return this;
    }

}
