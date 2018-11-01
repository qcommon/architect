package therealfarfetchd.qcommon.architect.loader;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.annotation.Nullable;

import therealfarfetchd.qcommon.architect.Architect;

public abstract class GenLoader<T> {

    // seriously, why is this an object, it has no fields at all and doesn't
    // implement/extend anything, and is final, why not make the methods static ffs
    protected static final JsonParser json = new JsonParser();

    protected GenLoader() {
        getTypeName();
    }

    public abstract T load(ParseContext ctx, String fileName, JsonObject json);

    public T load(ParseContext ctx, ResourceLocation rl) {
        String filename = getFileName(rl);

        JsonObject obj = loadJsonObject(ctx, rl);

        if (obj == null) return getError();

        return load(ctx, filename, obj);
    }

    @Nullable
    public T load(String fileName, JsonObject json) {
        ParseContext ctx = new ParseContext(String.format("%s '%s'", getTypeName(), fileName));
        T m = load(ctx, fileName, json);
        ctx.printMessages();
        return ctx.isResultValid() ? m : null;
    }

    @Nullable
    public T load(ResourceLocation rl) {
        ParseContext ctx = new ParseContext(String.format("%s '%s'", getTypeName(), getFileName(rl)));
        T m = load(ctx, rl);
        ctx.printMessages();
        return ctx.isResultValid() ? m : null;
    }

    protected abstract T getError();

    protected abstract String getTypeName();

    public static String getFileName(ResourceLocation rl) {
        return rl.getPath().substring(rl.getPath().lastIndexOf('/') + 1);
    }

    @Nullable
    public static JsonObject loadJsonObject(ParseContext ctx, ResourceLocation rl) {
        JsonObject obj = null;
        try (InputStream istr = Architect.proxy.openResource(rl, true)) {
            if (istr == null) {
                ctx.error(String.format("Could not open file '%s'", rl));
                return null;
            }
            JsonElement el = json.parse(new InputStreamReader(istr));
            if (el.isJsonObject()) {
                obj = el.getAsJsonObject();
            } else {
                ctx.error("Root tag is not an object");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (obj == null) {
                ctx.error("Failed to load JSON file: " + e.getMessage());
                return null;
            } else {
                ctx.warn(String.format("An exception occurred while loading the file: %s. We got data nonetheless, ignoring.", e.getMessage()));
            }
        }
        return obj;
    }

}
