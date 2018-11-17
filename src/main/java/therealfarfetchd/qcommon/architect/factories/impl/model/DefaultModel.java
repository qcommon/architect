package therealfarfetchd.qcommon.architect.factories.impl.model;

import java.util.List;

import therealfarfetchd.qcommon.architect.model.Model;
import therealfarfetchd.qcommon.architect.model.part.Part;
import therealfarfetchd.qcommon.architect.model.texref.TextureMapper;
import therealfarfetchd.qcommon.architect.model.value.Value;

public class DefaultModel implements Model {

    private final Value<List<Part>> parts;
    private final Value<TextureMapper> mapper;

    public DefaultModel(Value<List<Part>> parts, Value<TextureMapper> mapper) {
        this.parts = parts;
        this.mapper = mapper;
    }

    @Override
    public Value<List<Part>> getParts() {
        return parts;
    }

    @Override
    public Value<TextureMapper> getTextureMapper() {
        return mapper;
    }

}
