package therealfarfetchd.qcommon.architect.factories;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import javax.annotation.Nullable;

import therealfarfetchd.qcommon.architect.Architect;
import therealfarfetchd.qcommon.architect.loader.GenLoader;
import therealfarfetchd.qcommon.architect.loader.ParseContext;

public class FactoryRegistry {

    public static final FactoryRegistry INSTANCE = new FactoryRegistry();

    private Map<ResourceLocation, TransformFactory> transforms = new HashMap<>();
    private Map<ResourceLocation, PartFactory> parts = new HashMap<>();
    private Map<ResourceLocation, ModelFactory> models = new HashMap<>();

    private FactoryRegistry() {}

    private void registerTransformFactory(ResourceLocation rl, TransformFactory tf) {
        if (transforms.containsKey(rl)) {
            Architect.INSTANCE.logger.warn("Overriding existing transform {} ({}) with {}", rl, transforms.get(rl).getClass(), tf.getClass());
        }

        transforms.put(rl, tf);
    }

    private void registerPartFactory(ResourceLocation rl, PartFactory pf) {
        if (parts.containsKey(rl)) {
            Architect.INSTANCE.logger.warn("Overriding existing part {} ({}) with {}", rl, parts.get(rl).getClass(), pf.getClass());
        }

        parts.put(rl, pf);
    }

    private void registerModelFactory(ResourceLocation rl, ModelFactory mf) {
        if (models.containsKey(rl)) {
            Architect.INSTANCE.logger.warn("Overriding existing model {} ({}) with {}", rl, models.get(rl).getClass(), mf.getClass());
        }

        models.put(rl, mf);
    }

    @Nullable
    public TransformFactory getTransformFactory(ResourceLocation rl) {
        return transforms.get(rl);
    }

    @Nullable
    public PartFactory getPartFactory(ResourceLocation rl) {
        return parts.get(rl);
    }

    @Nullable
    public ModelFactory getModelFactory(ResourceLocation rl) {
        return models.get(rl);
    }

    private void readFactoryDefinitions(ParseContext ctx, String modid, JsonObject json) {
        readDefinitionPart(ctx, modid, json, "transforms", "transform", TransformFactory.class, this::registerTransformFactory);
        readDefinitionPart(ctx, modid, json, "parts", "part", PartFactory.class, this::registerPartFactory);
        readDefinitionPart(ctx, modid, json, "models", "model", ModelFactory.class, this::registerModelFactory);

        for (Map.Entry<String, JsonElement> e : json.entrySet()) {
            ctx.error(String.format("Invalid factory type '%s' in factory definition", e.getKey()));
        }
    }

    private void readFactoryDefinitions(String modid) {
        ResourceLocation rl = new ResourceLocation(modid, "render/_factories.json");
        ParseContext ctx = new ParseContext(String.format("'%s' factories", modid));
        JsonObject obj = GenLoader.loadJsonObject(ctx, rl);
        if (ctx.isResultValid()) {
            assert obj != null;
            readFactoryDefinitions(ctx, modid, obj);
        }
        ctx.printMessages();
    }

    public void readFactoryDefinitions() {
        Loader.instance().getIndexedModList().keySet().forEach(this::readFactoryDefinitions);
    }

    private <T> void readDefinitionPart(ParseContext ctx, String modid, JsonObject json, String key, String typeSpec, Class<T> clazz, BiConsumer<ResourceLocation, T> register) {
        if (json.has(key)) {
            final JsonElement transforms = json.get(key);
            if (transforms.isJsonObject()) {
                readGenDefinitions(ctx, modid, transforms.getAsJsonObject(), clazz, typeSpec, register);
            } else {
                ctx.error(String.format("Key '%s' needs to be an object!", key));
            }
            json.remove(key);
        }
    }

    private <T> void readGenDefinitions(ParseContext ctx, String modid, JsonObject json, Class<T> clazz, String typeSpec, BiConsumer<ResourceLocation, T> register) {
        for (Map.Entry<String, JsonElement> e : json.entrySet()) {
            final String name = e.getKey();
            ResourceLocation rl;
            if (name.contains(":")) {
                rl = new ResourceLocation(name);
            } else {
                rl = new ResourceLocation(modid, name);
            }
            if (!(e.getValue() instanceof JsonPrimitive)) {
                ctx.error(String.format("Invalid type for %s '%s' (%s)", typeSpec, name, e.getValue().toString()));
                continue;
            }
            final JsonPrimitive jp = e.getValue().getAsJsonPrimitive();
            if (!jp.isString()) {
                ctx.error(String.format("Invalid type for %s '%s' (%s)", typeSpec, name, e.getValue().toString()));
                continue;
            }
            String implName = jp.getAsString();
            Class<?> implClass;
            try {
                implClass = Class.forName(implName);
            } catch (ClassNotFoundException e1) {
                ctx.error(String.format("Class '%s' for %s '%s' not found", implName, typeSpec, name));
                continue;
            }
            if (!clazz.isAssignableFrom(implClass)) {
                ctx.error(String.format("Class '%s' is not a valid %s factory (must implement %s)", implName, typeSpec, clazz.getSimpleName()));
                continue;
            }
            T o;
            try {
                o = (T) implClass.getConstructor().newInstance();
            } catch (NoSuchMethodException e1) {
                ctx.error(String.format("Class '%s' is not a valid %s factory (must have constructor with 0 parameters)", implName, typeSpec));
                continue;
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e1) {
                ctx.error(String.format("Failed to instantiate %s class '%s'", typeSpec, implName));
                continue;
            }
            register.accept(rl, o);
        }
    }

}
