package therealfarfetchd.qcommon.architect.loader;

import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nullable;

import therealfarfetchd.qcommon.architect.Architect;

public abstract class GenLoader<T, S> {

    protected GenLoader() { }

    public abstract T load(ParseContext ctx, SourceFileInfo info, S source);

    public T load(ParseContext ctx, Identifier id) {
        S source = loadSource(ctx, id);

        if (source == null) return getError();

        return load(ctx, new SourceFileInfo(id), source);
    }

    @Nullable
    public T load(Identifier rl) {
        ParseContext ctx = new ParseContext(String.format("%s '%s'", getTypeName(), SourceFileInfo.getFileName(rl)));
        T m = load(ctx, rl);
        ctx.printMessages();
        return ctx.isResultValid() ? m : null;
    }

    protected abstract T getError();

    protected abstract String getTypeName();

    @Nullable
    protected S loadSource(ParseContext ctx, Identifier id) {
        S source = null;
        try (InputStream istr = Architect.proxy.openResource(id, true)) {
            if (istr == null) {
                ctx.error(String.format("Could not open file '%s'", id));
                return null;
            }
            source = loadSourceFromStream(ctx, istr);
        } catch (IOException e) {
            e.printStackTrace();
            if (source == null) {
                ctx.error("Failed to load file: " + e.getMessage());
                return null;
            } else {
                ctx.warn(String.format("An exception occurred while loading the file: %s. We got data nonetheless, ignoring.", e.getMessage()));
            }
        }
        return source;
    }

    @Nullable
    protected abstract S loadSourceFromStream(ParseContext ctx, InputStream stream);

}
