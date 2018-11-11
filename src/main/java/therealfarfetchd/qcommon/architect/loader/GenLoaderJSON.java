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

public abstract class GenLoaderJSON<T> extends GenLoader<T, JsonObject> {

    // seriously, why is this an object, it has no fields at all and doesn't
    // implement/extend anything, and is final, why not make the methods static ffs
    protected static final JsonParser json = new JsonParser();

    @Nullable
    @Override
    protected JsonObject loadSourceFromStream(ParseContext ctx, InputStream stream) {
        JsonElement el = json.parse(new InputStreamReader(stream));
        if (el.isJsonObject()) {
            return el.getAsJsonObject();
        } else {
            ctx.error("Root tag is not an object");
            return null;
        }
    }

}
