package therealfarfetchd.qcommon.architect.factories;

import net.minecraft.util.Identifier;
import net.fabricmc.loader.FabricLoader;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import javax.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import therealfarfetchd.qcommon.architect.Architect;
import therealfarfetchd.qcommon.architect.loader.ModelLoader;
import therealfarfetchd.qcommon.architect.loader.ParseMessageContainer;

public class FactoryRegistry {

    public static final FactoryRegistry INSTANCE = new FactoryRegistry();

    private Map<Identifier, TransformFactory> transforms = new HashMap<>();
    private Map<Identifier, PartFactory> parts = new HashMap<>();
    private Map<Identifier, ModelFactory> models = new HashMap<>();

    private FactoryRegistry() {}

    private void registerTransformFactory(Identifier id, TransformFactory tf) {
        if (transforms.containsKey(id)) {
            Architect.INSTANCE.logger.warn("Overriding existing transform {} ({}) with {}", id, transforms.get(id).getClass(), tf.getClass());
        }

        transforms.put(id, tf);
    }

    private void registerPartFactory(Identifier id, PartFactory pf) {
        if (parts.containsKey(id)) {
            Architect.INSTANCE.logger.warn("Overriding existing part {} ({}) with {}", id, parts.get(id).getClass(), pf.getClass());
        }

        parts.put(id, pf);
    }

    private void registerModelFactory(Identifier id, ModelFactory mf) {
        if (models.containsKey(id)) {
            Architect.INSTANCE.logger.warn("Overriding existing model {} ({}) with {}", id, models.get(id).getClass(), mf.getClass());
        }

        models.put(id, mf);
    }

    @Nullable
    public TransformFactory getTransformFactory(Identifier id) {
        return transforms.get(id);
    }

    @Nullable
    public PartFactory getPartFactory(Identifier id) {
        return parts.get(id);
    }

    @Nullable
    public ModelFactory getModelFactory(Identifier id) {
        return models.get(id);
    }

    private void readFactoryDefinitions(ParseMessageContainer ctx, String modid, JsonObject json) {
        readDefinitionPart(ctx, modid, json, "transforms", "transform", TransformFactory.class, this::registerTransformFactory);
        readDefinitionPart(ctx, modid, json, "parts", "part", PartFactory.class, this::registerPartFactory);
        readDefinitionPart(ctx, modid, json, "models", "model", ModelFactory.class, this::registerModelFactory);

        for (Map.Entry<String, JsonElement> e : json.entrySet()) {
            ctx.error(String.format("Invalid factory type '%s' in factory definition", e.getKey()));
        }
    }

    private void readFactoryDefinitions(String modid) {
        Identifier id = new Identifier(modid, "render/_factories.json");
        if (!Architect.proxy.resourceExists(id, false)) return;
        ParseMessageContainer ctx = new ParseMessageContainer(String.format("'%s' factories", modid));
        JsonObject obj = ModelLoader.INSTANCE.loadSource(ctx, id);
        if (ctx.isResultValid()) {
            assert obj != null;
            readFactoryDefinitions(ctx, modid, obj);
        }
        ctx.printMessages();
    }

    public void readFactoryDefinitions() {
        FabricLoader.INSTANCE.getMods().forEach(mc -> readFactoryDefinitions(mc.getInfo().getId()));
    }

    private <T> void readDefinitionPart(ParseMessageContainer ctx, String modid, JsonObject json, String key, String typeSpec, Class<T> clazz, BiConsumer<Identifier, T> register) {
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

    private <T> void readGenDefinitions(ParseMessageContainer ctx, String modid, JsonObject json, Class<T> clazz, String typeSpec, BiConsumer<Identifier, T> register) {
        for (Map.Entry<String, JsonElement> e : json.entrySet()) {
            final String name = e.getKey();
            Identifier id;
            if (name.contains(":")) {
                id = new Identifier(name);
            } else {
                id = new Identifier(modid, name);
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
            register.accept(id, o);
        }
    }

}
