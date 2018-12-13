package therealfarfetchd.qcommon.architect.loader;

import net.minecraft.util.Identifier;

public class SourceFileInfo {

    public final String fileName;
    public final Identifier path;

    public SourceFileInfo(Identifier path) {
        this(getFileName(path), path);
    }

    public SourceFileInfo(String fileName, Identifier path) {
        this.fileName = fileName;
        this.path = path;
    }

    public static String getFileName(Identifier rl) {
        return rl.getPath().substring(rl.getPath().lastIndexOf('/') + 1);
    }

}
