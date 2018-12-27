package therealfarfetchd.qcommon.architect.model;

import java.util.List;

import therealfarfetchd.qcommon.architect.model.part.Part;
import therealfarfetchd.qcommon.architect.model.texref.TextureMapper;
import therealfarfetchd.qcommon.architect.model.value.StateProvider;

public interface Model {

    Model EMPTY = new EmptyModel();

    List<Part> getParts(StateProvider sp);

    TextureMapper getTextureMapper();

    ModelTransformMap getModelTransforms();

    Model withModelTransforms(ModelTransformMap map);

}
