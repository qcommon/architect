package therealfarfetchd.qcommon.architect.factories.impl.part;

import net.minecraft.util.EnumFacing;

import java.util.EnumMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import therealfarfetchd.qcommon.architect.model.Face;
import therealfarfetchd.qcommon.architect.model.Part;
import therealfarfetchd.qcommon.architect.model.value.Value;
import therealfarfetchd.qcommon.croco.Vec3;

public class PartBox implements Part {

    private final EnumMap<EnumFacing, Value<BoxFace>> textures;
    private final Vec3 from, to;

    public PartBox(EnumMap<EnumFacing, Value<BoxFace>> textures, Vec3 from, Vec3 to) {
        this.textures = textures;
        this.from = from;
        this.to = to;
    }

    @Override
    public Value<List<Face>> getFaces() {
        return Value.extract(
            textures.entrySet().stream()
                .map(entry -> entry.getValue().map($1 -> $1.makeFace(entry.getKey(), from, to)))
                .collect(Collectors.toList())
        ).map(
            $ -> $.parallelStream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList())
        );
    }

}
