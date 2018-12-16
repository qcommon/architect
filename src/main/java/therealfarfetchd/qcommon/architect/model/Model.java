package therealfarfetchd.qcommon.architect.model;

import java.util.List;

import therealfarfetchd.qcommon.architect.model.part.Part;
import therealfarfetchd.qcommon.architect.model.texref.TextureMapper;
import therealfarfetchd.qcommon.architect.model.value.Value;

public interface Model {

    Model EMPTY = new EmptyModel();

    Value<List<Part>> getParts();

    Value<TextureMapper> getTextureMapper();


}
