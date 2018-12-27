package therealfarfetchd.qcommon.architect.loader;

import net.minecraft.util.Identifier;

import java.util.Optional;

import com.google.gson.JsonObject;

import therealfarfetchd.qcommon.architect.model.ModelTransformMap;
import therealfarfetchd.qcommon.architect.model.ModelTransformMap.ModelTransform;
import therealfarfetchd.qcommon.croco.Vec3;

public class ModelTransformLoader extends GenLoaderJSON<ModelTransformMap> {

    public static final ModelTransformLoader INSTANCE = new ModelTransformLoader();

    @Override
    public ModelTransformMap load(ParseMessageContainer log, SourceFileInfo info, JsonObject source) {
        ParseContext ctx = ParseContext.wrap(log).withScale(16);

        Optional<ModelTransformMap> parent = Optional.empty();

        if (source.has("parent")) {
            final Identifier fallback = new Identifier("minecraft", "identity");
            final Identifier rl = ctx.dp.parseGenStringStatic(ctx.log, source, "parent", "identifier", $ -> true, Identifier::new, fallback);

            if (rl == fallback) return ModelTransformMap.IDENTITY;

            parent = Optional.ofNullable(load(new Identifier(rl.getNamespace(), String.format("render/model-transform/%s.json", rl.getPath()))));
        }

        ModelTransform thirdPersonLeftHand = load(ctx, source, "thirdperson_lefthand").orElse(parent.map($ -> $.thirdPersonLeftHand).orElse(ModelTransform.IDENTITY));
        ModelTransform thirdPersonRightHand = load(ctx, source, "thirdperson_righthand").orElse(parent.map($ -> $.thirdPersonRightHand).orElse(ModelTransform.IDENTITY));
        ModelTransform firstPersonLeftHand = load(ctx, source, "firstperson_lefthand").orElse(parent.map($ -> $.firstPersonLeftHand).orElse(ModelTransform.IDENTITY));
        ModelTransform firstPersonRightHand = load(ctx, source, "firstperson_lefthand").orElse(parent.map($ -> $.firstPersonRightHand).orElse(ModelTransform.IDENTITY));
        ModelTransform head = load(ctx, source, "head").orElse(parent.map($ -> $.head).orElse(ModelTransform.IDENTITY));
        ModelTransform gui = load(ctx, source, "gui").orElse(parent.map($ -> $.gui).orElse(ModelTransform.IDENTITY));
        ModelTransform ground = load(ctx, source, "ground").orElse(parent.map($ -> $.ground).orElse(ModelTransform.IDENTITY));
        ModelTransform fixed = load(ctx, source, "fixed").orElse(parent.map($ -> $.fixed).orElse(ModelTransform.IDENTITY));

        return new ModelTransformMap(thirdPersonLeftHand, thirdPersonRightHand, firstPersonLeftHand, firstPersonRightHand, head, gui, ground, fixed);
    }

    private Optional<ModelTransform> load(ParseContext ctx, JsonObject jo, String name) {
        if (!jo.has(name)) return Optional.empty();

        return ctx.dp.parseGenObjectStatic(ctx.log, jo, name, "translation", $ -> true, tag -> {
            Vec3 rotation = ctx.dp.parseVec3Static(ctx.log, tag, "rotation", Vec3.ORIGIN);
            Vec3 translation = ctx.dp.parseCoords3DStatic(ctx.log, tag, "translation", Vec3.ORIGIN);
            Vec3 scale = ctx.dp.parseVec3Static(ctx.log, tag, "scale", new Vec3(1, 1, 1));

            return Optional.of(new ModelTransform(rotation, translation, scale));
        }, Optional.empty());
    }

    @Override
    protected ModelTransformMap getError() {
        return ModelTransformMap.IDENTITY;
    }

    @Override
    protected String getTypeName() {
        return "model transform";
    }

}
