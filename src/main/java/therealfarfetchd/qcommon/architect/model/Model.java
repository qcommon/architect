package therealfarfetchd.qcommon.architect.model;

import java.util.List;

import therealfarfetchd.qcommon.architect.model.part.Part;
import therealfarfetchd.qcommon.architect.model.texref.TextureMapper;
import therealfarfetchd.qcommon.architect.model.value.StateProvider;
import therealfarfetchd.qcommon.architect.model.value.Value;

public interface Model {

    Model EMPTY = new EmptyModel();

    List<Part> getParts(StateProvider sp);

    Value<TextureMapper> getTextureMapper();

}
