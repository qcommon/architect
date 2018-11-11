package therealfarfetchd.qcommon.architect.loader.obj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import therealfarfetchd.qcommon.architect.loader.ParseContext;
import therealfarfetchd.qcommon.architect.loader.SourceFileInfo;
import therealfarfetchd.qcommon.architect.loader.obj.structs.OBJFace;
import therealfarfetchd.qcommon.architect.loader.obj.structs.OBJMaterial;
import therealfarfetchd.qcommon.architect.loader.obj.structs.OBJObject;
import therealfarfetchd.qcommon.architect.loader.obj.structs.OBJRoot;
import therealfarfetchd.qcommon.architect.loader.obj.structs.OBJVertex;
import therealfarfetchd.qcommon.croco.Vec3;

import static therealfarfetchd.qcommon.architect.loader.obj.ParserUtils.getRelativeResource;
import static therealfarfetchd.qcommon.architect.loader.obj.ParserUtils.readCustom;
import static therealfarfetchd.qcommon.architect.loader.obj.ParserUtils.readFloats;

public class OBJLoader extends OBJLoaderBase<OBJRoot> {

    public static final OBJLoader INSTANCE = new OBJLoader();

    @Override
    public OBJRoot load(ParseContext ctx, SourceFileInfo info, List<String> source) {
        Map<String, OBJMaterial> materials = new HashMap<>();
        List<Vec3> vertPos = new ArrayList<>();
        List<Vec3> vertTex = new ArrayList<>();
        List<Vec3> vertNormal = new ArrayList<>();
        List<OBJFace> faces = new ArrayList<>();
        Map<String, OBJObject> objects = new HashMap<>();

        String activeMaterial = null;

        String objname = null;
        List<String> objgroups = new ArrayList<>();
        List<OBJFace> currentFaces = new ArrayList<>();

        for (String it : source) {
            it = regex.matcher(it).replaceAll("").trim();
            if (it.isEmpty()) continue;
            final String[] s = it.split("\\s");
            if (s.length < 1) continue;
            String cmd = s[0];

            switch (cmd) {
                case "mtllib": {
                    Map<String, OBJMaterial> m = MTLLoader.INSTANCE.load(getRelativeResource(info.path, readCustom(it, 1, 1, 0).get(0)));
                    if (m != null) materials.putAll(m);
                    break;
                }
                case "usemtl": {
                    activeMaterial = readCustom(it, 1, 1, 0).get(0);
                    break;
                }
                case "o": {
                    if (objname != null) {
                        objects.put(objname, new OBJObject(objgroups, currentFaces));
                    }
                    objname = readCustom(it, 1, 1, 0).get(0);
                    objgroups = new ArrayList<>();
                    currentFaces = new ArrayList<>();
                    break;
                }
                case "g": {
                    if (objname == null) ctx.warn("No active object, ignoring group definition");
                    else objgroups.addAll(readCustom(it, 1, 1, -1));
                    break;
                }
                case "v": {
                    List<Float> c = readFloats(it, 1, 3, 1);
                    switch (c.size()) {
                        case 3:
                            vertPos.add(new Vec3(c.get(0), c.get(1), c.get(2)));
                            break;
                        case 4:
                            final Float w = c.get(3);
                            vertPos.add(new Vec3(c.get(0) / w, c.get(1) / w, c.get(2) / w));
                            break;
                    }
                    break;
                }
                case "vt": {
                    List<Float> c = readFloats(it, 1, 2, 1);
                    vertTex.add(new Vec3(c.get(0), c.get(1), c.size() > 2 ? c.get(2) : 0));
                    break;
                }
                case "vn": {
                    List<Float> c = readFloats(it, 1, 3, 0);
                    vertNormal.add(new Vec3(c.get(0), c.get(1), c.get(2)));
                    break;
                }
                case "f": {
                    currentFaces.add(new OBJFace(activeMaterial, readCustom(it, 1, 3, -1).stream().map(OBJLoader::parseVertex).collect(Collectors.toList())));
                    break;
                }
                default: {
                    ctx.warn(String.format("Unrecognized OBJ statement '%s'", it));
                }
            }
        }

        if (objname != null) objects.put(objname, new OBJObject(objgroups, currentFaces));
        else faces.addAll(currentFaces);

        return new OBJRoot(materials, vertPos, vertTex, vertNormal, faces, objects);
    }

    @Override
    protected OBJRoot getError() {
        return OBJRoot.EMPTY;
    }

    @Override
    protected String getTypeName() {
        return "obj model";
    }

    private static OBJVertex parseVertex(String s) {
        final List<Integer> c = Stream.of(s.split("/")).map(str -> {
            try {
                return Integer.parseInt(str);
            } catch (NumberFormatException e) {
                return 0;
            }
        }).collect(Collectors.toList());
        return new OBJVertex(c.get(0), c.get(1), c.get(2));
    }

}
