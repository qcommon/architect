package therealfarfetchd.qcommon.architect.factories.impl.model;

import java.util.List;

import therealfarfetchd.qcommon.architect.model.Model;
import therealfarfetchd.qcommon.architect.model.part.Part;
import therealfarfetchd.qcommon.architect.model.texref.TextureMapper;
import therealfarfetchd.qcommon.architect.model.value.StateProvider;
import therealfarfetchd.qcommon.architect.model.value.Value;

public class DefaultModel implements Model {

    private final List<Part> parts;
    private final Value<TextureMapper> mapper;

    public DefaultModel(List<Part> parts, Value<TextureMapper> mapper) {
        this.parts = parts;
        this.mapper = mapper;
    }

    @Override
    public List<Part> getParts(StateProvider sp) {
        return parts;
    }

    @Override
    public Value<TextureMapper> getTextureMapper() {
        return mapper;
    }

}
