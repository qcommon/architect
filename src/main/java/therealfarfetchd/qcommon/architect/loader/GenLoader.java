package therealfarfetchd.qcommon.architect.loader;

import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloadListener;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import therealfarfetchd.qcommon.architect.Architect;

public abstract class GenLoader<T, S> implements ResourceReloadListener {

    protected Map<Identifier, T> cache = new HashMap<>();

    protected GenLoader() {
        Architect.proxy.registerReloadListener(this);
    }

    public abstract T load(ParseMessageContainer log, SourceFileInfo info, S source);

    public T load(ParseMessageContainer log, Identifier id) {
        return cache.computeIfAbsent(id, id1 -> {
            S source = loadSource(log, id1);

            if (source == null) return getError();

            return load(log, new SourceFileInfo(id1), source);
        });
    }

    @Nullable
    public T load(Identifier rl) {
        ParseMessageContainer log = new ParseMessageContainer(String.format("%s '%s'", getTypeName(), SourceFileInfo.getFileName(rl)));
        T m = load(log, rl);
        log.printMessages();
        return log.isResultValid() ? m : null;
    }

    protected abstract T getError();

    protected abstract String getTypeName();

    @Nullable
    protected S loadSource(ParseMessageContainer log, Identifier id) {
        S source = null;
        try (InputStream istr = Architect.proxy.openResource(id, true)) {
            if (istr == null) {
                log.error(String.format("Could not open file '%s'", id));
                return null;
            }
            source = loadSourceFromStream(log, istr);
        } catch (IOException e) {
            e.printStackTrace();
            if (source == null) {
                log.error("Failed to load file: " + e.getMessage());
                return null;
            } else {
                log.warn(String.format("An exception occurred while loading the file: %s. We got data nonetheless, ignoring.", e.getMessage()));
            }
        }
        return source;
    }

    @Nullable
    protected abstract S loadSourceFromStream(ParseMessageContainer log, InputStream stream);

    @Override
    public void onResourceReload(ResourceManager var1) {
        cache.clear();
    }

}
