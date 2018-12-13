package therealfarfetchd.qcommon.architect.loader.obj;

import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class ParserUtils {

    public static Identifier getRelativeResource(Identifier base, String rpath) {
        if (rpath.contains(":")) {
            return new Identifier(rpath);
        } else if (rpath.startsWith("/")) {
            return new Identifier(base.getNamespace(), rpath.replaceAll("^/+", ""));
        } else {
            return new Identifier(base.getNamespace(), base.getPath().replaceAll("[^/]+$", rpath));
        }
    }

    public static List<String> readCustom(String s, int ignore, int m, int o) {
        List<String> components = Arrays.asList(s.split(" "));
        components = components.subList(ignore, components.size());
        if ((o >= 0 && components.size() > m + o) || components.size() < m) {
            throw new IllegalStateException(String.format("Invalid number of parameters! Expected %d mandatory, %d optional, got %d. Line: '%s'", m, o, components.size(), s));
        }
        return components;
    }

    public static List<Float> readFloats(String s, int ignore, int m, int o) {
        return readCustom(s, ignore, m, o).stream().map(Float::parseFloat).collect(Collectors.toList());
    }

}
