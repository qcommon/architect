package therealfarfetchd.qcommon.architect.model.part;

import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.IntFunction;

import org.apache.commons.lang3.tuple.Pair;

import therealfarfetchd.qcommon.architect.factories.impl.part.BoxFace;
import therealfarfetchd.qcommon.architect.model.Face;
import therealfarfetchd.qcommon.architect.model.texref.TextureRef;
import therealfarfetchd.qcommon.architect.model.value.StateProvider;
import therealfarfetchd.qcommon.architect.model.value.Value;
import therealfarfetchd.qcommon.croco.Vec2;
import therealfarfetchd.qcommon.croco.Vec3;

public class PartBox implements Part {

    private final Value<Vec3> from;
    private final Value<Vec3> to;
    private final Value<Boolean>[] show;
    private final Value<TextureRef>[] textures;
    private final Value<Optional<Pair<Vec2, Vec2>>>[] uvs;

    private final Value<Face>[] faces;

    @SuppressWarnings({"unchecked"})
    public PartBox(Value<Vec3> from, Value<Vec3> to, BoxFace[] faces) {
        this(from, to,
            PartBox.<Value<Boolean>>fill(new Value[6], i -> faces[i].getShow()),
            PartBox.<Value<TextureRef>>fill(new Value[6], i -> faces[i].getTexture()),
            PartBox.<Value<Optional<Pair<Vec2, Vec2>>>>fill(new Value[6], i -> faces[i].getUVs())
        );
    }

    public PartBox(Value<Vec3> from, Value<Vec3> to, Value<Boolean>[] show, Value<TextureRef>[] textures, Value<Optional<Pair<Vec2, Vec2>>>[] uvs) {
        this.from = from;
        this.to = to;
        this.show = show;
        this.textures = textures;
        this.uvs = uvs;

        // noinspection unchecked
        this.faces = new Value[6];

        for (int i = 0; i < 6; i++) {
            int j = i;
            faces[i] = from.flatMap(from1 ->
                to.flatMap(to1 ->
                    uvs[j].map(uv ->
                        BoxFace.makeFace(Direction.byId(j), TextureRef.PLACEHOLDER, from1, to1, uv.map(Pair::getLeft).orElse(null), uv.map(Pair::getRight).orElse(null)))));
        }
    }

    @Override
    public List<Face> getFaces(StateProvider sp) {
        List<Face> result = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            if (!this.show[i].get(sp)) continue;

            result.add(faces[i].get(sp).withTexture(textures[i].get(sp)));
        }

        return result;
    }

    private static <T> T[] fill(T[] dest, IntFunction<T> mapper) {
        for (int i = 0; i < dest.length; i++) {
            dest[i] = mapper.apply(i);
        }

        return dest;
    }

}
