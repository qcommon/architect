package therealfarfetchd.qcommon.architect.factories.impl.model;

import java.util.List;

import therealfarfetchd.qcommon.architect.model.Model;
import therealfarfetchd.qcommon.architect.model.ModelTransformMap;
import therealfarfetchd.qcommon.architect.model.part.Part;
import therealfarfetchd.qcommon.architect.model.texref.TextureMapper;
import therealfarfetchd.qcommon.architect.model.value.StateProvider;

public class DefaultModel implements Model {

    private final List<Part> parts;
    private final TextureMapper mapper;
    private final ModelTransformMap modelTransforms;

    public DefaultModel(List<Part> parts, TextureMapper mapper, ModelTransformMap modelTransforms) {
        this.parts = parts;
        this.mapper = mapper;
        this.modelTransforms = modelTransforms;
    }

    @Override
    public List<Part> getParts(StateProvider sp) {
        return parts;
    }

    @Override
    public TextureMapper getTextureMapper() {
        return mapper;
    }

    @Override
    public ModelTransformMap getModelTransforms() {
        return modelTransforms;
    }

    @Override
    public Model withModelTransforms(ModelTransformMap map) {
        return new DefaultModel(parts, mapper, map);
    }

}
