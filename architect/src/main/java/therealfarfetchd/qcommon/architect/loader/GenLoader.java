package therealfarfetchd.qcommon.architect.loader;

import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nullable;

import therealfarfetchd.qcommon.architect.Architect;

public abstract class GenLoader<T, S> {

    protected GenLoader() { }

    public abstract T load(ParseContext ctx, SourceFileInfo info, S source);

    public T load(ParseContext ctx, ResourceLocation rl) {
        S source = loadSource(ctx, rl);

        if (source == null) return getError();

        return load(ctx, new SourceFileInfo(rl), source);
    }

    @Nullable
    public T load(ResourceLocation rl) {
        ParseContext ctx = new ParseContext(String.format("%s '%s'", getTypeName(), SourceFileInfo.getFileName(rl)));
        T m = load(ctx, rl);
        ctx.printMessages();
        return ctx.isResultValid() ? m : null;
    }

    protected abstract T getError();

    protected abstract String getTypeName();

    @Nullable
    protected S loadSource(ParseContext ctx, ResourceLocation rl) {
        S source = null;
        try (InputStream istr = Architect.proxy.openResource(rl, true)) {
            if (istr == null) {
                ctx.error(String.format("Could not open file '%s'", rl));
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
