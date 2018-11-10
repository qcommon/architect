package therealfarfetchd.qcommon.architect.loader.obj;

import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class ParserUtils {

    public static ResourceLocation getRelativeResource(ResourceLocation base, String rpath) {
        if (rpath.contains(":")) {
            return new ResourceLocation(rpath);
        } else if (rpath.startsWith("/")) {
            return new ResourceLocation(base.getNamespace(), rpath.replaceAll("^/+", ""));
        } else {
            return new ResourceLocation(base.getNamespace(), base.getPath().replaceAll("[^/]+$", rpath));
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
