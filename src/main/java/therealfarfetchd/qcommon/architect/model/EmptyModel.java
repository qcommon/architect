package therealfarfetchd.qcommon.architect.model;

import java.util.Collections;
import java.util.List;

import therealfarfetchd.qcommon.architect.model.part.Part;
import therealfarfetchd.qcommon.architect.model.texref.TextureMapper;
import therealfarfetchd.qcommon.architect.model.texref.TextureRef;
import therealfarfetchd.qcommon.architect.model.value.Value;

public class EmptyModel implements Model {

    public static final Value<TextureMapper> EMPTY_MAPPER = Value.wrap(unused -> TextureRef.PLACEHOLDER.texture);

    @Override
    public Value<List<Part>> getParts() {
        return Value.wrap(Collections.emptyList());
    }

    @Override
    public Value<TextureMapper> getTextureMapper() {
        return EMPTY_MAPPER;
    }

}
