package therealfarfetchd.qcommon.architect.proxy;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ItemModelMesherForge;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IRegistryDelegate;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import therealfarfetchd.qcommon.architect.client.ArchitectBlockStateMapper;
import therealfarfetchd.qcommon.architect.client.ArchitectItemMeshDef;
import therealfarfetchd.qcommon.architect.client.BlockModelLoader;
import therealfarfetchd.qcommon.architect.client.ItemModelLoader;

public class ClientProxy extends CommonProxy {

    private Minecraft mc;

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);

        mc = Minecraft.getMinecraft();

        ModelLoaderRegistry.registerLoader(BlockModelLoader.INSTANCE);
        ModelLoaderRegistry.registerLoader(ItemModelLoader.INSTANCE);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onModelLoad(ModelRegistryEvent e) {
        for (Item item : ForgeRegistries.ITEMS.getValuesCollection()) {
            ResourceLocation registryName = item.getRegistryName();
            if (registryName == null) continue;

            ResourceLocation rl = new ResourceLocation(registryName.getNamespace(), String.format("item/%s", registryName.getPath()));
            ModelResourceLocation mrl = new ModelResourceLocation(rl, "invalid");
            if (ItemModelLoader.INSTANCE.accepts(mrl)) {
                final ArchitectItemMeshDef meshDefinition = new ArchitectItemMeshDef(item);
                ModelLoader.setCustomMeshDefinition(item, meshDefinition);
                ModelBakery.registerItemVariants(item, meshDefinition.getModelLocation(null));
            }
        }

        for (Block block : ForgeRegistries.BLOCKS.getValuesCollection()) {
            ResourceLocation registryName = block.getRegistryName();
            if (registryName == null) continue;

            ResourceLocation rl = new ResourceLocation(registryName.getNamespace(), String.format("block/%s", registryName.getPath()));
            ModelResourceLocation mrl = new ModelResourceLocation(rl, "invalid");
            if (BlockModelLoader.INSTANCE.accepts(mrl)) {
                ModelLoader.setCustomStateMapper(block, ArchitectBlockStateMapper.INSTANCE);
            }
        }
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);

        List<IRegistryDelegate<Item>> items = new ArrayList<>();

        for (Item item : ForgeRegistries.ITEMS.getValuesCollection()) {
            ResourceLocation registryName = item.getRegistryName();
            if (registryName == null) continue;

            ResourceLocation rl = new ResourceLocation(registryName.getNamespace(), String.format("item/%s", registryName.getPath()));
            ModelResourceLocation mrl = new ModelResourceLocation(rl, "invalid");
            if (ItemModelLoader.INSTANCE.accepts(mrl)) {
                items.add(item.delegate);
            }
        }

        ItemModelMesher imm = mc.getRenderItem().getItemModelMesher();
        if (imm instanceof ItemModelMesherForge) {
            try {
                Field $models = ItemModelMesherForge.class.getDeclaredField("models");
                Field $locations = ItemModelMesherForge.class.getDeclaredField("locations");

                $models.setAccessible(true);
                $locations.setAccessible(true);

                Map models = (Map) $models.get(imm);
                Map locations = (Map) $locations.get(imm);

                for (IRegistryDelegate<Item> item : items) {
                    models.remove(item);
                    locations.remove(item);
                }
            } catch (NoSuchFieldException | IllegalAccessException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Nullable
    @Override
    public InputStream openResource(ResourceLocation rl, boolean respectResourcePack) {
        if (!respectResourcePack) return super.openResource(rl, respectResourcePack);

        try {
            return Minecraft.getMinecraft().getResourceManager().getResource(rl).getInputStream();
        } catch (IOException e) {
            return null;
        }
    }

}
