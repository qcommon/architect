package therealfarfetchd.qcommon.architect.client;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ArchitectItemMeshDef implements ItemMeshDefinition {

    private final Item item;

    public ArchitectItemMeshDef(Item item) {this.item = item;}

    @Override
    public ModelResourceLocation getModelLocation(ItemStack stack) {
        return new ModelResourceLocation(new ResourceLocation(item.getRegistryName().getNamespace(), String.format("item/%s", item.getRegistryName().getPath())), "meta=0");
    }

}
