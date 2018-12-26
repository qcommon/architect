package therealfarfetchd.qcommon.architect.loader.obj;

import java.awt.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import therealfarfetchd.qcommon.architect.loader.ParseMessageContainer;
import therealfarfetchd.qcommon.architect.loader.SourceFileInfo;
import therealfarfetchd.qcommon.architect.loader.obj.structs.OBJMaterial;
import therealfarfetchd.qcommon.architect.loader.obj.structs.OBJMaterialImpl;

import static therealfarfetchd.qcommon.architect.loader.obj.ParserUtils.readCustom;
import static therealfarfetchd.qcommon.architect.loader.obj.ParserUtils.readFloats;

public class MTLLoader extends OBJLoaderBase<Map<String, OBJMaterial>> {

    public static MTLLoader INSTANCE = new MTLLoader();

    @Override
    public Map<String, OBJMaterial> load(ParseMessageContainer log, SourceFileInfo info, List<String> source) {
        Map<String, OBJMaterialImpl.Mutable> mats = new HashMap<>();

        String name = "";
        OBJMaterial.Mutable mat = null;

        for (String it : source) {
            int comment = it.indexOf('#');
            if (comment != -1) it = it.substring(0, comment).trim();
            if (it.isEmpty()) continue;
            final String[] s = it.split("\\s");
            if (s.length < 1) continue;
            String cmd = s[0];

            if ("newmtl".equals(cmd)) {
                if (mat != null) {
                    mats.put(name, mat);
                }
                mat = OBJMaterial.FALLBACK.copy();
                name = readCustom(it, 1, 1, 0).get(0);
                continue;
            }

            if (mat == null) {
                log.warn(String.format("Ignoring line '%s' because no material is currently being defined", it));
                continue;
            }

            switch (cmd) {
                case "Kd": {
                    List<Float> c = readFloats(it, 1, 3, 0);
                    mat.setDiffuse(new Color(c.get(0), c.get(1), c.get(2)));
                    break;
                }
                case "d": {
                    mat.setTransparency(readFloats(it, 1, 1, 0).get(0));
                    break;
                }
                case "Tr": {
                    mat.setTransparency(1 - readFloats(it, 1, 1, 0).get(0));
                    break;
                }
                case "map_Kd": {
                    mat.setDiffuseTexture(readCustom(it, 1, 1, 0).get(0));
                    break;
                }
                default: {
                    log.warn(String.format("Unrecognized/unimplemented MTL statement '%s'", it));
                }
            }

        }

        if (mat != null) {
            mats.put(name, mat);
        }

        // noinspection unchecked
        return (Map<String, OBJMaterial>) (Map) mats;
    }

    @Override
    protected Map<String, OBJMaterial> getError() {
        return Collections.emptyMap();
    }

    @Override
    protected String getTypeName() {
        return "material library";
    }

}
