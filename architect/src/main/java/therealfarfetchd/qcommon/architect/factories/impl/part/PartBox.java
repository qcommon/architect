package therealfarfetchd.qcommon.architect.factories.impl.part;

import net.minecraft.util.EnumFacing;

import java.util.EnumMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import therealfarfetchd.qcommon.architect.math.Vec3;
import therealfarfetchd.qcommon.architect.model.Face;
import therealfarfetchd.qcommon.architect.model.Part;

public class PartBox implements Part {

    private final EnumMap<EnumFacing, BoxFace> textures;
    private final Vec3 from, to;

    public PartBox(EnumMap<EnumFacing, BoxFace> textures, Vec3 from, Vec3 to) {
        this.textures = textures;
        this.from = from;
        this.to = to;
    }

    @Override
    public List<Face> getFaces() {
        return textures.entrySet().stream()
            .map($ -> $.getValue().makeFace($.getKey(), from, to))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    }

}
