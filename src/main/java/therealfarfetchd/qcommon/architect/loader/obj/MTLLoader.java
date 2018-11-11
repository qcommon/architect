package therealfarfetchd.qcommon.architect.loader.obj;

import java.awt.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import therealfarfetchd.qcommon.architect.Architect;
import therealfarfetchd.qcommon.architect.loader.ParseContext;
import therealfarfetchd.qcommon.architect.loader.SourceFileInfo;
import therealfarfetchd.qcommon.architect.loader.obj.structs.OBJMaterial;

import static therealfarfetchd.qcommon.architect.loader.obj.ParserUtils.readCustom;
import static therealfarfetchd.qcommon.architect.loader.obj.ParserUtils.readFloats;

public class MTLLoader extends OBJLoaderBase<Map<String, OBJMaterial>> {

    public static MTLLoader INSTANCE = new MTLLoader();

    @Override
    public Map<String, OBJMaterial> load(ParseContext ctx, SourceFileInfo info, List<String> source) {
        Map<String, OBJMaterial> mats = new HashMap<>();

        String name = "";
        OBJMaterial mat = null;

        for (String it : source) {
            it = regex.matcher(it).replaceAll("").trim();
            if (it.isEmpty()) continue;
            final String[] s = it.split("\\s");
            if (s.length < 1) continue;
            String cmd = s[0];

            if ("newmtl".equals(cmd)) {
                if (mat != null) {
                    mats.put(name, mat);
                }
                mat = OBJMaterial.FALLBACK;
                name = readCustom(it, 1, 1, 0).get(0);
                continue;
            }

            if (mat == null) {
                ctx.warn(String.format("Ignoring line '%s' because no material is currently being defined", it));
                continue;
            }

            switch (cmd) {
                case "Kd": {
                    List<Float> c = readFloats(it, 1, 3, 0);
                    mat = mat.withDiffuse(new Color(c.get(0), c.get(1), c.get(2)));
                    break;
                }
                case "d": {
                    mat = mat.withTransparency(readFloats(it, 1, 1, 0).get(0));
                    break;
                }
                case "Tr": {
                    mat = mat.withTransparency(1 - readFloats(it, 1, 1, 0).get(0));
                    break;
                }
                case "map_Kd": {
                    mat = mat.withDiffuseTexture(readCustom(it, 1, 1, 0).get(0));
                    break;
                }
                default: {
                    ctx.warn(String.format("Unrecognized/unimplemented MTL statement '%s'", it));
                }
            }

        }

        if (mat != null) {
            mats.put(name, mat);
        }

        return mats;
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
