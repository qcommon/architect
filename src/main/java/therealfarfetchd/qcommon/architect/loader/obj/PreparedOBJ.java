package therealfarfetchd.qcommon.architect.loader.obj;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import therealfarfetchd.qcommon.architect.loader.obj.structs.OBJFace;
import therealfarfetchd.qcommon.architect.loader.obj.structs.OBJMaterial;
import therealfarfetchd.qcommon.architect.loader.obj.structs.OBJRoot;
import therealfarfetchd.qcommon.architect.model.Vertex;
import therealfarfetchd.qcommon.croco.Vec2;
import therealfarfetchd.qcommon.croco.Vec3;

public class PreparedOBJ {

    public final Map<String, Set<String>> groupToObjsMap;
    public final Map<String, List<PreparedFace>> objects;

    private PreparedOBJ(Map<String, Set<String>> groupToObjsMap, Map<String, List<PreparedFace>> objects) {
        this.groupToObjsMap = groupToObjsMap;
        this.objects = objects;
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

        return new PreparedFace(mat.diffuseTexture, new Color(mat.diffuse.getRed(), mat.diffuse.getGreen(), mat.diffuse.getBlue(), (int) (mat.transparency * 255f)), verts);
    }

    public static PreparedOBJ get(OBJRoot data) {
        Map<String, Set<String>> groupToObjsMap = new HashMap<>();
        Map<String, List<PreparedFace>> objects = new HashMap<>();

        data.objects.forEach((obj, i) -> i.groups.forEach(group -> groupToObjsMap.computeIfAbsent(group, g -> new HashSet<>()).add(obj)));

        objects.put(null, data.faces.parallelStream().map(f -> process(data, f)).collect(Collectors.toList()));
        data.objects.forEach((obj, i) -> objects.put(obj, i.faces.parallelStream().map(f -> process(data, f)).collect(Collectors.toList())));

        return new PreparedOBJ(groupToObjsMap, objects); // TODO cache
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

}
