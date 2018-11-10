package therealfarfetchd.qcommon.architect.model.part;

import net.minecraft.util.EnumFacing;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import therealfarfetchd.qcommon.architect.factories.impl.part.BoxFace;
import therealfarfetchd.qcommon.architect.model.Face;
import therealfarfetchd.qcommon.croco.Vec3;

public class PartBox implements Part {

    private final Map<EnumFacing, BoxFace> textures;
    private final Vec3 from, to;

    public PartBox(Map<EnumFacing, BoxFace> textures, Vec3 from, Vec3 to) {
        this.textures = textures;
        this.from = from;
        this.to = to;
    }

    @Override
    public List<Face> getFaces() {
        return textures.entrySet().stream()
            .map(entry -> entry.getValue().makeFace(entry.getKey(), from, to))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    }

}
