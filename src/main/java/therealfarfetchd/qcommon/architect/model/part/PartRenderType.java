package therealfarfetchd.qcommon.architect.model.part;

import therealfarfetchd.qcommon.architect.model.part.ValueVariance.Type;

public enum PartRenderType {
    STATIC,
    FIXED_VARIANTS,
    DYNAMIC,
    NONE,
    ;

    public PartRenderType getRenderType(Part part) {
        RawPart raw = part.getRaw();

        if (raw == null) return FIXED_VARIANTS;

        if (raw.getFaces().isEmpty()) return NONE;

        final Type trType = raw.getTransformVariance().getType();
        if (trType == Type.DYNAMIC) return DYNAMIC;
        if (trType == Type.SINGLE) return STATIC;
        if (trType == Type.VARIANTS) return FIXED_VARIANTS;

        return DYNAMIC;
    }

}
