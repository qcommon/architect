package therealfarfetchd.qcommon.architect.client;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelItemPropertyOverrideList;
import net.minecraft.client.render.model.json.ModelTransformations;
import net.minecraft.client.texture.Sprite;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.ToIntFunction;

import javax.annotation.Nullable;

public class ItemModelMulti implements BakedModel {

    private final BakedModel[] models;
    private final ToIntFunction<ItemStack> mapper;

    public ItemModelMulti(BakedModel model, ToIntFunction<ItemStack> mapper) {
        this.models = new BakedModel[]{model};
        this.mapper = mapper;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState var1, @Nullable Direction var2, Random var3) {
        return models[0].getQuads(var1, var2, var3);
    }

    @Override
    public boolean useAmbientOcclusion() {
        return models[0].useAmbientOcclusion();
    }

    @Override
    public boolean hasDepthInGui() {
        return models[0].hasDepthInGui();
    }

    @Override
    public boolean isBuiltin() {
        return models[0].isBuiltin();
    }

    @Override
    public Sprite getSprite() {
        return models[0].getSprite();
    }

    @Override
    public ModelTransformations getTransformations() {
        return models[0].getTransformations();
    }

    @Override
    public ModelItemPropertyOverrideList getItemPropertyOverrides() {
        return new CustomOverrides(models, mapper);
    }

    private static class CustomOverrides extends ModelItemPropertyOverrideList {

        private final BakedModel[] models;
        private final ToIntFunction<ItemStack> mapper;

        public CustomOverrides(BakedModel[] models, ToIntFunction<ItemStack> mapper) {
            // noinspection ConstantConditions
            super(null, null, null, Collections.emptyList());
            this.models = models;
            this.mapper = mapper;
        }

        @Nullable
        @Override
        public BakedModel method_3495(BakedModel var1, ItemStack var2, @Nullable World var3, @Nullable LivingEntity var4) {
            final int i = mapper.applyAsInt(var2);
            if (i >= 0 && i < models.length) {
                return models[i];
            } else {
                return var1;
            }
        }

    }

}
