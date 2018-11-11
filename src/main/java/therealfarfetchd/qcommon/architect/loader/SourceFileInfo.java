package therealfarfetchd.qcommon.architect.loader;

import net.minecraft.util.ResourceLocation;

public class SourceFileInfo {

    public final String fileName;
    public final ResourceLocation path;

    public SourceFileInfo(ResourceLocation path) {
        this(getFileName(path), path);
    }

    public SourceFileInfo(String fileName, ResourceLocation path) {
        this.fileName = fileName;
        this.path = path;
    }

    public static String getFileName(ResourceLocation rl) {
        return rl.getPath().substring(rl.getPath().lastIndexOf('/') + 1);
    }

}
