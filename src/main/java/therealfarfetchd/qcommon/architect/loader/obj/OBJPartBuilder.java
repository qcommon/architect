package therealfarfetchd.qcommon.architect.loader.obj;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import therealfarfetchd.qcommon.architect.loader.obj.structs.OBJFace;
import therealfarfetchd.qcommon.architect.loader.obj.structs.OBJMaterial;
import therealfarfetchd.qcommon.architect.loader.obj.structs.OBJRoot;
import therealfarfetchd.qcommon.architect.model.Vertex;
import therealfarfetchd.qcommon.architect.model.part.Part;
import therealfarfetchd.qcommon.architect.model.part.PartOBJ;
import therealfarfetchd.qcommon.architect.model.value.Value;
import therealfarfetchd.qcommon.croco.Vec2;
import therealfarfetchd.qcommon.croco.Vec3;

public class OBJPartBuilder {

    private final OBJRoot root;

    private Map<String, MatDef> materials;

    private OBJPartBuilder(OBJRoot root) {
        this.root = root;

        materials = root.materials.entrySet().parallelStream().collect(Collectors.toMap(Entry::getKey, $ -> new MatDef($.getValue())));
    }

    public void setMatColor(String material, Function<Value<Color>, Value<Color>> mapper) {
        MatDef mat = materials.get(material);
        if (mat == null) return;
        mat.color = mapper.apply(mat.color);
    }

    public void setMatTexture(String material, Function<Value<Optional<String>>, Value<Optional<String>>> mapper) {
        MatDef mat = materials.get(material);
        if (mat == null) return;
        mat.tex = mapper.apply(mat.tex);
    }

    public void setMatTransparency(String material, Function<Value<Float>, Value<Float>> mapper) {
        MatDef mat = materials.get(material);
        if (mat == null) return;
        mat.alpha = mapper.apply(mat.alpha);
    }

    public Part build() {
        return new PartOBJ(materials);
    }

    public static OBJPartBuilder from(OBJRoot root) {
        return new OBJPartBuilder(root);
    }

    private static <T> T accessIndexed(int i, List<T> data) {
        if (i > 0) return data.get(i - 1);
        else if (i < 0) return data.get(data.size() + i);
        else throw new IllegalArgumentException("index must not be 0!");
    }

    private static PreparedFace process(OBJRoot data, OBJFace face) {
        OBJMaterial mat = data.materials.getOrDefault(face.material, OBJMaterial.FALLBACK);
        boolean[] noTex = {false};
        final List<Vertex> verts = face.vertices.parallelStream().map(vt -> {
            Vec3 xyz = accessIndexed(vt.xyz, data.vertPos);
            Vec2 uv;
            if (vt.tex != 0) {
                final Vec3 uv1 = accessIndexed(vt.tex, data.vertTex);
                uv = new Vec2(uv1.x, uv1.y);
            } else {
                uv = new Vec2(0.5f, 0.5f);
                noTex[0] = true;
            }
            return new Vertex(xyz, uv);
        }).collect(Collectors.toList());
        if (noTex[0]) mat = mat.withDiffuseTexture(null);

        return new PreparedFace(mat.getDiffuseTexture(), new Color(mat.getDiffuse().getRed(), mat.getDiffuse().getGreen(), mat.getDiffuse().getBlue(), (int) (mat.getTransparency() * 255f)), verts);
    }

    public static void get(OBJRoot data) {
        Map<String, Set<String>> groupToObjsMap = new HashMap<>();
        Map<String, List<PreparedFace>> objects = new HashMap<>();

        data.objects.forEach((obj, i) -> i.groups.forEach(group -> groupToObjsMap.computeIfAbsent(group, g -> new HashSet<>()).add(obj)));

        objects.put(null, data.faces.parallelStream().map(f -> process(data, f)).collect(Collectors.toList()));
        data.objects.forEach((obj, i) -> objects.put(obj, i.faces.parallelStream().map(f -> process(data, f)).collect(Collectors.toList())));
    }

    public static class PreparedFace {

        @Nullable public final String tex;
        public final Color rgba;
        public final List<Vertex> verts;

        public PreparedFace(@Nullable String tex, Color rgba, List<Vertex> verts) {
            this.tex = tex;
            this.rgba = rgba;
            this.verts = verts;
        }

    }

    public static class MatDef {

        public Value<Color> color;
        public Value<Optional<String>> tex;
        public Value<Float> alpha;

        public MatDef(OBJMaterial mat) {
            this.color = Value.wrap(mat.getDiffuse());
            this.tex = Value.wrap(Optional.ofNullable(mat.getDiffuseTexture()));
            this.alpha = Value.wrap(mat.getTransparency());
        }

    }

}
